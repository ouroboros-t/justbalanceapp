package com.pg.justbalance.screens.balance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BalanceViewModel : ViewModel(){
    //EVENTUALLY USE LIVE DATA
    //FEATURE: UPDATE DEBTS, TRACK PAYMENTS MADE, LOG PAID OFF DEBTS


    private val _balance = MutableLiveData<Double>()
    val balance : LiveData<Double>
        get() = _balance


}