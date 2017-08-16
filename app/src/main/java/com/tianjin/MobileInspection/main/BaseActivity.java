package com.tianjin.MobileInspection.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.module.LoginModule;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.umeng.message.PushAgent;

/**
 *
 * Created by wuchang on 2016-11-21.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LoginModule.INT_LOGIN_SUCCESS:
                    Toast.makeText(BaseActivity.this, "网络错误请重新提交请求", Toast.LENGTH_SHORT).show();
                    break;
                case LoginModule.INT_LOGIN_FILED:
                    Toast.makeText(BaseActivity.this, "登录超时，请重新登录", Toast.LENGTH_SHORT).show();
                    retuenTologin();
                    break;
            }
            resultData(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(false);//true:半透明的黑色；false全透明的
//          tintManager.setStatusBarTintResource(R.color.title);//通知栏所需颜色
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
//        PushAgent.getInstance(this).onAppStart();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void toLoginActivity(Message msg){
        if(msg.arg1==0){
            LoginModule module =new LoginModule(handler,this);
            module.login(MyApplication.getStringSharedPreferences("username",""),
                    MyApplication.getStringSharedPreferences("password",""));
//            retuenTologin();
        }else {
            Toast.makeText(this, CheckUntil.checkEditext(msg.obj), Toast.LENGTH_SHORT).show();
        }
    }

    public void retuenTologin(){
        MyApplication.setStringSharedPreferences("password","");
        MyApplication.setStringSharedPreferences("JSESSIONID","");
        Intent i=new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void resultData(Message msg){

    }

    @Override
    public void onClick(View v) {

    }

    public Context getContext(){
        return this;
    }

}
