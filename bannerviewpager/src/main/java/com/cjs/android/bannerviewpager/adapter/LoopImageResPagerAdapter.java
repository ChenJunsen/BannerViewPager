package com.cjs.android.bannerviewpager.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cjs.android.bannerviewpager.BannerViewPager;

/**
 * 描述:主体是ImageView的无限轮播适配器
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月04日 14:18
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class LoopImageResPagerAdapter extends FInfiniteLoopPagerAdapter {
    private int[] mImgResList;
    private ImageView.ScaleType mScaleType;

    public LoopImageResPagerAdapter(BannerViewPager viewPager, int[] imgResList) {
        super(viewPager);
        mImgResList = imgResList;
        mScaleType=ImageView.ScaleType.FIT_XY;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(mImgResList[position]);
        view.setScaleType(mScaleType);
        return view;
    }

    @Override
    protected int getRealCount() {
        return mImgResList == null ? 0 : mImgResList.length;
    }

    public ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        mScaleType = scaleType;
    }
}
