package com.pg.justbalance.screens.payment

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

        fun bind(clickListener: BalancePaymentAdapter.PaymentListener, item: PaymentModel) {
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

    class PaymentListener(val clickListener: (paymentId: String) -> Unit){
        fun onClick(payment: PaymentModel) = clickListener(payment.paymentId)
    }


}