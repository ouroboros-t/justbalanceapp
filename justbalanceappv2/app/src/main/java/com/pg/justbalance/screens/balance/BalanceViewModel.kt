package com.pg.justbalance.screens.balance

import android.app.Application
import androidx.lifecycle.*
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.decimalFormatDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.math.BigDecimal

class BalanceViewModel (database: BalanceDatabaseDao, application: Application) : AndroidViewModel(application){
    //EVENTUALLY USE LIVE DATA
    //FEATURE: UPDATE DEBTS, TRACK PAYMENTS MADE, LOG PAID OFF DEBTS

   // var addBalanceViewModel = AddBalanceViewModel(database, application)
    var balances = database.getAllBalances()

    //as the list gets bigger and bigger, you'll want these processes to run in the background/on
    //a different thread, so use coroutines
        val viewModelJob = Job()

    var totalBalance : String = ""

   var database = database

    //we need to use this somewhere..right?
    private val _balance = MutableLiveData<Balance>()
    val balance : LiveData<Balance>
        get() = _balance

   var _navigateToBalanceInfo = MutableLiveData<Long?>()
    val navigateToBalanceInfo
        get() = _navigateToBalanceInfo

    fun onBalanceItemClicked(id: Long) {
        _navigateToBalanceInfo.value = id
    }

    fun onBalanceItemInfoNavigated() {
        _navigateToBalanceInfo.value = null
    }





   fun deleteFromDatabase(){
        viewModelScope.launch(Dispatchers.IO) {
            database.deleteAll()
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun showTotalBalance(balances: List<Balance>):String{
        var total : BigDecimal = BigDecimal.ZERO
        for(balance in balances){
            total += BigDecimal(balance.currentBalance)
        }
        val totalFormatted = decimalFormatDouble(total)
        totalBalance = total.toString()
        return totalBalance
    }




}