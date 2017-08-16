package com.tianjin.MobileInspection.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.EvaluateAdapter;
import com.tianjin.MobileInspection.customview.CircleImg;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.EvaluateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 综合评价
 * Created by 吴昶 on 2016/12/29.
 */
public class EvaluateCompreFragment extends BaseFragment{


    private View view;
    private ScrollListView mlvEvaluate;
    private EvaluateAdapter adapter;
    private List<EvaluateManager> testdata;
    private RelativeLayout mrelatEvaluateHeader;
    private CircleImg mciStep;

    private String type;
    private String[] name;
    private String[] grade;

    public void setType(String type) {
        this.type = type;
        KLog.d(type);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_evaluate_detail, null);
            initView();
            initData();
        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    private void initData() {
        adapter=new EvaluateAdapter(context);
        mlvEvaluate.setAdapter(adapter);
        KLog.d(type);
        testdata=new ArrayList<EvaluateManager>();
        if(type.equals("xunjian")){
            name=context.getResources().getStringArray(R.array.xunjian);
            grade=context.getResources().getStringArray(R.array.xunjian_1);
        }else if(type.equals("weixiu")){
            name=context.getResources().getStringArray(R.array.weixiu);
            grade=context.getResources().getStringArray(R.array.weixiu_1);
        }else {
            name=context.getResources().getStringArray(R.array.yangguan);
            grade=context.getResources().getStringArray(R.array.yangguan_1);
        }
        for(int i=0;i<name.length;i++){
            EvaluateManager em=new EvaluateManager();
            em.setItemName(name[i]);
            em.setItemGrade(grade[i%grade.length]);
            testdata.add(em);
        }
        adapter.updata(testdata);
    }

    private void initView() {
        mlvEvaluate=(ScrollListView)view.findViewById(R.id.lv_evaluate_list);
        mrelatEvaluateHeader=(RelativeLayout)view.findViewById(R.id.linear_evaluate_header);
        mciStep=(CircleImg)view.findViewById(R.id.iv_evaluate_step);
    }
}
