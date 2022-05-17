package com.pg.justbalance.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

interface writingServiceInterface {
    var db: FirebaseFirestore
    fun addBalanceToDatabase(balance: HashMap<String, Any?>): Task<DocumentReference>
    fun recordPayment(payment: HashMap<String, Any?>, balanceId: String?): Task<DocumentReference>
}