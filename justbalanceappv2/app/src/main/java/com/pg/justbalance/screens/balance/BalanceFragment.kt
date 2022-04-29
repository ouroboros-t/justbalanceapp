package com.pg.justbalance.screens.balance


import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.database.Balance
import com.pg.justbalance.database.BalanceDatabase
import com.pg.justbalance.databinding.BalanceLayoutBinding
import com.pg.justbalance.decimalFormatDouble
import com.pg.justbalance.screens.add.AddBalanceViewModel
import com.pg.justbalance.screens.add.AddBalanceViewModelFactory


class BalanceFragment : androidx.fragment.app.Fragment(R.layout.balance_layout) {
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

        //we can now directly update TextViews, Buttons, etc inside of the xml..
        //EXAMPLE INSIDE TEXT VIEW : android:text="@{@string/quote_format(gameViewModel.word)}"
        binding.balanceViewModel = balanceViewModel

        //binding viewmodel to view directly?
        binding.lifecycleOwner = this

        setActionBarToBeEmpty()

        val adapter = BalanceAdapter(BalanceAdapter.BalanceListener{
            balanceId -> balanceViewModel.onBalanceItemClicked(balanceId)
        })
        binding.balancesList.adapter = adapter

        balanceViewModel.balances.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.data = it
                Log.i("This is the data:", adapter.data.toString())
               binding.totalBalanceAmountTextView.text = decimalFormatDouble(balanceViewModel.showTotalBalance(adapter.data))
                Log.i("This is the total:", balanceViewModel.showTotalBalance(it).toString())
            }
        })
        if(binding.balancesList.isEmpty()){
            binding.youHavent.visibility = View.GONE
        }

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_balanceFragment_to_addBalanceFragment)
//            balanceViewModel.deleteFromDatabase()
//            Toast.makeText(application.applicationContext, "Data is gone for good :(", Toast.LENGTH_SHORT).show()
            //alertDialog?.show()
        }


        balanceViewModel.navigateToBalanceInfo.observe(viewLifecycleOwner, Observer {
            balance -> balance?.let {
                    this.findNavController().navigate(BalanceFragmentDirections.actionBalanceFragmentToBalanceInfoFragment(balance))
            //this.findNavController().navigate(R.id.action_balanceFragment_to_balanceInfoFragment)
                   balanceViewModel.onBalanceItemInfoNavigated()
            }
        })



        return binding.root
    }
    fun setActionBarToBeEmpty(){
        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (context as AppCompatActivity).supportActionBar!!.title = ""
        (context as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    requireActivity().baseContext,
                    R.color.main_blue
                )
            )
        )
    }

//What do we want to do?
    //We want to a database to keep our balances (BalanceDatabase)
    //we want to hold our data somewhere else other than UI layer/fragment (Balance ViewModel)
    //Live Data:
    //balance name and amount and ID?
    //which view should I use to list all of the balances the user has?








}