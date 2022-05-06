package com.pg.justbalance

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pg.justbalance.database.Balance
import com.pg.justbalance.screens.balance.BalanceViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat


fun decimalFormatDouble(item: BigDecimal): String{
    var decimalFormat = DecimalFormat("0.00")
    decimalFormat.roundingMode = RoundingMode.DOWN
    val formattedItem = decimalFormat.format(item)
    return "$ ${formattedItem}"
}

//todo: dateformatter

fun doStuff(id: String){
    val balanceViewModel = BalanceViewModel()
    balanceViewModel.onBalanceItemClicked(id)
}