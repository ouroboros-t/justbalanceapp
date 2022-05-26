package com.pg.justbalance.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.pg.justbalance.services.AuthService
import com.pg.justbalance.services.AuthServiceInterface

class LoginViewModel(
    private val authService: AuthServiceInterface = AuthService()
    ): ViewModel() {

        private var _signInSuccess = MutableLiveData<AuthResult>()
                val signInSuccess : LiveData<AuthResult> = _signInSuccess

        fun signIn(email: String, password: String){
            authService.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _signInSuccess.value = it
                }
        }
}