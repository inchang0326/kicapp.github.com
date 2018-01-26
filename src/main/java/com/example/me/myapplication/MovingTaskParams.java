package com.example.me.myapplication;

import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MovingTaskParams {
    private RelativeLayout m_relativeLayout;
    private ImageView m_imageView;
    private Arrow m_arrow;
    private int m_mapInfo;

    public MovingTaskParams() {
    }

    public MovingTaskParams(RelativeLayout relativeLayout, ImageView imageView, Arrow arrow, int mapInfo) {
        this.m_relativeLayout = relativeLayout;
        this.m_imageView = imageView;
        this.m_arrow = arrow;
        this.m_mapInfo = mapInfo;
    }

    public Arrow getArrow() {
        return this.m_arrow;
    }

    public void setArrow(Arrow arrow) {
        this.m_arrow = arrow;
    }

    public int getMapInfo() {
        return this.m_mapInfo;
    }

    public void setMapInfo(int mapInfo) {
        this.m_mapInfo = mapInfo;
    }

    public RelativeLayout getRelativeLayout() {
        return this.m_relativeLayout;
    }

    public void setRelativeLayout(RelativeLayout relativeLayout) {
        this.m_relativeLayout = relativeLayout;
    }

    public ImageView getImageView() {
        return this.m_imageView;
    }

    public void setImageView(ImageView imageView) {
        this.m_imageView = imageView;
    }
}
