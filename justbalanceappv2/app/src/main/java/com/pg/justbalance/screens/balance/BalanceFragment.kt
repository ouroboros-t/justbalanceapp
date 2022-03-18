package com.pg.justbalance.screens.balance


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabase
import com.pg.justbalance.databinding.BalanceLayoutBinding
import com.pg.justbalance.screens.add.AddBalanceViewModel
import com.pg.justbalance.screens.add.AddBalanceViewModelFactory


class BalanceFragment : androidx.fragment.app.Fragment() {
    //inflate layout
    //hookupViewModel to fragment

    var alertDialog : AlertDialog? = null
    //enable databinding in gradle (module) scripts:
    private lateinit var balanceViewModel: BalanceViewModel
    private lateinit var binding: BalanceLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.balance_layout, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = BalanceDatabase.getInstance(application).balanceDatabaseDao


        val viewModelFactory = BalanceViewModelFactory(dataSource,application)

         balanceViewModel = ViewModelProvider(this, viewModelFactory).get(BalanceViewModel::class.java)

        binding.balanceViewModel = balanceViewModel

        //binding viewmodel to view directly?
        binding.lifecycleOwner = this

        binding.dataSize.text = balanceViewModel.balances.value?.size.toString()

        val adapter = BalanceAdapter()
        binding.balancesList.adapter = adapter

        balanceViewModel.balances.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.data = it
            }
        })


        //get the viewModel (generate it upon launch)
        //viewModel = ViewModelProvider(this).get(BalanceViewModel::class.java)

        //we can now directly update TextViews, Buttons, etc inside of the xml..
        //EXAMPLE INSIDE TEXT VIEW : android:text="@{@string/quote_format(gameViewModel.word)}"


        //tell recyclerview about the adapter
//        val adapter = BalanceAdapter()
//        binding.balancesList.adapter = adapter
//
//        balanceViewModel.balances.observe(viewLifecycleOwner) {
//            it?.let { adapter.data = it }
//        }

           // createDialog()

        //balanceViewModel.showBalance()

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_balanceFragment_to_addBalanceFragment)
            //alertDialog?.show()
        }
//AlertDialog.Builder(getActivity())
//            .setMessage("Here is a message")
//            .setPositiveButton(getString(R.string.ok)) { _,_ -> }
//            .create()

        return binding.root
    }
//What do we want to do?
    //We want to a database to keep our balances (BalanceDatabase)
    //we want to hold our data somewhere else other than UI layer/fragment (Balance ViewModel)
    //Live Data:
    //balance name and amount and ID?
    //which view should I use to list all of the balances the user has?





    fun createDialog() {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        val editText1 : EditText = EditText(activity)
        val editText2 : EditText = EditText(activity)

        alertDialogBuilder.setTitle("Hello There")
        alertDialogBuilder.setView(editText1)
        alertDialogBuilder.setView(editText2)
        alertDialogBuilder.setPositiveButton("Add") { dialogInterface: DialogInterface, id: Int ->

        }
        alertDialogBuilder.setNegativeButton("Cancel", {dialogInterface: DialogInterface, id: Int ->})

        alertDialog = alertDialogBuilder.create()
    }





}