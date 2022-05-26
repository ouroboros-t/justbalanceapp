package com.pg.justbalance.sharedViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.pg.justbalance.services.AuthService
import com.pg.justbalance.services.AuthServiceInterface

class UserViewModel(private val authService: AuthServiceInterface = AuthService()): ViewModel() {

    private var _user = MutableLiveData< FirebaseUser?>()
            val user : LiveData<FirebaseUser?> = _user

    fun getUser(){
        _user.value = authService.getCurrentUser()
    }
    fun signOut(){
        authService.signOut()
    }


}