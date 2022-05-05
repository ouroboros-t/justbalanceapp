package com.pg.justbalance.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class writingService : writingServiceInterface {


    override fun addBalanceToDatabase(balance: HashMap<String, Any?>) {

        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        firestore.collection("balances").add(balance)
    }
}