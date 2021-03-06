package com.pg.justbalance.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface AuthServiceInterface {
    fun getCurrentUser():FirebaseUser?
    fun signInWithEmailAndPassword(email:String, password:String): Task<AuthResult?>
    fun signOut()
    fun createAuthUser(email: String, password: String): Task<AuthResult>
    fun getUserId(): String?
    fun sendVerificationEmail(): Task<Void>?
    fun getEmail():String?
}