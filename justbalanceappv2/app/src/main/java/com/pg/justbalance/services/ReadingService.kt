package com.pg.justbalance.services

import android.util.Log
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.rpc.context.AttributeContext
import com.pg.justbalance.database.Balance
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.models.PaymentModel
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

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
                    listener.remove()
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        listener.remove()
                        continuation.resumeWith(Result.failure(error))
                    }else {
                        for (documentChange in value?.documentChanges!!) {
                            if (documentChange.type == DocumentChange.Type.ADDED) {
                                val balance =
                                    documentChange.document.toObject(BalanceModel::class.java)
                                balance.balanceId = documentChange.document.id
                                payment.balanceId = documentChange.document.id
                                listBalance.add(balance)
//                            listBalance.sortBy {
//                                it.balanceName.lowercase(Locale.getDefault())
//                            }
                            }
                        }
                        continuation.resumeIfActive(listBalance)
                    }
                }
            })
            continuation.invokeOnCancellation { listener.remove() }

        }
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T> CancellableContinuation<T>.resumeIfActive(value: T) {
        if(isActive) {
            resume(value)
        }
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

    override suspend fun getStartingBalance(balanceId: String, userId: String): Any? =
        suspendCancellableCoroutine { continuation ->
            var query = db.collection("users").document(userId)
                .collection("balances")
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

    override fun updateCurrentBalance(userId: String, balanceId: String, bal: Double) {
        val currentBalanceToUpdateTo = bal
        db.collection("users").document(userId)
            .collection("balances").document(balanceId)
            .update("currentBalance", currentBalanceToUpdateTo)
    }

}

enum class SortingOptions {
    StartingBalance_Low_High,
    StartingBalance_High_Low,
    Balance_Name
}

fun filter(option: String) {
    when (option) {

    }
}

fun MutableList<BalanceModel>.sortByBalanceName(): MutableList<BalanceModel> {
    sortBy {
        it.balanceName
    }
    return this
}

fun MutableList<BalanceModel>.sortByStartingBalanceLowToHigh(): MutableList<BalanceModel> {
    sortBy {
        it.currentBalance
    }
    return this
}

fun MutableList<BalanceModel>.sortByStartingBalanceHighToLow(): MutableList<BalanceModel> {
    sortByDescending {
        it.currentBalance
    }
    return this
}


