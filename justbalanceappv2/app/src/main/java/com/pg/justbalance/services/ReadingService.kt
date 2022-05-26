package com.pg.justbalance.services

import android.util.Log
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.rpc.context.AttributeContext
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.models.PaymentModel
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*

class readingService(
) : readingServiceInterface {
    private lateinit var listener: ListenerRegistration
    var payment = PaymentModel()

    override val db = FirebaseFirestore.getInstance()
    var service: AuthServiceInterface = AuthService()

    override suspend fun readBalances(userId: String): MutableList<BalanceModel> =
        suspendCancellableCoroutine { continuation ->
            var listBalance = mutableListOf<BalanceModel>()
            var query = db.collection("users").document(userId).collection("balances")

            listener = query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        listener.remove()
                        return continuation.resumeWith(Result.failure(error))
                    }
                    for (documentChange in value?.documentChanges!!)
                        if (documentChange.type == DocumentChange.Type.ADDED) {
                            val balance = documentChange.document.toObject(BalanceModel::class.java)
                            balance.balanceId = documentChange.document.id
                            payment.balanceId = documentChange.document.id
                            listBalance.add(balance)
                            listBalance.sortBy {
                                it.balanceName.lowercase(Locale.getDefault())
                            }
                        }
                    listener.remove()
                    return continuation.resumeWith(Result.success(listBalance))
                }
            })

        }


    override suspend fun readPayments(balanceId: String): MutableList<PaymentModel> =
        suspendCancellableCoroutine { continuation ->
            var listPayment = mutableListOf<PaymentModel>()
            var query = db.collection("users")
                .document(service.getCurrentUser()?.uid!!)
                .collection("balances").document(balanceId).collection("payments")

            listener = query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        listener.remove()
                        return continuation.resumeWith(Result.failure(error))
                    }
                    for (documentChange in value?.documentChanges!!)
                        if (documentChange.type == DocumentChange.Type.ADDED) {
                            payment = documentChange.document.toObject(PaymentModel::class.java)
                            payment.paymentId = documentChange.document.id
                            listPayment.add(payment)
                            listPayment.sortByDescending {
                                it.paymentDate
                            }

                        }
                    listener.remove()
                    return continuation.resumeWith(Result.success(listPayment))
                }
            })
        }

    override suspend fun getStartingBalance(balanceId: String): Any? =
        suspendCancellableCoroutine { continuation ->
            var query = db.collection("balances")
            var startingBalance: Double? = 0.0

            listener = query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    value?.documents?.forEach { ds ->
                        if (balanceId == ds.id) {
                            startingBalance = ds.toObject(BalanceModel::class.java)?.startingBalance
                        }
                    }
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        listener.remove()
                        return continuation.resumeWith(Result.failure(error))
                    }
                    listener.remove()
                    return continuation.resumeWith(Result.success(startingBalance))
                }
            })

        }

    override fun updateCurrentBalance(balanceId: String, bal: Double) {
        val currentBalanceToUpdateTo = bal
        db.collection("balances").document(balanceId)
            .update("currentBalance", currentBalanceToUpdateTo)
    }

}



