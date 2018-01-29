package com.example.me.codinggame;

import android.graphics.drawable.Drawable;

public class GridViewItem {
    private Drawable m_drawable;

    public GridViewItem() {

    }

    public GridViewItem(Drawable drawable) {
        this.m_drawable = drawable;
    }

    public Drawable getImage() {
        return this.m_drawable;
    }

    public void setImage(Drawable drawable) {
        this.m_drawable = drawable;
    }
}