package com.pg.justbalance.screens.payment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.database.Payment
import kotlinx.coroutines.launch
import java.util.*

class BalanceRecordPaymentViewModel
    (val database: BalanceDatabaseDao, application: Application)
    : AndroidViewModel(application) {

    fun addPayment(paymentAmount: Double){
        val date = Date()
        viewModelScope.launch {
            val newPayment = Payment()
            newPayment.paymentAmount = paymentAmount
            newPayment.paymentDate = date.toString()
            database.insertIntoPaymentsTable(newPayment)
            Log.i("HERE", newPayment.balanceId.toString())
            Log.i("HERE", newPayment.paymentId.toString())
            Log.i("HERE", newPayment.paymentAmount.toString())
            Log.i("HERE", newPayment.paymentDate)
        }

    }


    var _navigateToBalanceInfo = MutableLiveData<Long?>()
    val navigateToBalanceInfo
        get() = _navigateToBalanceInfo

}