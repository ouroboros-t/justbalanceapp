package com.pg.justbalance.screens.payment

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.pg.justbalance.decimalFormatDouble
import com.pg.justbalance.models.PaymentModel
import java.text.SimpleDateFormat

class PaymentBindingUtils {
    companion object {
        @JvmStatic
        @BindingAdapter("paymentAmount")
        fun TextView.setPaymentAmount(item: PaymentModel?) {
            item?.let {
                text = decimalFormatDouble(item.paymentAmount.toBigDecimal())
            }
        }

        @JvmStatic
        @BindingAdapter("paymentDate")
        fun TextView.setPaymentDate(item: PaymentModel?) {
            val sdf = SimpleDateFormat("MMM d, yyyy")
            item?.let {
                text = sdf.format(it.paymentDate!!)
            }
        }

    }
}