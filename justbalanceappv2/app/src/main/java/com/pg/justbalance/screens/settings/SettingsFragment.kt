package com.pg.justbalance.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.databinding.SettingsLayoutBinding
import com.pg.justbalance.sharedViewModels.UserViewModel

class SettingsFragment : Fragment(R.layout.settings_layout) {
    private lateinit var binding: SettingsLayoutBinding
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_layout, container, false)
        val navController = findNavController()
        binding.userViewModel = userViewModel
        binding.logoutButton.setOnClickListener {
            userViewModel.signOut()
            navController.navigate(R.id.loginFragment)
        }

        return binding.root
    }

}