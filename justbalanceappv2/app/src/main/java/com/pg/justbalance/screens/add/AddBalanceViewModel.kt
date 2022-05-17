package com.pg.justbalance.screens.add

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.pg.justbalance.firebase.writingService
import com.pg.justbalance.firebase.writingServiceInterface
import kotlinx.coroutines.launch
import java.lang.Exception

class AddBalanceViewModel(
    private val service: writingServiceInterface = writingService()
) : ViewModel() {
    private var _success = MutableLiveData<DocumentReference?>()
    val success : LiveData<DocumentReference?> = _success
    private var _failure = MutableLiveData<Exception?>()
    val failure : LiveData<Exception?> = _failure

    fun addBalance(balanceName: String, balanceAmount: Double) {
        viewModelScope.launch {
            val balance = hashMapOf<String, Any?>(
                "balanceName" to balanceName,
                "startingBalance" to balanceAmount,
                "currentBalance" to balanceAmount
            )
            service.addBalanceToDatabase(balance)
                .addOnSuccessListener {
                    _success.postValue(it)
                }
                .addOnFailureListener {
                    _failure.postValue(it)
                }
        }
    }

}

fun Editable.parseToDouble() = trim().toString().toDoubleOrNull() ?: 0.0


