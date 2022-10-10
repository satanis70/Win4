package com.example.win4

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.win4.model.BetModel

class MainHolder(
    private val list: ArrayList<BetModel>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MainHolder.MainHolder>() {

    inner class MainHolder(itemView: View, onItemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val textViewAmount = itemView.findViewById<TextView>(R.id.text_view_win_balance)
        private val textViewNameBet = itemView.findViewById<TextView>(R.id.text_view_name_bet)
        private val buttonWin = itemView.findViewById<AppCompatImageButton>(R.id.button_win)
        private val buttonLoss = itemView.findViewById<AppCompatImageButton>(R.id.button_loss)
        private val buttonDelete = itemView.findViewById<AppCompatImageButton>(R.id.button_delete)
        private val textViewResult = itemView.findViewById<TextView>(R.id.textView_result)
        fun bind(name: String, amount: String, odd: String, capital: String, status: String) {
            textViewNameBet.text = name
            textViewAmount.text = "$amount€"
            if (status == "win") {
                buttonWin.visibility = View.INVISIBLE
                buttonLoss.visibility = View.INVISIBLE
                textViewResult.visibility = View.VISIBLE
                textViewResult.setBackgroundDrawable(itemView.resources.getDrawable(R.drawable.text_result_win))
                val result = odd.toDouble() * amount.toDouble()
                textViewResult.text = "+${result}€"
            } else if (status == "loss") {
                buttonWin.visibility = View.INVISIBLE
                buttonLoss.visibility = View.INVISIBLE
                textViewResult.visibility = View.VISIBLE
                textViewResult.setBackgroundDrawable(itemView.resources.getDrawable(R.drawable.border_text))
                textViewResult.text = "-${amount}€"
            } else {
                textViewResult.setBackgroundDrawable(itemView.resources.getDrawable(R.drawable.text_result_wait))
                textViewResult.text = "0€"
            }
            buttonWin.setOnClickListener {
                onItemClickListener.onClick(
                    adapterPosition,
                    BetModel(adapterPosition, adapterPosition, name, odd, amount, "win", capital),
                    "win",
                    textViewResult
                )
                buttonLoss.visibility = View.INVISIBLE
                buttonWin.visibility = View.INVISIBLE
                textViewResult.visibility = View.VISIBLE
                textViewResult.setBackgroundDrawable(itemView.resources.getDrawable(R.drawable.text_result_win))
            }
            buttonDelete.setOnClickListener {
                onItemClickListener.onClick(
                    adapterPosition,
                    BetModel(adapterPosition, adapterPosition, name, odd, amount, "", capital),
                    "delete",
                    textViewResult
                )
            }
            buttonLoss.setOnClickListener {
                onItemClickListener.onClick(
                    adapterPosition,
                    BetModel(adapterPosition, adapterPosition, name, odd, amount, "loss", capital),
                    "loss",
                    textViewResult
                )
                buttonWin.visibility = View.INVISIBLE
                buttonLoss.visibility = View.INVISIBLE
                textViewResult.visibility = View.VISIBLE
                textViewResult.setBackgroundDrawable(itemView.resources.getDrawable(R.drawable.border_text))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_main, parent, false)
        return MainHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(
            list[position].name,
            list[position].amount,
            list[position].odd,
            list[position].capital,
            list[position].status
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onClick(position: Int, betModel: BetModel, s: String, textViewResult: TextView) {

        }
    }
}