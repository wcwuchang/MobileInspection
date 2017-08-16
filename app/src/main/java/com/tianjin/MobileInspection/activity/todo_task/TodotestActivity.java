package com.tianjin.MobileInspection.activity.todo_task;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.fragment.TodoDeailyFragment;
import com.tianjin.MobileInspection.fragment.TodoInspectionFragment;
import com.tianjin.MobileInspection.fragment.TodoSpecialFragment;
import com.tianjin.MobileInspection.fragment.TodoTroubleFragment;
import com.tianjin.MobileInspection.main.BaseActivity;

/**
 * Created by wuchang on 2016-12-15.
 */
public class TodotestActivity extends BaseActivity {

    private LinearLayout mlinearBack;
    private TextView mtvTitle;

    private LinearLayout mlinearTrouble;
    private LinearLayout mlinearSpecial;
    private LinearLayout mlinearInspection;
    private LinearLayout mlinearDeaily;
    private TextView mtvTrouble;
    private TextView mtvSpecial;
    private TextView mtvInspection;
    private TextView mtvDeaily;

    private FragmentManager fragmentManager;
    private TodoTroubleFragment toubleFragment;
    private TodoSpecialFragment specialFragment;
    private TodoInspectionFragment inspectionFragment;
    private TodoDeailyFragment deailyFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        initView();
        initData();
    }

    private void initData() {
        fragmentManager = getSupportFragmentManager();
        toubleFragment = new TodoTroubleFragment();
        toubleFragment.setContext(this);
        changeFragment(toubleFragment);
        changeTableColor();
        mtvTrouble.setTextColor(getResources().getColor(R.color.inspection_linearBG));
        mlinearTrouble.setBackgroundColor(getResources().getColor(R.color.inspection_linearBG));
    }

    private void initView() {
        mlinearBack = (LinearLayout) findViewById(R.id.linear_return_back);
        mtvTitle = (TextView) findViewById(R.id.tv_activity_title);

        mlinearTrouble = (LinearLayout) findViewById(R.id.linear_todo_hidden_trouble);
        mlinearSpecial = (LinearLayout) findViewById(R.id.linear_todo_special);
        mlinearInspection = (LinearLayout) findViewById(R.id.linear_todo_inspection);
        mlinearDeaily = (LinearLayout) findViewById(R.id.linear_todo_deaily);

        mtvTrouble = (TextView) findViewById(R.id.tv_todo_hidden_trouble);
        mtvSpecial = (TextView) findViewById(R.id.tv_todo_special);
        mtvInspection = (TextView) findViewById(R.id.tv_todo_inspection);
        mtvDeaily = (TextView) findViewById(R.id.tv_todo_deaily);

        mtvTitle.setText("待办事项");
        mlinearTrouble.setOnClickListener(this);
        mlinearSpecial.setOnClickListener(this);
        mlinearInspection.setOnClickListener(this);
        mlinearDeaily.setOnClickListener(this);
        mlinearBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case  R.id.linear_todo_hidden_trouble:
                if(toubleFragment==null){
                    toubleFragment=new TodoTroubleFragment();
                    toubleFragment.setContext(this);
                }
                changeFragment(toubleFragment);
                changeTableColor();
                mtvTrouble.setTextColor(getResources().getColor(R.color.inspection_linearBG));
                mlinearTrouble.setBackgroundColor(getResources().getColor(R.color.inspection_linearBG));
                break;
            case  R.id.linear_todo_special:
                if(specialFragment==null){
                    specialFragment=new TodoSpecialFragment();
                    specialFragment.setContext(this);
                }
                changeFragment(specialFragment);
                changeTableColor();
                mtvSpecial.setTextColor(getResources().getColor(R.color.inspection_linearBG));
                mlinearSpecial.setBackgroundColor(getResources().getColor(R.color.inspection_linearBG));
                break;
            case  R.id.linear_todo_inspection:
                if(inspectionFragment==null){
                    inspectionFragment=new TodoInspectionFragment();
                    inspectionFragment.setContext(this);
                }
                changeFragment(inspectionFragment);
                changeTableColor();
                mtvInspection.setTextColor(getResources().getColor(R.color.inspection_linearBG));
                mlinearInspection.setBackgroundColor(getResources().getColor(R.color.inspection_linearBG));
                break;
            case  R.id.linear_todo_deaily:
                if(deailyFragment==null){
                    deailyFragment=new TodoDeailyFragment();
                    deailyFragment.setContext(this);
                }
                changeFragment(deailyFragment);
                changeTableColor();
                mtvDeaily.setTextColor(getResources().getColor(R.color.inspection_linearBG));
                mlinearDeaily.setBackgroundColor(getResources().getColor(R.color.inspection_linearBG));
                break;
            case  R.id.linear_return_back:
                finish();
                break;
        }
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_todo_list, fragment);
        transaction.commit();
    }

    private void changeTableColor() {
        mlinearTrouble.setBackgroundColor(Color.WHITE);
        mlinearSpecial.setBackgroundColor(Color.WHITE);
        mlinearInspection.setBackgroundColor(Color.WHITE);
        mlinearDeaily.setBackgroundColor(Color.WHITE);

        mtvTrouble.setTextColor(getResources().getColor(R.color.V333333));
        mtvSpecial.setTextColor(getResources().getColor(R.color.V333333));
        mtvInspection.setTextColor(getResources().getColor(R.color.V333333));
        mtvDeaily.setTextColor(getResources().getColor(R.color.V333333));
    }


}
