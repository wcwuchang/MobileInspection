package com.tianjin.MobileInspection.activity.inspection;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.fragment.InspectionDoingFragment;
import com.tianjin.MobileInspection.fragment.InspectionFinishedFragment;
import com.tianjin.MobileInspection.main.BaseActivity;

/**
 * 巡检列表界面
 * Created by wuchang on 2016-11-21.
 */
public class InspectionListActivity extends BaseActivity implements View.OnClickListener{

    private String TAG="InspectionListActivity";
    private TextView mtvTitle;
    private LinearLayout mlinearBack;
    private LinearLayout mlinearADD;

    private RadioGroup mrgGroup;

    private FragmentManager fragmentManager;
    private InspectionDoingFragment doingFragment;
    private InspectionFinishedFragment finishedFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        initView();
        initData();
    }

    private void initData() {
        fragmentManager=getSupportFragmentManager();
        mlinearBack.setOnClickListener(this);
        mlinearADD.setOnClickListener(this);
        mrgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_inspection_doing:
                        if (doingFragment==null){
                            doingFragment=new InspectionDoingFragment();
                            doingFragment.setContext(InspectionListActivity.this);
                        }
                        changeFragment(doingFragment);
                        break;
                    case R.id.rb_inspection_finished:
                        if(finishedFragment==null){
                            finishedFragment=new InspectionFinishedFragment();
                            finishedFragment.setContext(InspectionListActivity.this);
                        }
                        changeFragment(finishedFragment);
                        break;
                }
            }
        });
        mrgGroup.check(R.id.rb_inspection_doing);
    }

    private void initView() {
        mtvTitle=(TextView) findViewById(R.id.tv_activity_title);
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearADD=(LinearLayout)findViewById(R.id.linear_add);
//        mlinearADD.setVisibility(View.VISIBLE);

        mtvTitle.setText(getString(R.string.str_inspection_list));

        mrgGroup=(RadioGroup)findViewById(R.id.rg_type_bottom);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_return_back:
                this.finish();
                break;
            case R.id.linear_add:
                Intent in=new Intent("android.intent.action.NEWINSPECTIONACTIVITY");
                startActivity(in);
                break;
        }
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_inspection_list,fragment);
        transaction.commit();
    }

}
