package com.pg.justbalance.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pg.justbalance.R
import com.pg.justbalance.databinding.LoginLayoutBinding

class LoginFragment: Fragment(R.layout.login_layout) {
    private lateinit var binding: LoginLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_layout, container, false)

        return binding.root
    }

}