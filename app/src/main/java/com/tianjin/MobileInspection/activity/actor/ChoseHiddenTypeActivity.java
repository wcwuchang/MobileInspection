package com.tianjin.MobileInspection.activity.actor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.ChoseHiddenTypeAdapter;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.InspectionModule;

import java.util.ArrayList;

/**
 * Created by wuchang on 2016-12-14.
 */
public class ChoseHiddenTypeActivity extends BaseActivity{

    private LinearLayout mlinearBack;
    private LinearLayout mlinearSure;
    private TextView mtvTitle;
    private TextView mtvSure;

    private ListView mlvHiddenList;
    private ArrayList<HiddenType> hiddenlist;
    private ChoseHiddenTypeAdapter adapter;
    private InspectionModule module;

    private ArrayList<HiddenType> hasChoseList;
    private Intent intent;
    private int oldChose=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_yh_detail);
        KLog.d("gg");
        initView();
        initData();
    }

    private void initData() {
        hiddenlist=new ArrayList<HiddenType>();
        adapter=new ChoseHiddenTypeAdapter(this);
        mlvHiddenList.setAdapter(adapter);
        intent=getIntent();
        hasChoseList=(ArrayList<HiddenType>)intent.getExtras().getSerializable("hasChose");

        mlvHiddenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hiddenlist.get(oldChose).setChosed(false);
                if(((HiddenType)adapter.getItem(position)).isChosed()){
                    hiddenlist.get(position).setChosed(false);
                }else {
                    hiddenlist.get(position).setChosed(true);
                }
                oldChose=position;
                adapter.updata(hiddenlist);
            }
        });
        module=new InspectionModule(handler,this);
        module.getHiddenTroubleType();
    }


    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearSure=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvSure=(TextView)findViewById(R.id.tv_activity_mun);

        mlvHiddenList=(ListView)findViewById(R.id.lv_yh_detail_list);

        mtvTitle.setText("选择隐患类型");
        mlinearSure.setVisibility(View.VISIBLE);
        mtvSure.setText("确定");

        mlinearBack.setOnClickListener(this);
        mlinearSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_return_back:
                finish();
                break;
            case R.id.linear_save:
                ArrayList<HiddenType> list=new ArrayList<HiddenType>();
                for(HiddenType ht:hiddenlist){
                    if(ht.isChosed()){
                        list.add(ht);
                    }
                }
                Bundle bundle=new Bundle();
                bundle.putSerializable("hiddentype",list);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case InspectionModule.INT_GET_HIDDENTROUBLETYPE_SUCCESS:
                hiddenlist=(ArrayList<HiddenType>)msg.obj;
                if(hasChoseList!=null){
                    for(int i=0;i<hasChoseList.size();i++){
                        for(int j=0;j<hiddenlist.size();j++){
                            if(hasChoseList.get(i).getTypeId().equals(hiddenlist.get(j).getTypeId())){
                                hiddenlist.get(j).setChosed(true);
                                break;
                            }
                        }
                    }
                }
                adapter.updata(hiddenlist);
                break;
            case InspectionModule.INT_GET_HIDDENTROUBLETYPE_FILED:
                toLoginActivity(msg);
                break;
        }
    }
}
