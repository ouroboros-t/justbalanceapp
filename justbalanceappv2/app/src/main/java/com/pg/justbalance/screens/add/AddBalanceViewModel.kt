package com.pg.justbalance.screens.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabaseDao

class AddBalanceViewModel(
    val database: BalanceDatabaseDao,
    application: Application
): AndroidViewModel(application) {



    fun insert(balance: Balance){
        database.insert(balance)
    }





}