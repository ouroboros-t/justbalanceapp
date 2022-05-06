package com.pg.justbalance.firebase

import com.google.firebase.firestore.FirebaseFirestore

class deleteService: deleteServiceInterface {
    override val firestore = FirebaseFirestore.getInstance()
    override fun deleteService(balanceId: String) {
        firestore.collection("balances").document(balanceId).delete()
    }
}