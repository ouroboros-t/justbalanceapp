package com.pg.justbalance.screens.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.pg.justbalance.services.AuthService
import com.pg.justbalance.services.AuthServiceInterface
import com.pg.justbalance.services.writingService
import com.pg.justbalance.services.writingServiceInterface
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class BalanceRecordPaymentViewModel
    (val service: writingServiceInterface = writingService(),
     val authService: AuthServiceInterface = AuthService()       )
    : ViewModel() {


    private var _successData= MutableLiveData<DocumentReference?>()
    val successData: LiveData<DocumentReference?> = _successData
    private var _errorData = MutableLiveData<Exception?>()
    val errorData: LiveData<Exception?> = _errorData


    fun addPayment(paymentAmount: Double, balanceId: String){
        val userId = authService.getUserId()
        val date = Date()
        viewModelScope.launch {
           val payment = hashMapOf<String, Any?>(
               "paymentAmount" to paymentAmount,
                "paymentDate" to date,
                "balanceId" to balanceId
           )
            Log.i("TAG", balanceId)
            service.recordPayment(payment, balanceId, userId!!)
                .addOnSuccessListener {
                    _successData.postValue(it)
                    Log.i("successData", _successData.value.toString())
                }
                .addOnFailureListener {
                    _errorData.postValue(it)
                    Log.i("errorData", _errorData.value.toString())
                }
        }

    }
    fun resetData(){
        _successData.value = null
        _errorData.value = null
    }

}