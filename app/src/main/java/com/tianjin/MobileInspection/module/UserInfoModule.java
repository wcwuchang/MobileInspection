package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DialogUnit;
import com.tianjin.MobileInspection.until.FileUtil;
import com.tianjin.MobileInspection.until.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhuangaoran on 2016-12-2.
 */
public class UserInfoModule {

    private Handler handler;
    private Context mContext;
    public final static int INT_GET_USERINFO_SUCCESS=0x1001;
    public final static int INT_GET_USERINFO_FILED=0x1000;
    public final static int INT_CHANGE_USERINFO_SUCCESS=0x1002;
    public final static int INT_CHANGE_USERINFO_FILED=0x1003;

    public UserInfoModule(Handler handler,Context context) {
        this.handler = handler;
        this.mContext=context;
    }

    /**
     * 获取个人信息
     */
    public void getInfo(){
        new JSONReauest(mContext,ConnectionURL.STR_USER_INFO, MyApplication.CONNECTION_TIME_OUT, new JSONReauest.ResultListener() {
            @Override
            public void Success(JSONObject result) {
                try {
                    Message msg=new Message();
                    JSONObject obj=result.getJSONObject("body").getJSONObject("data");
                    msg.what=INT_GET_USERINFO_SUCCESS;
                    msg.obj="获取个人信息成功";
                    msg.arg1=-1;
                    MyApplication.setStringSharedPreferences(MyApplication.USER_INFO_EMAIL,CheckUntil.getJsonIsNull(obj.getString("email")));
                    MyApplication.setStringSharedPreferences(MyApplication.USER_INFO_PHOTO_PATH,CheckUntil.getJsonIsNull(obj.getString("photo")));
                    MyApplication.setStringSharedPreferences(MyApplication.USER_INFO_PHOTO_NAME, FileUtil.getFileNameForUrl(obj.getString("photo")));
                    MyApplication.setStringSharedPreferences(MyApplication.USER_INFO_PHONE, CheckUntil.getJsonIsNull(obj.getString("mobile")));
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Error(VolleyError volleyError) {
                Message msg=new Message();
                msg.what=INT_GET_USERINFO_FILED;
                msg.obj="网络连接失败";
                msg.arg1=1;
                handler.sendMessage(msg);
            }

            @Override
            public void Filed(String msg1,int errorcode) {
                KLog.d(msg1+"     "+errorcode);
                Message msg=new Message();
                msg.what=INT_GET_USERINFO_FILED;
                msg.obj=msg1;
                msg.arg1=errorcode;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 信息修改
     * @param type
     * @param name
     */
    public void userInfoEdit(final String type,final String name){
        KLog.d(type,name);
        String jsessionid=MyApplication.getStringSharedPreferences("JSESSIONID","");
        DialogUnit.showDialog(mContext);
        KLog.d(ConnectionURL.STR_USER_EDIT+jsessionid+ConnectionURL.HTTP_SERVER_END);
        StringRequest request=new StringRequest(Request.Method.POST, ConnectionURL.STR_USER_EDIT+jsessionid+ConnectionURL.HTTP_SERVER_END,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogUnit.closeDialog();
                        try {
                            KLog.json(s);
                            JSONObject job = new JSONObject(s);
                            Message msg=new Message();
                            if (job.getBoolean("success")) {
                                msg.what=INT_CHANGE_USERINFO_SUCCESS;
                                msg.obj="修改成功";
                            }else{
                                msg.what=INT_CHANGE_USERINFO_FILED;
                                msg.obj="修改失败";
                            }
                            msg.arg1=job.getInt("errorCode");
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DialogUnit.closeDialog();
                volleyError.printStackTrace();
                Message msg=new Message();
                msg.what=INT_CHANGE_USERINFO_FILED;
                msg.obj="服务器连接失败";
                msg.arg1=1;
                handler.sendMessage(msg);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> map=new HashMap<String,String>();
                map.put(type, name);
                KLog.d(map.toString());
                return map;
            }
        };
        VolleyUtil.getRequestQueue().add(request);
    }


}
