package com.pg.justbalance.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.models.PaymentModel
import com.pg.justbalance.screens.balance.BalanceAdapter
import com.pg.justbalance.screens.balance.BalanceFirestoreAdapter
import com.pg.justbalance.screens.payment.BalancePaymentAdapter

interface readingServiceInterface {
    val balanceAdapter: BalanceFirestoreAdapter
    val paymentAdapter: BalancePaymentAdapter
    val db: FirebaseFirestore
    suspend fun readBalances() : MutableList<BalanceModel>
    suspend fun readPayments(balanceId: String):MutableList<PaymentModel>
    fun empty(): Boolean
    var balanceList : MutableList<BalanceModel>
}