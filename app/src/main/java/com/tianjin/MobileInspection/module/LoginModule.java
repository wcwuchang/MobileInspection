package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.service.PushService;
import com.tianjin.MobileInspection.until.ConnectionURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginModule extends BaseModule {

    private String TAG = "LoginModule";
    private Handler handler;
    public final static int INT_LOGIN_SUCCESS = 0x786111;
    public final static int INT_LOGIN_FILED = 0x786121;
    public final static int INT_LOGINOUT_SUCCESS = 0x78613;
    public final static int INT_LOGINOUT_FILED = 0x78614;

    public LoginModule(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;
    }

    //登录
    public void login(final String name, final String password) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", name);
        map.put("password", password);
        map.put("mobileLogin", "true");
        new JSONReauest(context, ConnectionURL.STR_LOGIN_CONNECTION, MyApplication.CONNECTION_TIME_OUT, map, new JSONReauest.ResultListener() {
            @Override
            public void Success(JSONObject result) {
                KLog.d(result.toString());
                try {
                    JSONObject json = result.getJSONObject("body");
                    MyApplication.setStringSharedPreferences("username", name);
                    MyApplication.setStringSharedPreferences("name", json.getString("name"));
                    MyApplication.setStringSharedPreferences("password", password);
                    MyApplication.setStringSharedPreferences("JSESSIONID", json.getString("JSESSIONID"));
                    MyApplication.setStringSharedPreferences("userId", json.getString("id"));
//                    PushUntil.addAlias();
                    Intent intent=new Intent(context, PushService.class);
                    context.startService(intent);
                    resultMessage(handler, INT_LOGIN_SUCCESS, result.getString("msg"), -1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Error(VolleyError volleyError) {
                resultMessage(handler, INT_LOGIN_FILED, "网络错误", -1);
            }

            @Override
            public void Filed(String msg, int errorcode) {
                resultMessage(handler, INT_LOGIN_FILED, msg, errorcode);
            }
        });

    }

    public void loginout() {
        String jsessionid = MyApplication.getStringSharedPreferences("JSESSIONID", "");
        new JSONReauest(context, ConnectionURL.STR_LOGIN_OUT, MyApplication.CONNECTION_TIME_OUT, new JSONReauest.ResultListener() {
            @Override
            public void Success(JSONObject result) {
                try {
                    resultMessage(handler, INT_LOGINOUT_SUCCESS, result.getString("msg"), 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Error(VolleyError volleyError) {
                resultMessage(handler, INT_LOGINOUT_FILED, "服务器连接失败", 1);
            }

            @Override
            public void Filed(String msg, int errorcode) {
                resultMessage(handler, INT_LOGINOUT_FILED, msg, errorcode);
            }
        });

    }


}
