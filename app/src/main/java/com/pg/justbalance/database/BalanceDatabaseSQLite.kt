package com.pg.justbalance.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BalanceDatabaseSQLite(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    //ID: INTEGER PRIMARY KEY
    //BALANCE_NAME: TEXT
    //BALANCE_AMOUNT: REAL
    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "BALANCE_DATABASE"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val TABLE_NAME = "balance_table"

        // below is the variable for id column
        val ID_COL = "id"

        // below is the variable for name column
        val NAME_COl = "balance_name"

        // below is the variable for amount column
        val AMOUNT_COL = "balance_amount"
    }

    override fun onCreate(database: SQLiteDatabase) {
        val query = "CREATE TABLE $TABLE_NAME " +
                "($ID_COL INTEGER PRIMARY KEY, $NAME_COl TEXT, $AMOUNT_COL REAL)"

        database.execSQL(query)
    }

    override fun onUpgrade(database: SQLiteDatabase, p1: Int, p2: Int) {
        //this methods checks to see if table already exists
        database.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }
    //add balance to database:
    fun addBalance(balanceName : String, amount: Double){
        val values = ContentValues()

        values.put(NAME_COl, balanceName)
        values.put(AMOUNT_COL, amount)

        // create a writable variable to
        // insert value in our database
        val database = this.writableDatabase

        database.insert(TABLE_NAME, null, values)
        //don't quite understand what nullColumnHack does yet


        //always close database
        database.close()
    }

    fun getBalances(): Cursor? {
        //create readable variable of
        //database to read value from it:
        val database = this.readableDatabase

        return database.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }


}