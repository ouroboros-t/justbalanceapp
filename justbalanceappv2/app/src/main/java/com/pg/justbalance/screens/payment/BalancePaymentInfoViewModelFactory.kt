package com.pg.justbalance.screens.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore

class BalancePaymentInfoViewModelFactory(
    private val paymentId: String,
    private val balanceId: String,
    private val dataSource: FirebaseFirestore,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BalancePaymentInfoViewModel::class.java)) {
            return BalancePaymentInfoViewModel(paymentId, balanceId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}