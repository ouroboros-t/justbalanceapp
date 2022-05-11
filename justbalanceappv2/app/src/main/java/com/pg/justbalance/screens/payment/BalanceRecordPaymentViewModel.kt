package com.pg.justbalance.screens.payment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pg.justbalance.firebase.writingService
import com.pg.justbalance.firebase.writingServiceInterface
import kotlinx.coroutines.launch
import java.util.*

class BalanceRecordPaymentViewModel
    (val service: writingServiceInterface = writingService(), )
    : ViewModel() {

    private var _successOrFailToAddPayment = MutableLiveData<Boolean>()
    val successOrFailToAddPayment: LiveData<Boolean> = _successOrFailToAddPayment

    fun addPayment(paymentAmount: Double, balanceId: String){
        val date = Date()
        viewModelScope.launch {
           val payment = hashMapOf<String, Any?>(
               "paymentAmount" to paymentAmount,
                "paymentDate" to date,
                "balanceId" to balanceId
           )
            Log.i("TAG", balanceId)
            service.recordPayment(payment, balanceId).addOnCompleteListener {
                task->
                if(task.isSuccessful){
                    _successOrFailToAddPayment.postValue(true)
                }else{
                    _successOrFailToAddPayment.postValue(false)
                }
            }
        }

    }

}