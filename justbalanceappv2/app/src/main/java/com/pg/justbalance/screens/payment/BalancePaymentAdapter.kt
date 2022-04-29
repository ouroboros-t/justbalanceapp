package com.pg.justbalance.screens.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pg.justbalance.database.Payment
import com.pg.justbalance.databinding.ListItemPaymentBinding


class BalancePaymentAdapter(val clickListener: BalancePaymentAdapter.PaymentListener) : RecyclerView.Adapter<BalancePaymentAdapter.ViewHolder>() {

    var data = listOf<Payment>()
        set(value) {
            field = value
            notifyDataSetChanged() //there are better ways to do this -- using DiffUtil
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(clickListener,item)
    }

    override fun getItemCount() = data.size


    //access to the ViewHolder comes from the 'from' function, which inflates and controls the layout
    class ViewHolder private constructor(val binding: ListItemPaymentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: BalancePaymentAdapter.PaymentListener, item: Payment) {
            //binding the textviews via the binding adapters instead of findViewById
            binding.payment = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                //create layout inflater based on the parent view:
                //important to use the right context
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPaymentBinding.inflate(layoutInflater, parent, false)
                //val view = layoutInflater.inflate(R.layout.list_item_balance, parent, false)
                //make a viewholder
                return ViewHolder(binding)
            }
        }

    }

    class PaymentListener(val clickListener: (paymentId: Long) -> Unit){
        fun onClick(payment: Payment) = clickListener(payment.balanceId)
    }


}