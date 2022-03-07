package com.pg.justbalance.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface BalanceDatabaseDao {

    @Insert
    fun insert(balance: Balance)

    @Query("SELECT * FROM balances_table ORDER BY balanceId DESC")
    fun getAllBalances(): LiveData<List<Balance>>

}