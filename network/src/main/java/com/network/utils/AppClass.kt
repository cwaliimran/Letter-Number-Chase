package com.network.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.network.models.ModelUser


public class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        myApp = this
        sharedPref = SharedPref(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "App Native Alerts"
            val channelName = "App Alerts"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }

    }


    companion object {
        private const val TAG = "AppClass"
        var myApp: AppClass? = null
        val instance get() = myApp!!
        lateinit var sharedPref: SharedPref

        //for storing data
        //    AppClass.sharedPref?.storeObject(AppConstants.CURRENT_USER, responseData.body)
        fun getCurrentUser(): ModelUser? {
            return sharedPref.getObject(AppConstants.CURRENT_USER, ModelUser::class.java)
        }


        fun getAccessToken(): String? {
            return getCurrentUser()?.token
        }


    }
}