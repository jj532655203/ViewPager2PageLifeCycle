package com.jj.viewpager2pagelifecycle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.jj.fst_disk_lru.memory_lru_cache.MemoryLruCacheUtils;
import com.jj.page_lifecycle.base.BaseViewPager2Page;
import com.jj.scribble_sdk_pen.TransparentScribbleView;
import com.jj.scribble_sdk_pen.data.TouchPoint;
import com.jj.scribble_sdk_pen.data.TouchPointList;
import com.jj.scribble_sdk_pen.intf.RawInputCallback;
import com.jj.viewpager2pagelifecycle.Constant;
import com.jj.viewpager2pagelifecycle.R;
import com.jj.viewpager2pagelifecycle.bean.DemoScribbleAdapterItemBean;
import com.jj.viewpager2pagelifecycle.bean.PageUserDataInfo;

import java.util.ArrayList;
import java.util.List;

public class DemoScribblePageView extends BaseViewPager2Page<DemoScribbleAdapterItemBean> {

    private static final String TAG = "MathWritePageView";
    private ImageView mImg;
    private TransparentScribbleView mScribbleView;
    private List<TouchPointList> mAddPathList = new ArrayList<>();
    private DrawScribblesView mDrawScribblesView;
    private int mPosition = -1;
    private int mImgUrl;

    public DemoScribblePageView(Context context) {
        this(context, null);
    }

    public DemoScribblePageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoScribblePageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_item_page_view_demo_scribble, this);

        mImg = findViewById(R.id.bg_img);
        mScribbleView = findViewById(R.id.scribble_view);
        mDrawScribblesView = findViewById(R.id.show_path_view);

        mScribbleView.setRawInputCallback(new RawInputCallback() {
            @Override
            public void onBeginRawDrawing(TouchPoint var2) {
            }

            @Override
            public void onEndRawDrawing(TouchPoint var2) {
            }

            @Override
            public void onRawDrawingTouchPointMoveReceived(TouchPoint var1) {
            }

            @Override
            public void onRawDrawingTouchPointListReceived(TouchPointList var1) {
                Log.d(TAG, "onRawDrawingTouchPointListReceived");
                mAddPathList.add(var1);
            }
        });
        mScribbleView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                setPathList();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });

    }

    @Override
    protected void onClearOldPageData() {
        Log.d(TAG, "onClearOldPageData");

        //新增笔迹清空
        mAddPathList.clear();

        mDrawScribblesView.clearData();

        mScribbleView.post(new Runnable() {
            @Override
            public void run() {
                mScribbleView.clearScreenAfterSurfaceViewCreated();
            }
        });

    }

    @Override
    protected void onConvertView(DemoScribbleAdapterItemBean bean, int adapterPosition) {
        Log.d(TAG, "onConvertView bean=" + bean.toString());

        this.mPosition = adapterPosition;
        mImgUrl = bean.getImgUrl();

        mImg.setImageResource(mImgUrl);

        setPathList();

    }

    private void setPathList() {
        if (mImgUrl == 0) return;
        PageUserDataInfo pageUserDataInfo = (PageUserDataInfo) MemoryLruCacheUtils.getInstance().get(Constant.MONITOR_OSS_URL_PREFIX + mImgUrl);
        List<TouchPointList> pathList;
        if (pageUserDataInfo == null || ObjectUtils.isEmpty(pageUserDataInfo.getPathList())) {
            pathList = null;
        } else {
            pathList = pageUserDataInfo.getPathList();
        }
        mDrawScribblesView.setPathList(pathList, null, mPosition);
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume position=" + mPosition);
        mScribbleView.setRawDrawingEnableAfterSurfaceCreated(true);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause position=" + mPosition);
        mScribbleView.setRawDrawingEnableAfterSurfaceCreated(false);
    }

    @Override
    public void onSavePageData() {
        Log.d(TAG, "onSavePageData position=" + mPosition);

        //无新增笔迹:return
        if (ObjectUtils.isEmpty(mAddPathList)) {
            return;
        }

        List<TouchPointList> showPathViewPathList = mDrawScribblesView.getPathList();
        if (ObjectUtils.isNotEmpty(showPathViewPathList)) {
            this.mAddPathList.addAll(showPathViewPathList);
        }

        //为简单演示,此处不用ossUrl,直接用项目中图片资源id
        int myOssUrl = mImgUrl;
        PageUserDataInfo pageUserDataInfo = new PageUserDataInfo(myOssUrl, mPosition);

        pageUserDataInfo.setPathList(new ArrayList<>(mAddPathList));

        //内存缓存
        MemoryLruCacheUtils.getInstance().put(Constant.MONITOR_OSS_URL_PREFIX + pageUserDataInfo.getMyOssUrl(), pageUserDataInfo);

        //为简单演示,此处不做磁盘缓存和服务器缓存

    }

}
