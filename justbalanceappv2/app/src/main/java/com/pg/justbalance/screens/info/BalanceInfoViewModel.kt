package com.pg.justbalance.screens.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabaseDao

class BalanceInfoViewModel(
    private val balanceId: Long = 0L,
    dataSource: BalanceDatabaseDao
) : ViewModel() {

    val database = dataSource

    private val balance = MediatorLiveData<Balance>()

    fun getBalance() = balance

    init{
      balance.addSource(database.getBalanceWithId(balanceId), balance::setValue)
    }


    private val _navigateToPaymentScreen = MutableLiveData<Boolean?>()
        val navigateToPaymentScreen: LiveData<Boolean?>
            get()=_navigateToPaymentScreen


    private val _navigateToBalances = MutableLiveData<Boolean?>()

    val navigateToBalances: LiveData<Boolean?>
        get()=_navigateToBalances


    fun doneNavigating(){
        _navigateToBalances.value = null
    }

    fun onClose(){
        _navigateToBalances.value = true
    }


}