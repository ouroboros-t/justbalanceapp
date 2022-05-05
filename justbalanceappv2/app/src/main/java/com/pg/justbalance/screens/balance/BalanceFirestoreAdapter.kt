package com.pg.justbalance.screens.balance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pg.justbalance.R
import com.pg.justbalance.database.Balance
import com.pg.justbalance.decimalFormatDouble
import com.pg.justbalance.models.BalanceModel

class BalanceFirestoreAdapter(private val balanceList: MutableList<BalanceModel>) : RecyclerView.Adapter<BalanceFirestoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val balance = balanceList[position]
        holder.bind(balance)
    }

    override fun getItemCount() = balanceList.size


    //access to the ViewHolder comes from the 'from' function, which inflates and controls the layout
    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //this defines what goes on the recycler view
        val balanceName: TextView = itemView.findViewById(R.id.balance_name_textView)
        val balanceAmount: TextView = itemView.findViewById(R.id.balance_amount_textView)
        fun bind(item: BalanceModel) {

            balanceName.text = item.balanceName
            balanceAmount.text = decimalFormatDouble(item.startingBalance.toBigDecimal())
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