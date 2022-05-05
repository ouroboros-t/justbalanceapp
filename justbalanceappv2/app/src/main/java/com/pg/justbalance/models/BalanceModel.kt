package com.pg.justbalance.models

import androidx.room.ColumnInfo
import com.google.firebase.firestore.PropertyName

class BalanceModel {
    var balanceId: String = ""

    @PropertyName("balanceName")
    var balanceName : String = ""

    @PropertyName("startingBalance")
    var startingBalance : Double= 0.0

    //before any updates, currentBalance is same as starting Balance

    @PropertyName("currentBalance")
    var currentBalance: Double = startingBalance
}