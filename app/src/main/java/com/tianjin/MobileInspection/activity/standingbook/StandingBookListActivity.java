package com.tianjin.MobileInspection.activity.standingbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.until.DownLoadImageTask;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吴昶 on 2017/2/28.
 */
public class StandingBookListActivity extends BaseActivity {

    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private ListView mlvBooks;

    private List<StandingBook> data;
    private StandBookAdapter adapter;

    private String boolid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standing_book_list);
        initView();
        initData();
    }

    private void initData() {
        boolid=getIntent().getStringExtra("id");
        mtvTitle.setText(getIntent().getStringExtra("name"));
        data=new ArrayList<StandingBook>();
        adapter=new StandBookAdapter();
        mlvBooks.setAdapter(adapter);
        if(boolid.equals("1")) {
            StandingBook sb1 = new StandingBook();
            sb1.setBookName("2016年6月旅游区道路养护维修全年完成情况及维修计划.xls");
            data.add(sb1);
        }else {
            StandingBook sb2 = new StandingBook();
            sb2.setBookName("城建统计年报设施明细表及说明台账20150114.xls");
            data.add(sb2);
        }

        for(int i=0;i<data.size();i++){
            String filename=data.get(i).getBookName();
            File file=new File(MyApplication.DOWNLOAD_PATH,filename);
            if(file.exists()){
                data.get(i).setHasDown(true);
            }else {
                data.get(i).setHasDown(false);
            }
        }
        adapter.setData(data);

        mlvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String filename=data.get(position).getBookName();
//                File file=new File(MyApplication.DOWNLOAD_PATH,filename);
                if(data.get(position).isHasDown()){
                    File file=new File(MyApplication.DOWNLOAD_PATH,filename);
                    FileUtil.openFile(getContext(),file.getPath());
                }else {
                    AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(getContext()).setMessage("下载附件？");
                    tuichuDialog.setPositiveButton("下载",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DownLoadImageTask task=new DownLoadImageTask(getContext(),handler,filename);
                                    StringBuffer sb=new StringBuffer();
                                    sb.append("http://139.196.223.73:20102/jeeplus/").append(filename);
                                    task.execute(sb.toString());
                                }
                            });
                    tuichuDialog.setNegativeButton("取消",null);
                    tuichuDialog.show();
                }
//                if(file.exists()){
//                    FileUtil.openFile(getContext(),file.getPath());
//                }else {
//                    AlertDialog.Builder tuichuDialog = new AlertDialog.Builder(getContext()).setMessage("下载附件？");
//                    tuichuDialog.setPositiveButton("下载",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    DownLoadImageTask task=new DownLoadImageTask(getContext(),handler,filename);
//                                    StringBuffer sb=new StringBuffer();
//                                    sb.append("http://139.196.223.73:20102/jeeplus/").append(filename);
//                                    task.execute(sb.toString());
//                                }
//                            });
//                    tuichuDialog.setNegativeButton("取消",null);
//                    tuichuDialog.show();
//                }
            }
        });
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mlvBooks=(ListView)findViewById(R.id.lv_book_list) ;
        mlinearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;

        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case DownLoadImageTask.DOWN_LOAD_SUCCESS:
                KLog.d(msg.obj.toString());
                FileUtil.openFile(getContext(),msg.obj.toString());
                break;
        }
    }

    public class StandingBook{

        String bookName;
        String bookUrl;
        boolean hasDown=false;

        public boolean isHasDown() {
            return hasDown;
        }

        public void setHasDown(boolean hasDown) {
            this.hasDown = hasDown;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public String getBookUrl() {
            return bookUrl;
        }

        public void setBookUrl(String bookUrl) {
            this.bookUrl = bookUrl;
        }
    }


    public class StandBookAdapter extends BaseAdapter{

        private List<StandingBook> data;

        public void setData(List<StandingBook> data){
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
                convertView= LayoutInflater.from(getContext()).inflate(R.layout.listview_standing_book_item,null);
                holder.textview=(TextView)convertView.findViewById(R.id.tv_standing_name);
                holder.downImage=(ImageView)convertView.findViewById(R.id.iv_download);
                convertView.setTag(holder);
            }else {
                holder=(Holder)convertView.getTag();
            }
            holder.textview.setText(data.get(position).getBookName());
            if(data.get(position).isHasDown()){
                holder.downImage.setImageResource(R.drawable.select_iv_open_file);
            }else {
                holder.downImage.setImageResource(R.drawable.select_iv_down_load);
            }
            return convertView;
        }

        class Holder{
            TextView textview;
            ImageView downImage;
        }
    }
}
