package com.tianjin.MobileInspection.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.HiddenManageAdapter;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.module.HiddenModule;

import java.util.ArrayList;

/**
 * Created by 吴昶 on 2017/2/28.
 */
public class HiddenlistFragment extends BaseFragment {

    private View view;

    private ListView mlvHiddenList;

    private String state;
    private String maintenance;

    private HiddenModule module;
//    private List<PlanManage> list;
//    private PlanListAdapter adapter;

    private ArrayList<HiddenTroubleDetail> hiddenList;
    private HiddenManageAdapter adapter;

    public void initFragment(Context context,String state, String maintenance){
        this.context=context;
        this.state=state;
        this.maintenance=maintenance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_hidden_list, container, false);
            initView();
            initData();
        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }

    private void initView(){
        mlvHiddenList=(ListView)view.findViewById(R.id.lv_hidden_list);
    }

    private void initData(){
//        list=new ArrayList<PlanManage>();
//        adapter=new PlanListAdapter(context);
        mlvHiddenList.setAdapter(adapter);
        module=new HiddenModule(handler,context);
//        module.getTroubles(state,maintenance);

        hiddenList=new ArrayList<HiddenTroubleDetail>();
        adapter=new HiddenManageAdapter(context);
        mlvHiddenList.setAdapter(adapter);
        if(maintenance.equals("0")){
            for(int i=0;i<3;i++){
                HiddenTroubleDetail ht=new HiddenTroubleDetail();
                ht.setStockName("道路-侧缘石-破损");
                ht.setQuantity(String.valueOf(5-i));
                ht.setUnit("平米");
                ht.setRoadName("海旭路");
                ht.setLightId("018");
                ht.setDate("2017-07-0"+i+" "+(4+i)+":"+(45-i*2)+":00");
                ht.setState("1");
                hiddenList.add(ht);
            }
        }else {
            for(int i=0;i<13;i++){
                if(i%6==1||i%6==0) continue;
                HiddenTroubleDetail ht=new HiddenTroubleDetail();
                ht.setStockName("道路-侧缘石-破损");
                ht.setQuantity(String.valueOf(i));
                ht.setUnit("平米");
                ht.setRoadName("海旭路");
                ht.setLightId("018");
                ht.setDate("2017-07-0"+i+" "+(4+i)+":"+(45-i*2)+":00");
                ht.setState(String.valueOf(i%6));
                hiddenList.add(ht);
            }
        }
        adapter.updata(hiddenList);
//        mlvHiddenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent trouble = new Intent("android.intent.action.HIDDENTROUBLEDETAILACTIVITY");
//                trouble.putExtra("id", list.get(position).getPlanId());
//                startActivity(trouble);
//            }
//        });
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case HiddenModule.INT_GET_TROUBLES_LIST_SUCCESS:
//                list=(ArrayList<PlanManage>)msg.obj;
//                adapter.updata(list);
                break;
            case HiddenModule.INT_GET_TROUBLES_LIST_Filed:
                break;
        }
    }
}
