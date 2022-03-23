package com.pg.justbalance.screens.add


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pg.justbalance.R
import com.pg.justbalance.database.BalanceDatabase
import com.pg.justbalance.databinding.AddBalanceLayoutBinding
import java.math.BigDecimal
import java.math.RoundingMode

class AddBalanceFragment : Fragment() {

    private lateinit var binding: AddBalanceLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_balance_layout, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = BalanceDatabase.getInstance(application).balanceDatabaseDao


        val viewModelFactory = AddBalanceViewModelFactory(dataSource, application)

        val addBalanceViewModel =
            ViewModelProvider(this, viewModelFactory).get(AddBalanceViewModel::class.java)

        binding.addBalanceViewModel = addBalanceViewModel

        binding.lifecycleOwner = this

        binding.addBalanceConfirmButton.setOnClickListener {
            var balanceName = ""
            if(binding.balanceNameEditText.text.isBlank() || binding.balanceAmountEditText.text.isBlank()){
                Toast.makeText(application.applicationContext, "Balance must have a name and amount", Toast.LENGTH_SHORT).show()
            }else {
                balanceName = binding.balanceNameEditText.text.toString()

                val balanceAmount: Double = binding.balanceAmountEditText.text.parseToDouble()
                addBalanceViewModel.onAddBalance(balanceName, balanceAmount)
                val message = "Wowowowowow"
                Toast.makeText(application.applicationContext, message, Toast.LENGTH_SHORT).show()
                binding.balanceNameEditText.text.clear()
                binding.balanceAmountEditText.text.clear()
            }
        }


        return binding.root
    }
}