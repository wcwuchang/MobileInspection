package com.tianjin.MobileInspection.activity.todo_task;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.TodoListAdapter;
import com.tianjin.MobileInspection.customview.pullReflashView.PullToRefreshLayout;
import com.tianjin.MobileInspection.customview.pullReflashView.PullableListView;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.TodoModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchang on 2016-11-21.
 *
 * 待办事项列表
 */
public class TodoActivity extends BaseActivity implements TodoListAdapter.ClaimListener{

    private LinearLayout mlinearBack;
    private TextView mtvTitle;

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mPLVContractList;

    private TodoListAdapter adapter;
    private TodoModule module;
    private List<Todo> inspectiondata;
    private List<Todo> getdata;

    private int pageNo=1;
    private int pageSize=20;
    private boolean refresh=false;
    private boolean loadMore=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_inspection);
        initView();
        initData();
    }

    private void initData() {
        inspectiondata=new ArrayList<Todo>();
        getdata=new ArrayList<Todo>();
        module=new TodoModule(handler,this);
        adapter = new TodoListAdapter(this,this);
        mPLVContractList.setAdapter(adapter);

        mPLVContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Todo todo=(Todo)adapter.getItem(position);
                KLog.d(todo.toString());
                Intent intent = new Intent();
                if(todo.getTodoType()==1) {//巡检
                    intent.setAction("android.intent.action.TODOINSPECTIONDETAILACTIVITY");
                }else if(todo.getTodoType()==2){//隐患
                    intent.setAction("android.intent.action.TODOHIDDENDETAILACTIVITY");
                    intent.putExtra("GET", true);
                }else if(todo.getTodoType()==3){//日常（废弃）
                    intent.setAction("android.intent.action.TODODEAILYDETAILACTIVITY");
                }else if(todo.getTodoType()==4){//专项（废弃）
                    intent.setAction("android.intent.action.TODOSPECIALDETAILACTIVITY");
                }else if(todo.getTodoType()==5){//机动
                    intent.setAction("android.intent.action.TODOMOTORDETAILACTIVITY");
                }else if(todo.getTodoType()==6){//计划
                    intent.setAction("android.intent.action.TODOPLANTASKDETAILACTIVITY");
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("todo", todo);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    private void initView() {
        mlinearBack = (LinearLayout) findViewById(R.id.linear_return_back);
        mtvTitle = (TextView) findViewById(R.id.tv_activity_title);

        pullToRefreshLayout=(PullToRefreshLayout)findViewById(R.id.prelat_layout);
        mPLVContractList=(PullableListView)findViewById(R.id.lv_pullreflash);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());

        mtvTitle.setText("待办事项");
        mlinearBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 每次进入均获取数据确保待办事项的数据都是最新的
         */
        module.getTodoList();
    }

    private void showData(){
        inspectiondata.clear();
        inspectiondata.addAll(getdata);
        adapter.updata(inspectiondata);

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
            case TodoModule.INT_GET_TODO_INSPECTION_SUCCESS:
                getdata=(List<Todo>)msg.obj;
                showData();
                break;
            case TodoModule.INT_GET_TODO_INSPECTION_FILED:
                toLoginActivity(msg);
                break;
            case 0x77:
                module.getTodoList();
                break;

        }
    }

    private void stopPullToRefreshLayoutSuccess() {
        if(refresh){
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            refresh=false;
        }
        if(loadMore){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            loadMore=false;
        }
    }

    private void stopPullToRefreshLayoutFiled() {
        if(refresh){
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
            refresh=false;
        }
        if(loadMore){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            loadMore=false;
        }
    }

    @Override
    public void claim(String taskId) {
        module.claim(taskId);
    }

    //PullableListView下拉上拉监听
    public class MyListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            refresh=true;
            pageNo=1;
            stopPullToRefreshLayoutSuccess();
            module.getTodoList();
        }


        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            //上拉加载操作
            loadMore=true;
            stopPullToRefreshLayoutSuccess();
//            if(getList.size()>pageSize){
//                Toast.makeText(TodoActivity.this,"已加载全部数据",Toast.LENGTH_SHORT).show();
//                handler.sendEmptyMessageAtTime(INT_HAS_LOAD_ALL_DATA,1000);
//            }else {
//                pageNo++;
//                module.getInspectionChoseLList(String.valueOf(pageNo),String.valueOf(pageSize));
//            }
        }
    }
}
