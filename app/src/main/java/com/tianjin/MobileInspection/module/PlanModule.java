package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.PlanManage;
import com.tianjin.MobileInspection.until.ConnectionURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 计划
 * Created by zhuangaoran on 2017-1-6.
 */
public class PlanModule extends BaseModule{

    private Handler handler;

    public PlanModule(Handler handler, Context context){
        this.context=context;
        this.handler=handler;
    }


    public final static int PLAN_LIST_SUCCESS=0x6753;
    public final static int PLAN_LIST_FILED=0x6754;
    /**
     * 获取计划列表
     * @param type
     */
    public void getPlanTypeList(String type){
        Map<String,String> map=new HashMap<String, String>();
        map.put("type",type);
        new JSONReauest(context, ConnectionURL.STR_PLAN_LIST, MyApplication.CONNECTION_TIME_OUT,map,
                new JSONReauest.ResultListener() {
            @Override
            public void Success(JSONObject result) {
                KLog.json(result.toString());
                try {
                    JSONObject body=result.getJSONObject("body");
                    ArrayList<PlanManage> planManages=new ArrayList<PlanManage>();
                    if(body.has("list")&&body.getJSONArray("list")!=null) {
                        JSONArray jsonArray = body.getJSONArray("list");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject job=jsonArray.getJSONObject(i);
                            PlanManage planManage=new PlanManage();
                            planManage.setPlanName(job.getString("title"));
                            planManage.setPlanContent(job.getString("content"));
                            planManage.setPlanStartDate(job.getString("startTime"));
                            planManage.setPlanEndDate(job.getString("endTime"));
                            planManage.setPlanType(job.getString("type"));
                            planManage.setPlanId(job.getString("id"));
                            planManages.add(planManage);
                        }
                    }
                    resultMessage(handler,PLAN_LIST_SUCCESS,planManages,-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void Error(VolleyError volleyError) {
                resultMessage(handler,PLAN_LIST_FILED,"服务器连接错误",-1);
            }

            @Override
            public void Filed(String msg, int errorcode) {
                resultMessage(handler,PLAN_LIST_FILED,msg,errorcode);
            }
        });

    }

    /**
     * 获取计划列表
     */
    public void getPlanTypeList(){
        KLog.d("===========");
        new JSONReauest(context, ConnectionURL.STR_PLAN_LIST, MyApplication.CONNECTION_TIME_OUT,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            ArrayList<PlanManage> planManages=new ArrayList<PlanManage>();
                            if(body.has("list")&&body.getJSONArray("list")!=null) {
                                JSONArray jsonArray = body.getJSONArray("list");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject job=jsonArray.getJSONObject(i);
                                    PlanManage planManage=new PlanManage();
                                    planManage.setPlanName(job.getString("title"));
                                    planManage.setPlanId(job.getString("id"));
                                    if(job.has("state")) {
                                        int state = Integer.valueOf(job.getString("state"));
                                        if (state == 0) {
                                            planManages.add(planManage);
                                        } else {
                                            continue;
                                        }
                                    }
                                }
                            }
                            resultMessage(handler,PLAN_LIST_SUCCESS,planManages,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,PLAN_LIST_FILED,"服务器连接错误",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler,PLAN_LIST_FILED,msg,errorcode);
                    }
                });

    }

    public final static int GET_PLAN_DETAIL_SUCCESS=0x6755;
    public final static int GET_PLAN_DETAIL_FAIL=0x6756;

    /**
     * 获取计划详情
     * @param id
     */
    public void getPlanDetail(String id){
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        KLog.d(map.toString());
        new JSONReauest(context, ConnectionURL.STR_PLAN_DETAIL, MyApplication.CONNECTION_TIME_OUT,map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONObject data=body.getJSONObject("data");
                            PlanManage planManage=new PlanManage();
                            planManage.setPlanId(data.getString("id"));
                            planManage.setPlanName(data.getString("title"));
                            planManage.setPlanStartDate(data.getString("startTime"));
                            planManage.setPlanEndDate(data.getString("endTime"));
                            if(data.has("contractMain")) {
                                planManage.setContractId(data.getJSONObject("contractMain").getString("id"));
                                planManage.setContract(data.getJSONObject("contractMain").getString("name"));
                            }

                            ArrayList<HiddenType> hiddenTypes=new ArrayList<HiddenType>();

                            if(data.getJSONArray("detailList")!=null){
                                JSONArray array=data.getJSONArray("detailList");
                                for(int i=0;i<array.length();i++){
                                    JSONObject job=array.getJSONObject(i);
                                    JSONObject disease=job.getJSONObject("disease");
                                    HiddenType hiddenType=new HiddenType();
                                    hiddenType.setTypeId(job.getString("id"));
                                    hiddenType.setShow(true);
                                    hiddenType.setTypeName(disease.getString("name"));
                                    hiddenType.setNum(Integer.valueOf(job.getString("quantity")));
                                    hiddenType.setTypeUnit(job.getString("measures"));
                                    hiddenTypes.add(hiddenType);
                                }
                            }

                            planManage.setPlanDetails(hiddenTypes);
                            resultMessage(handler,GET_PLAN_DETAIL_SUCCESS,planManage,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,GET_PLAN_DETAIL_FAIL,"服务器连接错误",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler,GET_PLAN_DETAIL_FAIL,msg,errorcode);
                    }
                });
    }


    public static final int ISSUED_PLANTASK_SUCCESS=0x743;
    public static final int ISSUED_PLANTASK_FILED=0x744;

    /**
     * 获取计划列表
     */
    public void issuedPlanTask(String id){
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        KLog.d(map.toString());
        new JSONReauest(context, ConnectionURL.STR_ISSUED_PLAN_TASK, MyApplication.CONNECTION_TIME_OUT,map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        handler.sendEmptyMessage(ISSUED_PLANTASK_SUCCESS);
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,ISSUED_PLANTASK_FILED,"下发任务失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler,ISSUED_PLANTASK_FILED,msg,errorcode);
                    }
                });

    }




}
