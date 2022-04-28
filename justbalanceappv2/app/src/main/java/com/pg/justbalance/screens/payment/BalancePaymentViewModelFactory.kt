package com.pg.justbalance.screens.payment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.screens.add.AddBalanceViewModel


class BalancePaymentViewModelFactory (
    private val dataSource: BalanceDatabaseDao,
    private val application: Application
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalancePaymentViewModel::class.java)) {
            return BalancePaymentViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}