package com.pg.justbalance.screens.payment

import android.widget.TextView
import androidx.databinding.BindingAdapter

import com.pg.justbalance.database.Payment
import com.pg.justbalance.decimalFormatDouble

class PaymentBindingUtils {
    companion object{
        @JvmStatic
        @BindingAdapter("paymentAmount")
        fun TextView.setPaymentAmount(item: Payment?) {
            item?.let {
                text = decimalFormatDouble(item.paymentAmount.toBigDecimal())
            }
        }
        @JvmStatic
        @BindingAdapter("paymentDate")
        fun TextView.setPaymentDate(item: Payment?) {
            item?.let {
                text = item.paymentDate.toString()
            }
        }

    }
}