package com.pg.justbalance.screens.balance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pg.justbalance.decimalFormatDouble
import com.pg.justbalance.firebase.readingService
import com.pg.justbalance.firebase.readingServiceInterface
import com.pg.justbalance.models.BalanceModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.math.BigDecimal

class BalanceViewModel(
    private val readingService: readingServiceInterface = readingService()
) : ViewModel() {
    private var hasRan = false

    private val _balances = MutableLiveData<MutableList<BalanceModel>>()
    val balances: LiveData<MutableList<BalanceModel>> = _balances

    //as the list gets bigger and bigger, you'll want these processes to run in the background/on
    //a different thread, so use coroutines
    val viewModelJob = Job()

    var totalBalance: String = ""


    //we need to use this somewhere..right?


    var _navigateToBalanceInfo = MutableLiveData<String?>()
    val navigateToBalanceInfo = _navigateToBalanceInfo


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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun showTotalBalance(balances: List<BalanceModel>): String {
        var total: BigDecimal = BigDecimal.ZERO
        for (balance in balances) {
            total += BigDecimal(balance.currentBalance)
        }
        val totalFormatted = decimalFormatDouble(total)
        totalBalance = total.toString()
        return totalFormatted
    }
}