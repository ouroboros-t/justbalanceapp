package com.pg.justbalance.screens.add


import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.databinding.AddBalanceLayoutBinding

class AddBalanceFragment : Fragment() {
    private lateinit var addBalanceViewModel: AddBalanceViewModel
    private lateinit var binding: AddBalanceLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home ->
                findNavController().navigate(R.id.action_addBalanceFragment_to_balanceFragment)
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_balance_layout, container, false)


         addBalanceViewModel =
            ViewModelProvider(this).get(AddBalanceViewModel::class.java)

        binding.addBalanceViewModel = addBalanceViewModel

        binding.lifecycleOwner = this

        setupActionBar()

        binding.enterBalanceName.setupClearButtonWithAction()
        binding.enterBalanceAmount.setupClearButtonWithAction()

        enableAddBalanceButtonWhenInfoValid()

        binding.addBalanceButton.setOnClickListener {
            var balanceName = binding.enterBalanceName.text.toString()
            var balanceAmount: Double = binding.enterBalanceAmount.text.parseToDouble()

            addBalanceViewModel.addBalance(balanceName, balanceAmount)
            //this below is useful for when firestore isn't working, and balances don't add.
//            findNavController().navigate(AddBalanceFragmentDirections.actionAddBalanceFragmentToBalanceFragment())
//            val message = "Balance Sucessfully Added"
//            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addBalanceViewModel.success.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                findNavController().navigate(AddBalanceFragmentDirections.actionAddBalanceFragmentToBalanceFragment())
                val message = "Balance Sucessfully Added"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        addBalanceViewModel.failure.observe(viewLifecycleOwner, Observer {
            if(it!=null) {
                val message = "Balance FAILED to add."
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
            })
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setupActionBar() {
        (context as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (context as AppCompatActivity).supportActionBar!!.title = getString(R.string.action_bar)
        (context as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    requireActivity().baseContext,
                    R.color.main_blue
                )
            )
        )
    }

    fun enableAddBalanceButtonWhenInfoValid() {
        binding.addBalanceButton.disableAddBalanceButton()
        binding.enterBalanceName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.enterBalanceName.text.isEmpty()) {
                    showBalanceNameError()
                } else {
                    binding.balanceNameError.visibility = View.GONE
                    binding.addBalanceButton.enableAddBalanceButton()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.enterBalanceAmount.text.isEmpty()) {
                    binding.addBalanceButton.disableAddBalanceButton()
                }
            }

        })
        binding.enterBalanceAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.enterBalanceAmount.text.isEmpty()) {
                    binding.balanceAmountError.visibility = View.VISIBLE
                    binding.balanceAmountError.text = getString(R.string.balance_amount_error)
                } else {
                    binding.balanceNameError.visibility = View.GONE
                    binding.addBalanceButton.enableAddBalanceButton()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.enterBalanceName.text.isEmpty()) {
                    binding.addBalanceButton.disableAddBalanceButton()
                }
            }

        })
    }


    fun showBalanceNameError() {
        binding.balanceNameError.visibility = View.VISIBLE
    }

    fun EditText.setupClearButtonWithAction() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.close else 0
                setPadding(51, 0, 51, 0)
                setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        setOnTouchListener(
            View.OnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                        this.setText("")
                        //  binding.confirmCreateAccountButton.disableCreateAccountButton()
                        binding.addBalanceButton.disableAddBalanceButton()
                        //  binding.termsSwitch.isEnabled = false
                        return@OnTouchListener true
                    }
                }
                return@OnTouchListener false
            }
        )
    }

    private fun Button.disableAddBalanceButton() {
        binding.addBalanceButton.isEnabled = false
        binding.addBalanceButton.alpha = .5f
    }

    private fun Button.enableAddBalanceButton() {
        binding.addBalanceButton.isEnabled = true
        binding.addBalanceButton.alpha = 1f
    }

}