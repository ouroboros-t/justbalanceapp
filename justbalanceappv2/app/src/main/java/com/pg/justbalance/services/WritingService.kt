package com.pg.justbalance.services

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class writingService : writingServiceInterface {
    override var db = FirebaseFirestore.getInstance()

    override fun addBalanceToDatabase(userId:String, balance: HashMap<String, Any?>): Task<DocumentReference> {
      return  db.collection("users").document(userId)
            .collection("balances").add(balance)
    }

    override fun recordPayment(
        payment: HashMap<String, Any?>,
        balanceId: String?
    ): Task<DocumentReference> {
        val task = db.collection("balances").document(balanceId!!)
            .collection("payments").add(payment)
        return task
    }
}