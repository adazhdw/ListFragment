package com.adazhdw.listapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.adazhdw.baselibrary.initLibrary

class App :Application() {
    override fun onCreate() {
        super.onCreate()

        initLibrary("https://wanandroid.com",true)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}