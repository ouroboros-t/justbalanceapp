package com.pg.justbalance.screens.payment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R

import com.pg.justbalance.database.BalanceDatabase
import com.pg.justbalance.databinding.BalanceRecordPaymentBinding
import com.pg.justbalance.screens.add.parseToDouble

class BalanceRecordPaymentFragment : Fragment(R.layout.balance_record_payment){
    private lateinit var binding: BalanceRecordPaymentBinding
    private lateinit var balanceRecordPaymentViewModel: BalanceRecordPaymentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home ->
                findNavController().navigate(R.id.action_balanceRecordPaymentFragment_to_balanceFragment)
        }
        return true
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.balance_record_payment,
            container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = BalanceDatabase.getInstance(application).balanceDatabaseDao

       // val viewModelFactory = BalanceRecordPaymentViewModelFactory(dataSource,application)

        balanceRecordPaymentViewModel =
            ViewModelProvider(this).get(BalanceRecordPaymentViewModel::class.java)

        binding.balanceRecordPaymentViewModel = balanceRecordPaymentViewModel

        setupActionBar()
        binding.confirmRecordPayment.setOnClickListener {

            val paymentAmount = binding.enterNewEmailAddressBox.text.parseToDouble()
            balanceRecordPaymentViewModel.addPayment(paymentAmount )
        }

        return binding.root
    }

    private fun setupActionBar() {
        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (context as AppCompatActivity).supportActionBar!!.title = "Record Payment"
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