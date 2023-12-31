package com.cwnextgen.letternumberchase.utils

import android.content.Context
import android.util.TypedValue
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import com.cwnextgen.letternumberchase.models.TableItem

object GlobalUtils {
    fun setCustomFont(view: View, context: Context, fontIdentifier: String? = "palamecia_titling") {

        val fontResourceId = context.resources.getIdentifier(
            fontIdentifier, "font", context.packageName
        )
        if (view is ViewGroup) {
            val childCount = view.childCount
            for (i in 0 until childCount) {
                setCustomFont(view.getChildAt(i), context, fontIdentifier)
            }
        } else if (view is TextView) {
            val customFont = ResourcesCompat.getFont(context, fontResourceId)
            view.typeface = customFont
        } else if (view is Button) {
            val customFont = ResourcesCompat.getFont(context, fontResourceId)
            view.typeface = customFont
        }
    }

    fun setCustomFontSpecificView(view: View, fontIdentifier: String? = "palamecia_titling") {
        val context = view.context
        val fontResourceId = context.resources.getIdentifier(
            fontIdentifier, "font", context.packageName
        )

        when (view) {
            is TextView -> {
                val customFont = ResourcesCompat.getFont(context, fontResourceId)
                view.typeface = customFont
            }

            is Button -> {
                val customFont = ResourcesCompat.getFont(context, fontResourceId)
                view.typeface = customFont
            }

            is ViewGroup -> {
                // If the view is a ViewGroup, apply the custom font recursively to its children
                view.children.forEach { childView ->
                    setCustomFont(childView, context, fontIdentifier)
                }
            }
        }
    }

    fun increaseFontSize(context: Context, rootView: View, increaseAmount: Float) {
        if (rootView is ViewGroup) {
            val childCount = rootView.childCount
            for (i in 0 until childCount) {
                val childView = rootView.getChildAt(i)
                increaseFontSize(context, childView, increaseAmount)
            }
        } else if (rootView is TextView) {
            val currentSize = rootView.textSize
            val newSize = currentSize + increaseAmount
            rootView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize)
        }
    }

    fun increaseFontSizeSingleView(textView: TextView, increaseAmount: Float) {
        val currentSize = textView.textSize
        val currentSizeSp = currentSize / textView.context.resources.displayMetrics.scaledDensity
        val newSizeSp = currentSizeSp + increaseAmount
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSizeSp)
    }

    // Function to generate a multiplication table
    fun generateMultiplicationTable(tableNo: Int, count: Int): MutableList<TableItem> {
        val tableData = mutableListOf<TableItem>()
        for (i in 1..count) {
            tableData.add(TableItem(tableNo, i, tableNo * i))
        }
        return tableData
    }

    fun Context.showOptionsMenu(
        view: View,
        menuResId: Int,
        hideMenuItemIds: List<Int>? = mutableListOf(), // pass id like this mutableListOf(R.id.delete)
        onMenuItemClickListener: (MenuItem) -> Boolean
    ) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(menuResId, popupMenu.menu)

        // Get a reference to the menu and loop through its items to hide the desired ones
        val menu = popupMenu.menu
        if (hideMenuItemIds != null) {
            for (id in hideMenuItemIds) {
                val menuItem = menu.findItem(id)
                menuItem?.isVisible = false
            }
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            // Optionally, you can still pass the clicked MenuItem to the onMenuItemClickListener
            onMenuItemClickListener(menuItem)
        }

        popupMenu.show()
    }
}