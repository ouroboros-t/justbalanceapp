package com.pg.justbalance.screens.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.AuthResult
import com.pg.justbalance.R
import com.pg.justbalance.databinding.LoginLayoutBinding
import com.pg.justbalance.sharedViewModels.UserViewModel

class LoginFragment: Fragment(R.layout.login_layout) {
    private lateinit var binding: LoginLayoutBinding
    private lateinit var loginViewModel: LoginViewModel
    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var savedStateHandle: SavedStateHandle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_layout, container, false)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginViewModel = loginViewModel

        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(LOGIN_SUCCESSFUL, false)




        binding.loginButton.setOnClickListener {
            val email = binding.enterEmailAddressBox.text.toString()
            Log.i("TAG email", email)
            val pass = binding.enterPasswordBox.text.toString()
            loginViewModel.signIn(email, pass)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loginViewModel.signInSuccess.observe(viewLifecycleOwner, Observer { result ->
            if(result != null){
                savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                findNavController().popBackStack()
            }else{
                Toast.makeText(activity, "Something went wrong :(", Toast.LENGTH_LONG).show()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

}