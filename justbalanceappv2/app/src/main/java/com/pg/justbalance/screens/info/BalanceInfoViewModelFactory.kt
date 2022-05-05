package com.pg.justbalance.screens.info

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.screens.balance.BalanceViewModel

class BalanceViewModelFactory(
    private val balanceId : String,
    private val dataSource: FirebaseFirestore,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalanceInfoViewModel::class.java)) {
            return BalanceInfoViewModel(balanceId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}