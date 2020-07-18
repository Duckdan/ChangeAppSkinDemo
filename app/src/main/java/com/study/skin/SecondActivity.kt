package com.study.skin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.study.skinlib.SkinManager

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }

    fun changeSkin(view: View) {
        //换肤，收包裹，皮肤包是独立的apk包，可以来自网络下载
        SkinManager.getInstance()?.loadSkin("/data/data/com.study.skin/skin/skinresource-debug.apk")
    }

    fun resetSkin(view: View) {
        SkinManager.getInstance()?.loadSkin(null)
    }
}
