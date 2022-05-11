package com.pg.justbalance.screens.payment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.R
import com.pg.justbalance.databinding.BalancePaymentLayoutBinding
import com.pg.justbalance.screens.info.BalanceInfoFragmentArgs
import kotlinx.coroutines.launch

class BalancePaymentInfoFragment : Fragment(R.layout.balance_payment_layout) {
    private lateinit var binding: BalancePaymentLayoutBinding
    private lateinit var balancePaymentInfoViewModel: BalancePaymentInfoViewModel
    private var alertDialog: AlertDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.balance_payment_layout, container, false
        )

        val arguments = BalancePaymentInfoFragmentArgs.fromBundle(requireArguments())
        val dataSource = FirebaseFirestore.getInstance()

        Log.i("arguments:payment", arguments.paymentId)
        Log.i("balance:payment", arguments.balanceId)
        val viewModelFactory =
            BalancePaymentInfoViewModelFactory(arguments.paymentId, arguments.balanceId, dataSource)

        balancePaymentInfoViewModel =
            ViewModelProvider(this, viewModelFactory).get(BalancePaymentInfoViewModel::class.java)

        binding.balancePaymentViewModel = balancePaymentInfoViewModel

        balancePaymentInfoViewModel.navigateToBalanceInfo.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    BalancePaymentInfoFragmentDirections.actionBalancePaymentInfoFragmentToBalanceInfoFragment(
                        arguments.balanceId
                    )
                )
                balancePaymentInfoViewModel.doneNavigating()
            }
        })

        binding.deletePaymentButton.setOnClickListener {
            createAlert()
            alertDialog?.show()
        }



        binding.lifecycleOwner = this

        return binding.root
    }

    private fun createAlert() {
        val alert = AlertDialog.Builder(activity)
        val arguments = BalancePaymentInfoFragmentArgs.fromBundle(requireArguments())
        alert.setMessage("Pressing Ok Will Permanently Remove This Payment!")
            .setCancelable(false)
            .setTitle("Are You Sure?")
            .setPositiveButton(
                "Ok",
                DialogInterface.OnClickListener { dialog, id ->
                    run {
                        lifecycleScope.launch {
                            balancePaymentInfoViewModel.deleteFromDatabase(
                                arguments.balanceId,
                                arguments.paymentId
                            )
                            findNavController().navigate(
                                BalancePaymentInfoFragmentDirections.actionBalancePaymentInfoFragmentToBalanceInfoFragment(
                                    arguments.balanceId
                                )
                            )
                            Toast.makeText(
                                activity,
                                "Balance successfully deleted",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            )
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i -> })
        alertDialog = alert.create()
    }

}