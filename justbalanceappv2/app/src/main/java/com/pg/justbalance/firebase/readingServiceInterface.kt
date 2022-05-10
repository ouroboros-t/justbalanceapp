package com.pg.justbalance.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.models.PaymentModel
import com.pg.justbalance.screens.balance.BalanceAdapter
import com.pg.justbalance.screens.balance.BalanceFirestoreAdapter
import com.pg.justbalance.screens.payment.BalancePaymentAdapter

interface readingServiceInterface {
    val db: FirebaseFirestore
    suspend fun readBalances() : MutableList<BalanceModel>
    suspend fun readPayments(balanceId: String):MutableList<PaymentModel>
    suspend fun calculateCurrentBalance(balanceId: String): Any?
    fun updateCurrentBalance(balanceId: String, bal: Double)
}