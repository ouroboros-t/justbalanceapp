package com.pg.justbalance.screens.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.firebase.deleteService
import com.pg.justbalance.firebase.deleteServiceInterface
import com.pg.justbalance.models.PaymentModel

class BalancePaymentInfoViewModel (
    private val paymentId: String = "",
    private val balanceId: String = "",
    dataSource: FirebaseFirestore
        )
        : ViewModel() {
    private val service: deleteServiceInterface = deleteService()
    private val _payment = MutableLiveData<PaymentModel>()
    val paymentModel : LiveData<PaymentModel> = _payment
    val database = dataSource

    fun getPayment() = paymentModel

    init{
        database.collection("balances").document(balanceId)
            .collection("payments").document(paymentId)
            .addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore error", error.message.toString())
            }
                Log.i("hi", _payment.value.toString())
            _payment.postValue(value?.toObject(PaymentModel::class.java))
        }

    }

    private var _navigateToBalanceInfo = MutableLiveData<Boolean?>()
            val navigateToBalanceInfo: LiveData<Boolean?> = _navigateToBalanceInfo

    fun doneNavigating(){
        _navigateToBalanceInfo.value = null
    }

    fun onClose(){
        _navigateToBalanceInfo.value = true
    }

    fun deleteFromDatabase(balanceId: String, paymentId: String) {
            service.deletePayment(paymentId, balanceId)
    }


}