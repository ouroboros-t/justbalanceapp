package com.pg.justbalance.screens.balance


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.databinding.BalanceLayoutBinding


class BalanceFragment : androidx.fragment.app.Fragment() {
    //inflate layout
    //hookupViewModel to fragment
    private lateinit var viewModel: BalanceViewModel
    //enable databinding in gradle (module) scripts:
    private lateinit var binding: BalanceLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.balance_layout, container, false)

        //binding viewmodel to view directly?
        binding.lifecycleOwner = this


        //get the viewModel (generate it upon launch)
        viewModel = ViewModelProvider(this).get(BalanceViewModel::class.java)

        //we can now directly update TextViews, Buttons, etc inside of the xml..
        //EXAMPLE INSIDE TEXT VIEW : android:text="@{@string/quote_format(gameViewModel.word)}"

        //on Add button go to next screen
        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_balanceFragment_to_addBalanceFragment)
        }

        //allow layout access to all of data in ViewModel
        binding.balanceViewModel = viewModel




        return binding.root
    }
//What do we want to do?
    //We want to a database to keep our balances (BalanceDatabase)
    //we want to hold our data somewhere else other than UI layer/fragment (Balance ViewModel)
    //Live Data:
    //balance name and amount and ID?
    //which view should I use to list all of the balances the user has?
}