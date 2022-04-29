package com.pg.justbalance.screens.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pg.justbalance.R
import com.pg.justbalance.database.BalanceDatabase
import com.pg.justbalance.databinding.BalancePaymentLayoutBinding

class BalancePaymentFragment: Fragment(R.layout.balance_payment_layout) {
        private lateinit var binding: BalancePaymentLayoutBinding
        private lateinit var balancePaymentViewModel: BalancePaymentViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.balance_payment_layout, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = BalanceDatabase.getInstance(application).balanceDatabaseDao

        val viewModelFactory = BalancePaymentViewModelFactory(dataSource,application)

        balancePaymentViewModel =
            ViewModelProvider(this, viewModelFactory).get(BalancePaymentViewModel::class.java)

        binding.balancePaymentViewModel = balancePaymentViewModel

        binding.lifecycleOwner = this



        /*val adapter = BalanceAdapter(BalanceAdapter.BalanceListener{
            balanceId -> balanceViewModel.onBalanceItemClicked(balanceId)
        })*/


        return binding.root
    }

}