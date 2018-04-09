package com.cjs.android.bannerviewpager.indicateview;

/**
 * 描述:指示器视图代理方法
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月02日 15:40
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public interface IndicateViewDelegate {
    void initView(IndicateView view, int length, int gravity);
    void setCurrentView(IndicateView view,int totalSize,int currentPosition);
}
