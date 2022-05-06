package com.pg.justbalance.screens.info


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
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
import com.pg.justbalance.database.BalanceDatabase
import com.pg.justbalance.databinding.BalanceInfoLayoutBinding
import com.pg.justbalance.screens.payment.BalancePaymentAdapter
import kotlinx.coroutines.launch

class BalanceInfoFragment : Fragment(R.layout.balance_info_layout) {
    private lateinit var binding: BalanceInfoLayoutBinding
    private lateinit var balanceInfoViewModel: BalanceInfoViewModel
    private var alertDialog: AlertDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.balance_info_layout,
                container, false
            )

        val application = requireNotNull(this.activity).application

        val arguments = BalanceInfoFragmentArgs.fromBundle(requireArguments())

        val dataSource = FirebaseFirestore.getInstance()
        val viewModelFactory = BalanceViewModelFactory(arguments.balanceId, dataSource)

        balanceInfoViewModel =
            ViewModelProvider(this, viewModelFactory).get(BalanceInfoViewModel::class.java)

        binding.balanceInfoViewModel = balanceInfoViewModel


        binding.lifecycleOwner = this

        balanceInfoViewModel.payments.observe(viewLifecycleOwner, Observer {
            paymentList ->
            binding.paymentsList.adapter = BalancePaymentAdapter(paymentList,BalancePaymentAdapter.PaymentListener { paymentID ->
            balanceInfoViewModel.onPaymentItemClicked(paymentID)
        })
        })

        binding.deleteButton.setOnClickListener {
            createAlert()
            alertDialog?.show()
        }
//        balanceInfoViewModel.payments.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.data = it
//            }
//        })

        balanceInfoViewModel.navigateToBalances.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    BalanceInfoFragmentDirections.actionBalanceInfoFragmentToBalanceFragment()
                )
                balanceInfoViewModel.doneNavigating()
            }
        })

        binding.recordPaymentButton.setOnClickListener {
            findNavController().navigate(R.id.action_balanceInfoFragment_to_balanceRecordPaymentFragment)
        }

        return binding.root
    }

    private fun createAlert() {
        val alert = AlertDialog.Builder(activity)
        val arguments = BalanceInfoFragmentArgs.fromBundle(requireArguments())
        alert.setMessage("Pressing Ok Will Permanently Remove This Balance!")
            .setCancelable(false)
            .setTitle("Are You Sure?")
            .setPositiveButton(
                "Ok",
                DialogInterface.OnClickListener { dialog, id ->
                    run {
                        // updates the documents date used
                        lifecycleScope.launch {
                            balanceInfoViewModel.deleteFromDatabase(arguments.balanceId)
                            findNavController().navigate(R.id.action_balanceInfoFragment_to_balanceFragment)
                            Toast.makeText(
                                activity,
                                "Balance successfully deleted",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            )
            .setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialogInterface, i -> })
        alertDialog = alert.create()
    }
}