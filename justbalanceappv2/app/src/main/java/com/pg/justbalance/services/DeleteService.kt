package com.pg.justbalance.services

import com.google.firebase.firestore.FirebaseFirestore

class deleteService: deleteServiceInterface {
    var service: AuthServiceInterface = AuthService()
    override val firestore = FirebaseFirestore.getInstance()
    override fun deleteService(balanceId: String, userId:String) {
        deleteAll(balanceId, userId)
        firestore.collection("users").document(userId)
            .collection("balances").document(balanceId).delete()
    }
    override fun deleteAll(balanceId: String, userId: String){
        firestore.collection("users").document(userId)
            .collection("balances").document(balanceId).collection("payments").document().delete()
            .addOnSuccessListener {
                deleteService(balanceId, userId)
            }
    }

    override fun deletePayment(paymentId: String, balanceId: String, userId: String){
        firestore.collection("users").document(userId)
            .collection("balances").document(balanceId)
            .collection("payments").document(paymentId).delete()
    }

}