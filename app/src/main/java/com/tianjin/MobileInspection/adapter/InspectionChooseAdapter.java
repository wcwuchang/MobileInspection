package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.activity.inspection.InspectionChooseActivity;
import com.tianjin.MobileInspection.entity.InspectionChoose;
import com.tianjin.MobileInspection.entity.Road;

import java.util.List;

/**
 * 开始巡检列表
 * Created by wuchang on 2016/10/14.
 */
public class InspectionChooseAdapter extends BaseAdapter {

    private Context mContext;
    private List<InspectionChoose> data;
    private InspectionChooseActivity activity;

    public InspectionChooseAdapter(Context context,InspectionChooseActivity activity){
        mContext=context;
        this.activity=activity;
    }

    public void update(List<InspectionChoose> data){
        this.data=data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        if(convertView==null){
            holder=new Holder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_inspection_choose_item,null);
            holder.name=(TextView)convertView.findViewById(R.id.tv_choose_name);
            holder.traffic=(TextView)convertView.findViewById(R.id.tv_choose_traffic);
            holder.content=(TextView)convertView.findViewById(R.id.tv_choose_content);
            holder.person=(TextView)convertView.findViewById(R.id.tv_choose_worker_name);
            holder.date=(TextView)convertView.findViewById(R.id.tv_choose_time);
            holder.node=(LinearLayout)convertView.findViewById(R.id.linear_node_list);
            holder.showmore=(TextView)convertView.findViewById(R.id.tv_show_more);
//            holder.node=(ListView)convertView.findViewById(R.id.lv_node_list);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        InspectionChoose insp=data.get(position);
        holder.name.setText(insp.getInspectionName());
        holder.traffic.setText(insp.getTraffic());
        holder.content.setText(insp.getContent());
//        holder.person.setText(insp.getPerson());
        holder.person.setText(insp.getFlowState());
        holder.date.setText(insp.getDate());
        List<Road> nds=insp.getRoads();
        holder.node.removeAllViews();
        if(nds!=null&&nds.size()>0) {
            int s = nds.size();
            if (s <= 2) {
                holder.showmore.setVisibility(View.GONE);
                LinearLayout layout = (LinearLayout) LinearLayout.inflate(mContext, R.layout.listview_inspection_choose_itemlist_item, null);
                ((TextView) layout.findViewById(R.id.tv_node_name)).setText(nds.get(0).getRoadName());
                ((ImageView) layout.findViewById(R.id.iv_nodes_link)).setVisibility(View.GONE);
                holder.node.addView(layout);
                if (s == 2) {
                    LinearLayout layout2 = (LinearLayout) LinearLayout.inflate(mContext, R.layout.listview_inspection_choose_itemlist_item, null);
                    ((TextView) layout2.findViewById(R.id.tv_node_name)).setText(nds.get(1).getRoadName());
                    holder.node.addView(layout2);
                }
            } else {
                holder.showmore.setVisibility(View.VISIBLE);
                if (insp.isMore()) {
                    holder.showmore.setText(mContext.getResources().getString(R.string.str_show_litile));
                    for (int i = 0; i < nds.size(); i++) {
                        LinearLayout layout = (LinearLayout) LinearLayout.inflate(mContext, R.layout.listview_inspection_choose_itemlist_item, null);
                        ((TextView) layout.findViewById(R.id.tv_node_name)).setText(nds.get(i).getRoadName());
                        if (i == 0) {
                            ((ImageView) layout.findViewById(R.id.iv_nodes_link)).setVisibility(View.GONE);
                        }
                        holder.node.addView(layout);
                    }
                } else {
                    holder.showmore.setText(mContext.getResources().getString(R.string.str_show_more));
                    LinearLayout layout = (LinearLayout) LinearLayout.inflate(mContext, R.layout.listview_inspection_choose_itemlist_item, null);
                    ((TextView) layout.findViewById(R.id.tv_node_name)).setText(nds.get(0).getRoadName());
                    ((ImageView) layout.findViewById(R.id.iv_nodes_link)).setVisibility(View.GONE);
                    holder.node.addView(layout);
                    LinearLayout layout2 = (LinearLayout) LinearLayout.inflate(mContext, R.layout.listview_inspection_choose_itemlist_item, null);
                    ((TextView) layout2.findViewById(R.id.tv_node_name)).setText(nds.get(nds.size() - 1).getRoadName());
                    holder.node.addView(layout2);
                }
            }
        }else {
            holder.showmore.setVisibility(View.GONE);
        }
        holder.showmore.setOnClickListener(new ShowMoreListener(position));
//        NodeListAdapter adapter=new NodeListAdapter(mContext,insp.getNodes());
//        holder.node.setAdapter(adapter);
        return convertView;
    }

   private class Holder{
//       ListView node;
        TextView name;
        TextView traffic;
        TextView content;
        TextView person;
        TextView date;
       TextView showmore;
       LinearLayout node;
    }

    class ShowMoreListener implements View.OnClickListener{
        private int id;

        public ShowMoreListener(int id){
            this.id=id;
        }

        @Override
        public void onClick(View v) {
            activity.showMore(id);
        }
    }


    class NodeListAdapter extends BaseAdapter{
        private Context context;
        private List<String> node;

        public NodeListAdapter(Context context,List<String> node){
            this.context=context;
            this.node=node;
        }

        @Override
        public int getCount() {
            return node==null?0:node.size();
        }

        @Override
        public Object getItem(int position) {
            return node.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder=null;
            if(convertView==null){
                holder=new Holder();
                convertView=LayoutInflater.from(context).inflate(R.layout.listview_inspection_choose_itemlist_item,null);
                holder.name=(TextView) convertView.findViewById(R.id.tv_node_name);
                convertView.setTag(holder);
            }else{
                holder=(Holder)convertView.getTag();
            }
            holder.name.setText(node.get(position));
            return convertView;
        }

        private class Holder{
            TextView name;
        }
    }
}
