package com.pg.justbalance.screens.title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.databinding.TitleLayoutBinding

class TitleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: TitleLayoutBinding = DataBindingUtil.inflate(inflater,
            R.layout.title_layout, container, false)


        binding.titleButton.setOnClickListener {
            findNavController().navigate(R.id.action_titleFragment_to_balanceFragment)
        }

        return binding.root
    }
}