package com.cjs.android.bannerviewpager.annotation;

import android.support.annotation.IntDef;

import com.cjs.android.bannerviewpager.BannerViewPager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 描述:指示视图的内部Gravity属性限制
 * <p>
 * 作者:陈俊森
 * 创建时间:2018年04月03日 14:33
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
@IntDef(value = {
        BannerViewPager.INDICATE_GRAVITY_BOTTOM,
        BannerViewPager.INDICATE_GRAVITY_CENTER,
        BannerViewPager.INDICATE_GRAVITY_TOP
})
@Retention(RetentionPolicy.SOURCE)
public @interface IndicateViewGravity {
}
