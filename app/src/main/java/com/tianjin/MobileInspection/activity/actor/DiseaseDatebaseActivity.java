package com.tianjin.MobileInspection.activity.actor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.adapter.DiseaseSelectAdapter;
import com.tianjin.MobileInspection.customview.TreeListView.TreeElement;
import com.tianjin.MobileInspection.customview.TreeListView.TreeListView;
import com.tianjin.MobileInspection.customview.TreeListView.TreeViewAdapter;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.InspectionModule;
import com.tianjin.MobileInspection.until.ConnectionURL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuangaoran on 2017-1-4.
 *
 * 病害库
 * 专家库
 */
public class DiseaseDatebaseActivity extends BaseActivity {

    private LinearLayout mlinearBack;
    private LinearLayout mlinearSave;
    private TextView mtvTitle;
    private ImageView mivSave;

    private TreeListView mlvData;
    private TreeViewAdapter adapter;
    private List<TreeElement> disexp;
    private List<TreeElement> selectData;

    private EditText medtSelect;
    private ImageView mivShow;

    private InspectionModule module;
    private boolean isDisease=false;
    private Intent intent;
    private int chose=-1;
    private String content="";
    private ListView mlvSelsetList;
    private DiseaseSelectAdapter diseaseSelectAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_datebase);
        initView();
        initData();
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearSave=(LinearLayout)findViewById(R.id.linear_add);
        mivSave=(ImageView)findViewById(R.id.iv_add);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mivSave.setImageResource(R.drawable.list_ico_save);

        mlvData=(TreeListView)findViewById(R.id.lv_disease_expert);
        mlinearSave.setVisibility(View.VISIBLE);
        medtSelect=(EditText) findViewById(R.id.edt_seach);
        mivShow=(ImageView)findViewById(R.id.iv_seach_show);
        mlvSelsetList=(ListView)findViewById(R.id.lv_diaease_select);

        mlinearBack.setOnClickListener(this);
        mlinearSave.setOnClickListener(this);
    }

    private void initData() {
        module=new InspectionModule(handler,this);
        selectData=new ArrayList<TreeElement>();
        intent=getIntent();
        isDisease=intent.getBooleanExtra("dis",false);
        diseaseSelectAdapter=new DiseaseSelectAdapter(this);
        mlvSelsetList.setAdapter(diseaseSelectAdapter);
        if(isDisease) {
            module.getDiseaseList(ConnectionURL.STR_GET_DISEASEDATEBASE);
            mtvTitle.setText("病害库");
        }else {
            module.getDiseaseList(ConnectionURL.STR_GET_EXPERTDATEBASE);
            mtvTitle.setText("专家库");
        }
        mlvData.setLastLevelItemClickCallBack(itemClickCallBack);// 设置节点点击事件监听

        medtSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mlvSelsetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("dis",((TreeElement)diseaseSelectAdapter.getItem(position)).getContent());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    private void selectData(String str){
        selectData.clear();
        if (!str.equals("")) {
            mivShow.setVisibility(View.GONE);
            mlvData.setVisibility(View.GONE);
            mlvSelsetList.setVisibility(View.VISIBLE);
            for (int i = 0; i < disexp.size(); i++) {
                if(disexp.get(i).getTitle().indexOf(str)!=-1&&!disexp.get(i).isHasChild()){
                    selectData.add(disexp.get(i));
                }
            }
            diseaseSelectAdapter.updata(selectData);
        }else {
            mlvData.setVisibility(View.VISIBLE);
            mlvSelsetList.setVisibility(View.GONE);
            mivShow.setVisibility(View.VISIBLE);
            selectData.addAll(disexp);
            mlvData.initData(this,selectData);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.linear_add:
                if(chose!=-1){
                    intent.putExtra("dis",content);
                    setResult(RESULT_OK,intent);
                }
            case R.id.linear_return_back:
                finish();
                break;
        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case InspectionModule.INT_GET_DISEASE_OR_EXPERT_SUCCESS:
                disexp=(List<TreeElement>)msg.obj;
                if(disexp==null||disexp.size()==0){
                    medtSelect.setFocusable(false);
                    return;
                }else {
                    medtSelect.setFocusable(true);
                }
                for(int i=0;i<disexp.size();i++){
                    for(int j=0;j<disexp.size();j++){
                        if(disexp.get(i).getId().equals(disexp.get(j).getParentId())){
                            disexp.get(i).setHasChild(true);
                        }
                        if(disexp.get(i).getParentId().equals(disexp.get(j).getId())){
                            disexp.get(i).setHasParent(true);
                        }
                    }
                    selectData.add(disexp.get(i));
                }
                mlvData.initData(this, selectData);// 初始化数据
                break;
            case InspectionModule.INT_GET_DISEASE_OR_EXPERT_FILED:
                toLoginActivity(msg);
                break;
        }
    }

    TreeListView.LastLevelItemClickListener itemClickCallBack = new TreeListView.LastLevelItemClickListener() {// 创建节点点击事件监听
        @Override
        public void onLastLevelItemClick(int position,TreeViewAdapter adapter) {
            KLog.d(chose+"");
            if(((TreeElement) adapter.getItem(position)).isChosed()){
                ((TreeElement) adapter.getItem(position)).setChosed(false);
                content="";
            }else {
                if(chose!=-1&&chose<adapter.getCount()){
                    ((TreeElement) adapter.getItem(chose)).setChosed(false);
                }
                ((TreeElement) adapter.getItem(position)).setChosed(true);
                content = ((TreeElement) adapter.getItem(position)).getContent();
            }
            chose=position;
            adapter.notifyDataSetChanged();
            KLog.d(chose+"");
        }
    };
}
