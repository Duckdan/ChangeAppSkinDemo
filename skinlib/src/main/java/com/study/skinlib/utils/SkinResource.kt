package com.study.skinlib.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils

class SkinResource {

    //app原始的resource
    private lateinit var mAppResource: Resources
    //皮肤的resource
    private var mSkinResources: Resources? = null

    //皮肤包的名字
    private lateinit var mSkinPkgName: String
    //是否使用默认皮肤包
    private var isDefaultSkin = true

    private constructor(context: Context) {
        mAppResource = context.resources
    }

    fun applySkin(resources: Resources, pkgName: String) {
        mSkinResources = resources
        mSkinPkgName = pkgName
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null
    }


    /**
     * 1.通过原始app中的resId(R.color.XX)获取到自己的 名字
     * 2.根据名字和类型获取皮肤包中的ID
     */
    fun getIdentifier(resId: Int): Int {
        if (isDefaultSkin) {
            return resId
        }
        //获取资源的名称和类型
        val resName = mAppResource.getResourceEntryName(resId)
        val resType = mAppResource.getResourceTypeName(resId)
        //通过插件的Resources获取到插件中相同名称和类型的id
        return mSkinResources?.getIdentifier(resName, resType, mSkinPkgName) ?: 0
    }

    /**
     * 输入主APP的ID，到皮肤APK文件中去找到对应ID的颜色值
     *
     * @param resId
     * @return
     */
    fun getColor(resId: Int): Int {
        if (isDefaultSkin) {
            return mAppResource.getColor(resId)
        }
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            mAppResource.getColor(resId)
        } else mSkinResources?.getColor(skinId) ?: 0
    }

    fun getColorStateList(resId: Int): ColorStateList? {
        if (isDefaultSkin) {
            return mAppResource.getColorStateList(resId)
        }
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            mAppResource.getColorStateList(resId)
        } else mSkinResources?.getColorStateList(skinId)
    }

    fun getDrawable(resId: Int): Drawable? {
        if (isDefaultSkin) {
            return mAppResource.getDrawable(resId)
        }
        //通过 app的resource 获取id 对应的 资源名 与 资源类型
        //找到 皮肤包 匹配 的 资源名资源类型 的 皮肤包的 资源 ID
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            mAppResource.getDrawable(resId)
        } else mSkinResources?.getDrawable(skinId)
    }


    /**
     * 可能是Color 也可能是drawable
     *
     * @return
     */
    fun getBackground(resId: Int): Any? {
        //根据资源id获取资源类型
        val resourceTypeName = mAppResource.getResourceTypeName(resId)

        return if ("color" == resourceTypeName) {
            getColor(resId)
        } else {
            // drawable
            getDrawable(resId)
        }
    }


    /**
     * 重置资源
     */
    fun reset() {
        mSkinResources = null
        mSkinPkgName = ""
        isDefaultSkin = true
    }

    companion object {
        @Volatile
        private var instance: SkinResource? = null

        /**
         * 初始化操作
         */
        fun init(context: Context) {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = SkinResource(context)
                    }
                }
            }
        }

        fun getInstance() = instance
    }
}