package com.cwnextgen.letternumberchase.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.cwnextgen.letternumberchase.databinding.DialogAnswerBinding
import com.network.utils.AppClass
import com.network.utils.AppConstants

fun Context.answerDialog(
    data: String = "",
) {
    val dialog = Dialog(this)
    val layoutInflater = LayoutInflater.from(this)
    val binding = DialogAnswerBinding.inflate(layoutInflater)
    binding.tvAnswer.text = data


    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(binding.root)

    binding.btnOk.setOnClickListener {
        dialog.dismiss()
    }
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )
    dialog.show()
}