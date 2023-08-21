package com.cwnextgen.letternumberchase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cwnextgen.letternumberchase.databinding.RowTablesListBinding
import com.cwnextgen.letternumberchase.utils.GlobalUtils
import com.network.utils.AppClass
import com.network.utils.AppConstants

class TablesListAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<TablesListAdapter.ViewHolder>() {
    lateinit var context: Context
    private var options: List<String> = listOf()

    inner class ViewHolder(private val binding: RowTablesListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnOption.setOnClickListener {
                onItemClick(options[absoluteAdapterPosition])
            }
        }

        fun bind(option: String) {
            binding.btnOption.text = option
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = RowTablesListBinding.inflate(LayoutInflater.from(context), parent, false)
        val selectedOptionFont =
            AppClass.sharedPref.getString(AppConstants.FONT_TYPE, "palamecia_titling")
        GlobalUtils.setCustomFont(binding.root, context, selectedOptionFont)
        //set font size
        val fontSize = AppClass.sharedPref.getInt(AppConstants.FONT_PERCENT, 0)
        GlobalUtils.increaseFontSize(context, binding.root, fontSize.toFloat())

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option)
    }

    override fun getItemCount(): Int = options.size

    fun updateData(newOptions: List<String>) {
        options = newOptions
        notifyDataSetChanged()
    }

}