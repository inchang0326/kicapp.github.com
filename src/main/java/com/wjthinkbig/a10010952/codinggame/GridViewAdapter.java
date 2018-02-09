package com.wjthinkbig.a10010952.codinggame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.Stack;

public class GridViewAdapter extends ArrayAdapter<GridViewItem> {

    private Context m_context;
    private int m_resource;
    private Stack<GridViewItem> mData = new Stack<>();

    public GridViewAdapter(Context context, int resource, Stack<GridViewItem> data) {
        super(context, resource, data);
        m_context = context;
        m_resource = resource;
        mData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder = null;

        if (itemView == null) {
            final LayoutInflater layoutInflater =
                    (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(m_resource, parent, false);

            holder = new ViewHolder();
            holder.imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        GridViewItem item = getItem(position);
        holder.imgItem.setImageDrawable(item.getImage());
        return itemView;
    }

    static class ViewHolder {
        ImageView imgItem;
    }
}