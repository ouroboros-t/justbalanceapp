package com.pg.justbalance.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase



@Database(entities = [Balance::class], version = 3)
abstract class BalanceDatabase: RoomDatabase() {
    //implementation is done by the RoomDatabase, that's why it's abstract class
    abstract val balanceDatabaseDao : BalanceDatabaseDao


    //allow client to access methods for getting database without instantiating class:
    companion object {
        //Since the schema doesn’t change, we just need to provide an empty migration implementation and tell Room to use it.
        val MIGRATION_1_2  = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                //we aren't doing anything here, because the schema isn't changing
            }
        }
        @Volatile
        private var INSTANCE: BalanceDatabase? = null

        fun getInstance(context: Context): BalanceDatabase {
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        BalanceDatabase::class.java,
                        "balance_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }





}