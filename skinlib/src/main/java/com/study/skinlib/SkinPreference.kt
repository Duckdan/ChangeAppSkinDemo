package com.study.skinlib

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * 皮肤路径首选项设置
 */
class SkinPreference {
    private lateinit var mPref: SharedPreferences


    private constructor(context: Context) {
        mPref = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE)
    }

    /**
     * 设置皮肤资源路径
     */
    fun setSkin(skinPath: String) = mPref.edit().putString(KEY_SKIN_PATH, skinPath).apply()


    /**
     * 重置皮肤路径
     */
    fun reset() = mPref.edit().remove(KEY_SKIN_PATH).apply()


    /**
     * 获取皮肤路径
     */
    fun getSkin(): String? = mPref.getString(KEY_SKIN_PATH, null)

    companion object {
        val SKIN_SHARED = "skins"
        val KEY_SKIN_PATH = "skin-path"


        @Volatile
        private var instance: SkinPreference? = null

        fun init(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = SkinPreference(context)
                    }
                }
            }
        }

        fun getInstance() = instance
    }
}