package com.example.me.thinkbig;

import android.graphics.drawable.Drawable;

public class GridViewItem {
    private Drawable m_drawable;

    public GridViewItem() {

    }

    public GridViewItem(Drawable drawable) {
        m_drawable = drawable;
    }

    public Drawable getImage() {
        return m_drawable;
    }

    public void setImage(Drawable drawable) {
        m_drawable = drawable;
    }
}