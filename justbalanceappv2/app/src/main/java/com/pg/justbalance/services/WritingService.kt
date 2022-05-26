package com.pg.justbalance.services

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.models.User

class writingService : writingServiceInterface {
    override var db = FirebaseFirestore.getInstance()

    override fun addBalanceToDatabase(userId:String, balance: HashMap<String, Any?>): Task<DocumentReference> {
      return  db.collection("users").document(userId)
            .collection("balances").add(balance)
    }

    override fun recordPayment(
        payment: HashMap<String, Any?>,
        balanceId: String?,
        userId: String
    ): Task<DocumentReference> {
        return db.collection("users").document(userId).collection("balances").document(balanceId!!)
            .collection("payments").add(payment)

    }

    override fun addToUsersTable(user: User): Task<Void>{
        val userToAdd = hashMapOf(
            "email" to user.email
        )

        return db.collection("users").document(user.userID).set(userToAdd)
    }

}