package com.tianjin.MobileInspection.activity.hiddentrouble;

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
import com.tianjin.MobileInspection.fragment.HiddenTimeFragment;
import com.tianjin.MobileInspection.fragment.HiddenlistFragment;
import com.tianjin.MobileInspection.main.BaseActivity;

/**
 * Created by 吴昶 on 2017/2/28.
 */
public class HiddenListActivity extends BaseActivity {

    private RadioGroup mrgHidden;
    private LinearLayout mlinearBack;
    private LinearLayout mlinearSave;
    private TextView mtvTitle;

    private FragmentManager fragmentManager;
    private HiddenlistFragment hiddenWite;
    private HiddenlistFragment hiddenDoing;
//    private HiddenlistFragment hiddenFinish;
    private HiddenTimeFragment hiddenTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_trouble_list);
        initView();
        initData();
    }

    private void initView() {
        mrgHidden=(RadioGroup)findViewById(R.id.rg_hidden_group);
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mlinearBack.setOnClickListener(this);
        mtvTitle.setText("隐患列表");
    }

    private void initData(){
        fragmentManager=getSupportFragmentManager();
        mrgHidden.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_hidden_wite:
                        if(hiddenWite==null){
                            hiddenWite=new HiddenlistFragment();
                            hiddenWite.initFragment(getContext(),"1","0");
                        }
                        changFragment(hiddenWite);
                        mtvTitle.setText("审核通过尚未维修");
                        break;
                    case R.id.rb_hidden_doing:
                        if(hiddenDoing==null){
                            hiddenDoing=new HiddenlistFragment();
                            hiddenDoing.initFragment(getContext(),"1","1");
                        }
                        changFragment(hiddenDoing);
                        mtvTitle.setText("已分配任务");
                        break;
                    case R.id.rb_hidden_time:
                        if(hiddenTime==null){
                            hiddenTime=new HiddenTimeFragment();
                            hiddenTime.initFragment(getContext());
                        }
                        changFragment(hiddenTime);
                        mtvTitle.setText("时间图");
                        break;
                }
            }
        });

        mrgHidden.check(R.id.rb_hidden_wite);

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

    private void changFragment(Fragment fragment){
        FragmentTransaction tran=fragmentManager.beginTransaction();
        tran.replace(R.id.frame_hidden_body,fragment);
        tran.commit();
    }
}
