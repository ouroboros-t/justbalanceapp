package com.pg.justbalance.services

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.models.User

interface writingServiceInterface {
    var db: FirebaseFirestore
    fun addBalanceToDatabase(userId: String, balance: HashMap<String, Any?>): Task<DocumentReference>
    fun recordPayment(payment: HashMap<String, Any?>, balanceId: String?, userId: String): Task<DocumentReference>
    fun addToUsersTable(user: User): Task<Void>
}