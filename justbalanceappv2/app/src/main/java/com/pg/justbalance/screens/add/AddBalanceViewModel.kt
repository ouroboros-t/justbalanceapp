package com.pg.justbalance.screens.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.screens.balance.BalanceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddBalanceViewModel(
    val database: BalanceDatabaseDao,
    application: Application
): AndroidViewModel(application) {
    val balanceViewModel = BalanceViewModel(database,application)
    val balanceList = balanceViewModel.balanceList

    val viewModelJob = Job()


    private suspend fun insert(balance: Balance){
        withContext(Dispatchers.IO) {
            database.insert(balance)
        }
    }

    fun onAddBalance(balanceName: String, balanceAmount : Int) {
            val newBalance = Balance()
            newBalance.balanceName = balanceName
            newBalance.startingBalance = balanceAmount
            newBalance.currentBalance = balanceAmount

            balanceList.add(newBalance)

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        }

    }


