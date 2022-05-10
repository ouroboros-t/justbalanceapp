package com.pg.justbalance.firebase

import com.google.firebase.firestore.FirebaseFirestore

class deleteService: deleteServiceInterface {
    override val firestore = FirebaseFirestore.getInstance()
    override fun deleteService(balanceId: String) {
        deleteAll(balanceId)
        firestore.collection("balances").document(balanceId).delete()
    }
    override fun deleteAll(balanceId: String){
        firestore.collection("balances").document(balanceId).collection("payments").document().delete()
            .addOnSuccessListener {
                deleteService(balanceId)
            }
    }
}