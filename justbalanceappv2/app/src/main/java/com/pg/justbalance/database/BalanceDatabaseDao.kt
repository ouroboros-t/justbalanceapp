package com.pg.justbalance.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface BalanceDatabaseDao {

    @Insert
    suspend fun insertToBalancesTable(balance: Balance)

    @Query("SELECT * FROM balances_table ORDER BY balance_id DESC")
    fun getAllBalances(): LiveData<List<Balance>>

    @Query("DELETE FROM balances_table")
    fun deleteAll()

    @Query("SELECT * FROM balances_table WHERE balance_id = :key")
    fun getBalanceWithId(key: Long): LiveData<Balance>

    @Query("SELECT * FROM payment_table WHERE balance_id = :key")
    fun getPaymentsOfSpecificBalance(key:Long): LiveData<Payment>

    @Query("SELECT * FROM payment_table ORDER BY payment_id DESC")
    fun getAllPayments(): LiveData<List<Payment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoPaymentsTable(){}

}