package com.tianjin.MobileInspection.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.TodoListAdapter;
import com.tianjin.MobileInspection.customview.pullReflashView.PullToRefreshLayout;
import com.tianjin.MobileInspection.customview.pullReflashView.PullableListView;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.module.TodoModule;

import java.util.ArrayList;
import java.util.List;

/**
 * xunjian
 * Created by wuchang on 2016-11-21.
 */
public class TodoSpecialFragment extends BaseFragment {

    private String TAG="InspectionDoingFragment";
    private View view;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mPLVContractList;

    private TodoListAdapter adapter;
    private TodoModule module;
    private List<Todo> inspectiondata;
    private List<Todo> getdata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_inspection, container, false);
            initView();
            initData();
        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    private void initData() {
        inspectiondata=new ArrayList<Todo>();
        getdata=new ArrayList<Todo>();
        module=new TodoModule(handler,context);
//        adapter = new TodoListAdapter(context);
        mPLVContractList.setAdapter(adapter);

        mPLVContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Todo todo=(Todo)adapter.getItem(position);
                if(todo.getStatus()==0){
                    Toast.makeText(context,"待办",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context,"待签收",Toast.LENGTH_LONG).show();
                }
//                Intent intent=new Intent("android.intent.action.INSPECTIONDETAIL");
//                startActivity(intent);

            }
        });
        module.getTodoList();
    }

    private void initView() {
        pullToRefreshLayout=(PullToRefreshLayout)view.findViewById(R.id.prelat_layout);
        mPLVContractList=(PullableListView)view.findViewById(R.id.lv_pullreflash);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
    }

    private void showData(){
        inspectiondata.addAll(getdata);
        adapter.updata(inspectiondata);

    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case TodoModule.INT_GET_TODO_INSPECTION_SUCCESS:
                getdata=(List<Todo>)msg.obj;
                showData();
                break;
            case TodoModule.INT_GET_TODO_INSPECTION_FILED:
                toLoginActivity(msg);
                break;

        }
    }

    //PullableListView下拉上拉监听
    public class MyListener implements PullToRefreshLayout.OnRefreshListener
    {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
        {
            // 下拉刷新操作
            Handler handler=new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    // 千万别忘了告诉控件刷新完毕了哦！
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            };
            //无响应时间
            handler.sendEmptyMessageDelayed(0, 1000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
        {
            //上拉加载操作
            Handler handler=new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    // 千万别忘了告诉控件加载完毕了哦！
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            };
            //无响应时间
            handler.sendEmptyMessageDelayed(0, 1000);
        }

    }
}
