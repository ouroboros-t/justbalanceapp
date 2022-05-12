package com.pg.justbalance.screens.payment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.databinding.BalanceRecordPaymentBinding
import com.pg.justbalance.screens.add.parseToDouble

class BalanceRecordPaymentFragment : Fragment(R.layout.balance_record_payment) {
    private lateinit var binding: BalanceRecordPaymentBinding
    private lateinit var balanceRecordPaymentViewModel: BalanceRecordPaymentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val arguments = BalanceRecordPaymentFragmentArgs.fromBundle(requireArguments())
        val action =
            BalanceRecordPaymentFragmentDirections.actionBalanceRecordPaymentFragmentToBalanceInfoFragment(
                arguments.balanceId
            )
        when (item.getItemId()) {
            android.R.id.home ->
                findNavController().navigate(action)
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.balance_record_payment,
            container, false
        )

        val arguments = BalanceRecordPaymentFragmentArgs.fromBundle(requireArguments())
        balanceRecordPaymentViewModel =
            ViewModelProvider(this).get(BalanceRecordPaymentViewModel::class.java)

        binding.balanceRecordPaymentViewModel = balanceRecordPaymentViewModel

        setupActionBar()
        binding.confirmRecordPayment.setOnClickListener {
            val paymentAmount = binding.enterNewEmailAddressBox.text.parseToDouble()
            balanceRecordPaymentViewModel.addPayment(paymentAmount, arguments.balanceId)
            val action =
                BalanceRecordPaymentFragmentDirections.actionBalanceRecordPaymentFragmentToBalanceInfoFragment(
                    arguments.balanceId
                )
            findNavController().navigate(action)
            val message = "Payment successfully added."
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val arguments = BalanceRecordPaymentFragmentArgs.fromBundle(requireArguments())
        balanceRecordPaymentViewModel.successData.observe(viewLifecycleOwner, Observer {
            Log.i("successData", it.toString())
            if (it != null) {
                val action =
                    BalanceRecordPaymentFragmentDirections.actionBalanceRecordPaymentFragmentToBalanceInfoFragment(
                        arguments.balanceId
                    )
                findNavController().navigate(action)
                val message = "Payment successfully added."
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        balanceRecordPaymentViewModel.errorData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val message = "Something went wrong."
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        balanceRecordPaymentViewModel.resetData()
        super.onPause()
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