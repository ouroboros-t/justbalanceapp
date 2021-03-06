package com.pg.justbalance.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "balances_table")
data class Balance (
        @PrimaryKey(autoGenerate = true)
        var balanceId: Long = 0L,

        @ColumnInfo(name = "starting_balance")
        val startingBalance : Double = 0.0,

        //before any updates, currentBalance is same as starting Balance
        @ColumnInfo(name = "current_balance")
        var currentBalance: Double = startingBalance

        )