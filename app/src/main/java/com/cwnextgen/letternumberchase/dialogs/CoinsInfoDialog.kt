package com.cwnextgen.letternumberchase.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.LinearLayout
import com.cwnextgen.letternumberchase.databinding.DialogCoinsInfoBinding

fun Context.coinsInfoDialog(
) {
    val dialog = Dialog(this)
    val layoutInflater = LayoutInflater.from(this)
    val binding = DialogCoinsInfoBinding.inflate(layoutInflater)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(binding.root)
    binding.root.setOnClickListener { dialog.dismiss() }
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )
    dialog.show()
}