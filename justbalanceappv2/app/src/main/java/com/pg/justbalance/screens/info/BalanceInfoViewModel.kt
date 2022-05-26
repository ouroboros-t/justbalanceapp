package com.pg.justbalance.screens.info

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.decimalFormatDoubleCurrentBalance
import com.pg.justbalance.models.BalanceModel
import com.pg.justbalance.models.PaymentModel
import com.pg.justbalance.services.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BalanceInfoViewModel(
    private val balanceId: String = "",
    dataSource: FirebaseFirestore,
    private val readingService: readingServiceInterface = readingService(),
    private val deleteService: deleteServiceInterface = deleteService(),
    private val authService: AuthServiceInterface = AuthService()
) : ViewModel() {
    var hasRan = false

    val database = dataSource
    val viewModelJob = Job()

    private var _currentBalDouble = MutableLiveData<Double>()
    val currentBalDouble: LiveData<Double> = _currentBalDouble

    private var _currentBalanceString = MutableLiveData<String>()
    val currentBalanceString: LiveData<String> = _currentBalanceString

    private val _payments = MutableLiveData<MutableList<PaymentModel>>()
    val payments: LiveData<MutableList<PaymentModel>> = _payments

    private val _balanceModel = MutableLiveData<BalanceModel>()
    val balanceModel: LiveData<BalanceModel> = _balanceModel

    fun getBalance() = balanceModel

    init {
        database.collection("users").document(authService.auth.currentUser!!.uid)
            .collection("balances").document(balanceId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore error", error.message.toString())
            }
            _balanceModel.postValue(value?.toObject(BalanceModel::class.java))
        }
    }

    var _navigateToPaymentInfo = MutableLiveData<String?>()
    val navigateToPaymentInfo = _navigateToPaymentInfo

    fun onPaymentItemClicked(id: String) {
        _navigateToPaymentInfo.value = id
    }

    fun onPaymentItemInfoNavigated() {
        _navigateToPaymentInfo.value = null
    }

    fun readingService(balanceId: String) {
        viewModelScope.launch {
                _payments.postValue(readingService.readPayments(balanceId))
        }
    }

    fun calculateCurrentBalance(list: MutableList<PaymentModel>, balanceId: String, userId: String) {
        viewModelScope.launch {
           var startingBalance =
                readingService.getStartingBalance(balanceId, userId).toString().toDouble()
            if(list.isEmpty()){
                updateCurrentBalance(userId,balanceId, startingBalance)
            }else {
                list.forEach { payment ->
                    startingBalance -= payment.paymentAmount
                    val currentBalance = startingBalance
                    updateCurrentBalance(userId,balanceId, currentBalance)
                    _currentBalanceString.value =
                        decimalFormatDoubleCurrentBalance(currentBalance.toBigDecimal())
                }
            }
        }
    }

    fun updateCurrentBalance(userId: String,balanceId: String, bal: Double) {
        readingService.updateCurrentBalance(userId, balanceId, bal)
    }


    private val _navigateToBalances = MutableLiveData<Boolean?>()
    val navigateToBalances: LiveData<Boolean?> = _navigateToBalances


    fun doneNavigating() {
        _navigateToBalances.value = null
    }

    fun onClose() {
        _navigateToBalances.value = true
    }

    fun deleteFromDatabase(balanceId: String, userId: String) {
        viewModelScope.launch {
            deleteService.deleteService(balanceId, userId)
        }
    }

    private val _payment = MutableLiveData<PaymentModel>()
    val payment: LiveData<PaymentModel> = _payment


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}

