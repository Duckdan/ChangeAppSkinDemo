package com.study.skinlib.utils

import android.app.Activity
import android.content.Context
import android.os.Build

/**
 * 皮肤主题工具类
 */
object SkinThemeUtils {
    //app控件主题色
    val APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = IntArray(1)
    //顶部状态栏/底部导航栏颜色属性
    val STATUSBAR_COLOR_ATTRS = IntArray(2)

    init {
        APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS[0] = androidx.appcompat.R.attr.colorPrimaryDark

        STATUSBAR_COLOR_ATTRS[0] = android.R.attr.statusBarColor
        STATUSBAR_COLOR_ATTRS[1] = android.R.attr.navigationBarColor
    }


    /**
     * 获得theme中的属性中定义的 资源id
     *
     * @param context
     * @param attrs
     * @return
     */
    fun getResId(context: Context, attrs: IntArray): IntArray {
        val resIds = IntArray(attrs.size)
        val a = context.obtainStyledAttributes(attrs)
        for (i in attrs.indices) {
            resIds[i] = a.getResourceId(i, 0)
        }
        a.recycle()
        return resIds
    }


    /**
     * 更新状态栏
     *
     * @param activity
     */
    fun updateStatusBarColor(activity: Activity) {
        //5.0以上才能修改
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        //获得 statusBarColor 与 nanavigationBarColor (状态栏颜色)
        //当与 colorPrimaryDark  不同时 以statusBarColor为准
        val resIds = getResId(activity, STATUSBAR_COLOR_ATTRS)
        val statusBarColorResId = resIds[0]
        val navigationBarColor = resIds[1]

        //如果直接在style中写入固定颜色值(而不是 @color/XXX ) 获得0
        if (statusBarColorResId != 0) {
            SkinResource.getInstance()?.getColor(statusBarColorResId)?.apply {
                activity.window.statusBarColor = this
            }
        } else {
            //获得 colorPrimaryDark
            val colorPrimaryDarkResId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0]
            if (colorPrimaryDarkResId != 0) {
                SkinResource.getInstance()?.getColor(colorPrimaryDarkResId)?.apply {
                    activity.window.statusBarColor = this
                }
            }
        }
        if (navigationBarColor != 0) {
            SkinResource.getInstance()?.getColor(navigationBarColor)?.apply {
                activity.window.navigationBarColor = this
            }
        }
    }

}