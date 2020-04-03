package com.jj.viewpager2pagelifecycle.bean;


import com.jj.scribble_sdk_pen.data.TouchPointList;

import java.util.List;

public class PageUserDataInfo {

    private int position;
    //为简单演示,此处不用ossUrl,直接用项目中图片资源id
    private int myOssUrl;
    private List<TouchPointList> pathList;

    public PageUserDataInfo(int myOssUrl, int position) {
        this.myOssUrl = myOssUrl;
        this.position = position;
    }

    @Override
    public String toString() {
        return "PageUserDataInfo{" +
                "position=" + position +
                ", myOssUrl='" + myOssUrl + '\'' +
                ", pathList=" + pathList +
                '}';
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMyOssUrl() {
        return myOssUrl;
    }

    public void setMyOssUrl(int myOssUrl) {
        this.myOssUrl = myOssUrl;
    }

    public List<TouchPointList> getPathList() {
        return pathList;
    }

    public void setPathList(List<TouchPointList> pathList) {
        this.pathList = pathList;
    }
}
