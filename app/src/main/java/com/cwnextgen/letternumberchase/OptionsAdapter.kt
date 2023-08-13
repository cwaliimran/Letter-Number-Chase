package com.cwnextgen.letternumberchase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class OptionsAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {
    private lateinit var context: Context
    private var options: List<String> = listOf()
    private var highlightedIndex: Int = -1 // Index of the highlighted option

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnOption: Button = itemView.findViewById(R.id.btnOption)

        init {
            btnOption.setOnClickListener {
                onItemClick(options[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.letter_option_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        holder.btnOption.text = option

        // Set the background color for the correct option
        if (position == highlightedIndex) {
            holder.btnOption.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.highlightColor
                )
            )
        } else {
            holder.btnOption.setBackgroundColor(ContextCompat.getColor(context, R.color.primary))

        }
    }

    override fun getItemCount(): Int = options.size

    fun updateOptions(newOptions: List<String>) {
        highlightedIndex = -1 // Clear the highlighted index
        options = newOptions
        notifyDataSetChanged()
    }

    fun highlightCorrectOption(correctOptionIndex: Int) {
        highlightedIndex = correctOptionIndex
        notifyDataSetChanged()
    }
}