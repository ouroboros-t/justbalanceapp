package com.pg.justbalance.screens.add

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pg.justbalance.database.BalanceDatabaseDao

class AddBalanceViewModelFactory (
    private val dataSource: BalanceDatabaseDao,
            private val application: Application): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddBalanceViewModel::class.java)) {
            return AddBalanceViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}