package com.cjs.android.bannerviewpager;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;

import com.cjs.android.bannerviewpager.indicateview.IndicateView;

import java.lang.ref.WeakReference;

/**
 * 描述:
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月02日 16:04
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class BannerTimerHandler extends Handler {
    /**
     * 当前的BannerViewPage控件处于可见状态，且触摸间隔时间大于设置的延迟时间，
     * 此时几时状态为成功状态，即可以进行轮播消息的发送
     */
    static final int HANDLE_MSG_SUCCESS = 1;
    /**
     * 与{@link #HANDLE_MSG_SUCCESS}相反的状态
     */
    static final int HANDLE_MSG_FAILED = -1;

    private WeakReference<BannerViewPager> mBannerViewPagerWeakReference;

    /**
     * 是否开启平滑过渡动画
     */
    private boolean isEnableSmoothScroll=true;

    /**
     * 轮播定时器的Handler
     * @param bannerViewPager
     */
    public BannerTimerHandler(BannerViewPager bannerViewPager) {
        mBannerViewPagerWeakReference = new WeakReference<BannerViewPager>(bannerViewPager);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLE_MSG_SUCCESS:
                BannerViewPager bannerViewPager = mBannerViewPagerWeakReference.get();
                if (bannerViewPager != null) {
                    ViewPager viewPager = bannerViewPager.mViewPager;
                    //viewPager的position是从0开始的，总数量count则是真实数量，所以比较时要减一
                    int nextItem = viewPager.getCurrentItem() + 1;
                    int totalSize = viewPager.getAdapter().getCount();
                    if (nextItem > totalSize) {
                        nextItem = 0;
                    }
                    viewPager.setCurrentItem(nextItem, bannerViewPager.isEnableSmoothScroll);
                    bannerViewPager.mIndicateViewDelegate.setCurrentView((IndicateView) bannerViewPager.mIndicateView, totalSize, nextItem);
                }
                break;
            case HANDLE_MSG_FAILED:
                removeCallbacksAndMessages(null);
                break;
        }

    }

}
