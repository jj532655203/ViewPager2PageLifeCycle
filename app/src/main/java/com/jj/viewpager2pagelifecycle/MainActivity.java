package com.jj.viewpager2pagelifecycle;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.jj.page_lifecycle.DefaultViewPager2Adapter;
import com.jj.touch_aware.TouchAwareConstraintLayout;
import com.jj.touch_aware.utils.ViewPager2Util;
import com.jj.viewpager2pagelifecycle.bean.DemoScribbleAdapterItemBean;
import com.jj.viewpager2pagelifecycle.view.DemoScribblePageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ViewPager2 viewPager2;
    private TouchAwareConstraintLayout touchAwareCl;
    private int[] imgArr = new int[]{
            R.mipmap.xiao_ao_jiang_hu_00,
            R.mipmap.xiao_ao_jiang_hu_01,
            R.mipmap.xiao_ao_jiang_hu_02,
            R.mipmap.xiao_ao_jiang_hu_03,
            R.mipmap.xiao_ao_jiang_hu_04
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.view_pager_2);
        touchAwareCl = findViewById(R.id.touch_aware_cl);

        viewPager2.setUserInputEnabled(false);
        touchAwareCl.setOnTouchAwareListener(new TouchAwareConstraintLayout.SimpleOnTouchAwareListener() {
            @Override
            public void onScaleOrMultiFingerMove(double scale, float moveOffsetx, float moveOffsety, int pointerCount, boolean[] consumed) {
                Log.d(TAG, "onScaleOrMultiFingerMove pointerCount=" + pointerCount);

                //定义2指翻页(您可定义其它指数翻页)
                if (pointerCount == 2 && Math.abs(moveOffsetx) > Math.abs(moveOffsety) && !consumed[0]) {
                    consumed[0] = true;
                    String errorMsg = ViewPager2Util.enableWithoutDownTouch(viewPager2);
                    if (TextUtils.isEmpty(errorMsg)) {
                        if (!viewPager2.isUserInputEnabled()) viewPager2.setUserInputEnabled(true);
                    } else {
                        Log.e(TAG, "onScaleOrMultiFingerMove 反射获取RecyclerView失败 e=" + errorMsg);
                    }
                }

            }

            @Override
            public void onActionUp() {
                super.onActionUp();
                Log.d(TAG, "onActionUp");

                if (viewPager2.isUserInputEnabled()) {
                    viewPager2.post(new Runnable() {
                        @Override
                        public void run() {
                            viewPager2.setUserInputEnabled(false);
                        }
                    });
                }

            }

        });

        setScribblePageAdapter();

    }

    private void setScribblePageAdapter() {

        viewPager2.setOffscreenPageLimit(1);
        List<DemoScribbleAdapterItemBean> itemBeans = new ArrayList<>(imgArr.length);
        for (int imgUrl : imgArr) {
            itemBeans.add(new DemoScribbleAdapterItemBean(imgUrl));
        }
        viewPager2.setAdapter(new DefaultViewPager2Adapter<DemoScribbleAdapterItemBean, DemoScribblePageView>(R.layout.layout_demo_scribble_adapter_page, itemBeans, new WeakReference<>(viewPager2)));
    }
}
