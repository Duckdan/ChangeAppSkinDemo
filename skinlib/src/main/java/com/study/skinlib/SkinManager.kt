package com.study.skinlib

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.text.TextUtils
import com.study.skinlib.utils.SkinResource
import java.util.*

/**
 * 皮肤管理类
 */
class SkinManager : Observable {
    private lateinit var mApplication: Application


    private constructor(application: Application) {
        mApplication = application
        //初始化皮肤路径首选项
        SkinPreference.init(application)
        //资源管理类 用于从app/皮肤中加载资源
        SkinResource.init(application)
        //给Application注册Activity生命周期监听类
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl(this))
        //加载上次使用保存的皮肤
        SkinPreference.getInstance()?.getSkin()?.apply {
            loadSkin(this)
        }
    }

    /**
     * 加载皮肤
     */
    fun loadSkin(skinPath: String?) {
        if (TextUtils.isEmpty(skinPath)) {
            //还原默认皮肤
            SkinPreference.getInstance()?.reset()
            SkinResource.getInstance()?.reset()
        } else {
            try {
                //宿主app的 resources;
                val appResource = mApplication.getResources()
                //
                //反射创建AssetManager 与 Resource
                val assetManager = AssetManager::class.java.newInstance()
                //资源路径设置 目录或压缩包
                val addAssetPath = assetManager.javaClass.getMethod(
                        "addAssetPath",
                        String::class.java
                )
                addAssetPath.invoke(assetManager, skinPath)

                //根据当前的设备显示器信息 与 配置(横竖屏、语言等) 创建Resources
                val skinResource = Resources(
                        assetManager,
                        appResource.getDisplayMetrics(),
                        appResource.getConfiguration()
                )

                //获取外部Apk(皮肤包) 包名
                val mPm = mApplication.getPackageManager()
                val info = mPm.getPackageArchiveInfo(
                        skinPath, PackageManager
                        .GET_ACTIVITIES
                )
                val packageName = info!!.packageName
                SkinResource.getInstance()?.applySkin(skinResource, packageName)

                //记录
                skinPath?.apply {
                    SkinPreference.getInstance()?.setSkin(this)
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        //通知采集的View 更新皮肤
        //被观察者改变 通知所有观察者
        setChanged()
        notifyObservers(null)
    }

    companion object {
        @Volatile
        private var instance: SkinManager? = null

        /**
         * 初始化操作
         */
        fun init(mApplication: Application) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = SkinManager(mApplication)
                    }
                }
            }
        }

        fun getInstance() = instance
    }
}