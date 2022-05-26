package com.pg.justbalance.services

import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.models.PaymentModel

interface readingServiceInterface {
    val db: FirebaseFirestore
    suspend fun readBalances(userId: String): MutableList<BalanceModel>
    suspend fun readPayments(balanceId: String): MutableList<PaymentModel>
    suspend fun getStartingBalance(balanceId: String, userId: String): Any?
    fun updateCurrentBalance(userId: String,balanceId: String, bal: Double)
}