package com.tianjin.MobileInspection.activity.evaluate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.entity.EvaluateManager;
import com.tianjin.MobileInspection.fragment.EvaluateCompreFragment;
import com.tianjin.MobileInspection.fragment.EvaluateProdectFragment;
import com.tianjin.MobileInspection.fragment.EvaluateProjectFragment;
import com.tianjin.MobileInspection.main.BaseActivity;

/**
 * Created by 吴昶 on 2016/12/29.
 *
 * 评价详情
 */
public class EvaluateDetailActivity extends BaseActivity{

    private LinearLayout mlinearBack;
    private TextView mtvTitle;

    private FragmentManager fragmentManager;
    private EvaluateProjectFragment projectFragment;
    private EvaluateProdectFragment prodectFragment;
    private EvaluateCompreFragment compreFragment;

    private TextView mtvProject;
    private TextView mtvProjectBg;
    private TextView mtvProdect;
    private TextView mtvProdectBg;
    private TextView mtvCompre;
    private TextView mtvCompreBg;
    private LinearLayout mlinearBg1;
    private LinearLayout mlinearBg2;
    private LinearLayout mlinearBg3;

    private LinearLayout mlinearTitle;

    private EvaluateManager evaluateManager;

    private String project;
    private String compre;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_detail);
        initView();
        initData();
    }

    private void initData() {
        fragmentManager=getSupportFragmentManager();
        evaluateManager=(EvaluateManager) getIntent().getSerializableExtra("data");
        String name=evaluateManager.getEvaluateName();
        mtvTitle.setText(name+"评价详情");
//        mlinearTitle.setVisibility(View.GONE);
        if(name.equals("养管单位")){
            compre="综合评价";
        }else {
            if(name.equals("巡检单位")){
                compre="xunjian";
            }else {
                compre="weixiu";
            }
        }
        if(compreFragment==null){
            compreFragment=new EvaluateCompreFragment();
            compreFragment.setContext(this);
            compreFragment.setType(compre);
        }
        changeFragment(compreFragment);
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);

        mtvProject=(TextView)findViewById(R.id.tv_evaluate_title_1);
        mtvProjectBg=(TextView)findViewById(R.id.tv_evaluate_bg1);
        mtvProdect=(TextView)findViewById(R.id.tv_evaluate_title_2);
        mtvProdectBg=(TextView)findViewById(R.id.tv_evaluate_bg2);
        mtvCompre=(TextView)findViewById(R.id.tv_evaluate_title_3);
        mtvCompreBg=(TextView)findViewById(R.id.tv_evaluate_bg3);

        mlinearBg1=(LinearLayout)findViewById(R.id.linear_evaluate_bg1);
        mlinearBg2=(LinearLayout)findViewById(R.id.linear_evaluate_bg2);
        mlinearBg3=(LinearLayout)findViewById(R.id.linear_evaluate_bg3);

        mlinearTitle=(LinearLayout)findViewById(R.id.linear_title);

        mlinearBg1.setVisibility(View.GONE);
        mlinearBg1.setOnClickListener(this);
        mlinearBg2.setOnClickListener(this);
        mlinearBg3.setOnClickListener(this);
        mlinearBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_evaluate_bg1:
                textviewColor();
                mtvProjectBg.setVisibility(View.VISIBLE);
                mtvProject.setTextColor(getResources().getColor(R.color.white));
                if(projectFragment==null){
                    projectFragment=new EvaluateProjectFragment();
                    projectFragment.setContext(this);
                }
                changeFragment(projectFragment);
                break;
            case R.id.linear_evaluate_bg2:
                textviewColor();
                mtvProdectBg.setVisibility(View.VISIBLE);
                mtvProdect.setTextColor(getResources().getColor(R.color.white));
                if(prodectFragment==null){
                    prodectFragment=new EvaluateProdectFragment();
                    prodectFragment.setContext(this);
                    prodectFragment.setType(project);
                }
                changeFragment(prodectFragment);
                break;
            case R.id.linear_evaluate_bg3:
                textviewColor();
                mtvCompreBg.setVisibility(View.VISIBLE);
                mtvCompre.setTextColor(getResources().getColor(R.color.white));
                if(compreFragment==null){
                    compreFragment=new EvaluateCompreFragment();
                    compreFragment.setContext(this);
                    compreFragment.setType(compre);
                }
                changeFragment(compreFragment);
                break;
        }
    }

    private void textviewColor(){
        mtvCompreBg.setVisibility(View.GONE);
        mtvProjectBg.setVisibility(View.GONE);
        mtvProdectBg.setVisibility(View.GONE);
    }

    private void changeFragment(Fragment fragment){
        FragmentTransaction tran=fragmentManager.beginTransaction();
        tran.replace(R.id.frame_evaluate,fragment);
        tran.commit();
    }
}
