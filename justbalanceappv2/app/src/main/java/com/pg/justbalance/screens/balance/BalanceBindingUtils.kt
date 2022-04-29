package com.pg.justbalance.screens.balance

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pg.justbalance.R
import com.pg.justbalance.database.Balance
import com.pg.justbalance.decimalFormatDouble

class BalanceBindingUtils {
    companion object {
        @JvmStatic
        @BindingAdapter("balanceName")
        fun TextView.setBalanceName(item: Balance?) {
            item?.let {
                text = item.balanceName
            }
        }
        @JvmStatic
        @BindingAdapter("balanceAmount")
        fun TextView.setBalanceAmount(item: Balance?) {
            item?.let {
                text = decimalFormatDouble(item.currentBalance.toBigDecimal())
            }
        }
        @JvmStatic
        @BindingAdapter("startingBalance")
        fun TextView.setStartingBalance(item: Balance?) {
            item?.let {

                text =
                    "Starting Balance: ${decimalFormatDouble(item.startingBalance.toBigDecimal())}"
            }
        }
        @JvmStatic
        @BindingAdapter("currentBalance")
        fun TextView.setCurrentBalance(item: Balance?) {
            item?.let {
                text =  "Current Balance: ${decimalFormatDouble(item.currentBalance.toBigDecimal())}"
            }
        }

    }



}