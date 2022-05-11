package com.pg.justbalance.firebase

import com.google.firebase.firestore.FirebaseFirestore

interface deleteServiceInterface {
    val firestore: FirebaseFirestore
    fun deleteService(balanceId: String)
    fun deleteAll(balanceId: String)
    fun deletePayment(paymentId: String, balanceId: String)
}