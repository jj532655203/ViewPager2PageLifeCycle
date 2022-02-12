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
    private boolean isPaused;

    public BaseViewPager2Page(Context context) {
        this(context, null);
    }

    public BaseViewPager2Page(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseViewPager2Page(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void pause() {
        if (isPaused) {
            return;
        }
        isPaused = true;
        onPause();
        onSavePageData();
    }

    protected void clearOldPageData() {
        if (!isConvertedOnceTime) {
            isConvertedOnceTime = true;
            return;
        }
        onClearOldPageData();
    }

    /**
     * 挂载新条目数据之前,先将旧页的数据清除
     */
    protected abstract void onClearOldPageData();

    /**
     * 挂载新条目数据
     */
    protected abstract void onViewConverted(T bean, int adapterPosition);

    public void resume() {
        isPaused = false;
        onResume();
    }

    /**
     * 从预加载范围滑到可见条目
     * 此时动画/工作线程等耗费资源的工作应开启
     */
    protected abstract void onResume();

    /**
     * 从可见条目滑到预加载范围
     * 此时动画/工作线程等耗费资源的工作应关闭
     */
    protected abstract void onPause();

    /**
     * 从预加载范围到回收池,或viewpager2销毁
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //viewpage2销毁时,最后一个选中条目在此回调pause
        pause();
        //所有条目都需要清空缓存
        onRelease();
    }

    /**
     * 条目进入预加载范围,或进入回收池
     * 此时该条目产生的用户数据应保存好,因为一旦进入回收池,viewPager2.findViewByTag(int)将找不到本view了
     */
    public abstract void onSavePageData();

    /**
     * 条目控件销毁时机,必须防止内存泄漏
     */
    protected abstract void onRelease();

}
