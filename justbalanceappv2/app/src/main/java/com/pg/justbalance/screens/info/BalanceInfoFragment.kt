package com.pg.justbalance.screens.info


import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.pg.justbalance.R
import com.pg.justbalance.databinding.BalanceInfoLayoutBinding
import com.pg.justbalance.screens.payment.BalancePaymentAdapter
import com.pg.justbalance.sharedViewModels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BalanceInfoFragment : Fragment(R.layout.balance_info_layout) {
    private lateinit var binding: BalanceInfoLayoutBinding
    private lateinit var balanceInfoViewModel: BalanceInfoViewModel
    private val userViewModel: UserViewModel by activityViewModels()
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

        val arguments = BalanceInfoFragmentArgs.fromBundle(requireArguments())

        val dataSource = FirebaseFirestore.getInstance()
        val viewModelFactory = BalanceViewModelFactory(arguments.balanceId, dataSource)

        balanceInfoViewModel =
            ViewModelProvider(this, viewModelFactory).get(BalanceInfoViewModel::class.java)

        binding.balanceInfoViewModel = balanceInfoViewModel

        setActionBarToBeEmpty()
        binding.lifecycleOwner = this

        balanceInfoViewModel.readingService(arguments.balanceId)

        balanceInfoViewModel.payments.observe(viewLifecycleOwner, Observer { paymentList ->

            if (paymentList.isNotEmpty()) {
                binding.youHavent.visibility = View.GONE
            }
            userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
                balanceInfoViewModel.calculateCurrentBalance(paymentList, arguments.balanceId, user?.uid!! )
            })

            balanceInfoViewModel.currentBalanceString.observe(
                viewLifecycleOwner,
                Observer { string ->
                    binding.BalanceInfoCurrentBalance.text = string
                })

            binding.paymentsList.adapter = BalancePaymentAdapter(
                paymentList,
                BalancePaymentAdapter.PaymentListener { paymentID ->
                    balanceInfoViewModel.onPaymentItemClicked(paymentID)
                })


        })


        balanceInfoViewModel.navigateToPaymentInfo.observe(
            viewLifecycleOwner,
            Observer { paymentId ->
                paymentId?.let {
                    this.findNavController().navigate(
                        BalanceInfoFragmentDirections.actionBalanceInfoFragmentToBalancePaymentInfoFragment(
                            paymentId,
                            arguments.balanceId
                        )
                    )
                    balanceInfoViewModel.onPaymentItemInfoNavigated()
                }
            })



        binding.deleteButton.setOnClickListener {
            createAlert()
            alertDialog?.show()
        }

        balanceInfoViewModel.navigateToBalances.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    BalanceInfoFragmentDirections.actionBalanceInfoFragmentToBalanceFragment()
                )
                balanceInfoViewModel.doneNavigating()
            }
        })

        binding.recordPaymentButton.setOnClickListener {
            val place =
                BalanceInfoFragmentDirections.actionBalanceInfoFragmentToBalanceRecordPaymentFragment(
                    arguments.balanceId
                )
            findNavController().navigate(place)
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
                            userViewModel.user.observe(viewLifecycleOwner, Observer {
                                balanceInfoViewModel.deleteFromDatabase(arguments.balanceId, it?.uid!!)
                            })
                            displaySpinnerIcon()
                            delay(3000)
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
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i -> })
        alertDialog = alert.create()
    }

    fun setActionBarToBeEmpty() {
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

    fun displaySpinnerIcon(){
        binding.progressBar.visibility = View.VISIBLE
    }

}