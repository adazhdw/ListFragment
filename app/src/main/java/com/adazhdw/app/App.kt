package com.adazhdw.app

import android.app.Application
import com.adazhdw.baselibrary.initLibrary

class App :Application() {
    override fun onCreate() {
        super.onCreate()

        initLibrary("https://wanandroid.com",true)
    }
}