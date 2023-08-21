package com.cwnextgen.letternumberchase.utils

import android.R
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.provider.Settings
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cwnextgen.letternumberchase.BuildConfig
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun Activity.showToast(text: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, time).show()
}

fun Context.showToast(text: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, time).show()
}

fun Fragment.showToast(text: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.context, text, time).show()
}

fun Activity.hideKeyboard() {
    val view = this.findViewById<View>(R.id.content)
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun View.showSnackBar(message: String? = "please check your internet connection") {
    val sb = Snackbar.make(
        this, message!!, BaseTransientBottomBar.LENGTH_INDEFINITE
    )
    sb.setAction("Ok") { sb.dismiss() }
    sb.show()
}

fun TextView.strikeThrough(shouldStrike: Boolean) {
    paintFlags = if (shouldStrike) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}


fun String.isEmailValid(): Boolean {
    return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isFieldValid(): Boolean {
    return this.isNotEmpty() && this.length >= 3
}

fun updateStatusBar(window: Window?) {
    window?.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor = Color.TRANSPARENT
    }
}

fun View.isShow(visible: Boolean? = true, isGone: Boolean? = false) {
    if (visible == true) {
        this.visibility = View.VISIBLE
    } else {
        if (isGone == true) this.visibility = View.GONE
        else this.visibility = View.INVISIBLE
    }
}

fun Activity.openAppSettings() {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    })
}


fun Context.shareText(text: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, text)
    this.startActivity(Intent.createChooser(intent, "Share via"))
}


fun Context.shareApp() {

    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.type = "text/plain"
    shareIntent.putExtra(
        Intent.EXTRA_TEXT,
        this.getString(com.cwnextgen.letternumberchase.R.string.app_name) + " ⬇️\n https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    )
    this.startActivity(Intent.createChooser(shareIntent, "Share via"))
}

fun Context.openPlayStoreForRating() {
    val packageName = packageName
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

fun Context.openPlayStoreForMoreApps() {
    val publisherName = "NextGen Apps Developer"
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:$publisherName")))
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/dev?id=$publisherName")
            )
        )
    }
}