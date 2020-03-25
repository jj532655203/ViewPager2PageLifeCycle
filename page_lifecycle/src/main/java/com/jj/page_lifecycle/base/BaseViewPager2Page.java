package com.jj.page_lifecycle.base;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Jay
 * 根据在viewPager2翻页过程中,总结的条目的生命周期管理
 */
public abstract class BaseViewPager2Page<T> extends ConstraintLayout {

    private boolean isConvertedOnceTime;

    public BaseViewPager2Page(Context context) {
        this(context, null);
    }

    public BaseViewPager2Page(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseViewPager2Page(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void convert(T bean, int adapterPosition) {
        if (isConvertedOnceTime) {
            onClearOldPageData();
        } else {
            isConvertedOnceTime = true;
        }
        onConvertView(bean, adapterPosition);
    }

    /**
     * 挂载新条目数据之前,先将旧页的数据清除
     */
    protected abstract void onClearOldPageData();

    /**
     * 挂载新条目数据
     */
    protected abstract void onConvertView(T bean, int adapterPosition);

    /**
     * 从预加载范围滑到可见条目
     * 此时动画/工作线程等耗费资源的工作应开启
     */
    public abstract void onResume();

    /**
     * 从可见条目滑到预加载范围
     * 此时动画/工作线程等耗费资源的工作应关闭
     */
    public abstract void onPause();

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onSavePageData();
    }

    /**
     * 退到预加载范围之外
     * 此时该条目产生的用户数据应保存好,因为这之后,viewPager2.findViewByTag(int)将找不到本view了
     */
    public abstract void onSavePageData();

}
