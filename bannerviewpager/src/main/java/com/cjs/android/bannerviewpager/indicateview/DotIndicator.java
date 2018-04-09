package com.cjs.android.bannerviewpager.indicateview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/**
 * 描述:圆点指示器
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月04日 10:07
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class DotIndicator extends AbsIndicator {

    private int mSelectedColor = Color.parseColor("#E91E63");//被选中指示点的颜色
    private int mNormalColor = Color.parseColor("#66f1f3f5");//正常指示点的颜色
    private int mSelectedRadius = 16;//选中的指示点的半径
    private int mNormalRadius = 16;//未选中的指示点的半径

    /**
     * 圆点指示器
     * @param context
     */
    public DotIndicator(Context context) {
        super(context);
    }

    /**
     * 圆点指示器
     * @param context
     * @param indicatorMargin
     */
    public DotIndicator(Context context, int indicatorMargin) {
        super(context, indicatorMargin);
    }

    /**
     * 圆点指示器
     * @param context
     * @param indicatorMargin
     * @param selectedColor
     * @param normalColor
     */
    public DotIndicator(Context context, int indicatorMargin, int selectedColor, int normalColor) {
        super(context, indicatorMargin);
        mSelectedColor = selectedColor;
        mNormalColor = normalColor;
    }

    @Override
    protected Drawable generateSelectedDrawable() {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setSize(mSelectedRadius * 2, mSelectedRadius * 2);
        gd.setColor(mSelectedColor);
        gd.setDither(true);
        return gd;
    }

    @Override
    protected Drawable generateNormalDrawable() {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setSize(mNormalRadius * 2, mNormalRadius * 2);
        gd.setDither(true);
        gd.setColor(mNormalColor);
        return gd;
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
        postInvalidate();
    }

    public int getNormalColor() {
        return mNormalColor;
    }

    public void setNormalColor(int normalColor) {
        mNormalColor = normalColor;
        postInvalidate();
    }

    public int getSelectedRadius() {
        return mSelectedRadius;
    }

    public void setSelectedRadius(int selectedRadius) {
        mSelectedRadius = selectedRadius;
        postInvalidate();
    }

    public int getNormalRadius() {
        return mNormalRadius;
    }

    public void setNormalRadius(int normalRadius) {
        mNormalRadius = normalRadius;
        postInvalidate();
    }
}
