package com.cjs.widget.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.cjs.android.bannerviewpager.BannerViewPager;
import com.cjs.android.bannerviewpager.adapter.LoopImageResPagerAdapter;
import com.cjs.android.bannerviewpager.indicateview.DotIndicator;
import com.cjs.android.bannerviewpager.util.DimensionUtil;

/**
 * BannerViewPager测试样例
 */
public class MainActivity extends AppCompatActivity {
    private BannerViewPager mBannerViewPager;
    private LoopImageResPagerAdapter mLoopImageResPagerAdapter;
    private DotIndicator mDotIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBannerViewPager = findViewById(R.id.bannerView);
        mLoopImageResPagerAdapter = new LoopImageResPagerAdapter(mBannerViewPager, new int[]{
                R.mipmap.banner1, R.mipmap.banner2, R.mipmap.banner3, R.mipmap.banner4, R.mipmap.banner5
        });
        mLoopImageResPagerAdapter.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mBannerViewPager.setAdapter(mLoopImageResPagerAdapter);

        mDotIndicator = new DotIndicator(this, DimensionUtil.dip2px(this, 10), Color.parseColor("#FF4081"), Color.WHITE);
        mBannerViewPager.setIndicateView(mDotIndicator);
    }
}
