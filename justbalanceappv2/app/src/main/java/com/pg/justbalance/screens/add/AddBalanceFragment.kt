package com.pg.justbalance.screens.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pg.justbalance.R
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.databinding.AddBalanceLayoutBinding

class AddBalanceFragment: Fragment() {

    private lateinit var binding: AddBalanceLayoutBinding
    private lateinit var viewModel: AddBalanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_balance_layout,container,false)

        viewModel = ViewModelProvider(this).get(AddBalanceViewModel::class.java)

        binding.lifecycleOwner = this

//        binding.addBalanceConfirmButton.setOnClickListener {
//            val balanceName = binding.balanceNameEditText.toString()
//            val balanceAmount = Integer.parseInt(binding.balanceAmountEditText.toString()).toDouble()
//
//            viewModel.insert(Balance(balanceName = balanceName, startingBalance = balanceAmount, currentBalance = balanceAmount))
//        }


        return binding.root
    }
}