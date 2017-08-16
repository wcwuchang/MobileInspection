package com.tianjin.MobileInspection.activity.evaluate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.EvaluateManageListAdapter;
import com.tianjin.MobileInspection.customview.pullReflashView.PullToRefreshLayout;
import com.tianjin.MobileInspection.customview.pullReflashView.PullableListView;
import com.tianjin.MobileInspection.entity.EvaluateManager;
import com.tianjin.MobileInspection.main.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 评价管理列表
 * Created by wuchang on 2016-12-15.
 */
public class EvaluateManageActivity extends BaseActivity{

    private TextView mtvTitle;
    private LinearLayout mlinearBack;

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView mPLVContractList;

    private List<EvaluateManager> testData;
    private EvaluateManageListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_manage);
        initView();
        initData();
    }

    private void initData() {
        testData=new ArrayList<EvaluateManager>();
        setTestData();
        adapter=new EvaluateManageListAdapter(this);
        mPLVContractList.setAdapter(adapter);
        adapter.updata(testData);
    }

    private void setTestData() {
        String[] unit=getResources().getStringArray(R.array.unit);
        String[] time=getResources().getStringArray(R.array.evaluate_month);
        for(int i=0;i<time.length;i++){
            for(int j=0;j<unit.length;j++) {
                EvaluateManager em = new EvaluateManager();
                em.setEvaluateName(unit[j]);
                em.setEvaluateUnit("养管单位");
                em.setEvaluateDate(time[i]);
                testData.add(em);
            }
        }

        mPLVContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent("android.intent.action.EVALUATEDETAILACTIVITY");
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",(EvaluateManager)adapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    private void initView() {
        mtvTitle=(TextView) findViewById(R.id.tv_activity_title);
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);

        pullToRefreshLayout=(PullToRefreshLayout)findViewById(R.id.prelat_layout);
        mPLVContractList=(PullableListView)findViewById(R.id.lv_pullreflash);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());

        mtvTitle.setText("评价管理");
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
