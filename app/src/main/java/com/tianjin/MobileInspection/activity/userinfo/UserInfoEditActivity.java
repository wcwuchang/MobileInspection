package com.tianjin.MobileInspection.activity.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.UserInfoModule;
import com.tianjin.MobileInspection.until.CheckUntil;

/**
 * Created by 吴昶 on 2016/12/3.
 *
 * 个人信息修改
 */
public class UserInfoEditActivity extends BaseActivity{

    private LinearLayout mlinearServe;
    private LinearLayout mlinearBack;
    private TextView mtvTitle;
    private EditText medtChange;
    private ImageView mivCancel;

    private Intent intent;
    private int type=0;
    private String name;
    private String change;
    private UserInfoModule userInfoMoudle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_edit);
        initView();
        initData();
    }

    private void initData() {
        intent=getIntent();
        Bundle bundle=intent.getExtras();
        type=bundle.getInt(UserInfoActivity.STR_CHANG_TYPE);
        mtvTitle.setText(bundle.getString(UserInfoActivity.STR_CHANG_NAME));
        KLog.d(type+"");
        switch (type){
            case 3:
                name="email";
                break;
            case 2:
                name="phone";
                break;
            case 4:
                name="mobile";
                break;
        }
        medtChange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString()!=null&&!s.toString().equals("")){
                    mivCancel.setVisibility(View.VISIBLE);
                }else {
                    mivCancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userInfoMoudle=new UserInfoModule(handler,this);
    }

    private void initView() {
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mlinearServe=(LinearLayout)findViewById(R.id.linear_save);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        medtChange=(EditText)findViewById(R.id.edt_chang_detail);
        mivCancel=(ImageView)findViewById(R.id.iv_change_cancel);

        mlinearServe.setVisibility(View.VISIBLE);

        mlinearServe.setOnClickListener(this);
        mlinearBack.setOnClickListener(this);
        mivCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_return_back:
                this.finish();
                break;
            case R.id.linear_save:
                serveInfo();
                break;
            case R.id.iv_change_cancel:
                medtChange.setText("");
                break;
        }
    }

    private void serveInfo(){
        if(CheckUntil.checkEditext(medtChange.getText()).equals("")){
            this.finish();
            return;
        }else{
            change=medtChange.getText().toString();
            if(type==2){
                if(!CheckUntil.isMobileNO(change)){
                    Toast.makeText(this,"手机号码格式错误",Toast.LENGTH_LONG).show();
                    return;
                }
            }else if(type==2){
                if(!CheckUntil.isEmail(change)){
                    Toast.makeText(this,"邮箱格式错误",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            userInfoMoudle.userInfoEdit(name,change);
        }

    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case UserInfoModule.INT_CHANGE_USERINFO_SUCCESS:
                Bundle bundle=new Bundle();
                bundle.putInt(UserInfoActivity.STR_CHANG_TYPE,type);
                bundle.putString(UserInfoActivity.STR_CHANG_NAME,change);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                UserInfoEditActivity.this.finish();
                break;
            case UserInfoModule.INT_CHANGE_USERINFO_FILED:
                toLoginActivity(msg);
                break;
        }
    }
}
