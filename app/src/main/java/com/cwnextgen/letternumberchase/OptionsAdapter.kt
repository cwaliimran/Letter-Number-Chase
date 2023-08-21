package com.cwnextgen.letternumberchase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cwnextgen.letternumberchase.databinding.LetterOptionItemBinding
import com.cwnextgen.letternumberchase.utils.Animations.playShakeAnimation
import com.cwnextgen.letternumberchase.utils.GlobalUtils
import com.cwnextgen.letternumberchase.utils.GlobalUtils.setCustomFont
import com.network.utils.AppClass
import com.network.utils.AppConstants

class OptionsAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {
    lateinit var context: Context
    private var options: List<String> = listOf()
    private var highlightedIndex: Int =
        -1 // Index of the highlighted option - default none selected

    inner class ViewHolder(private val binding: LetterOptionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnOption.setOnClickListener {
                onItemClick(options[absoluteAdapterPosition])
            }
        }

        fun bind(option: String, isHighlighted: Boolean) {
            binding.btnOption.text = option
            if (isHighlighted) {
                binding.btnOption.setBackgroundResource(R.drawable.correct_option)
                playShakeAnimation(binding.btnOption)
            } else {
                binding.btnOption.setBackgroundResource(R.drawable.buttonbg1)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = LetterOptionItemBinding.inflate(LayoutInflater.from(context), parent, false)
        val selectedOptionFont = AppClass.sharedPref.getString(AppConstants.FONT_TYPE, "palamecia_titling")
        GlobalUtils.setCustomFont(binding.root, context, selectedOptionFont)
        //set font size
        val fontSize = AppClass.sharedPref.getInt(AppConstants.FONT_PERCENT, 0)
        GlobalUtils.increaseFontSize(context, binding.root, fontSize.toFloat())

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        val isHighlighted = position == highlightedIndex
        holder.bind(option, isHighlighted)
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