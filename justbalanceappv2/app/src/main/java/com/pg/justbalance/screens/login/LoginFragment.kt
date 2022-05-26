package com.pg.justbalance.screens.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.pg.justbalance.isValidPasswordFormat

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

        setupActionBar()

        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(LOGIN_SUCCESSFUL, false)

        binding.enterEmailAddressBox.setupClearButtonWithAction()
        binding.enterPasswordBox.setupClearButtonWithAction()
        loginButtonEnabledOnceEmailAndPasswordAreValid()

        binding.loginButton.setOnClickListener {
            val email = binding.enterEmailAddressBox.text.toString()
            Log.i("TAG email", email)
            val pass = binding.enterPasswordBox.text.toString()
            loginViewModel.signIn(email, pass)

        }

        binding.createAccountButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loginViewModel.signInSuccess.observe(viewLifecycleOwner, Observer { result ->
            if(result != null){
                savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                findNavController().navigate(R.id.action_loginFragment_to_balanceFragment)
            }else{
                Toast.makeText(activity, "Something went wrong :(", Toast.LENGTH_LONG).show()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupActionBar() {
        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (context as AppCompatActivity).supportActionBar!!.title = ""
    }

    fun EditText.setupClearButtonWithAction() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.close else 0
                setPadding(51, 0, 51, 0)
                setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        setOnTouchListener(
            View.OnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                        this.setText("")
                        binding.loginButton.disableLoginButton()
                        return@OnTouchListener true
                    }
                }
                return@OnTouchListener false
            }
        )
    }

    fun loginButtonEnabledOnceEmailAndPasswordAreValid() {
        binding.loginButton.disableLoginButton()
        binding.enterEmailAddressBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editText: Editable) {
                if (!isValidPasswordFormat(binding.enterPasswordBox.text.toString())) {
                    binding.loginButton.disableLoginButton()
                } else if (isValidPasswordFormat(binding.enterPasswordBox.text.toString()) &&
                    (android.util.Patterns.EMAIL_ADDRESS.matcher(binding.enterEmailAddressBox.text.toString().trim())
                        .matches())){
                    binding.loginButton.enableLoginButton()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                evaluateEmailValidOrInvalid()
            }
        })

        binding.enterPasswordBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editText: Editable) {

                if (!isValidPasswordFormat(binding.enterPasswordBox.text.toString())) {
                    binding.loginButton.disableLoginButton()
                } else if ((android.util.Patterns.EMAIL_ADDRESS.matcher(binding.enterEmailAddressBox.text.toString().trim())
                        .matches()) && isValidPasswordFormat(binding.enterPasswordBox.text.toString())){
                    binding.loginButton.enableLoginButton()
                } else{
                    binding.loginButton.disableLoginButton()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.loginButton.disableLoginButton()
            }
        })
    }
    private fun invalidEmailError() {
        binding.emailError.visibility = View.VISIBLE
        binding.emailError.text = getString(R.string.invalid_email)
        binding.loginButton.disableLoginButton()
    }

    fun evaluateEmailValidOrInvalid() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.enterEmailAddressBox.text.toString().trim())
                .matches()
        ) {
            invalidEmailError()
        } else {
            binding.emailError.visibility = View.GONE
        }
    }
    private fun invalidEmailOrPasswordError() {
        binding.passwordError.visibility = View.VISIBLE
        binding.passwordError.text = getString(R.string.incorrect_email_or_password)
    }

    private fun Button.enableLoginButton() {
        binding.loginButton.isEnabled = true
        binding.loginButton.alpha = 1f
    }

    private fun Button.disableLoginButton() {
        binding.loginButton.isEnabled = false
        binding.loginButton.alpha = .5f
    }





}