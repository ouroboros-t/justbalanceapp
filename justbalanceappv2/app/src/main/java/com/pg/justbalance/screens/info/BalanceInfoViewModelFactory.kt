package com.pg.justbalance.screens.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore

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