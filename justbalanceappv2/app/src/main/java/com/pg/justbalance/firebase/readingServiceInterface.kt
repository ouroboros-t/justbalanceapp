package com.pg.justbalance.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.screens.balance.BalanceAdapter
import com.pg.justbalance.screens.balance.BalanceFirestoreAdapter
import com.pg.justbalance.screens.payment.BalancePaymentAdapter

interface readingServiceInterface {
    val balanceAdapter: BalanceFirestoreAdapter
    val paymentAdapter: BalancePaymentAdapter
    val db: FirebaseFirestore
    fun readBalances()
    fun readPayments(balanceId: String)
    fun empty(): Boolean
    var balanceList : MutableList<BalanceModel>
}