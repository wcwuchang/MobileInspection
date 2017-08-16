package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.Approve;
import com.tianjin.MobileInspection.until.ConnectionURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuangaoran on 2016-12-19.
 */
public class ApproveModule extends BaseModule {

    public final static int INT_GET_APPROVE_SUCCESS=0x5568;
    public final static int INT_GET_APPROVE_FILED=0x5569;
    public final static int INT_COMMIT_APPROVE_SUCCESS=0x5570;
    public final static int INT_COMMIT_APPROVE_FILED=0x5571;

    private Handler handler;
    public ApproveModule (Handler handler,Context context){
        this.handler=handler;
        this.context=context;
    }

    //获取审批列表
    public void getApproveList(String procInsId){
        Map<String,String> map=new HashMap<String, String>();
        map.put("procInsId",procInsId);
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_GET_ACT_TASK_HISTOICFLOW, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.d(result.toString());
                        try {
                            JSONObject job=result.getJSONObject("body");
                            String str=job.getString("list");
                            JSONArray jsonArray=new JSONArray(str);
                            ArrayList<Approve> approves=new ArrayList<Approve>();
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jt=jsonArray.getJSONObject(i);
                                KLog.json(jt.toString());
                                JSONObject histIns=jt.getJSONObject("histIns");
                                Approve approve=new Approve();
                                approve.setApproveContent(histIns.optString("activityName"));
                                approve.setApproveId(histIns.getString("activityId"));
                                approve.setApproveCode(histIns.optString("activityName"));
                                approve.setApproveMan(jt.optString("assigneeName"));
                                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                                Date date=new Date(histIns.getString("startTime"));
                                approve.setApproveDate(sdf.format(date));
                                approve.setApproveStartTime(sdf.format(date));
                                approve.setApproveDecision(jt.optString("comment"));
                                if(histIns.has("endTime")){
                                    Date end=new Date(histIns.getString("endTime"));
                                    approve.setApproveFinishTime(sdf.format(end));
                                }else {
                                    approve.setApproveFinishTime("");
                                }
                                approves.add(approve);
                            }
                            resultMessage(handler,INT_GET_APPROVE_SUCCESS,approves,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_APPROVE_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_GET_APPROVE_FILED,msg,error);
                    }
                });

    }

    //提交审批
    public void approveCommit(String url,Map<String,String> map){
        KLog.d(map.toString());
        KLog.d(url);
        new JSONReauest(context,url, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        handler.sendEmptyMessage(INT_COMMIT_APPROVE_SUCCESS);
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        resultMessage(handler,INT_COMMIT_APPROVE_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_COMMIT_APPROVE_FILED,msg,error);
                    }
                });

    }


    //提交审批
    public void deferredMaintenance(String accidentMain,String planningManagement){
        Map<String,String> map=new HashMap<String, String>();
        map.put("accidentMain.id",accidentMain);
        map.put("planningManagement.id",planningManagement);
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_DEFERRED_MAINTENANCE, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        handler.sendEmptyMessage(INT_COMMIT_APPROVE_SUCCESS);
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        resultMessage(handler,INT_COMMIT_APPROVE_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_COMMIT_APPROVE_FILED,msg,error);
                    }
                });

    }
}
