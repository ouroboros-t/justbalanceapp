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



        balancePaymentViewModel =
            ViewModelProvider(this).get(BalancePaymentViewModel::class.java)

        binding.balancePaymentViewModel = balancePaymentViewModel

        binding.lifecycleOwner = this

        return binding.root
    }

}