package com.tianjin.MobileInspection.activity.maintenance_task;

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
import com.tianjin.MobileInspection.fragment.DeailyHasFinishedFragment;
import com.tianjin.MobileInspection.fragment.DeailyNotFinishedFragment;
import com.tianjin.MobileInspection.main.BaseActivity;

/**
 * 日常任务
 * Created by wuchang on 2016-11-21.
 */
public class DeailyListActivity extends BaseActivity implements View.OnClickListener{

    private String TAG="InspectionListActivity";
    private TextView mtvTitle;
    private LinearLayout mlinearBack;
    private LinearLayout mlinearAdd;

    private FragmentManager fragmentManager;
    private DeailyNotFinishedFragment doingFragment;
    private DeailyHasFinishedFragment finishedFragment;

    private RadioGroup mrgGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deaily);
        initView();
        initData();
    }

    private void initData() {
        fragmentManager=getSupportFragmentManager();
        mlinearBack.setOnClickListener(this);
//        mlinearAdd.setVisibility(View.VISIBLE);
//        mlinearAdd.setOnClickListener(this);
        mrgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_deaily_doing:
                        if (doingFragment==null) {
                            doingFragment = new DeailyNotFinishedFragment();
                            doingFragment.setContext(DeailyListActivity.this);
                        }
                        changeFragment(doingFragment);
                        break;
                    case R.id.rb_deaily_finished:
                        if(finishedFragment==null){
                            finishedFragment=new DeailyHasFinishedFragment();
                            finishedFragment.setContext(DeailyListActivity.this);
                        }
                        changeFragment(finishedFragment);
                        break;
                }
            }
        });
        mrgGroup.check(R.id.rb_deaily_doing);
    }

    private void initView() {
        mtvTitle=(TextView) findViewById(R.id.tv_activity_title);
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearAdd=(LinearLayout)findViewById(R.id.linear_add);

        mrgGroup=(RadioGroup)findViewById(R.id.rg_type_bottom);

        mtvTitle.setText("机动维修列表");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_return_back:
                this.finish();
                break;
            case R.id.linear_add:
                Intent in=new Intent("android.intent.action.NEWDEILAYTASKACTIVITY");
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
