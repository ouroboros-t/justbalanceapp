package com.pg.justbalance.screens.payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.pg.justbalance.database.BalanceDatabaseDao

class BalancePaymentViewModel (
        val database: BalanceDatabaseDao,
        application: Application)
        : AndroidViewModel(application) {


    fun addPayment(balanceId: Long, paymentAmount: Double){

    }
}