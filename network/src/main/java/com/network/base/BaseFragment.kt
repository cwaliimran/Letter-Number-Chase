package com.network.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import kotlin.properties.ReadOnlyProperty

abstract class BaseFragment : Fragment() {

    private val TAG = "BaseFragment"
    var fcmToken = ""
    private var mLastClickTime: Long = 0
    var bundle: Bundle? = null
    val gson = Gson()
    var lastClickTime: Long = 0

    protected val mContext: Context by lazy {
        requireActivity()
    }

    protected val fragmentActivity: FragmentActivity by lazy {
        requireActivity()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated()
        initData()
        initAdapter()
        initObservers()
        clicks()
        apiAndArgs()

    }

    abstract fun viewCreated()
    abstract fun clicks()
    open fun initData() {}
    open fun initAdapter() {}
    open fun initObservers() {}
    open fun apiAndArgs() {}

    protected fun show(view: View) {
        view.visibility = VISIBLE
    }

    protected fun hideGone(view: View) {
        view.visibility = GONE
    }

    protected fun hideInvisible(view: View) {
        view.visibility = INVISIBLE
    }

}