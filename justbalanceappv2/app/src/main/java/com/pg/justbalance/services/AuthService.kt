package com.pg.justbalance.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthService : AuthServiceInterface {
    override val auth = FirebaseAuth.getInstance()
    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult?> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    override fun signOut() {
        auth.signOut()
    }
}