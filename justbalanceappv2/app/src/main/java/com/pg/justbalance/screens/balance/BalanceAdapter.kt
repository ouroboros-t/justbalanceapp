package com.pg.justbalance.screens.balance

import android.text.style.TtsSpan
import android.text.style.TtsSpan.MoneyBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pg.justbalance.R
import com.pg.justbalance.database.Balance

class BalanceAdapter : RecyclerView.Adapter<BalanceAdapter.ViewHolder>() {

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
        holder.bind(item)
    }

    override fun getItemCount() = data.size


    //access to the ViewHolder comes from the 'from' function, which inflates and controls the layout
    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var stringBuffer = StringBuffer()
        //this defines what goes on the recycler view
        val balanceName: TextView = itemView.findViewById(R.id.balance_name_textView)
        val balanceAmount: TextView = itemView.findViewById(R.id.balance_amount_textView)
        fun bind(item: Balance) {
            balanceName.text = item.balanceName
            balanceAmount.text = stringBuffer.append("$").append(" ").append(item.currentBalance.toString())
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                //create layout inflater based on the parent view:
                //important to use the right context
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_balance, parent, false)
                //make a viewholder
                return ViewHolder(view)
            }
        }

    }


}


