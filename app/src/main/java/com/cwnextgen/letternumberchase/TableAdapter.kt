package com.cwnextgen.letternumberchase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cwnextgen.letternumberchase.databinding.RowTableItemsBinding
import com.cwnextgen.letternumberchase.models.TableItem
import com.cwnextgen.letternumberchase.utils.GlobalUtils
import com.network.utils.AppClass
import com.network.utils.AppConstants

class TableAdapter:
    RecyclerView.Adapter<TableAdapter.ViewHolder>() {
    lateinit var context: Context
    private var mList: List<TableItem> = listOf()

    inner class ViewHolder(private val binding: RowTableItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: TableItem) {
            binding.data = model
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = RowTableItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        val selectedOptionFont =
            AppClass.sharedPref.getString(AppConstants.FONT_TYPE, "palamecia_titling")
        GlobalUtils.setCustomFont(binding.root, context, selectedOptionFont)
        //set font size
        val fontSize = AppClass.sharedPref.getInt(AppConstants.FONT_PERCENT, 0)
        GlobalUtils.increaseFontSize(context, binding.root, fontSize.toFloat())

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mList[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int = mList.size

    fun updateData(data: MutableList<TableItem>) {
        mList = data
        notifyDataSetChanged()
    }

}