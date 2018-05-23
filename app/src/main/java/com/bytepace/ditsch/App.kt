package com.bytepace.ditsch

import android.app.Application
import com.rohitss.uceh.UCEHandler

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        UCEHandler.Builder(applicationContext).build()
    }
}