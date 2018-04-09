package com.cjs.android.bannerviewpager.indicateview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cjs.android.bannerviewpager.BannerViewPager;
import com.cjs.android.bannerviewpager.util.DimensionUtil;

/**
 * 描述:指示点抽象基类
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月03日 16:02
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public abstract class AbsIndicator extends LinearLayout implements IndicateView {
    private ImageView[] mIndicators;//所有的指示点集合
    private Drawable mSelectedIndicator;//选中的指示点的图片
    private Drawable mNormalIndicator;//未选中的指示点的图片
    private int mIndicatorMargin;//指示点之间的间距
    private int mLastSelectedPosition;//上一个被选中的指示点的位置，从0开始

    /**
     * 创建一个指示器，默认设置指示点之间的Margin为10dp
     * @param context
     */
    public AbsIndicator(Context context) {
        super(context);
        mIndicatorMargin = DimensionUtil.dip2px(context, 10);
    }

    /**
     * 创建一个指示器
     * @param context
     * @param indicatorMargin 指示点之间的Margin
     */
    public AbsIndicator(Context context, int indicatorMargin) {
        super(context);
        mIndicatorMargin = indicatorMargin;
    }

    /**
     * 生成被选中时的指示点图片
     *
     * @return
     */
    protected abstract Drawable generateSelectedDrawable();

    /**
     * 生成普通的指示点图片
     *
     * @return
     */
    protected abstract Drawable generateNormalDrawable();

    /**
     * 默认的起始位置
     *
     * @return 默认值为0，代表第一个
     */
    public int getDefaultStartPosition() {
        return 0;
    }

    @Override
    public void initView(int length, int gravity) {
        setOrientation(HORIZONTAL);
        if (length > 0) {
            switch (gravity) {
                case BannerViewPager.INDICATE_GRAVITY_BOTTOM:
                    setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                    break;
                case BannerViewPager.INDICATE_GRAVITY_CENTER:
                    setGravity(Gravity.CENTER);
                    break;
                case BannerViewPager.INDICATE_GRAVITY_TOP:
                    setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                    break;
                default:
                    setGravity(Gravity.CENTER);
                    break;
            }
            mNormalIndicator = generateNormalDrawable();
            mSelectedIndicator = generateSelectedDrawable();

            mIndicators = new ImageView[length];
            for (int i = 0; i < length; i++) {
                ImageView dot = new ImageView(getContext());
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.setMargins(mIndicatorMargin, 0, mIndicatorMargin, 0);
                dot.setLayoutParams(lp);
                dot.setImageDrawable(mNormalIndicator);
                mIndicators[i] = dot;
                addView(dot);
            }

            mLastSelectedPosition=getDefaultStartPosition();
            setCurrentView(length, mLastSelectedPosition);
        }
    }

    @Override
    public void setCurrentView(int totalSize, int currentPosition) {
        if (currentPosition < 0 || currentPosition > totalSize - 1) {
            return;
        }
        mIndicators[mLastSelectedPosition].setImageDrawable(mNormalIndicator);
        mIndicators[currentPosition].setImageDrawable(mSelectedIndicator);
        mLastSelectedPosition=currentPosition;
    }
}
