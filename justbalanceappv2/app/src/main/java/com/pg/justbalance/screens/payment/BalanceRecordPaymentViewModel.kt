package com.pg.justbalance.screens.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.pg.justbalance.firebase.writingService
import com.pg.justbalance.firebase.writingServiceInterface
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class BalanceRecordPaymentViewModel
    (val service: writingServiceInterface = writingService(), )
    : ViewModel() {


    private var _successData= MutableLiveData<DocumentReference?>()
    val successData: LiveData<DocumentReference?> = _successData
    private var _errorData = MutableLiveData<Exception?>()
    val errorData: LiveData<Exception?> = _errorData


    fun addPayment(paymentAmount: Double, balanceId: String){
        val date = Date()
        viewModelScope.launch {
           val payment = hashMapOf<String, Any?>(
               "paymentAmount" to paymentAmount,
                "paymentDate" to date,
                "balanceId" to balanceId
           )
            Log.i("TAG", balanceId)
            service.recordPayment(payment, balanceId)
                .addOnSuccessListener {
                    _successData.postValue(it)
                }
                .addOnFailureListener {
                    _errorData.postValue(it)
                }
        }

    }
    fun resetData(){
        _successData.value = null
        _errorData.value = null
    }

}