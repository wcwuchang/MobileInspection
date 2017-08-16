package com.tianjin.MobileInspection.activity.userinfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.customview.pullReflashView.PullToRefreshLayout;
import com.tianjin.MobileInspection.customview.pullReflashView.PullableListView;

/**
 * test
 *
 * Created by wuchang on 2016/9/27.
 */
public class TestActivity extends AppCompatActivity {
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        pullToRefreshLayout=(PullToRefreshLayout)findViewById(R.id.prelat_layout);
        listView=(PullableListView)findViewById(R.id.lv_pullreflash);
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
    }

    public class MyListener implements PullToRefreshLayout.OnRefreshListener
    {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
        {
            // 下拉刷新操作
            new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    // 千万别忘了告诉控件刷新完毕了哦！
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 1000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
        {
            // 加载操作
            new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    // 千万别忘了告诉控件加载完毕了哦！
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 500);
        }

    }

}
