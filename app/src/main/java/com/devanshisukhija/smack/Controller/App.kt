package com.devanshisukhija.smack.Controller

import android.app.Application
import com.devanshisukhija.smack.Utilities.SharedPrefs

/**
 * Created by devanshi on 09/02/18.
 */
class App : Application() {

    companion object {
        lateinit var prefs: SharedPrefs
    }
    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}