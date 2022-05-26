package com.pg.justbalance.screens.balance


import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var balanceViewModel: BalanceViewModel
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

        balanceViewModel = ViewModelProvider(this).get(BalanceViewModel::class.java)

        //we can now directly update TextViews, Buttons, etc inside of the xml..
        //EXAMPLE INSIDE TEXT VIEW : android:text="@{@string/quote_format(gameViewModel.word)}"
        binding.balanceViewModel = balanceViewModel

        //binding viewmodel to view directly?
        binding.lifecycleOwner = this

        setActionBarToBeEmpty()
        balanceViewModel.runService()


        val navController = findNavController()
        userViewModel.getUser()
        userViewModel.user.observe(viewLifecycleOwner, Observer{ user ->
                if(user != null){

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

}