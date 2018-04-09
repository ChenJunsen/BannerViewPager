package com.cjs.android.bannerviewpager;

import java.lang.ref.WeakReference;
import java.util.TimerTask;

/**
 * 描述:
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月02日 16:05
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class BannerTimerTask extends TimerTask {
    private WeakReference<BannerViewPager> mBannerViewPagerWeakReference;

    public BannerTimerTask(BannerViewPager bannerViewPager) {
        mBannerViewPagerWeakReference = new WeakReference<BannerViewPager>(bannerViewPager);
    }

    @Override
    public void run() {
        BannerViewPager bannerViewPager = mBannerViewPagerWeakReference.get();
        if (bannerViewPager != null) {
            if (bannerViewPager.isShown() && System.currentTimeMillis() - bannerViewPager.mRecentTouchMillis > bannerViewPager.mInterval) {
                bannerViewPager.mBannerTimerHandler.sendEmptyMessage(BannerTimerHandler.HANDLE_MSG_SUCCESS);
            }else{
                bannerViewPager.mBannerTimerHandler.sendEmptyMessage(BannerTimerHandler.HANDLE_MSG_FAILED);
            }
        } else {
            cancel();
        }
    }

}
