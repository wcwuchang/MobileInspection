package com.tianjin.MobileInspection.customview.TreeListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;

import java.util.List;
//javaapk.com提供测试

/**
 * 类名：TreeViewAdapter.java
 * 类描述：用于填充数据的类
 * @author wader
 * 创建时间：2011-11-03 16:32
 */
public class TreeViewAdapter extends BaseAdapter {

    class ViewHolder {
        ImageView icon;
        TextView title;
        ImageView chose;
    }

    Context context;
    ViewHolder holder;
    LayoutInflater inflater;

    List<TreeElement> elements;

    public TreeViewAdapter(Context context) {
        this.context = context;
    }

    public void updata(List<TreeElement> elements){
        this.elements = elements;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return elements==null?0:elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * ---------------------- get holder------------------------
         */
        if (convertView == null) {
            if (inflater == null) {
                inflater = LayoutInflater.from(context);
            }
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.tree_view_item_layout, null);
            holder.icon =  (ImageView) convertView.findViewById(R.id.tree_view_item_icon);
            holder.title = (TextView) convertView.findViewById(R.id.tree_view_item_title);
            holder.chose=  (ImageView) convertView.findViewById(R.id.iv_tree_chose);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /**
         * ---------------------- set holder--------------------------
         */
        if (elements.get(position).isHasChild()) {// 有子节点，要显示图标
            if (elements.get(position).isFold()) {
                holder.icon.setImageResource(R.drawable.show_less);
            } else if (!elements.get(position).isFold()) {
                holder.icon.setImageResource(R.drawable.go_to);
            }
            holder.icon.setVisibility(View.VISIBLE);
            holder.chose.setVisibility(View.GONE);
        } else {// 没有子节点，要隐藏图标
            holder.icon.setImageResource(R.drawable.show_less);
            holder.icon.setVisibility(View.INVISIBLE);
            holder.chose.setVisibility(View.VISIBLE);
            if(elements.get(position).isChosed()){
                holder.chose.setImageResource(R.drawable.list_add_choice);
            }else {
                holder.chose.setImageResource(R.drawable.list_add_normal);
            }
        }
        holder.icon.setPadding(25 * (elements.get(position).getLevel()), 0, 0, 0);// 根据层级设置缩进
        holder.title.setText(elements.get(position).getTitle());
//        holder.title.setTextSize(25- elements.get(position).getLevel() *2); // 根据层级设置字体大小

        return convertView;
    }
}
