package com.jj.viewpager2pagelifecycle.bean;

public class DemoScribbleAdapterItemBean {

    //为简单演示,此处不用ossUrl,直接用项目中图片资源id
    private int imgUrl;

    @Override
    public String toString() {
        return "DemoScribbleAdapterItemBean{" +
                "imgUrl='" + imgUrl + '\'' +
                '}';
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public DemoScribbleAdapterItemBean(int imgUrl) {

        this.imgUrl = imgUrl;
    }
}
