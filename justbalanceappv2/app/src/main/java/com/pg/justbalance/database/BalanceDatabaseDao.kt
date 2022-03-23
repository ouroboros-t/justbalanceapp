package com.pg.justbalance.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface BalanceDatabaseDao {

    @Insert
    suspend fun insert(balance: Balance)

    @Query("SELECT * FROM balances_table ORDER BY balance_id DESC")
    fun getAllBalances(): LiveData<List<Balance>>

    @Query("DELETE FROM balances_table")
    fun deleteAll()
}