package com.pg.justbalance.screens.balance

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pg.justbalance.database.Balance

class BindingUtils {
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
                text = item.currentBalance
            }
        }
    }



}