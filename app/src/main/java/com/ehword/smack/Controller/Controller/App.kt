package com.ehword.smack.Controller.Controller

import android.app.Application
import android.content.pm.ApplicationInfo
import com.ehword.smack.Controller.Utilities.SharedPrefs

class App: Application() {

    companion object {
        lateinit var sharedPreferences: SharedPrefs
    }

    override fun onCreate() {

        sharedPreferences = SharedPrefs(applicationContext)
        super.onCreate()
    }
}