package com.pg.justbalance.screens.payment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pg.justbalance.databinding.ListItemPaymentBinding
import com.pg.justbalance.models.PaymentModel


class BalancePaymentAdapter(private val paymentList: MutableList<PaymentModel>, val clickListener: PaymentListener) : RecyclerView.Adapter<BalancePaymentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = paymentList[position]
        holder.bind(clickListener,item)
    }

    override fun getItemCount() = paymentList.size


    //access to the ViewHolder comes from the 'from' function, which inflates and controls the layout
    class ViewHolder private constructor(val binding: ListItemPaymentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: PaymentListener, item: PaymentModel) {

            //binding the textviews via the binding adapters instead of findViewById
            binding.payment = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPaymentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    class PaymentListener(val clickListener: (paymentId: String) -> Unit){
        fun onClick(payment: PaymentModel) = clickListener(payment.paymentId)
    }


}