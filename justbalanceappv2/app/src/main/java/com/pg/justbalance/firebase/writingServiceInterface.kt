package com.pg.justbalance.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference

interface writingServiceInterface {

    fun addBalanceToDatabase(balance: HashMap<String, Any?>)
}