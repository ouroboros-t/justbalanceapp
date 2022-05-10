package com.pg.justbalance.screens.payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.database.Payment
import com.pg.justbalance.models.PaymentModel

class BalancePaymentViewModel ()
        : ViewModel() {

    private val _payment = MutableLiveData<PaymentModel>()
    val payment : LiveData<PaymentModel>
        get() = _payment

    var _navigateToPaymentInfo = MutableLiveData<Long?>()
    val navigateToPaymentInfo
        get() = _navigateToPaymentInfo

    fun onBalanceItemClicked(id: Long) {
        _navigateToPaymentInfo.value = id
    }

    fun onPaymentItemInfoNavigated() {
        _navigateToPaymentInfo.value = null
    }

    /*
    private val _payment = MutableLiveData<Payment>()
    val balance : LiveData<Payment>
        get() = _payment

   var _navigateToPaymentInfo = MutableLiveData<Long?>()
    val navigateToPaymentInfo
        get() = _navigateToPaymentInfo

    fun onBalanceItemClicked(id: Long) {
        _navigateToPaymentInfo.value = id
    }

    fun onPaymentItemInfoNavigated() {
        _navigateToPaymentInfo.value = null
    }*/


}