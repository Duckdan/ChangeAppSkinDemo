package com.study.skinlib

import android.util.AttributeSet
import android.view.View
import com.study.skinlib.data.SkinPair
import com.study.skinlib.utils.SkinThemeUtils

/**
 * 页面控件属性管理器
 */
class SkinAttribute {
    private val mAttributes = arrayListOf<String>()

    init {
        mAttributes.add("background")
        mAttributes.add("src")
        mAttributes.add("textColor")
        mAttributes.add("drawableLeft")
        mAttributes.add("drawableTop")
        mAttributes.add("drawableRight")
        mAttributes.add("drawableBottom")
    }

    //记录页面控件中View的属性
    private val mSkinViews = arrayListOf<SkinView>()

    /**
     * 实施替换
     */
    fun look(view: View, attrs: AttributeSet) {
        var mSkinPars = arrayListOf<SkinPair>()
        //0<attrs.attributeCount
        for (i in 0 until attrs.attributeCount) {
            //获取属性名
            val attributeName = attrs.getAttributeName(i)
            if (mAttributes.contains(attributeName)) {
                //获取属性值
                val attributeValue = attrs.getAttributeValue(i)
                //例如针对color的属性的属性值写死
                if (attributeValue.startsWith("#")) {
                    continue
                }
                var resId = 0
                // 以 ？开头的表示使用 属性
                if (attributeValue.startsWith("?")) {
                    val attrId = Integer.parseInt(attributeValue.substring(1))
                    resId = SkinThemeUtils.getResId(view.context, intArrayOf(attrId))[0]
                } else {
                    // 正常以 @ 开头
                    resId = Integer.parseInt(attributeValue.substring(1))
                }

                val skinPair = SkinPair(attributeName, resId)
                mSkinPars.add(skinPair)
            }
        }

        if (!mSkinPars.isEmpty() || view is SkinViewSupport) {
            val skinView = SkinView(view, mSkinPars)
            // 如果选择过皮肤 ，调用 一次 applySkin 加载皮肤的资源
            skinView.applySkin()
            mSkinViews.add(skinView)
        }
    }

    /**
     *  对所有的view中的所有的属性进行皮肤修改
     */
    fun applySkin() {
        for (mSkinView in mSkinViews) {
            mSkinView.applySkin()
        }
    }

}