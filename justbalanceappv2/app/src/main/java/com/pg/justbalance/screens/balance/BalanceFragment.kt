package com.pg.justbalance.screens.balance


import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pg.justbalance.R
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabase
import com.pg.justbalance.databinding.BalanceLayoutBinding
import com.pg.justbalance.decimalFormatDouble
import com.pg.justbalance.firebase.readingService
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.screens.add.AddBalanceViewModel
import com.pg.justbalance.screens.add.AddBalanceViewModelFactory
import com.pg.justbalance.screens.info.BalanceViewModelFactory


class BalanceFragment : androidx.fragment.app.Fragment(R.layout.balance_layout) {
    //inflate layout
    //hookupViewModel to fragment

    var alertDialog : AlertDialog? = null
    //enable databinding in gradle (module) scripts:
    private lateinit var balanceViewModel: BalanceViewModel
    private lateinit var binding: BalanceLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.balance_layout, container, false)


        val application = requireNotNull(this.activity).application
        val dataSource = BalanceDatabase.getInstance(application).balanceDatabaseDao


        //val viewModelFactory = BalanceViewModelFactory(dataSource,application)

         balanceViewModel = ViewModelProvider(this).get(BalanceViewModel::class.java)

        //we can now directly update TextViews, Buttons, etc inside of the xml..
        //EXAMPLE INSIDE TEXT VIEW : android:text="@{@string/quote_format(gameViewModel.word)}"
        binding.balanceViewModel = balanceViewModel

        //binding viewmodel to view directly?
        binding.lifecycleOwner = this

        setActionBarToBeEmpty()
        balanceViewModel.runService()

        val adapter = balanceViewModel.getAdapter()
        binding.balancesList.adapter = adapter

        //binding.totalBalanceAmountTextView.text = decimalFormatDouble(balanceViewModel.showTotalBalance(adapter.data))
        binding.youHavent.visibility = View.GONE

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_balanceFragment_to_addBalanceFragment)
        }


        balanceViewModel.navigateToBalanceInfo.observe(viewLifecycleOwner, Observer {
            balance -> balance?.let {
                    Log.i("HEre", balance.toString())
                    this.findNavController().navigate(BalanceFragmentDirections.actionBalanceFragmentToBalanceInfoFragment(balance))
                   balanceViewModel.onBalanceItemInfoNavigated()
            }
        })

        return binding.root
    }
    fun setActionBarToBeEmpty(){
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