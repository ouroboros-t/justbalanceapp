package com.pg.justbalance.screens.info

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabaseDao
import com.pg.justbalance.database.Payment
import com.pg.justbalance.decimalFormatDouble
import com.pg.justbalance.decimalFormatDoubleCurrentBalance
import com.pg.justbalance.firebase.deleteService
import com.pg.justbalance.firebase.deleteServiceInterface
import com.pg.justbalance.firebase.readingService
import com.pg.justbalance.firebase.readingServiceInterface
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.models.PaymentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class BalanceInfoViewModel(
    private val balanceId: String= "",
    dataSource: FirebaseFirestore,
    private val readingService: readingServiceInterface = readingService(),
    private val deleteService: deleteServiceInterface = deleteService()
) : ViewModel() {
    var hasRan= false

    val database = dataSource
    val viewModelJob = Job()
    private val balance =  MediatorLiveData<BalanceModel>()


    private var _currentBalDouble = MutableLiveData<Double>()
        val currentBalDouble : LiveData<Double> = _currentBalDouble

    private var _currentBalanceString = MutableLiveData<String>()
            val currentBalanceString: LiveData<String> = _currentBalanceString
    private val _balanceModel = MutableLiveData<BalanceModel>()
        val balanceModel : LiveData<BalanceModel> = _balanceModel

    private val _payments = MutableLiveData<MutableList<PaymentModel>>()
    val payments: LiveData<MutableList<PaymentModel>> = _payments

    fun getBalance() = balanceModel

    init{
         database.collection("balances").document(balanceId).addSnapshotListener { value, error ->
             if (error != null) {
                 Log.e("Firestore error", error.message.toString())
             }
             _balanceModel.postValue(value?.toObject(BalanceModel::class.java))
         }

    }

    fun readingService(balanceId: String){
        viewModelScope.launch {
            if(!hasRan) {
                Log.i("Balance id: ", balanceId)
               _payments.postValue(readingService.readPayments(balanceId))
            }
            hasRan = true
        }
    }
    fun calculateCurrentBalance(list: MutableList<PaymentModel>, balanceId: String){
        var currentBalance = 0.0
        viewModelScope.launch {
                currentBalance =
                    readingService.calculateCurrentBalance(balanceId).toString().toDouble()
                Log.i("currentBalance:", currentBalance.toString())
                list.forEach { payment ->
                    currentBalance -= payment.paymentAmount
                    _currentBalDouble.value = currentBalance
                    Log.i("balance", currentBalance.toString())
                    _currentBalanceString.value =
                        decimalFormatDoubleCurrentBalance(currentBalance.toBigDecimal())
                }
        }
    }

    //todo: this doesn't work -> putting this inside of fun above will cause it to constantly update
    fun updateCurrentBalance(balanceId: String, bal: Double){

           // readingService.updateCurrentBalance(balanceId, bal)

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

fun deleteFromDatabase(balanceId: String){
       viewModelScope.launch {
           deleteService.deleteService(balanceId)
       }
    }

    private val _payment = MutableLiveData<PaymentModel>()
    val payment : LiveData<PaymentModel>
        get() = _payment

    var _navigateToPaymentInfo = MutableLiveData<String?>()
    val navigateToPaymentInfo
        get() = _navigateToPaymentInfo

    fun onPaymentItemClicked(id: String) {
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

