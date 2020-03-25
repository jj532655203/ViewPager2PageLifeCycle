package com.jj.page_lifecycle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.jj.page_lifecycle.base.BaseViewPager2Adapter;
import com.jj.page_lifecycle.base.BaseViewPager2Page;

import java.lang.ref.WeakReference;
import java.util.List;

public class DefaultViewPager2Adapter<T, K extends BaseViewPager2Page> extends BaseViewPager2Adapter<T, K> {

    /**
     * @param layoutResId 此视图的根布局必须是泛型K所指的类!!!!!!!!
     */
    public DefaultViewPager2Adapter(int layoutResId, @Nullable List<T> data, WeakReference<ViewPager2> viewPager2WeakReference) {
        super(layoutResId, data, viewPager2WeakReference);
    }

    @Override
    public void convert(RecyclerView.ViewHolder helper, T item, BaseViewPager2Page<T> pageView) {
    }

}
