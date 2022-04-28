package com.pg.justbalance.screens.add

import android.app.Application
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.screens.balance.BalanceViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddBalanceViewModel(
    val database: BalanceDatabaseDao,
    application: Application
): AndroidViewModel(application) {
    val balanceViewModel = BalanceViewModel(database,application)

    val viewModelJob = Job()


  fun addBalance(balanceName: String, balanceAmount: Double) {
        viewModelScope.launch {
            val newBalance = Balance()
            newBalance.balanceName = balanceName
            newBalance.startingBalance = balanceAmount.toString()
            newBalance.currentBalance = balanceAmount.toString()

            database.insertToBalancesTable(newBalance)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        }

    }
fun Editable.parseToDouble() = trim().toString().toDoubleOrNull()?: 0.0


