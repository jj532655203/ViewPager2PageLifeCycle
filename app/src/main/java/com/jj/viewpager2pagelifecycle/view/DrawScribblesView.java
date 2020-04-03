package com.jj.viewpager2pagelifecycle.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ObjectUtils;
import com.jj.scribble_sdk_pen.data.TouchPoint;
import com.jj.scribble_sdk_pen.data.TouchPointList;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DrawScribblesView extends View {

    private static final String TAG = "ShowPathView";
    private Vector<TouchPointList> pathList = new Vector<>();
    private Vector<TouchPointList> pathList2 = new Vector<>();
    private Paint renderPaint;
    private float strokeWidth = 12F;
    private int strokeColor = Color.BLACK;
    private int mPosition;

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public List<TouchPointList> getPathList() {
        return new ArrayList<>(pathList);
    }

    public List<TouchPointList> getPathList2() {
        return new ArrayList<>(pathList2);
    }

    public void setPathList(List<TouchPointList> pathList, List<TouchPointList> pathList2, int position) {
        mPosition = position;

        this.pathList.clear();
        if (ObjectUtils.isNotEmpty(pathList)) {
            this.pathList.addAll(pathList);
        }

        this.pathList2.clear();
        if (ObjectUtils.isNotEmpty(pathList2)) {
            this.pathList2.addAll(pathList2);
        }

        if (Looper.getMainLooper() != Looper.myLooper()) {
            postInvalidate();
        } else {
            invalidate();
        }
    }

    public DrawScribblesView(Context context) {
        this(context, null);
    }

    public DrawScribblesView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawScribblesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundColor(Color.TRANSPARENT);

        initRenderPaint();
    }

    private void initRenderPaint() {
        if (renderPaint != null) return;
        renderPaint = new Paint();
        renderPaint.setStrokeWidth(strokeWidth);
        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setColor(strokeColor);
    }

    private Path activePath;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (ObjectUtils.isEmpty(pathList) && ObjectUtils.isEmpty(pathList2)) {
            return;
        }

        if (ObjectUtils.isNotEmpty(pathList)) {
            Log.d(TAG, "onDraw pathList.size=" + pathList.size() + "?mPosition=" + mPosition);
            drawScribbles(canvas, pathList);
        }
        if (ObjectUtils.isNotEmpty(pathList2)) {
            drawScribbles(canvas, pathList2);
        }

    }

    private void drawScribbles(Canvas canvas, List<TouchPointList> pathList) {
        for (TouchPointList touchPointList : pathList) {
            List<TouchPoint> points = touchPointList.getPoints();
            if (ObjectUtils.isEmpty(points)) continue;
            Log.d(TAG, "drawScribbles 绘制了一根 pathList.size=" + pathList.size() + "?mPosition=" + mPosition);

            int size = points.size();
            if (size == 1 || size == 2) {
                TouchPoint touchPoint = points.get(0);
                if (size == 1) {
                    canvas.drawPoint(touchPoint.x, touchPoint.y, renderPaint);
                } else {
                    TouchPoint touchPoint1 = points.get(1);
                    canvas.drawLine(touchPoint.x, touchPoint.y, touchPoint1.x, touchPoint1.y, renderPaint);
                }
            } else {

                if (activePath == null) {
                    activePath = new Path();
                } else {
                    activePath.reset();
                }
                TouchPoint touchPoint0 = points.get(0);
                activePath.moveTo(touchPoint0.x, touchPoint0.y);
                for (int i = 0; i < size - 1; i++) {
                    TouchPoint touchPointi = points.get(i);
                    TouchPoint touchPointiPlus = points.get(i + 1);
                    activePath.quadTo(touchPointi.x, touchPointi.y, touchPointiPlus.x, touchPointiPlus.y);
                }
                canvas.drawPath(activePath, renderPaint);

            }

        }
    }

    public void clearData() {
        pathList.clear();
        pathList2.clear();
    }

}
