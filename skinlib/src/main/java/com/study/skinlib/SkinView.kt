package com.study.skinlib

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.study.skinlib.data.SkinPair
import com.study.skinlib.utils.SkinResource

/**
 * 控件View及其使用的属性
 */
class SkinView(var view: View, var skinPairs: List<SkinPair>) {

    /**
     * 对控件的View修改其属性值
     */
    fun applySkin() {
        //自定义控件替换颜色
        if (view is SkinViewSupport) {
            (view as SkinViewSupport).applySkin()
        }

        var left: Drawable? = null
        var top: Drawable? = null
        var right: Drawable? = null
        var bottom: Drawable? = null

        for (skinPair in skinPairs) {
            when (skinPair.attributeName) {
                "background" -> {
                    var background = SkinResource.getInstance()?.getBackground(skinPair
                            .resId)
                    //背景可能是 @color 也可能是 @drawable
                    if (background is Int) {
                        view.setBackgroundColor(background)
                    } else {
                        ViewCompat.setBackground(view, background as Drawable)
                    }
                }
                "src" -> {
                    var background = SkinResource.getInstance()?.getBackground(skinPair
                            .resId)
                    if (background is Int) {
                        (view as ImageView).setImageDrawable(ColorDrawable(background))
                    } else {
                        (view as ImageView).setImageDrawable(background as Drawable)
                    }
                }
                "textColor" -> {
                    (view as TextView).setTextColor(SkinResource.getInstance()?.getColorStateList(skinPair.resId))
                }
                "drawableLeft" -> {
                    left = SkinResource.getInstance()?.getDrawable(skinPair.resId)
                }
                "drawableTop" -> {
                    top = SkinResource.getInstance()?.getDrawable(skinPair.resId)
                }
                "drawableRight" -> {
                    right = SkinResource.getInstance()?.getDrawable(skinPair.resId)
                }
                "drawableBottom" -> {
                    bottom = SkinResource.getInstance()?.getDrawable(skinPair.resId)
                }
                else -> {
                }
            }
        }

        if (view is TextView) {
            if (left != null || top != null || right != null || bottom != null) {
                (view as TextView).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
            }
        }
    }
}