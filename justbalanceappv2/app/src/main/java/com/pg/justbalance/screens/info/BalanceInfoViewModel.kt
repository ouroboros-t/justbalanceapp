package com.pg.justbalance.screens.info

import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.database.Payment
import com.pg.justbalance.models.BalanceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BalanceInfoViewModel(
    private val balanceId: String= "",
    dataSource: FirebaseFirestore
) : ViewModel() {

    val database = dataSource
    val viewModelJob = Job()
    private val balance =  MediatorLiveData<BalanceModel>()


    fun getBalance() = balance

    init{
        val ref = database.collection("balances").document(balanceId)
        ref as LiveData<BalanceModel>
      balance.addSource(ref, balance::setValue)
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

//    suspend fun deleteFromDatabase(balanceId: Long){
//        withContext(Dispatchers.IO) {
//                database.deleteBalanceWithId(balanceId)
//        }
//    }


   // var payments = database.getAllPayments()

    fun addPayment(balanceId: Long, paymentAmount: Double){

    }

    private val _payment = MutableLiveData<Payment>()
    val payment : LiveData<Payment>
        get() = _payment

    var _navigateToPaymentInfo = MutableLiveData<Long?>()
    val navigateToPaymentInfo
        get() = _navigateToPaymentInfo

    fun onPaymentItemClicked(id: Long) {
        _navigateToPaymentInfo.value = id
    }

    fun onPaymentItemInfoNavigated() {
        _navigateToPaymentInfo.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}

