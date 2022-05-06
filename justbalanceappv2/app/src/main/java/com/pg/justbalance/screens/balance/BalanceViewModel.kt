package com.pg.justbalance.screens.balance

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.decimalFormatDouble
import com.pg.justbalance.firebase.readingService
import com.pg.justbalance.firebase.readingServiceInterface
import com.pg.justbalance.models.BalanceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.math.BigDecimal

class BalanceViewModel(
    private val readingService: readingServiceInterface = readingService()
) : ViewModel() {
    private var hasRan = false

    private val _balances = MutableLiveData<MutableList<BalanceModel>>()
    val balances: LiveData<MutableList<BalanceModel>> = _balances


    //EVENTUALLY USE LIVE DATA
    //FEATURE: UPDATE DEBTS, TRACK PAYMENTS MADE, LOG PAID OFF DEBTS

    // var addBalanceViewModel = AddBalanceViewModel(database, application)
    // var balances = database.getAllBalances()

    //as the list gets bigger and bigger, you'll want these processes to run in the background/on
    //a different thread, so use coroutines
    val viewModelJob = Job()

    var totalBalance: String = ""

    //var database = database

    //we need to use this somewhere..right?
    private val _balance = MutableLiveData<BalanceModel>()
    val balance: LiveData<BalanceModel>
        get() = _balance

    var _navigateToBalanceInfo = MutableLiveData<String?>()
    val navigateToBalanceInfo
        get() = _navigateToBalanceInfo

    fun onBalanceItemClicked(id: String) {
        _navigateToBalanceInfo.value = id
    }

    fun onBalanceItemInfoNavigated() {
        _navigateToBalanceInfo.value = null
    }

    fun runService() {
        viewModelScope.launch {
            if (!hasRan) {
                _balances.postValue(readingService.readBalances())
            }
            hasRan = true
        }
    }

    fun balanceListIsEmpty(): Boolean {
        return readingService.empty()
    }


    //   fun deleteFromDatabase(){
//        viewModelScope.launch(Dispatchers.IO) {
//            database.deleteAll()
//        }
//    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun showTotalBalance(balances: List<Balance>): BigDecimal {
        var total: BigDecimal = BigDecimal.ZERO
        for (balance in balances) {
            total += BigDecimal(balance.currentBalance)
        }
        val totalFormatted = decimalFormatDouble(total)
        totalBalance = total.toString()
        return total
    }

    fun getAdapter(): BalanceFirestoreAdapter {
        return readingService.balanceAdapter
    }

    fun getList(): MutableList<BalanceModel> {
        return readingService.balanceList
    }


}