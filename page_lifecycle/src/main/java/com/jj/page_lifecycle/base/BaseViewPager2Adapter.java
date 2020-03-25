package com.jj.page_lifecycle.base;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class BaseViewPager2Adapter<T, K extends BaseViewPager2Page> extends BaseQuickAdapter<T, BaseViewHolder> {

    private WeakReference<ViewPager2> viewPager2WeakRef;

    /**
     * @param layoutResId 此视图的根布局必须是泛型K所指的类!!!!!!!
     */
    public BaseViewPager2Adapter(int layoutResId, @Nullable List<T> data, WeakReference<ViewPager2> viewPager2WeakReference) {
        super(layoutResId, data);
        viewPager2WeakRef = viewPager2WeakReference;
        viewPager2WeakReference.get().registerOnPageChangeCallback(new BaseViewPager2Adapter.OnPageChangeCallback(viewPager2WeakReference));
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        if (!(helper.itemView instanceof BaseViewPager2Page)) {
            throw new RuntimeException("条目视图的根布局必须是泛型K所指的类");
        }
        BaseViewPager2Page<T> pageView = (BaseViewPager2Page) helper.itemView;
        pageView.convert(item, helper.getAdapterPosition());

        helper.itemView.setTag(helper.getAdapterPosition());

        convert(helper, item, pageView);

    }

    /**
     * 保存所有页的数据
     * 此方法将找到未detachFromWindow的页,并保存其数据
     */
    public void saveAllPageData() {
        Log.d(TAG, "saveAllPageData");
        ViewPager2 viewPager2 = viewPager2WeakRef.get();
        if (viewPager2 == null) return;
        for (int i = 0; i < getItemCount(); i++) {
            BaseViewPager2Page pageView = viewPager2.findViewWithTag(i);
            if (pageView == null) continue;
            pageView.onSavePageData();
        }
    }

    public abstract void convert(RecyclerView.ViewHolder helper, T item, BaseViewPager2Page<T> pageContainer);

    private static class OnPageChangeCallback extends ViewPager2.OnPageChangeCallback {

        private int mmLastPosition = -1;
        private boolean mmNewPageNotFound;
        private WeakReference<ViewPager2> mmViewPager2Ref;

        OnPageChangeCallback(WeakReference<ViewPager2> viewPager2Ref) {
            this.mmViewPager2Ref = viewPager2Ref;
        }

        public void onPageSelected(int position) {
            Log.d(TAG, "onPageSelected position=" + position);
            ViewPager2 viewPager2 = mmViewPager2Ref.get();

            if (mmLastPosition >= 0 && mmLastPosition != position) {
                BaseViewPager2Page lastPageView = viewPager2.findViewWithTag(mmLastPosition);
                if (lastPageView != null) lastPageView.onPause();
            }

            mmLastPosition = position;

            BaseViewPager2Page pageView = viewPager2.findViewWithTag(viewPager2.getCurrentItem());
            if (pageView != null) {
                pageView.onResume();
                return;
            }

            mmNewPageNotFound = true;
        }

        public void onPageScrollStateChanged(@ViewPager2.ScrollState int state) {
            Log.d(TAG, "onPageScrollStateChanged state=" + state);
            if (state == ViewPager2.SCROLL_STATE_IDLE && mmNewPageNotFound) {
                ViewPager2 viewPager2 = mmViewPager2Ref.get();
                mmNewPageNotFound = false;

                BaseViewPager2Page pageView = viewPager2.findViewWithTag(viewPager2.getCurrentItem());
                if (pageView == null) {
                    Log.e(TAG, "onPageSelected page not found!未预料情况!");
                    return;
                }

                pageView.onResume();

            }
        }
    }
}
