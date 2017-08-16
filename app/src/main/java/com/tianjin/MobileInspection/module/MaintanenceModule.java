package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DialogUnit;
import com.tianjin.MobileInspection.until.JsonAndMap;
import com.tianjin.MobileInspection.until.VolleyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuchang on 2017-6-9.
 */
public class MaintanenceModule {


    public final static int INT_UPDATEMAINTENANCE_SUCCESS=0x534;
    public final static int INT_UPDATEMAINTENANCE_FILED=0x535;
    public static void updateMaintenance(final Handler handler, String id,String maintenance){
        final Map<String,String> map=new HashMap<String,String>();
        map.put("id", id);
        map.put("maintenance",maintenance);
        StringRequest request=new StringRequest(Request.Method.POST, ConnectionURL.STR_GET_NOT_MAINTANENCE+ MyApplication.getStringSharedPreferences("JSESSIONID", "")+ConnectionURL.HTTP_SERVER_END,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            KLog.d(s);
                            if(jsonObject.getBoolean("success")) {
                                handler.sendEmptyMessage(INT_UPDATEMAINTENANCE_SUCCESS);
                            }else {
                                handler.sendEmptyMessage(INT_UPDATEMAINTENANCE_FILED);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1.0f));//自定义超时时间,最大请求次数，退避乘数
        VolleyUtil.getRequestQueue().add(request);
    }


    public final static int NEW_MAINTANENCE_SUCCESS=0x577;
    public final static int NEW_MAINTANENCE_FILED=0x576;

    /**
     * 立刻维修
     * @param handler
     * @param mainId
     * @param contractId
     * @param maintenanceTime
     * @param userId
     * @param descripion
     * @param state  0:上报方案, 1:立即维修,3:维修上报过是，2:方案上报过是,4代表计划下发
     * @param month
     */
    public static void newMaintenance(final Handler handler, String mainId,String contractId,
                                      String maintenanceTime,String userId,String descripion,String state,int month){
        final Map<String,String> map=new HashMap<String,String>();
        map.put("accidentMain.id", mainId);
        map.put("tuser.id",userId);
        map.put("maintenanceTime", maintenanceTime);
        map.put("description",descripion);
        map.put("contractMain.id", contractId);
        map.put("maintenanceState",state);
        map.put("yearMonth",String.valueOf(month));
        KLog.d(map.toString());
        StringRequest request=new StringRequest(Request.Method.POST, ConnectionURL.STR_SAVE_DEILAY_TASK+ MyApplication.getStringSharedPreferences("JSESSIONID", "")+ConnectionURL.HTTP_SERVER_END,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            KLog.d(s);
                            if(jsonObject.getBoolean("success")) {
                                handler.sendEmptyMessage(NEW_MAINTANENCE_SUCCESS);
                            }else {
                                handler.sendEmptyMessage(NEW_MAINTANENCE_FILED);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1.0f));//自定义超时时间,最大请求次数，退避乘数
        VolleyUtil.getRequestQueue().add(request);
    }


    public static void reportShceme(final Handler handler, String programUpload,String description){
        final Map<String,String> map=new HashMap<String,String>();
        map.put("dailyMaintenanceProgram.description", description);
        map.put("id",programUpload);
        KLog.d(map.toString());
        StringRequest request=new StringRequest(Request.Method.POST, ConnectionURL.STR_REPORT_SHCEME+ MyApplication.getStringSharedPreferences("JSESSIONID", "")+ConnectionURL.HTTP_SERVER_END,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            KLog.d(s);
                            if(jsonObject.getBoolean("success")) {
                                JSONObject body=jsonObject.getJSONObject("body");
                                Message msg=handler.obtainMessage();
                                msg.what=NEW_MAINTANENCE_SUCCESS;
                                msg.obj=body.getString("id");
                                handler.sendMessage(msg);
                            }else {
                                handler.sendEmptyMessage(NEW_MAINTANENCE_FILED);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1.0f));//自定义超时时间,最大请求次数，退避乘数
        VolleyUtil.getRequestQueue().add(request);
    }


    public static final int INT_GET_SPECIAL_DETAIL_SUCCESS=0x214;
    public static final int INT_GET_SPECIAL_DETAIL_FILED=0x215;

    /**
     * 待办方案上报详情
     */
    public static void getReportDetail(final Handler handler, Context context, String id){

        Map<String,String> map=new HashMap<String, String>();
        map.put("is",id);
        DialogUnit.showDialog(context);
        StringRequest request=new StringRequest(Request.Method.GET, ConnectionURL.STR_GET_DEILAY_TASK_DETAIL+ MyApplication.getStringSharedPreferences("JSESSIONID", "")+ ConnectionURL.HTTP_SERVER_END_Y,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogUnit.closeDialog();
                        KLog.json(s);
                        try {
                            JSONObject job=new JSONObject(s);
                            if(job.getBoolean("success")) {
                                JSONObject body=job.getJSONObject("body");
                                JSONObject data = body.getJSONObject("data");
                                Task task = new Task();
                                task.setTaskId(data.getString("id"));
                                task.setTaskContent(data.getString("description"));
                                HiddenTroubleDetail htd=new HiddenTroubleDetail();
                                JSONObject acc=data.getJSONObject("accidentMain");
                                htd.setStockName(acc.getString("description"));
                                htd.setQuantity(acc.getString("quantity"));
                                htd.setUnit(acc.getString("unit"));
                                task.setHiddenTroubleDetail(htd);
                                ArrayList<String> fuj=new ArrayList<String>();
                                if(!data.isNull("dailyMaintenanceAttachmentList")){
                                    JSONArray fj=data.getJSONArray("dailyMaintenanceAttachmentList");
                                    for(int i=0;i<fj.length();i++){
                                        KLog.d(fj.getJSONObject(i).getString("path"));
                                        fuj.add(fj.getJSONObject(i).getString("path"));
                                    }
                                }
                                task.setFujian(fuj);
                                JSONObject act=data.getJSONObject("act");
                                task.setActtaskId(act.getString("taskId"));
                                task.setActtaskDefKey(act.getString("taskDefKey"));
                                task.setActprocInsId(act.getString("procInsId"));
                                task.setActprocDefId(act.getString("procDefId"));
                                if(body.has("button")){
                                    task.setOption(JsonAndMap.getMapForJson(body.getJSONObject("button").toString()).toString());
                                }
                                Message msg=handler.obtainMessage();
                                msg.what=INT_GET_SPECIAL_DETAIL_SUCCESS;
                                msg.obj=task;
                                msg.arg1=-1;
                                handler.sendMessage(msg);
                            }else {
                                Message msg=handler.obtainMessage();
                                msg.what=INT_GET_SPECIAL_DETAIL_FILED;
                                msg.obj=job.getString("msg");
                                msg.arg1=Integer.valueOf(job.getString("errorCode"));
                                handler.sendMessage(msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogUnit.closeDialog();
                Message msg=handler.obtainMessage();
                msg.what=INT_GET_SPECIAL_DETAIL_FILED;
                msg.obj="服务器连接失败";
                msg.arg1=-1;
                handler.sendMessage(msg);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(MyApplication.CONNECTION_TIME_OUT, 1, 1.0f));//自定义超时时间
        VolleyUtil.getRequestQueue().add(request);
    }
}
