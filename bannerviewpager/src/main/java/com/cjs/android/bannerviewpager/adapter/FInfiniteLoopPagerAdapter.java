package com.cjs.android.bannerviewpager.adapter;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.cjs.android.bannerviewpager.BannerViewPager;
import com.cjs.android.bannerviewpager.indicateview.IndicateView;
import com.cjs.android.bannerviewpager.indicateview.IndicateViewDelegate;

import java.util.ArrayList;

/**
 * 描述:无限轮播适配器(Fake)基类,原理是用{@link Integer#MAX_VALUE}模拟无限数量，优点是衔接完美流畅。
 * <p>
 * <br>作者: 陈俊森
 * <br>创建时间: 2018/4/4 0004 11:27
 * <br>邮箱: chenjunsen@outlook.com
 *
 * @version 1.0
 */
public abstract class FInfiniteLoopPagerAdapter extends PagerAdapter {
    private BannerViewPager mViewPager;

    private ArrayList<View> mViewList = new ArrayList<>();

    class LoopPagerDelegate implements IndicateViewDelegate {

        @Override
        public void initView(IndicateView view, int length, int gravity) {
            if (view != null) {
                view.initView(getRealCount(), gravity);
            }
        }

        @Override
        public void setCurrentView(IndicateView view, int totalSize, int currentPosition) {
            if (view != null) {
                view.setCurrentView(getRealCount(), currentPosition % getRealCount());
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mViewList.clear();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        if (getCount() == 0) return;
        int half = Integer.MAX_VALUE / 2;
        int start = half - half % getRealCount();
        mViewPager.getViewPager().setCurrentItem(start, false);
    }

    public FInfiniteLoopPagerAdapter(BannerViewPager viewPager) {
        this.mViewPager = viewPager;
        viewPager.setIndicateViewDelegate(new LoopPagerDelegate());
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = position % getRealCount();
        View itemView = findViewByPosition(container, realPosition);
        container.addView(itemView);
        return itemView;
    }


    private View findViewByPosition(ViewGroup container, int position) {
        for (View view : mViewList) {
            if (((int) view.getTag()) == position && view.getParent() == null) {
                return view;
            }
        }
        View view = getView(container, position);
        view.setTag(position);
        mViewList.add(view);
        return view;
    }

    public abstract View getView(ViewGroup container, int position);

    /**
     * 获取当前轮播的子元素数量。
     * 该方法是复写父类的getCount方法，用于构建ViewPager的数量。
     * 如果想要获取到实际的子元素数量，请不要使用该方法，改用{@link #getRealCount()}
     * @return 但是由于这里模拟无限轮播，所以，返回的是{@link Integer#MAX_VALUE}。但是如果实际传入的子元素数量<=1,则返回0或者1
     */
    @Override
    public final int getCount() {
        return getRealCount() <= 1 ? getRealCount() : Integer.MAX_VALUE;
    }

    /**
     * 获取实际的子元素数量
     * @return
     */
    protected abstract int getRealCount();
}
