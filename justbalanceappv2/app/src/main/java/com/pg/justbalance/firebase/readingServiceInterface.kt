package com.pg.justbalance.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.models.PaymentModel

interface readingServiceInterface {
    val db: FirebaseFirestore
    suspend fun readBalances(): MutableList<BalanceModel>
    suspend fun readPayments(balanceId: String): MutableList<PaymentModel>
    suspend fun calculateCurrentBalance(balanceId: String): Any?
    fun updateCurrentBalance(balanceId: String, bal: Double)
}