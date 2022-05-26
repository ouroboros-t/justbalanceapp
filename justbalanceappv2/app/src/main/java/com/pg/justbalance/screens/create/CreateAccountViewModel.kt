package com.pg.justbalance.screens.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.pg.justbalance.models.User
import com.pg.justbalance.services.AuthService
import com.pg.justbalance.services.AuthServiceInterface
import com.pg.justbalance.services.writingService
import com.pg.justbalance.services.writingServiceInterface

class CreateAccountViewModel(
    private val writingService: writingServiceInterface = writingService(),
    private val authService: AuthServiceInterface = AuthService()
) : ViewModel() {
    private var _successCreated = MutableLiveData<AuthResult>()
            val successCreated : LiveData<AuthResult> = _successCreated

    fun createUser(email: String, password: String){
        authService.createAuthUser(email, password).addOnSuccessListener {
            val user = User(userID = authService.auth.currentUser!!.uid)
            user.email = email
            user.userID = authService.auth.currentUser!!.uid
            _successCreated.value = it
            writingService.addToUsersTable(user)
        }


    }

}