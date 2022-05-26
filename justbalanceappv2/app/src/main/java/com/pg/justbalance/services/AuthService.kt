package com.pg.justbalance.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthService : AuthServiceInterface {
    override val auth = FirebaseAuth.getInstance()
    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}