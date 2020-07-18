package com.study.skin

import android.app.Application
import com.study.skinlib.SkinManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
    }
}