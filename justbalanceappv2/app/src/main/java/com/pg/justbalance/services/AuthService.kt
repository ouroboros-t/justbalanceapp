package com.pg.justbalance.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthService : AuthServiceInterface {
    private val auth = FirebaseAuth.getInstance()
    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun signInWithEmailAndPassword(email: String, password: String): Task<AuthResult?> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun createAuthUser(email: String, password: String): Task<AuthResult>{
       return auth.createUserWithEmailAndPassword(email, password)
    }

    override fun getUserId(): String? {
        return auth.currentUser?.uid
    }
    override fun sendVerificationEmail(): Task<Void>? {
        return auth.currentUser?.sendEmailVerification()
    }

    override fun getEmail(): String? {
        return auth.currentUser?.email
    }

}