package com.pg.justbalance.screens.payment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.screens.balance.BalanceViewModel

class BalanceRecordPaymentViewModelFactory(
    private val dataSource: BalanceDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalanceRecordPaymentViewModel::class.java)) {
            return BalanceRecordPaymentViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}