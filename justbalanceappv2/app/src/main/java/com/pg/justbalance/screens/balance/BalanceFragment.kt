package com.pg.justbalance.screens.balance


import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.databinding.BalanceLayoutBinding
import com.pg.justbalance.sharedViewModels.UserViewModel


class BalanceFragment : androidx.fragment.app.Fragment(R.layout.balance_layout) {
    private val balanceViewModel: BalanceViewModel by activityViewModels()
    private lateinit var binding: BalanceLayoutBinding
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.balance_layout, container, false
        )

       // balanceViewModel = ViewModelProvider(this).get(BalanceViewModel::class.java)

        //we can now directly update TextViews, Buttons, etc inside of the xml..
        //EXAMPLE INSIDE TEXT VIEW : android:text="@{@string/quote_format(gameViewModel.word)}"
        binding.balanceViewModel = balanceViewModel

        //binding viewmodel to view directly?
        binding.lifecycleOwner = this

        setActionBarToBeEmpty()


        val spinner: Spinner? = view?.findViewById(R.id.filter_spinner)
        ArrayAdapter.createFromResource(
            requireContext(), R.array.filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner?.adapter = adapter
        }



        val navController = findNavController()
        userViewModel.getUser()
        userViewModel.user.observe(viewLifecycleOwner, Observer{ user ->
                if(user != null){
                    //filter here by name
                    //make aware of sorting option -> returns to the default
                    balanceViewModel.runService()
                }else{
                    navController.navigate(R.id.loginFragment)
                }

        })


        balanceViewModel.balances.observe(viewLifecycleOwner, Observer { balancesList ->
            if (balancesList.isNotEmpty()) {
                binding.youHavent.visibility = View.GONE
            }
            binding.totalBalanceAmountTextView.text =
                balanceViewModel.showTotalBalance(balancesList)
            binding.balancesList.adapter = BalanceFirestoreAdapter(
                balancesList,

                BalanceFirestoreAdapter.BalanceFirestoreListener { balanceId ->
                    balanceViewModel.onBalanceItemClicked(balanceId)
                })

        })


        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_balanceFragment_to_addBalanceFragment)
        }

        balanceViewModel.navigateToBalanceInfo.observe(viewLifecycleOwner, Observer { balance ->
            balance?.let {
                this.findNavController().navigate(
                    BalanceFragmentDirections.actionBalanceFragmentToBalanceInfoFragment(balance)
                )
                balanceViewModel.onBalanceItemInfoNavigated()
            }
        })

        binding.button3.setOnClickListener {
            balanceViewModel.filterButtonPressed("Okay")
        }

        binding.logoutButton.setOnClickListener {
            userViewModel.signOut()
           navController.navigate(R.id.loginFragment)
        }


        return binding.root
    }

    fun setActionBarToBeEmpty() {
        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (context as AppCompatActivity).supportActionBar!!.title = ""
        (context as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    requireActivity().baseContext,
                    R.color.main_blue
                )
            )
        )
    }

    override fun onResume() {
        balanceViewModel.hasRan = false
        balanceViewModel.runService()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        balanceViewModel.sortingOption.observe(viewLifecycleOwner, Observer {

        })
        super.onViewCreated(view, savedInstanceState)
    }

}