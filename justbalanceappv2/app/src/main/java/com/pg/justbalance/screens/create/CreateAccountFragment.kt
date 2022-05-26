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
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.databinding.CreateAccountLayoutBinding
import com.pg.justbalance.sharedViewModels.UserViewModel

class CreateAccountFragment: Fragment(R.layout.create_account_layout) {
    private lateinit var binding: CreateAccountLayoutBinding
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home ->
                userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
                    if(user != null){
                        findNavController().navigate(R.id.balanceFragment)
                    }else{
                        findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)
                    }
                })

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



        return binding.root
    }

    private fun setupActionBar() {
        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (context as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (context as AppCompatActivity).supportActionBar!!.title = getString(R.string.create_jb_Account)
    }




}