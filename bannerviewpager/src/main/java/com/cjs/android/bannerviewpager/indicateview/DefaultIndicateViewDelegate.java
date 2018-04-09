package com.cjs.android.bannerviewpager.indicateview;

/**
 * 描述:默认的指示器代理方法
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月02日 15:44
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class DefaultIndicateViewDelegate implements IndicateViewDelegate {

    @Override
    public void initView(IndicateView view, int length, int gravity) {
        if (view != null) {
            view.initView(length, gravity);
        }
    }

    @Override
    public void setCurrentView(IndicateView view, int totalSize, int currentPosition) {
        if (view != null) {
            view.setCurrentView(totalSize, currentPosition);
        }
    }
}
