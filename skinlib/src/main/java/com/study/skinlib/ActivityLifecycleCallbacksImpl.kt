package com.study.skinlib

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import com.study.skinlib.utils.SkinThemeUtils
import java.util.*

/**
 * Activity生命周期回调管理类
 */
class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {

    private var mObserable: Observable
    private val mLayoutInflaterFactories = ArrayMap<Activity, SkinLayoutInflaterFactory>()

    public constructor(observable: Observable){
        mObserable = observable
    }

    /**
     * Activity的onCreate方法中dispatchActivityCreated(savedInstanceState);分发事件
     * setContentView方法之前调用
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        /**
         *  更新状态栏
         */
        SkinThemeUtils.updateStatusBarColor(activity)

        /**
         *  更新布局视图
         */
        //获得Activity的布局加载器
        val layoutInflater = activity.layoutInflater

        try {
            //Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
            //如设置过抛出一次
            //设置 mFactorySet 标签为false
            val field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            field.isAccessible = true
            field.setBoolean(layoutInflater, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        //使用factory2 设置布局加载工程
        val skinLayoutInflaterFactory = SkinLayoutInflaterFactory(activity)
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory)
        mLayoutInflaterFactories[activity] = skinLayoutInflaterFactory

        mObserable.addObserver(skinLayoutInflaterFactory)
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityStopped(activity: Activity) {}


    override fun onActivityResumed(activity: Activity) {}
}