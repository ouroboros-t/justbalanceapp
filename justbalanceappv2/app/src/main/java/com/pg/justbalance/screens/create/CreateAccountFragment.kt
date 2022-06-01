package com.pg.justbalance.screens.create

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.pg.justbalance.R
import com.pg.justbalance.databinding.CreateAccountLayoutBinding
import com.pg.justbalance.isValidPasswordFormat
import com.pg.justbalance.sharedViewModels.UserViewModel

class CreateAccountFragment: Fragment(R.layout.create_account_layout) {
    private lateinit var binding: CreateAccountLayoutBinding
    private lateinit var createAccountViewModel: CreateAccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home ->
                findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)

        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.create_account_layout,
                container, false)

        setupActionBar()
        createAccountButtonEnabledOnceEmailPasswordAndToggleAreValid()
        createAccountViewModel = ViewModelProvider(this).get(CreateAccountViewModel::class.java)

        binding.createAccountViewModel = createAccountViewModel


        binding.enterEmailAddressBox.setupClearButtonWithAction()
        binding.enterPasswordBox.setupClearButtonWithAction()
        binding.confirmCreateAccountButton.setOnClickListener {
            val email = binding.enterEmailAddressBox.text.toString()
            val password = binding.enterPasswordBox.text.toString()
            createAccountViewModel.createUser(email, password)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createAccountViewModel.successSentEmail.observe(viewLifecycleOwner, Observer {
            if(it == true){
                findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)
                showEmailSentToast()
            }
        })
        createAccountViewModel.failureSentEmail.observe(viewLifecycleOwner, Observer {
            if(it != null){
                showFailedToSendEmailToast()
            }
        })
        createAccountViewModel.failedToCreate.observe(viewLifecycleOwner, Observer { exception ->
            if(exception is FirebaseAuthUserCollisionException){
                accountAlreadyExistsError()
            }else {
                somethingWentWrongToast()
            }
        })


//        createAccountViewModel.successCreated.observe(viewLifecycleOwner, Observer {
//            if(it != null){
//                        findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)
//            }
//        })
//        createAccountViewModel.failedToCreate.observe(viewLifecycleOwner, Observer { exception ->
//            if(exception is FirebaseAuthUserCollisionException){
//                accountAlreadyExistsError()
//            }else {
//                somethingWentWrongToast()
//            }
//        })
        super.onViewCreated(view, savedInstanceState)
    }

    fun accountAlreadyExistsError() {
        binding.emailError.visibility = View.VISIBLE
        binding.emailError.text = getString(R.string.JB_account_already_exists)
    }

    fun showEmailSentToast() {
        val toast = Toast(activity)
        toast.setText(R.string.verification_email_sent_message)
        toast.setDuration(Toast.LENGTH_LONG)
        toast.show()
    }

    fun showFailedToSendEmailToast(){
        val toast = Toast(activity)
        toast.setText(R.string.verification_email_sent_error_message)
        toast.setDuration(Toast.LENGTH_LONG)
        toast.show()
    }

    fun somethingWentWrongToast() {
        val toast = Toast(activity)
       toast.setText(R.string.something_went_wrong_error_message)
        toast.setDuration(Toast.LENGTH_LONG)
        toast.show()
    }



    private fun setupActionBar() {
        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (context as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (context as AppCompatActivity).supportActionBar!!.title = getString(R.string.create_jb_Account)
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
                        //  binding.confirmCreateAccountButton.disableCreateAccountButton()
                        binding.termsSwitch.isChecked = false
                        //  binding.termsSwitch.isEnabled = false
                        return@OnTouchListener true
                    }
                }
                return@OnTouchListener false
            }
        )
    }


    fun createAccountButtonEnabledOnceEmailPasswordAndToggleAreValid() {
        binding.termsSwitch.isEnabled = false
        toggleClicked()
        // email text watcher
        binding.enterEmailAddressBox.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                binding.termsSwitch.isChecked = false
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding.enterEmailAddressBox.text.toString().trim())
                        .matches()
                ) {
                    binding.emailError.visibility = View.GONE
                } else {
                    binding.emailError.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        // password text watcher
        binding.enterPasswordBox.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.termsSwitch.isChecked = false
                if (!isValidPasswordFormat(binding.enterPasswordBox.text.toString())) {
                    binding.passwordLengthWarning.setTextColor(Color.parseColor("#CD2026"))
                    binding.termsSwitch.isChecked = false
                    //  binding.termsSwitch.isEnabled = false
                } else {
                    binding.passwordLengthWarning.setTextColor((Color.parseColor("#444444")))
                    binding.termsSwitch.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    fun toggleClicked() {
        binding.confirmCreateAccountButton.disableCreateAccountButton()

        binding.termsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.enterEmailAddressBox.text.isNotEmpty() && binding.enterPasswordBox.text.isNotEmpty()) {
                binding.termsSwitch.isEnabled = true
                if (isChecked) {
                    binding.confirmCreateAccountButton.enableCreateAccountButton()
                } else {
                    binding.confirmCreateAccountButton.disableCreateAccountButton()
                }
            } else {
                binding.confirmCreateAccountButton.disableCreateAccountButton()
            }
        }
    }

    private fun Button.disableCreateAccountButton() {
        binding.confirmCreateAccountButton.isEnabled = false
        binding.confirmCreateAccountButton.alpha = .5f
    }
    private fun Button.enableCreateAccountButton() {
        binding.confirmCreateAccountButton.isEnabled = true
        binding.confirmCreateAccountButton.alpha = 1f
    }



}