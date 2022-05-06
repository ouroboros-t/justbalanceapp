package com.pg.justbalance.firebase

import com.google.firebase.firestore.FirebaseFirestore

class writingService : writingServiceInterface {
    override var db = FirebaseFirestore.getInstance()

    override fun addBalanceToDatabase(balance: HashMap<String, Any?>) {

        db.collection("balances").add(balance)
    }

   override fun recordPayment(payment: HashMap<String, Any?>, balanceId: String?){
        db.collection("balances").document(balanceId!!)
            .collection("payments").add(payment)
    }
}