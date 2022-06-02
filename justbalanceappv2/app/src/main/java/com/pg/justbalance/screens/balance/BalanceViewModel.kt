package com.pg.justbalance.screens.balance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pg.justbalance.database.Balance
import com.pg.justbalance.decimalFormatDouble
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.services.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BalanceViewModel(
    private val readingService: readingServiceInterface = readingService(),
    private val authService: AuthServiceInterface = AuthService()
) : ViewModel() {
 var hasRan = false



    private var _sortingOption = MutableLiveData<SortingOptions>()
            val sortingOption: LiveData<SortingOptions> = _sortingOption
    private var currentSortingOptions: SortingOptions = SortingOptions.StartingBalance_High_Low
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
            if(!hasRan) {
                updateBalancesPerSorting()
            }
           hasRan = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun showTotalBalance(balances: List<BalanceModel>): String {
        var total = 0.0
        for (balance in balances) {
            total += balance.currentBalance
        }
        val totalFormatted = decimalFormatDouble(total.toBigDecimal())
        totalBalance = total.toString()
        return totalFormatted
    }

fun filterButtonPressed(option: String){
  _sortingOption.value = when(option){
        "A-Z" ->  SortingOptions.Balance_Name
        "$-$$" -> SortingOptions.StartingBalance_Low_High
        "$$-$" -> SortingOptions.StartingBalance_High_Low
        else -> SortingOptions.Balance_Name
    }
    updateBalancesPerSorting()
}

 fun updateBalancesPerSorting() {
     viewModelScope.launch {
     when (_sortingOption.value ) {
         SortingOptions.StartingBalance_High_Low -> {
             _balances.postValue(
                 readingService.readBalances(authService.getUserId()!!)
                     .sortByStartingBalanceHighToLow()
             )
         }
         SortingOptions.StartingBalance_Low_High -> {
             _balances.postValue(
                 readingService.readBalances(authService.getUserId()!!)
                     .sortByStartingBalanceLowToHigh())
         }
         SortingOptions.Balance_Name -> {
             _balances.postValue(
                 readingService.readBalances(authService.getUserId()!!).sortByBalanceName()
             )
         }
         else ->  _balances.postValue(
             readingService.readBalances(authService.getUserId()!!).sortByBalanceName()
         )
     }
 }
 }
}