package com.pg.justbalance.services

import com.google.firebase.firestore.FirebaseFirestore

interface deleteServiceInterface {
    val firestore: FirebaseFirestore
    fun deleteService(balanceId: String, userId:String)
    fun deleteAll(balanceId: String, userId: String)
    fun deletePayment(paymentId: String, balanceId: String, userId: String)
}