package com.pg.justbalance.screens.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.services.deleteService
import com.pg.justbalance.services.deleteServiceInterface
import com.pg.justbalance.models.PaymentModel
import com.pg.justbalance.services.AuthService
import com.pg.justbalance.services.AuthServiceInterface

class BalancePaymentInfoViewModel(
    private val paymentId: String = "",
    private val balanceId: String = "",
    dataSource: FirebaseFirestore
) : ViewModel() {
    private val authService : AuthServiceInterface = AuthService()
    private val service: deleteServiceInterface = deleteService()
    private val _payment = MutableLiveData<PaymentModel>()
    val paymentModel: LiveData<PaymentModel> = _payment
    val database = dataSource

    fun getPayment() = paymentModel

    init {
        database.collection("users").document(authService.auth.currentUser?.uid!!)
            .collection("balances").document(balanceId)
            .collection("payments").document(paymentId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                }
                _payment.postValue(value?.toObject(PaymentModel::class.java))
            }

    }

    private var _navigateToBalanceInfo = MutableLiveData<Boolean?>()
    val navigateToBalanceInfo: LiveData<Boolean?> = _navigateToBalanceInfo

    fun doneNavigating() {
        _navigateToBalanceInfo.value = null
    }

    fun onClose() {
        _navigateToBalanceInfo.value = true
    }

    fun deleteFromDatabase(balanceId: String, paymentId: String, userId:String) {
        service.deletePayment(paymentId, balanceId,userId)
    }


}