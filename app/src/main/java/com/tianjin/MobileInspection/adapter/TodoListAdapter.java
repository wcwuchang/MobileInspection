package com.tianjin.MobileInspection.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.Todo;

import java.util.List;

/**
 * 巡检列表适配器
 * Created by wuchang on 2016/10/13.
 */
public class TodoListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Todo> data;

    private ClaimListener listener;

    public interface ClaimListener{
        public void claim(String taskId);
    }

    public TodoListAdapter(Context context,ClaimListener listener){
        mContext=context;
        this.listener=listener;
    }
    public void updata(List<Todo> data){
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.listview_todo_inspection_item,null);
            holder=new Holder();
            holder.name=(TextView)convertView.findViewById(R.id.tv_todo_name);
            holder.recevice=(LinearLayout) convertView.findViewById(R.id.linear_todo_inspection_receiver);
            holder.task=(TextView)convertView.findViewById(R.id.tv_task_name);
            holder.taskNmae=(LinearLayout) convertView.findViewById(R.id.linear_task_name);
            holder.emecy=(LinearLayout)convertView.findViewById(R.id.linear_emerg_type);
            holder.tvemey=(TextView)convertView.findViewById(R.id.tv_emec_type);
            holder.tvname=(TextView)convertView.findViewById(R.id.tv_type_name);
            convertView.setTag(holder);
        }else{
            holder=(Holder)convertView.getTag();
        }
        Todo todo=data.get(position);
        holder.name.setText(todo.getTitle());
        if(data.get(position).getStatus()==0){
            holder.recevice.setVisibility(View.GONE);
            holder.taskNmae.setVisibility(View.GONE);
            holder.task.setText(todo.getTaskName());
        }else {
            holder.recevice.setVisibility(View.VISIBLE);
            holder.taskNmae.setVisibility(View.GONE);
            holder.recevice.setOnClickListener(new MyListener(todo.getTaskId()));
        }
        //
        if(todo.getType()!=null && !todo.getType().equals("")){
            holder.emecy.setVisibility(View.VISIBLE);
            holder.tvemey.setText(todo.getType());
            holder.tvname.setText(todo.getTypeName());
            if(todo.getType().equals("一般")){
                holder.tvemey.setTextColor(Color.GREEN);
            }else if(todo.getType().equals("紧急")){
                holder.tvemey.setTextColor(Color.RED);
            }else if(todo.getType().equals("管理")){
                holder.tvemey.setTextColor(Color.YELLOW);
            }else {
                holder.tvemey.setTextColor(Color.argb(51,51,51,51));
            }
        }else {
            holder.emecy.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class Holder{
        LinearLayout recevice;
        TextView name;
        LinearLayout taskNmae;
        TextView task;
        LinearLayout emecy;
        TextView tvname;
        TextView tvemey;
    }

    class MyListener implements View.OnClickListener{
        private String taskId;

        public MyListener(String taskId){
            this.taskId=taskId;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.linear_todo_inspection_receiver:
                    listener.claim(taskId);
                    break;
            }
        }
    }
}
