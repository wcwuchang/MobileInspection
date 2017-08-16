package com.tianjin.MobileInspection.activity.standingbook;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.StandingBookGridviewAdapter;
import com.tianjin.MobileInspection.entity.StandingBook;
import com.tianjin.MobileInspection.main.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 台账管理
 * Created by wuchang on 2016-12-15.
 */
public class StandingBookManageActivity extends BaseActivity{

    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private GridView mgvBookList;

    private StandingBookGridviewAdapter adapter;
    private List<StandingBook> testData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standing_book_manage);
        initView();
        initData();
    }

    private void initData() {
        adapter=new StandingBookGridviewAdapter(this);
        testData=new ArrayList<StandingBook>();
        StandingBook sb1=new StandingBook();
        sb1.setBookId("1");
        sb1.setType("巡查台账");
        sb1.setBookBmp(BitmapFactory.decodeResource(getResources(),R.mipmap.xunchataizhang));
        StandingBook sb2=new StandingBook();
        sb2.setBookId("2");
        sb2.setType("合同台账");
        sb2.setBookBmp(BitmapFactory.decodeResource(getResources(),R.mipmap.hetongtaizhang));
        testData.add(sb1);
        testData.add(sb2);
        mgvBookList.setAdapter(adapter);
        adapter.updata(testData);

        mgvBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent("android.intent.action.STANDINGBOOKLISTACTIVITY");
                intent.putExtra("id",testData.get(position).getBookId());
                intent.putExtra("name",testData.get(position).getType());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mgvBookList=(GridView)findViewById(R.id.gv_standing_book_gv);

        mtvTitle.setText("台账管理");
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
}
