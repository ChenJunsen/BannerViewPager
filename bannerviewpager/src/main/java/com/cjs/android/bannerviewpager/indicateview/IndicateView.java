package com.cjs.android.bannerviewpager.indicateview;

import com.cjs.android.bannerviewpager.annotation.IndicateViewGravity;

/**
 * 描述:指示器视图
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月02日 15:11
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public interface IndicateView {
    /**
     * 初始化IndicateView
     * @param totalSize 子元素的总长度
     * @param gravity 子元素在父布局中的Gravity,取值范围{@link IndicateViewGravity},具体还是需要到具体的实现类中进行设置
     */
    void initView(int totalSize,@IndicateViewGravity int gravity);

    /**
     * 设置当前选中的子元素视图
     * @param totalSize 子元素总数据量
     * @param currentPosition 当前子元素的位置，相对于totalSize,从0开始
     */
    void setCurrentView(int totalSize,int currentPosition);
}
