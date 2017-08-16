package com.tianjin.MobileInspection.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.EvaluateAdapter;
import com.tianjin.MobileInspection.customview.CircleImg;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.EvaluateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品评价
 * Created by 吴昶 on 2016/12/29.
 */
public class EvaluateProdectFragment extends BaseFragment{


    private View view;
    private ScrollListView mlvEvaluate;
    private EvaluateAdapter adapter;
    private List<EvaluateManager> testdata;
    private RelativeLayout mrelatEvaluateHeader;
    private CircleImg mciStep;

    private String type;

    public void setType(String type) {
        this.type = type;
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
        testdata=new ArrayList<EvaluateManager>();
        String[] name={"产品价格","产品质量","产品保质期限","产品供给及时率"};
        String[] grade={"98%","90%","99%","85%"};
        for(int i=0;i<4;i++){
            EvaluateManager em=new EvaluateManager();
            em.setItemName(name[i]);
            em.setItemGrade(grade[i]);
            testdata.add(em);
        }
        adapter.updata(testdata);
//        ScreenUtils.setListViewHeightBasedOnChildren(context,mlvEvaluate);
        mrelatEvaluateHeader.setBackgroundResource(R.color.step_b);
        mciStep.setImageResource(R.mipmap.step_b);
    }

    private void initView() {
        mlvEvaluate=(ScrollListView)view.findViewById(R.id.lv_evaluate_list);
        mrelatEvaluateHeader=(RelativeLayout)view.findViewById(R.id.linear_evaluate_header);
        mciStep=(CircleImg)view.findViewById(R.id.iv_evaluate_step);
    }
}
