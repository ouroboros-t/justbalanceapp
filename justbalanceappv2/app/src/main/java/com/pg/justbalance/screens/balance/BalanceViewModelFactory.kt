package com.pg.justbalance.screens.balance

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pg.justbalance.database.BalanceDatabaseDao

class BalanceViewModelFactory(
    private val dataSource: BalanceDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalanceViewModel::class.java)) {
            return BalanceViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}