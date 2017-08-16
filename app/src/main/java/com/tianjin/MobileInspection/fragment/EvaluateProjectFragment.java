package com.tianjin.MobileInspection.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.EvaluateAdapter;
import com.tianjin.MobileInspection.customview.ScrollListView;
import com.tianjin.MobileInspection.entity.EvaluateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目评价
 * Created by 吴昶 on 2016/12/29.
 */
public class EvaluateProjectFragment extends BaseFragment{


    private View view;
    private ScrollListView mlvEvaluate;
    private EvaluateAdapter adapter;
    private List<EvaluateManager> testdata;

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
        String[] name={"合格率","完成率","延时率","养护及时率","养护涵盖度"};
        String[] grade={"98%","92%","2%","85%","100%"};
        for(int i=0;i<5;i++){
            EvaluateManager em=new EvaluateManager();
            em.setItemName(name[i]);
            em.setItemGrade(grade[i]);
            testdata.add(em);
        }
        adapter.updata(testdata);
//        ScreenUtils.setListViewHeightBasedOnChildren(context,mlvEvaluate);

    }

    private void initView() {
        mlvEvaluate=(ScrollListView)view.findViewById(R.id.lv_evaluate_list);

    }
}
