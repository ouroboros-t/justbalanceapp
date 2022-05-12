package com.pg.justbalance.screens.balance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pg.justbalance.databinding.ListItemBalanceBinding
import com.pg.justbalance.models.BalanceModel

class BalanceFirestoreAdapter(
    private val balanceList: MutableList<BalanceModel>,
    private val clickListener: BalanceFirestoreListener
) : RecyclerView.Adapter<BalanceFirestoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val balance = balanceList[position]
        holder.bind(clickListener, balance)
    }

    override fun getItemCount() = balanceList.size


    //access to the ViewHolder comes from the 'from' function, which inflates and controls the layout
    class ViewHolder private constructor(val binding: ListItemBalanceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: BalanceFirestoreListener, item: BalanceModel) {
            binding.balance = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                //create layout inflater based on the parent view:
                //important to use the right context
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBalanceBinding.inflate(layoutInflater, parent, false)
                //make a viewholder
                return ViewHolder(binding)
            }
        }

    }

    class BalanceFirestoreListener(val clickListener: (balanceId: String) -> Unit) {
        fun onClick(balance: BalanceModel) = clickListener(balance.balanceId)
    }

}