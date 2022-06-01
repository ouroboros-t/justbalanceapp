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

        private var _signInSuccess = MutableLiveData<AuthResult?>()
                val signInSuccess : LiveData<AuthResult?> = _signInSuccess
        private var _signInError = MutableLiveData<Exception?>()
                val signInError : LiveData<Exception?> = _signInError

        fun signIn(email: String, password: String){
            authService.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _signInSuccess.value = it
                }
                .addOnFailureListener {
                    _signInError.value = it
                }
        }

        fun resetData(){
            _signInSuccess.value = null
            _signInError.value = null

        }

}