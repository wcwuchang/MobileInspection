package com.tianjin.MobileInspection.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.activity.plan.PlanDetailActivity;
import com.tianjin.MobileInspection.adapter.PlanContentAdapter;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.PlanManage;

/**
 * Created by wuchang on 2017-2-28.
 */
public class PlanContentFragment extends BaseFragment {

    private View view;

    private TextView mtvTitle;
    private TextView mtvContent;

    private TextView mtvStartTime;
    private TextView mtvEndTime;

    private ScrollListView mlvContent;
    private PlanDetailActivity detail;
    private PlanManage planManage;

    private PlanContentAdapter adapter;

    public void initFragment(Context context, PlanDetailActivity detail){
        this.context=context;
        this.detail=detail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_plan_content, container, false);
            initView();
            initData();
        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    private void initView() {
        mtvTitle=(TextView) view.findViewById(R.id.tv_plan_name);
        mtvContent=(TextView)view.findViewById(R.id.tv_plan_contract);
        mtvStartTime=(TextView)view.findViewById(R.id.tv_plan_start_time);
        mtvEndTime=(TextView)view.findViewById(R.id.tv_plan_end_time);
        mlvContent=(ScrollListView)view.findViewById(R.id.lv_content);
    }

    private void initData(){
        planManage=detail.getPlanManage();
        if(planManage==null){
            return;
        }
        mtvTitle.setText(planManage.getPlanName());
        mtvContent.setText(planManage.getContract());
        mtvEndTime.setText(planManage.getPlanEndDate());
        mtvStartTime.setText(planManage.getPlanStartDate());

        adapter=new PlanContentAdapter(context);
        mlvContent.setAdapter(adapter);
        adapter.updata(planManage.getPlanDetails());
    }
}
