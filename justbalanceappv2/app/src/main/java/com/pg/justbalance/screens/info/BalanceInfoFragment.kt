package com.pg.justbalance.screens.info


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.database.BalanceDatabase
import com.pg.justbalance.databinding.BalanceInfoLayoutBinding
import com.pg.justbalance.screens.payment.BalancePaymentAdapter

class BalanceInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: BalanceInfoLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.balance_info_layout,
                container, false)

        val application = requireNotNull(this.activity).application
        val arguments = BalanceInfoFragmentArgs.fromBundle(requireArguments())

        val dataSource = BalanceDatabase.getInstance(application).balanceDatabaseDao
        val viewModelFactory = BalanceViewModelFactory(arguments.balanceId, dataSource)

        val balanceInfoViewModel =
            ViewModelProvider(this, viewModelFactory).get(BalanceInfoViewModel::class.java)

        binding.balanceInfoViewModel = balanceInfoViewModel

        binding.lifecycleOwner = this


        val adapter = BalancePaymentAdapter(BalancePaymentAdapter.PaymentListener{
                paymentID -> balanceInfoViewModel.onPaymentItemClicked(paymentID)
        })

        binding.paymentsList.adapter = adapter

        balanceInfoViewModel.navigateToBalances.observe(viewLifecycleOwner, Observer {
            if(it==true){
                this.findNavController().navigate(
                    BalanceInfoFragmentDirections.actionBalanceInfoFragmentToBalanceFragment()
                )
                balanceInfoViewModel.doneNavigating()
            }
        })

        binding.recordPaymentButton.setOnClickListener {
            findNavController().navigate(R.id.action_balanceInfoFragment_to_balanceRecordPaymentFragment)
        }

        return binding.root
    }
}