package com.tianjin.MobileInspection.activity.contact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tianjin.MobileInspection.R;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.CircleImg;
import com.tianjin.MobileInspection.entity.Contact;
import com.tianjin.MobileInspection.main.BaseActivity;
import com.tianjin.MobileInspection.module.ContactsModule;
import com.tianjin.MobileInspection.module.FileUploadModule;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.FileUtil;

import java.io.File;

/**
 * Created by wuchang on 2016-12-2.
 * 通讯录详情
 */
public class ContactDetailActivity extends BaseActivity {

    private LinearLayout mlinearBack;
    private RelativeLayout mrelatCallNumber;
    private TextView mtvTitle;
    private TextView mtvName;
    private TextView mtvPhone;
    private TextView mtvEmail;
    private TextView mtvOffice;
    private CircleImg mciHead;
    private ImageView mivSms;
    private ImageView mivPhone;

    private Intent intent;
    private ContactsModule module;
    private Bitmap bitmap;
    private Contact con;

    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            mciHead.setImageBitmap(bitmap);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);
        initView();
        initData();
    }

    private void initData() {
        intent=getIntent();
        mtvTitle.setVisibility(View.GONE);
        mtvName.setText(intent.getExtras().getString(ContactActivity.STR_NAME));
        mtvOffice.setText(intent.getExtras().getString(ContactActivity.STR_OFFICENAME));
        module=new ContactsModule(handler,this);
        module.getContactDetail(intent.getExtras().getString(ContactActivity.STR_id));
        final String path=intent.getExtras().getString(ContactActivity.STR_PHOTO);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (path!=null||!path.equals("")) {
                    String name= FileUtil.getFileNameForUrl(path);
                    File head=new File(MyApplication.CONTACT_PATH,name);
                    if(!head.exists()){
                        bitmap = FileUploadModule.getBitmapFromUrl(path);
                        if(bitmap!=null) {
                            FileUtil.saveFile(ContactDetailActivity.this, MyApplication.CONTACT_PATH, name, bitmap);
                        }
                    }else {
                        bitmap=BitmapFactory.decodeFile(head.getPath());
                    }
                    handler.post(runnable);
                }
            }
        }).start();

    }

    private void initView() {
        mciHead=(CircleImg)findViewById(R.id.ci_contact_detail_head);
        mlinearBack=(LinearLayout)findViewById(R.id.linear_return_back);
        mtvTitle=(TextView)findViewById(R.id.tv_activity_title);
        mtvName=(TextView)findViewById(R.id.tv_contact_deatil_name);
        mtvPhone=(TextView)findViewById(R.id.tv_contact_phone);
        mtvEmail=(TextView)findViewById(R.id.tv_contact_email);
        mtvOffice=(TextView)findViewById(R.id.tv_contact_deatil_job);
        mivSms=(ImageView)findViewById(R.id.iv_contact_sms);
        mivPhone=(ImageView)findViewById(R.id.iv_contact_phone);
        mrelatCallNumber=(RelativeLayout) findViewById(R.id.relat_call_number);

        mlinearBack.setOnClickListener(this);
//        mrelatCallNumber.setOnClickListener(this);
        mivSms.setOnClickListener(this);
        mivPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_return_back:
                this.finish();
                break;
            case R.id.iv_contact_phone:
                if(CheckUntil.checkEditext(mtvPhone.getText()).equals("")) return;
                call(mtvPhone.getText().toString());
                break;
            case R.id.iv_contact_sms:
                if(con!=null && !CheckUntil.checkEditext(con.getPhoneNumber()).equals("")){
                    KLog.d(con.getPhoneNumber());
                    sendSMS(con.getPhoneNumber());
                }
                break;
        }
    }

    /**
     * 调用系统短信发送界面
     * @param phoneNumber
     */
    private void sendSMS(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    /**
     * 电话拨打
     * @param atr
     */
    private void call(String atr){
        Intent intent=new Intent(Intent.ACTION_CALL);
        Uri data= Uri.parse("tel:"+atr);
        intent.setData(data);
        try {
            startActivity(intent);
        }catch (SecurityException e){
            KLog.d(e.getMessage());
        }
    }

    @Override
    public void resultData(Message msg) {
        super.resultData(msg);
        switch (msg.what){
            case ContactsModule.INT_GET_CONTACTS_DETAIL_SUCCESS:
                con=(Contact)msg.obj;
                mtvPhone.setText(con.getPhoneNumber());
                mtvEmail.setText(con.getEmail());
                break;
            case ContactsModule.INT_GET_CONTACTS_DETAIL_FILEAD:
                toLoginActivity(msg);
                break;
        }
    }
}
