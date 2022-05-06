package com.pg.justbalance.screens.add

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pg.justbalance.firebase.writingService
import com.pg.justbalance.firebase.writingServiceInterface
import kotlinx.coroutines.launch

class AddBalanceViewModel(
    private val service: writingServiceInterface = writingService()
) : ViewModel() {

    fun addBalance(balanceName: String, balanceAmount: Double) {
        viewModelScope.launch {
            val balance = hashMapOf<String, Any?>(
                "balanceName" to balanceName,
                "startingBalance" to balanceAmount,
                "currentBalance" to balanceAmount
            )
            service.addBalanceToDatabase(balance)
        }
    }

}

fun Editable.parseToDouble() = trim().toString().toDoubleOrNull() ?: 0.0


