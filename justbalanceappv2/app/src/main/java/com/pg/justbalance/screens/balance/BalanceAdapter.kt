package com.pg.justbalance.screens.balance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pg.justbalance.database.Balance
import com.pg.justbalance.databinding.ListItemBalanceBinding

class BalanceAdapter(val clickListener: BalanceListener) : RecyclerView.Adapter<BalanceAdapter.ViewHolder>() {

    var data = listOf<Balance>()
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
    class ViewHolder private constructor(val binding: ListItemBalanceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: BalanceListener,item: Balance) {
            //binding the textviews via the binding adapters instead of findViewById
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
                //val view = layoutInflater.inflate(R.layout.list_item_balance, parent, false)
                //make a viewholder
                return ViewHolder(binding)
            }
        }

    }

    class BalanceListener(val clickListener: (balanceId: Long) -> Unit){
        fun onClick(balance: Balance) = clickListener(balance.balanceId)
    }


}


