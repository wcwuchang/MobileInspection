package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DialogUnit;
import com.tianjin.MobileInspection.until.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by wuchang on 2016-12-8.
 */
public class JSONReauest {

    public interface ResultListener{
        public void Success(JSONObject result);
        public void Error(VolleyError volleyError);
        public void Filed(String msg,int errorcode);
    }


    public JSONReauest(final Context context, final String url, final int timeout, final Map<String,String> map, final ResultListener listener){
        DialogUnit.showDialog(context);
        String jsessionid = MyApplication.getStringSharedPreferences("JSESSIONID", "");
        KLog.d("data",url + jsessionid + ConnectionURL.HTTP_SERVER_END);
        StringRequest request=new StringRequest(Request.Method.POST, url + jsessionid + ConnectionURL.HTTP_SERVER_END,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogUnit.closeDialog();
                        try {
                            final JSONObject job = new JSONObject(s);
                            if (job.getBoolean("success")) {
                                listener.Success(job);
                            } else {
                                int errorcode=Integer.valueOf(job.getString("errorCode"));
                                listener.Filed(job.getString("msg"),errorcode);
                            }
                        } catch (JSONException e) {
                            listener.Filed(e.getMessage(),-1);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DialogUnit.closeDialog();
                if(volleyError!=null) {
                    KLog.d("eee", volleyError.getMessage());
                    volleyError.printStackTrace();
                    listener.Error(volleyError);
                }else {
                    Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, 1.0f));//自定义超时时间,最大请求次数，退避乘数

        VolleyUtil.getRequestQueue().add(request);

    }


    public JSONReauest(Context context,final String url, final int timeout, final ResultListener listener){
        DialogUnit.showDialog(context);
        String jsessionid = MyApplication.getStringSharedPreferences("JSESSIONID", "");
        KLog.d(url + jsessionid + ConnectionURL.HTTP_SERVER_END);
        StringRequest request=new StringRequest(Request.Method.GET, url + jsessionid + ConnectionURL.HTTP_SERVER_END,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        KLog.d(s);
                        DialogUnit.closeDialog();
                        try {
                            final JSONObject job = new JSONObject(s);
                            if (job.getBoolean("success")) {
                                listener.Success(job);
                            } else {
                                int errorcode= Integer.valueOf(job.getString("errorCode"));
                                listener.Filed(job.getString("msg"),errorcode);
                            }
                        } catch (JSONException e) {
                            listener.Filed(e.getMessage(),-1);
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogUnit.closeDialog();
                listener.Error(volleyError);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, 1.0f));//自定义超时时间
        VolleyUtil.getRequestQueue().add(request);

    }

    public JSONReauest(Context context,final String url, final int timeout, final JSONObject data, final ResultListener listener){
        DialogUnit.showDialog(context);
        String jsessionid = MyApplication.getStringSharedPreferences("JSESSIONID", "");
        KLog.d(data.toString());
        JsonRequest<JSONObject> request = new JsonObjectRequest(Request.Method.POST,url + jsessionid + ConnectionURL.HTTP_SERVER_END, data,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        DialogUnit.closeDialog();
                        try {
                            if (response.getBoolean("success")) {
                                KLog.d(response.toString());
                                listener.Success(response);
                            } else {
                                int errorcode=Integer.valueOf(response.getString("errorCode"));
                                listener.Filed(response.getString("msg"),errorcode);
                            }
                        } catch (JSONException e) {
                            listener.Filed(e.getMessage(),-1);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                DialogUnit.closeDialog();
                listener.Error(error);
            }

        });

        request.setRetryPolicy(new DefaultRetryPolicy(timeout, 0, 1.0f));//自定义超时时间
        VolleyUtil.getRequestQueue().add(request);

    }




}
