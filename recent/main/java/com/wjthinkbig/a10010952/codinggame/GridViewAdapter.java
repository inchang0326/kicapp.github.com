package com.wjthinkbig.a10010952.codinggame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.wjthinkbig.a10010952.R;

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

     /*
        View에 들어가는 item을 어떻게 보여줄지에 대해 정의한다.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder = null;

        if (itemView == null) {
             /*
               inflater는 XML에 정의된 리소스들을 View의 형태로 반환해 준다.
               infalte는 XML에 씌어져 있는 View의 정의를 실제 View객체로 만드는 역할을 한다.

                getSystemService를 통해 context의 시스템 서비스를 가져올 수 있다.
                LAYOUT_INFLATER_SERVCE는 레이아웃 리소스를 가져온다.
             */

            // ViewActivity의 레이아웃 리소스를 객체를 가져온다.
            final LayoutInflater layoutInflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // ViewActivity에 gridview_item의 리소스를 인플레이트한다.
            itemView = layoutInflater.inflate(m_resource, parent, false);

            // 위젯 등록
            holder = new ViewHolder();
            holder.imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        // adapter에 있는 아이템을 가져온다.
        GridViewItem item = getItem(position);

        // holder의 멤버 변수의 이미지로 저장한다.
        holder.imgItem.setImageDrawable(item.getImage());
        return itemView;
    }

    private class ViewHolder {
        ImageView imgItem;
    }
}