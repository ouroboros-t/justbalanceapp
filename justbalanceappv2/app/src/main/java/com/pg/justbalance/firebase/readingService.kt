package com.pg.justbalance.firebase

import android.util.Log
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.pg.justbalance.database.Payment
import com.pg.justbalance.doStuff
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.screens.balance.BalanceFirestoreAdapter
import com.pg.justbalance.screens.balance.BalanceViewModel
import com.pg.justbalance.screens.payment.BalancePaymentAdapter
import java.util.*

class readingService(
) : readingServiceInterface {
    override var balanceList = mutableListOf<BalanceModel>()
    //var balanceViewModel = BalanceViewModel()
    override val balanceAdapter = BalanceFirestoreAdapter(balanceList, BalanceFirestoreAdapter.BalanceFirestoreListener {
        balanceId -> doStuff(balanceId)
    })
    override val paymentAdapter: BalancePaymentAdapter
        get() = TODO("Not yet implemented")

    override val db = FirebaseFirestore.getInstance()
    var paymentList = mutableListOf<Payment>()

    override fun readBalances() {
        db.collection("balances")
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    val balance = document.toObject(Balance::class.java)
//                    balanceList.add(balance)
//                    balanceAdapter.notifyDataSetChanged()
//                    Log.d("TAG", "${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w("TAG", "Error getting documents.", exception)
//            }
            // .orderBy("dateUsed", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for (documentChange in value?.documentChanges!!)
                        if (documentChange.type == DocumentChange.Type.ADDED) {
                            val balance = documentChange.document.toObject(BalanceModel::class.java)
                            balanceList.add(balance)
                            balanceList.sortBy {
                                it.balanceName.lowercase(Locale.getDefault())
                            }
                        }
                    balanceAdapter.notifyDataSetChanged()
                }
            })
    }

   override fun empty(): Boolean{
        return balanceList.isNullOrEmpty()
    }


    override fun readPayments(balanceId: String) {
        db.collection("balances")
            .document(balanceId).collection("payments")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }
                    for (documentChange in value?.documentChanges!!)
                        if (documentChange.type == DocumentChange.Type.ADDED) {
                            val payment = documentChange.document.toObject(Payment::class.java)
                            paymentList.add(payment)
                        }
                    paymentAdapter.notifyDataSetChanged()
                }
            })
    }
}