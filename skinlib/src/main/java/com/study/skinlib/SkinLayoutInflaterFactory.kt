package com.study.skinlib

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import java.lang.Exception
import java.lang.reflect.Constructor
import java.util.*

/**
 * 皮肤控件填充器
 */
class SkinLayoutInflaterFactory : LayoutInflater.Factory2, Observer {
    lateinit var mActivity: Activity
    lateinit var skinAttribute: SkinAttribute

    public constructor(activity: Activity) {
        mActivity = activity
        skinAttribute = SkinAttribute()
    }


    /**
     * 如果利用Factory2创建控件，那么就会通过该方法来创建
     */
    override fun onCreateView(
            parent: View?,
            name: String,
            context: Context,
            attrs: AttributeSet
    ): View? {
        var view = createSDKView(name, context, attrs)
        if (view == null) {
            view = createView(name, context, attrs)
        }

        //记录我们所创建的view
        if (view != null) {
            //加载控件属性
            //加载属性
            skinAttribute.look(view, attrs)
        }
        return view
    }

    fun createSDKView(
            name: String,
            context: Context,
            attrs: AttributeSet
    ): View? {
        //如果包含，说明是自定义控件或者Google提供的扩展view
        if (-1 != name.indexOf(".")) {
            return null
        }

        //不包含"."，则需要在name前面加上包名，通过反射创建控件
        for (prefix in mClassPrefixList) {
            var view = createView(prefix + name, context, attrs)
            if (view != null) {
                return null
            }
        }
        return null
    }

    /**
     * 创建view
     */
    private fun createView(qualityName: String, context: Context, attrs: AttributeSet): View? {
        var constructor = findConstructor(context, qualityName)
        return constructor?.newInstance(context, attrs) ?: null
    }

    /**
     * 查找构造函数
     */
    private fun findConstructor(context: Context, qualityName: String): Constructor<out View>? {
        var constructor = mConstructorMap[qualityName]
        if (constructor == null) {
            try {
                val clazz = context.classLoader.loadClass(qualityName).asSubclass(View::class.java)
                constructor = clazz.getConstructor(*mConstructorSignature)
                mConstructorMap[qualityName] = constructor
            } catch (e: Exception) {
                Log.e("factory：：", e.message)
            }
        }
        return constructor
    }

    /**
     * Factory2的实现类不调用该接口
     */
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? = null

    /**
     * 观察者接受到被观察的改变通知调用该方法
     */
    override fun update(o: Observable?, arg: Any?) {

    }

    companion object {
        /**
         * 控件的报名
         */
        val mClassPrefixList = arrayOf(
                "android.widget.",
                "android.webkit.",
                "android.app.",
                "android.view."
        )

        /**
         * 创建view的构造函数
         */
        val mConstructorSignature = arrayOf(
                Context::class.java, AttributeSet::class.java
        )

        /**
         * view与构造函数的map
         */
        val mConstructorMap = hashMapOf<String, Constructor<out View>>()
    }
}