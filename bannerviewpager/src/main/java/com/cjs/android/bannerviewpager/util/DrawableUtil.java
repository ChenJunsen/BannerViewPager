package com.cjs.android.bannerviewpager.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;

/**
 * 描述:图片工具类
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月03日 11:01
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class DrawableUtil {

    /**
     * 给指定控件设置背景图片
     *
     * @param v
     * @param backgroundDrawable
     */
    public static void setBackgroundDrawable(View v, Drawable backgroundDrawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            v.setBackground(backgroundDrawable);
        } else {
            v.setBackgroundDrawable(backgroundDrawable);
        }
    }

    /**
     * 给指定控件设置背景图片
     *
     * @param v
     * @param alpha 背景透明度（0~255,0是全透明，255完全不透明）
     * @param color 背景颜色（ARGB）
     */
    public static void setBackgroundDrawable(View v, int alpha, @ColorInt int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setDither(true);
        gd.setColor(color);
        gd.setAlpha(alpha);
        setBackgroundDrawable(v, gd);
    }
}
