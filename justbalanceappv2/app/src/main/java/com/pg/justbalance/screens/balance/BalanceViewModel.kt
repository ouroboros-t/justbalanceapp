package com.pg.justbalance.screens.balance

import android.app.AlertDialog
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.screens.add.AddBalanceViewModel
import kotlinx.coroutines.Job

class BalanceViewModel (database: BalanceDatabaseDao, application: Application) : AndroidViewModel(application){
    //EVENTUALLY USE LIVE DATA
    //FEATURE: UPDATE DEBTS, TRACK PAYMENTS MADE, LOG PAID OFF DEBTS

   // var addBalanceViewModel = AddBalanceViewModel(database, application)
    var balances = database.getAllBalances()

    //as the list gets bigger and bigger, you'll want these processes to run in the background/on
    //a different thread, so use coroutines
        val viewModelJob = Job()



    //we need to use this somewhere..right?
    private val _balance = MutableLiveData<Balance>()
    val balance : LiveData<Balance>
        get() = _balance

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}