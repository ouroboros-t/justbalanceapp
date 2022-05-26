package com.pg.justbalance.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface AuthServiceInterface {
    val auth: FirebaseAuth
    fun getCurrentUser():FirebaseUser?
}