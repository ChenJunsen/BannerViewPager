package com.cjs.android.bannerviewpager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.cjs.android.bannerviewpager.annotation.IndicateViewGravity;
import com.cjs.android.bannerviewpager.annotation.IndicateViewLayoutGravity;
import com.cjs.android.bannerviewpager.indicateview.DefaultIndicateViewDelegate;
import com.cjs.android.bannerviewpager.indicateview.DotIndicator;
import com.cjs.android.bannerviewpager.indicateview.IndicateView;
import com.cjs.android.bannerviewpager.indicateview.IndicateViewDelegate;
import com.cjs.android.bannerviewpager.util.DrawableUtil;

import java.lang.reflect.Field;
import java.util.Timer;

/**
 * 描述:带指示视图的轮播ViewPager
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月02日 15:00
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class BannerViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener {
    /**
     * IndicateView在父布局中居于父布局底部，且水平居中
     */
    public static final int INDICATE_LAYOUT_GRAVITY_BOTTOM = 0x00000;
    /**
     * IndicateView在父布局中居于父布局中心
     */
    public static final int INDICATE_LAYOUT_GRAVITY_CENTER = 0x0001;
    /**
     * IndicateView在父布局中居于父布局顶部，且水平居中
     */
    public static final int INDICATE_LAYOUT_GRAVITY_TOP = 0x0002;
    /**
     * IndicateView内部子元素居于顶部，且水平居中
     */
    public static final int INDICATE_GRAVITY_TOP = 0x0002;
    /**
     * IndicateView内部子元素居于中心
     */
    public static final int INDICATE_GRAVITY_CENTER = 0x0000;
    /**
     * IndicateView内部子元素居于底部，且水平居中
     */
    public static final int INDICATE_GRAVITY_BOTTOM = 0x0001;
    /**
     * 默认过渡动画持续时间（毫秒）,-1表示采用系统默认计算的duration
     */
    public static final int DEFAULT_SMOOTH_SCROLL_DURATION = -1;
    /**
     * 内置ViewPager
     */
    ViewPager mViewPager;
    /**
     * 内置ViewPager的适配器
     */
    PagerAdapter mPagerAdapter;
    /**
     * 指示器View
     */
    View mIndicateView;
    IndicateViewDelegate mIndicateViewDelegate = new DefaultIndicateViewDelegate();
    /**
     * 控件最近被触摸到的时间戳
     */
    long mRecentTouchMillis;
    /**
     * 轮播播放间隔(毫秒)
     */
    int mInterval;
    /**
     * 开始轮播时的播放延迟,默认等于轮播播放间隔
     */
    int mDelay = mInterval;
    /**
     * 轮播定时器的Handler
     */
    BannerTimerHandler mBannerTimerHandler;
    BannerTimerTask mBannerTimerTask;
    /**
     * 轮播定时器
     */
    Timer mTimer;
    /**
     * 指示控件的Gravity
     */
    int mIndicateGravity;
    /**
     * IndicateView内边距
     */
    int mIndicatePaddingTop, mIndicatePaddingBottom, mIndicatePaddingLeft, mIndicatePaddingRight;
    /**
     * IndicateView外边距
     */
    int mIndicateMarginTop, mIndicateMarginBottom, mIndicateMarginLeft, mIndicateMarginRight;

    Drawable mIndicateBackgroundDrawable;
    /**
     * IndicateView的背景透明度
     */
    int mIndicateBackgroundOpacity;
    /**
     * IndicateView的背景颜色
     */
    @ColorInt
    int mIndicateBackgroundColor;
    /**
     * IndicateView的内部Gravity
     */
    @IndicateViewGravity
    int mIndicateViewGravity;
    /**
     * IndicateView的外部Gravity
     */
    @IndicateViewLayoutGravity
    int mIndicateViewLayoutGravity;
    /**
     * 是否开启平滑过渡动画
     */
    boolean isEnableSmoothScroll = true;
    /**
     * 平滑过渡动画持续时间(毫秒)
     */
    int mSmoothScrollDuration;
    /**
     * 默认的滑动插值器。静态写法是参考官方ViewPager的实现方式
     */
    static final DefaultSmoothInterpolator sDefaultSmoothInterpolator = new DefaultSmoothInterpolator();

    /**
     * 带指示视图的轮播ViewPager
     *
     * @param context
     */
    public BannerViewPager(Context context) {
        super(context);
        initView(context, null);
    }

    /**
     * 带指示视图的轮播ViewPager
     *
     * @param context
     * @param attrs
     */
    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    /**
     * 带指示视图的轮播ViewPager
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 带指示视图的轮播ViewPager
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @TargetApi(21)
    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (mViewPager != null) {
            removeView(mViewPager);
        }
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BannerViewPager);
            mInterval = array.getInt(R.styleable.BannerViewPager_banner_interval, 0);
            mDelay = array.getInt(R.styleable.BannerViewPager_banner_start_delay, 0);
            mIndicateBackgroundDrawable = array.getDrawable(R.styleable.BannerViewPager_indicate_background);
            mIndicateBackgroundColor = array.getColor(R.styleable.BannerViewPager_indicate_background_color, Color.TRANSPARENT);
            mIndicateBackgroundOpacity = array.getInt(R.styleable.BannerViewPager_indicate_background_alpha, 0);
            mIndicateGravity = array.getInt(R.styleable.BannerViewPager_indicate_gravity, INDICATE_GRAVITY_CENTER);
            mIndicateViewLayoutGravity = array.getInt(R.styleable.BannerViewPager_indicate_layout_gravity, INDICATE_LAYOUT_GRAVITY_BOTTOM);
            mIndicateMarginBottom = array.getDimensionPixelOffset(R.styleable.BannerViewPager_indicate_margin_bottom, 0);
            mIndicateMarginLeft = array.getDimensionPixelOffset(R.styleable.BannerViewPager_indicate_margin_left, 0);
            mIndicateMarginRight = array.getDimensionPixelOffset(R.styleable.BannerViewPager_indicate_margin_right, 0);
            mIndicateMarginTop = array.getDimensionPixelOffset(R.styleable.BannerViewPager_indicate_margin_top, 0);
            mIndicatePaddingBottom = array.getDimensionPixelOffset(R.styleable.BannerViewPager_indicate_padding_bottom, 0);
            mIndicatePaddingTop = array.getDimensionPixelOffset(R.styleable.BannerViewPager_indicate_padding_top, 0);
            mIndicatePaddingLeft = array.getDimensionPixelOffset(R.styleable.BannerViewPager_indicate_padding_left, 0);
            mIndicatePaddingRight = array.getDimensionPixelOffset(R.styleable.BannerViewPager_indicate_padding_right, 0);
            isEnableSmoothScroll = array.getBoolean(R.styleable.BannerViewPager_banner_enable_smooth_scroll, true);
            mSmoothScrollDuration = array.getInt(R.styleable.BannerViewPager_banner_smooth_scroll_duration, DEFAULT_SMOOTH_SCROLL_DURATION);
            array.recycle();
        }
        mViewPager = new ViewPager(context);
        mViewPager.setId(R.id.id_banner_view_pager);
        mViewPager.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mViewPager);
        mBannerTimerHandler = new BannerTimerHandler(this);

        if (!(mSmoothScrollDuration == DEFAULT_SMOOTH_SCROLL_DURATION))
            setSmoothScrollDuration(mSmoothScrollDuration);

        setIndicateView(new DotIndicator(getContext()));
    }

    /**
     * 获取内置真实ViewPager
     *
     * @return
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 获取当前控件的IndicateView的代理器
     *
     * @return
     */
    public IndicateViewDelegate getIndicateViewDelegate() {
        return mIndicateViewDelegate;
    }

    /**
     * 设置当前控件的IndicateView代理器。使用方式参考:<br>
     * {@link DefaultIndicateViewDelegate} 默认的代理器<br>
     * {@link com.cjs.android.bannerviewpager.adapter.FInfiniteLoopPagerAdapter.LoopPagerDelegate} 无限轮播代理器<br>
     *
     * @param indicateViewDelegate
     */
    public void setIndicateViewDelegate(IndicateViewDelegate indicateViewDelegate) {
        mIndicateViewDelegate = indicateViewDelegate;
    }

    /**
     * 获取当前控件内嵌的IndicateView
     *
     * @return
     */
    public View getIndicateView() {
        return mIndicateView;
    }

    /**
     * 设置IndicateView
     *
     * @param indicateView
     */
    public void setIndicateView(IndicateView indicateView) {
        if (indicateView == null) {
            throw new IllegalArgumentException("设置IndicateView不能为空!");
        } else if (!(indicateView instanceof View)) {
            throw new IllegalArgumentException("设置IndicateView必须得是View的子类");
        } else {
            if (mIndicateView != null) {
                removeView(mIndicateView);
            }
            mIndicateView = (View) indicateView;

            addView(mIndicateView);
            if (Build.VERSION.SDK_INT >= 17) {
                /*国际化兼容*/
                mIndicateView.setPaddingRelative(mIndicatePaddingLeft, mIndicatePaddingTop, mIndicatePaddingRight, mIndicatePaddingBottom);
            } else {
                mIndicateView.setPadding(mIndicatePaddingLeft, mIndicatePaddingTop, mIndicatePaddingRight, mIndicatePaddingBottom);
            }
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp.addRule(getRelativeRule());
            lp.setMargins(mIndicateMarginLeft, mIndicateMarginTop, mIndicateMarginRight, mIndicateMarginBottom);
            mIndicateView.setLayoutParams(lp);
            if (mIndicateBackgroundDrawable != null) {
                DrawableUtil.setBackgroundDrawable(mIndicateView, mIndicateBackgroundDrawable);
            } else {
                DrawableUtil.setBackgroundDrawable(mIndicateView, mIndicateBackgroundOpacity, mIndicateBackgroundColor);
            }
            mIndicateViewDelegate.initView((IndicateView) mIndicateView, mPagerAdapter == null ? 0 : mPagerAdapter.getCount(), mIndicateGravity);
        }
    }

    private int getRelativeRule() {
        switch (mIndicateViewLayoutGravity) {
            default:
                return RelativeLayout.ALIGN_PARENT_BOTTOM;
            case INDICATE_LAYOUT_GRAVITY_TOP:
                return RelativeLayout.ALIGN_PARENT_TOP;
            case INDICATE_LAYOUT_GRAVITY_CENTER:
                return RelativeLayout.CENTER_IN_PARENT;
        }
    }

    /**
     * 获取自动轮播的间隔时间
     *
     * @return 单位毫秒
     */
    public int getInterval() {
        return mInterval;
    }

    /**
     * 设置自动轮播的间隔时间
     *
     * @param interval 单位毫秒
     */
    public void setInterval(int interval) {
        mInterval = interval;
        if (isPlaying()) {
            stopPlay();
            startPlay();
        }
    }

    /**
     * 获取自动轮播时的开始播放时的延迟时间
     *
     * @return ms
     */
    public int getDelay() {
        return mDelay;
    }

    /**
     * 设置自动轮播时的开始播放时的延迟时间
     *
     * @param delay 默认时间为轮播的间隔时间，单位ms
     */
    public void setDelay(int delay) {
        mDelay = delay;
    }

    /**
     * 获取IndicateView内部的子元素布局
     *
     * @return {@link #INDICATE_GRAVITY_BOTTOM},{@link #INDICATE_GRAVITY_CENTER},{@link #INDICATE_GRAVITY_TOP}
     */
    public @IndicateViewGravity
    int getIndicateViewGravity() {
        return mIndicateViewGravity;
    }

    /**
     * 设置IndicateView内部的子元素布局
     *
     * @param indicateViewGravity {@link #INDICATE_GRAVITY_BOTTOM},{@link #INDICATE_GRAVITY_CENTER},{@link #INDICATE_GRAVITY_TOP}
     */
    public void setIndicateViewGravity(@IndicateViewGravity int indicateViewGravity) {
        mIndicateViewGravity = indicateViewGravity;
    }

    /**
     * 获取IndicateView在父布局中的位置
     *
     * @return {@link #INDICATE_LAYOUT_GRAVITY_BOTTOM},{@link #INDICATE_LAYOUT_GRAVITY_CENTER},{@link #INDICATE_LAYOUT_GRAVITY_TOP}
     */
    public @IndicateViewLayoutGravity
    int getIndicateViewLayoutGravity() {
        return mIndicateViewLayoutGravity;
    }

    /**
     * 设置IndicateView在父布局中的位置：
     *
     * @param indicateViewLayoutGravity {@link #INDICATE_LAYOUT_GRAVITY_BOTTOM},{@link #INDICATE_LAYOUT_GRAVITY_CENTER},{@link #INDICATE_LAYOUT_GRAVITY_TOP}
     */
    public void setIndicateViewLayoutGravity(@IndicateViewLayoutGravity int indicateViewLayoutGravity) {
        mIndicateViewLayoutGravity = indicateViewLayoutGravity;
    }

    /**
     * 轮播时的平滑过渡效果是否开启
     *
     * @return
     */
    public boolean isEnableSmoothScroll() {
        return isEnableSmoothScroll;
    }

    /**
     * 设置是否开启轮播时的平滑过渡效果
     *
     * @param enableSmoothScroll
     */
    public void setEnableSmoothScroll(boolean enableSmoothScroll) {
        isEnableSmoothScroll = enableSmoothScroll;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        /*ViewPager联动IndicateView*/
        mIndicateViewDelegate.setCurrentView((IndicateView) mIndicateView, mPagerAdapter == null ? 0 : mPagerAdapter.getCount(), position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mRecentTouchMillis = System.currentTimeMillis();
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 开始轮播
     */
    public void startPlay() {
        /*轮播时间间隔不大于0以及轮播的数量不大于1，此时轮播无意义，都认为是无效的轮播*/
        if (mInterval <= 0 || mViewPager == null || mPagerAdapter.getCount() <= 1) {
            return;
        }
        stopPlay();
        mTimer = new Timer();
        mBannerTimerTask = new BannerTimerTask(this);
        mTimer.schedule(mBannerTimerTask, mDelay < 0 ? 0 : mDelay, mInterval);
    }

    /**
     * 停止轮播
     */
    public void stopPlay() {
        if (mBannerTimerTask != null) {
            mBannerTimerTask.cancel();
        }
        if (mTimer != null) {
            mTimer.purge();//purge的作用在于清除掉timer中已经cancel掉的timerTask,回收内存空间
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 轮播功能是否开启
     *
     * @return true-开启  false-未开启
     */
    public boolean isPlaying() {
        return mTimer != null;
    }

    /**
     * 设置列表适配器
     *
     * @param pagerAdapter
     */
    public void setAdapter(PagerAdapter pagerAdapter) {
        if (pagerAdapter != null) {
            mPagerAdapter = pagerAdapter;
            mViewPager.addOnPageChangeListener(this);
            mViewPager.setAdapter(mPagerAdapter);
            dataSetChanged();
            pagerAdapter.registerDataSetObserver(new BannerPagerObserver());
        } else {
            throw new IllegalArgumentException("BannerViewPager设置的适配器不能为空！");
        }
    }

    private class BannerPagerObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            dataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            dataSetChanged();
        }
    }

    private void dataSetChanged() {
        startPlay();
        if (mIndicateView != null) {
            mIndicateViewDelegate.initView((IndicateView) mIndicateView, mPagerAdapter == null ? 0 : mPagerAdapter.getCount(), mIndicateGravity);
        }
    }

    /**
     * 设置ViewPager滑动动画持续时间,目前一旦设置了自定义的持续时间就无法变为默认时间了
     *
     * @param duration 持续时间(毫秒)
     */
    public void setSmoothScrollDuration(int duration) {
        setSmoothScrollDuration(duration, null);
    }

    /**
     * 设置viewPager滑动动画持续时间
     *
     * @param during       持续时间(毫秒)
     * @param interpolator 滑动插值器，若为空，则采用默认插值器
     */
    public void setSmoothScrollDuration(final int during, Interpolator interpolator) {
        try {
            // viePager平移动画事件
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            Scroller mScroller = new Scroller(getContext(), interpolator == null ? sDefaultSmoothInterpolator : interpolator) {

                @Override
                public void startScroll(int startX, int startY, int dx,
                                        int dy, int duration) {
                    // 如果手工滚动,则加速滚动
                    if (System.currentTimeMillis() - mRecentTouchMillis > mInterval) {
                        duration = during;
                    } else {
                        duration /= 2;
                    }
                    super.startScroll(startX, startY, dx, dy, duration);
                }

                @Override
                public void startScroll(int startX, int startY, int dx,
                                        int dy) {
                    super.startScroll(startX, startY, dx, dy, during);
                }
            };
            mField.set(mViewPager, mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@link ViewPager}自带的滑动插值器
     */
    private static class DefaultSmoothInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    }


}
