package com.pg.justbalance.screens.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.databinding.CreateAccountLayoutBinding
import com.pg.justbalance.sharedViewModels.UserViewModel

class CreateAccountFragment: Fragment(R.layout.create_account_layout) {
    private lateinit var binding: CreateAccountLayoutBinding
    private lateinit var createAccountViewModel: CreateAccountViewModel
    private val userViewModel: UserViewModel by activityViewModels()
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

        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if(user != null){
                findNavController().navigate(R.id.action_createAccountFragment_to_balanceFragment)
            }
        })


        setupActionBar()

        createAccountViewModel = ViewModelProvider(this).get(CreateAccountViewModel::class.java)

        binding.createAccountViewModel = createAccountViewModel





        binding.confirmCreateAccountButton.setOnClickListener {
            val email = binding.enterEmailAddressBox.text.toString()
            val password = binding.enterPasswordBox.text.toString()
            createAccountViewModel.createUser(email, password)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createAccountViewModel.successCreated.observe(viewLifecycleOwner, Observer {
            if(it != null){
                        findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupActionBar() {
        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (context as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (context as AppCompatActivity).supportActionBar!!.title = getString(R.string.create_jb_Account)
    }




}