package com.pg.justbalance.screens.payment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pg.justbalance.firebase.writingService
import com.pg.justbalance.firebase.writingServiceInterface
import kotlinx.coroutines.launch
import java.util.*

class BalanceRecordPaymentViewModel
    (val service: writingServiceInterface = writingService(),
private val balanceId: String= "")
    : ViewModel() {

    fun addPayment(paymentAmount: Double){
        val date = Date()
        viewModelScope.launch {
           val payment = hashMapOf<String, Any?>(
               "paymentAmount" to paymentAmount,
                "paymentDate" to date,
                "balanceId" to balanceId
           )
            Log.i("TAG", balanceId)
            service.recordPayment(payment, balanceId)

//            val newPayment = Payment()
//            newPayment.paymentAmount = paymentAmount
//            newPayment.paymentDate = date.toString()
//            database.insertIntoPaymentsTable(newPayment)
//            Log.i("HERE", newPayment.balanceId.toString())
//            Log.i("HERE", newPayment.paymentId.toString())
//            Log.i("HERE", newPayment.paymentAmount.toString())
//            Log.i("HERE", newPayment.paymentDate)
        }

    }


    var _navigateToBalanceInfo = MutableLiveData<Long?>()
    val navigateToBalanceInfo
        get() = _navigateToBalanceInfo

}