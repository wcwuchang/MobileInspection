package com.tianjin.MobileInspection.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.main.LoginActivity;

/**
 * Created by 吴昶 on 2016/12/15.
 */
public class BaseFragment extends Fragment {

    public Context context;
    public void setContext(Context context){
        this.context=context;
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            resultData(msg);
        }
    };


    public void resultData(Message msg){

    }


    public void toLoginActivity(Message msg){
        if(msg.arg1==0){
            Toast.makeText(context,"登陆超时请重新登陆",Toast.LENGTH_SHORT).show();
            retuenTologin();
        }else {
            Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void retuenTologin(){
        MyApplication.setStringSharedPreferences("password","");
        MyApplication.setStringSharedPreferences("JSESSIONID","");
        Intent i=new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();
    }

}
