package com.pg.justbalance.database

import androidx.room.*
import java.util.*
import kotlin.reflect.KClass


@Entity(tableName = "payment_table")
class Payment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_id")
    var paymentId: Long = 0L

    //needs to be foreign key constraint
    @ColumnInfo(name = "balance_id")
    var balanceId: Long = Balance().balanceId

    @ColumnInfo(name = "payment_amount")
    var paymentAmount: Double = 0.0

    @ColumnInfo(name = "payment_date")
    var paymentDate: String = ""

}