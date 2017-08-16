package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.until.ConnectionURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuchang on 2016-12-23.
 */
public class TaskModlue extends BaseModule{

    private Handler handler;
    private Context context;
    public TaskModlue(Handler handler,Context context){
        this.handler=handler;
        this.context=context;
    }
    public final static int INT_SAVE_DEILAY_TASK_SUCCESS=0x70;
    public final static int INT_SAVE_DEILAY_TASK_FILED=0x71;
    /**
     * 保存日常任务
     * @param title
     * @param content
     * @param contact
     * @param contract
     * @param date
     * @param troubleid
     */
    public void saveDeilayTask(final String title, final String content, final String contact, final String contract, final String date, final String troubleid, final List<String> hiddenId, final List<String> hiddenNum){
        Map<String,String> map=new HashMap<String, String>();
        map.put("title",title);
        map.put("description",content);
        map.put("header.id",contact);
        map.put("contractMain.id",contract);
        map.put("maintenanceTime",date);
        map.put("accidentMain.id",troubleid);
        map.put("reworkNumber","0");
        if(hiddenId!=null){
            for(int i=0;i<hiddenId.size();i++){
                map.put("dailyMaintenanceDetailList["+i+"].num",hiddenNum.get(i));
                map.put("dailyMaintenanceDetailList["+i+"].unit.id",hiddenId.get(i));
            }
        }
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_SAVE_DEILAY_TASK, MyApplication.CONNECTION_TIME_OUT, map
                , new JSONReauest.ResultListener() {
            @Override
            public void Success(JSONObject result) {
                KLog.json(result.toString());
                try {
                    JSONObject body=result.getJSONObject("body");
                    resultMessage(handler,INT_SAVE_DEILAY_TASK_SUCCESS,body.getString("id"),-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Error(VolleyError volleyError) {
                resultMessage(handler,INT_SAVE_DEILAY_TASK_FILED,"服务器连接失败",-1);
            }

            @Override
            public void Filed(String msg,int error) {
                resultMessage(handler,INT_SAVE_DEILAY_TASK_FILED,msg,error);
            }
        });
    }



    public final static int INT_SAVE_SPECIAL_TASK_SUCCESS=0x38;
    public final static int INT_SAVE_SPECIAL_TASK_FILED=0x39;
    /**
     * 保存专项任务
     * @param title
     * @param content
     * @param contact
     * @param date
     * @param troubleid
     */
    public void saveSpecialTask(final String title, final String content, final String contact, final String date, final String troubleid, final List<String> hiddenId, final List<String> hiddenNum){
        Map<String,String> map=new HashMap<String, String>();
        map.put("title",title);
        map.put("description",content);
        map.put("header.id",contact);
        map.put("maintenanceTime",date);
        map.put("accidentMain.id",troubleid);
        map.put("reworkNumber","0");
        if(hiddenId!=null){
            for(int i=0;i<hiddenId.size();i++){
                map.put("dailyMaintenanceDetailList["+i+"].num",hiddenNum.get(i));
                map.put("dailyMaintenanceDetailList["+i+"].unit.id",hiddenId.get(i));
            }
        }
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_SAVE_SPECIAL_TASK, MyApplication.CONNECTION_TIME_OUT, map
                , new JSONReauest.ResultListener() {
            @Override
            public void Success(JSONObject result) {
                KLog.json(result.toString());
                try {
                    JSONObject body=result.getJSONObject("body");
                    resultMessage(handler,INT_SAVE_SPECIAL_TASK_SUCCESS,body.getString("id"),-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Error(VolleyError volleyError) {
                resultMessage(handler,INT_SAVE_SPECIAL_TASK_FILED,"服务器连接失败",-1);
            }

            @Override
            public void Filed(String msg,int error) {
                resultMessage(handler,INT_SAVE_SPECIAL_TASK_FILED,msg,error);
            }
        });
    }

    public final static int INT_GET_SPECIAL_LIST_SUCCESS=0x40;
    public final static int INT_GET_SPECIAL_LIST_FILED=0x41;
    /**
     * 获取专项列表
     * @param pageNo
     * @param pageSize
     * @param state （0：审核中;1：待完成;2：已完成）
     */
    public void getSpecialList(int pageNo,int pageSize,String state){
        Map<String,String> map=new HashMap<String,String>();
        map.put("pageNo",String.valueOf(pageNo));
        map.put("pageSize",String.valueOf(pageSize));
        map.put("state",state);
        KLog.d(map.toString());

        new JSONReauest(context,ConnectionURL.STR_GET_SPECIAL_TASK_LIST, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONArray list=body.getJSONArray("list");
                            ArrayList<Task> tasks=new ArrayList<Task>();
                            if(list!=null&&list.length()>0){
                                for(int i=0;i<list.length();i++){
                                    Task task=new Task();
                                    JSONObject jb=list.getJSONObject(i);
                                    task.setTaskName(jb.getString("title"));
                                    task.setTaskContent(jb.getString("description"));
                                    task.setDate(jb.getString("maintenanceTime"));
                                    task.setTaskId(jb.getString("id"));
                                    tasks.add(task);
                                }
                            }
                            resultMessage(handler,INT_GET_SPECIAL_LIST_SUCCESS,tasks,-1);
                        } catch (JSONException e) {
                            resultMessage(handler,INT_GET_SPECIAL_LIST_FILED,e.getMessage(),-1);
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_SPECIAL_LIST_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_GET_SPECIAL_LIST_FILED,msg,error);
                    }
                });
    }

    public final static int INT_GET_DEILAY_LIST_SUCCESS=0x42;
    public final static int INT_GET_DEILAY_LIST_FILED=0x43;

    /**
     * 获取日常列表
     * @param pageNo
     * @param pageSize
     * @param state
     */
    public void getDeliayList(int pageNo,int pageSize,String state){
        Map<String,String> map=new HashMap<String,String>();
        map.put("pageNo",String.valueOf(pageNo));
        map.put("pageSize",String.valueOf(pageSize));
        map.put("state",state);
        KLog.d(map.toString());

        new JSONReauest(context,ConnectionURL.STR_GET_DEILAY_TASK_LIST, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONArray list=body.getJSONArray("list");
                            ArrayList<Task> tasks=new ArrayList<Task>();
                            if(list!=null&&list.length()>0){
                                for(int i=0;i<list.length();i++){
                                    Task task=new Task();
                                    HiddenTroubleDetail htd=new HiddenTroubleDetail();
                                    JSONObject jb=list.getJSONObject(i);
                                    KLog.d(jb.toString());
                                    if(jb.has("accidentMain")) {
                                        JSONObject acc = jb.getJSONObject("accidentMain");
                                        task.setTroubleId(acc.getString("id"));
                                        htd.setQuantity(acc.optString("quantity"));
                                        htd.setUnit(acc.optString("unit"));
                                        if(acc.has("diseaseStock")){
                                            JSONObject st=acc.getJSONObject("diseaseStock");
                                            htd.setStockName(st.optString("name"));
                                        }
                                    }
                                    task.setHiddenTroubleDetail(htd);
//                                    task.setTaskName(jb.getString("title"));
                                    if(jb.isNull("description")) continue;
                                    task.setTaskContent(jb.optString("description"));
                                    if(!jb.isNull("tuser")) {
                                        JSONObject contact = jb.getJSONObject("tuser");
                                        task.setContactId(contact.getString("id"));
//                                    task.setContactName(contact.getString("name"));
                                    }
                                    if(jb.has("maintenanceTime")) {
                                        task.setDate(jb.optString("maintenanceTime"));
                                    }else {
                                        continue;
                                    }
                                    if(jb.has("contractMain")) {
                                        JSONObject contrac = jb.getJSONObject("contractMain");
                                        task.setContractId(contrac.getString("id"));
                                        task.setContractName(contrac.optString("name"));
                                    }
                                    if(!jb.isNull("maintenanceState")) {
                                        task.setMaintenanceState(Integer.valueOf(jb.optString("maintenanceState")));
                                    }
                                    if(task.getMaintenanceState()==4)continue;
                                    task.setTaskId(jb.getString("id"));
                                    tasks.add(task);
                                }
                            }
                            resultMessage(handler,INT_GET_DEILAY_LIST_SUCCESS,tasks,-1);
                        } catch (JSONException e) {
                            resultMessage(handler,INT_GET_DEILAY_LIST_FILED,e.getMessage(),-1);
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_DEILAY_LIST_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_GET_DEILAY_LIST_FILED,msg,error);
                    }
                });
    }

    /**
     * 获取日常列表
     * @param pageNo
     * @param pageSize
     * @param state
     */
    public void getDeliayFinishList(int pageNo,int pageSize,String state){
        Map<String,String> map=new HashMap<String,String>();
        map.put("pageNo",String.valueOf(pageNo));
        map.put("pageSize",String.valueOf(pageSize));
        map.put("state",state);
        KLog.d(map.toString());
//       STR_GET_DEILAY_TASK_LIST STR_FINISH_APPROVE
        new JSONReauest(context,ConnectionURL.STR_GET_DEILAY_TASK_LIST, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONArray list=body.getJSONArray("list");
                            ArrayList<Task> tasks=new ArrayList<Task>();
                            if(list!=null&&list.length()>0){
                                for(int i=0;i<list.length();i++){
                                    Task task=new Task();
                                    HiddenTroubleDetail htd=new HiddenTroubleDetail();
                                    JSONObject jb=list.getJSONObject(i);
                                    if(jb.has("accidentMain")) {
                                        JSONObject acc = jb.getJSONObject("accidentMain");
                                        task.setTroubleId(acc.getString("id"));
                                        htd.setQuantity(acc.optString("quantity"));
                                        htd.setUnit(acc.optString("unit"));
                                        if(!acc.isNull("diseaseStock")){
                                            JSONObject st=acc.getJSONObject("diseaseStock");
                                            htd.setStockName(st.optString("name"));
                                        }
                                    }
                                    task.setHiddenTroubleDetail(htd);
//                                    task.setTaskName(jb.getString("title"));
                                    if(jb.isNull("description")) continue;
                                    task.setTaskContent(jb.optString("description"));
                                    if(!jb.isNull("tuser")) {
                                        JSONObject contact = jb.getJSONObject("tuser");
                                        task.setContactId(contact.getString("id"));
//                                    task.setContactName(contact.getString("name"));
                                    }
                                    if(jb.has("maintenanceTime")) {
                                        task.setDate(jb.optString("maintenanceTime"));
                                    }else {
                                        continue;
                                    }
                                    if(jb.has("contractMain")) {
                                        JSONObject contrac = jb.getJSONObject("contractMain");
                                        task.setContractId(contrac.getString("id"));
                                        task.setContractName(contrac.optString("name"));
                                    }
                                    if(!jb.isNull("maintenanceState")) {
                                        task.setMaintenanceState(Integer.valueOf(jb.optString("maintenanceState")));
                                    }
                                    if(task.getMaintenanceState()==4)continue;
                                    task.setTaskId(jb.getString("id"));
                                    tasks.add(task);
                                }
                            }
                            resultMessage(handler,INT_GET_DEILAY_LIST_SUCCESS,tasks,-1);
                        } catch (JSONException e) {
                            resultMessage(handler,INT_GET_DEILAY_LIST_FILED,e.getMessage(),-1);
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_DEILAY_LIST_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_GET_DEILAY_LIST_FILED,msg,error);
                    }
                });
    }

    /**
     * 获取计划维修
     * @param pageNo
     * @param pageSize
     * @param state
     */
    public void getPlanMainList(int pageNo,int pageSize,String state){
        Map<String,String> map=new HashMap<String,String>();
        map.put("pageNo",String.valueOf(pageNo));
        map.put("pageSize",String.valueOf(pageSize));
        map.put("maintenanceState",state);
        KLog.d(map.toString());

        new JSONReauest(context,ConnectionURL.STR_GET_DEILAY_TASK_LIST, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONArray list=body.getJSONArray("list");
                            ArrayList<Task> tasks=new ArrayList<Task>();
                            if(list!=null&&list.length()>0){
                                for(int i=0;i<list.length();i++){
                                    Task task=new Task();
                                    JSONObject jb=list.getJSONObject(i);
                                    KLog.d(jb.toString());
                                    if(jb.has("accidentMain")) {
                                        JSONObject acc = jb.getJSONObject("accidentMain");
                                        task.setTroubleId(acc.getString("id"));
                                    }
//                                    task.setTaskName(jb.getString("title"));
                                    if(jb.isNull("description")) continue;
                                    task.setTaskContent(jb.optString("description"));
                                    if(!jb.isNull("tuser")) {
                                        JSONObject contact = jb.getJSONObject("tuser");
                                        task.setContactId(contact.getString("id"));
                                        task.setContactName(contact.optString("name"));
                                    }
                                    task.setDate(jb.optString("maintenanceTime"));
                                    if(jb.has("contractMain")) {
                                        JSONObject contrac = jb.getJSONObject("contractMain");
                                        KLog.d(contrac.toString());
                                        task.setContractId(contrac.getString("id"));
                                        task.setContractName(contrac.optString("name"));
                                    }
                                    if(!jb.isNull("maintenanceState")) {
                                        task.setMaintenanceState(Integer.valueOf(jb.optString("maintenanceState")));
                                    }
                                    if(task.getMaintenanceState()==4) {
                                        task.setTaskId(jb.getString("id"));
                                        tasks.add(task);
                                    }else {
                                        continue;
                                    }
                                }
                            }
                            resultMessage(handler,INT_GET_SPECIAL_LIST_SUCCESS,tasks,-1);
                        } catch (JSONException e) {
                            resultMessage(handler,INT_GET_SPECIAL_LIST_FILED,e.getMessage(),-1);
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_SPECIAL_LIST_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_GET_SPECIAL_LIST_FILED,msg,error);
                    }
                });
    }

    /**
     * 获取计划维修已完成
     * @param pageNo
     * @param pageSize
     * @param state
     */
    public void getPlanMainFinishList(int pageNo,int pageSize,String state){
        Map<String,String> map=new HashMap<String,String>();
//        map.put("pageNo",String.valueOf(pageNo));
//        map.put("pageSize",String.valueOf(pageSize));
//        map.put("maintenanceState",state);
        KLog.d(map.toString());

        new JSONReauest(context,ConnectionURL.STR_GET_DEILAY_TASK_LIST, MyApplication.CONNECTION_TIME_OUT,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONArray list=body.getJSONArray("list");
                            ArrayList<Task> tasks=new ArrayList<Task>();
                            if(list!=null&&list.length()>0){
                                for(int i=0;i<list.length();i++){
                                    Task task=new Task();
                                    JSONObject jb=list.getJSONObject(i);
                                    if(jb.has("accidentMain")) {
                                        JSONObject acc = jb.getJSONObject("accidentMain");
                                        task.setTroubleId(acc.getString("id"));
                                    }
                                    task.setTaskContent(jb.optString("description"));
                                    if(!jb.isNull("tuser")) {
                                        JSONObject contact = jb.getJSONObject("tuser");
                                        task.setContactId(contact.getString("id"));
                                        task.setContactName(contact.optString("name"));
                                    }
                                    task.setDate(jb.optString("maintenanceTime"));
                                    if(jb.has("contractMain")) {
                                        JSONObject contrac = jb.getJSONObject("contractMain");
                                        task.setContractId(contrac.getString("id"));
                                        task.setContractName(contrac.optString("name"));
                                    }
                                    if(!jb.isNull("maintenanceState")) {
                                        task.setMaintenanceState(Integer.valueOf(jb.optString("maintenanceState")));
                                    }
                                    boolean ih=(task.getMaintenanceState()==3&&!jb.isNull("dailyMaintenanceDetailList"));
                                    if(ih) {
                                        if(jb.getJSONArray("dailyMaintenanceDetailList").length()>0) {
                                            KLog.d(jb.getJSONArray("dailyMaintenanceDetailList").toString());
                                            task.setTaskId(jb.getString("id"));
                                            tasks.add(task);
                                        }
                                    }else {
                                        task=null;
                                    }
                                }
                            }
                            resultMessage(handler,INT_GET_SPECIAL_LIST_SUCCESS,tasks,-1);
                        } catch (JSONException e) {
                            resultMessage(handler,INT_GET_SPECIAL_LIST_FILED,e.getMessage(),-1);
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_SPECIAL_LIST_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_GET_SPECIAL_LIST_FILED,msg,error);
                    }
                });
    }


    public final static int INT_DEILAY_UPLOAD_SUCCESS=0x44;
    public final static int INT_DEILAY_UPLOAD_FILED=0x45;
    /**
     * 日常任务上报
     */
    public void saveDeilayTaskUpload(Map<String,String> map){
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_SAVE_DEILAY_TASK_REPORT, MyApplication.CONNECTION_TIME_OUT, map
                , new JSONReauest.ResultListener() {
            @Override
            public void Success(JSONObject result) {
                KLog.json(result.toString());
                try {
                    JSONObject jb=result.getJSONObject("body");
                    resultMessage(handler,INT_DEILAY_UPLOAD_SUCCESS,jb.getString("id"),-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Error(VolleyError volleyError) {
                KLog.d(volleyError.getMessage());
                resultMessage(handler,INT_DEILAY_UPLOAD_FILED,"服务器连接错误",-1);
            }

            @Override
            public void Filed(String msg,int error) {
                resultMessage(handler,INT_DEILAY_UPLOAD_FILED,msg,error);
            }
        });
    }


    public final static int INT_SPECIAL_UPLOAD_SUCCESS=0x46;
    public final static int INT_SPECIAL_UPLOAD_FILED=0x47;
    /**
     * 专项任务上传
     */
    public void saveSpecialTaskUpload(Map<String,String> map){
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_SAVE_SPECIAL_TASK_REPORT, MyApplication.CONNECTION_TIME_OUT, map
                , new JSONReauest.ResultListener() {
            @Override
            public void Success(JSONObject result) {
                KLog.json(result.toString());
                try {
                    JSONObject jb=result.getJSONObject("body");
                    resultMessage(handler,INT_SPECIAL_UPLOAD_SUCCESS,jb.getString("id"),-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Error(VolleyError volleyError) {

            }

            @Override
            public void Filed(String msg,int error) {
                resultMessage(handler,INT_SPECIAL_UPLOAD_FILED,msg,error);
            }
        });
    }

    public final static int INT_DEILAY_REPORT_DETAIL_SUCCESS=0x48;
    public final static int INT_DEILAY_REPORT_DETAIL_FILED=0x49;
    /**
     * 获取日常维修的上报的详情
     * @param id
     */
    public void getDeilayReportDetail(String id){
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        new JSONReauest(context,ConnectionURL.STR_SAVE_DEILAY_TASK_REPORT_DETAIL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                    }

                    @Override
                    public void Error(VolleyError volleyError) {

                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_DEILAY_REPORT_DETAIL_FILED,msg,error);
                    }
                });

    }

    public final static int INT_SPECIAL_REPORT_DETAIL_SUCCESS=0x50;
    public final static int INT_SPECIAL_REPORT_DETAIL_FILED=0x51;
    /**
     * 获取专项维修的上报的详情
     * @param id
     */
    public void getSpecialReportDetail(String id){
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        new JSONReauest(context,ConnectionURL.STR_SAVE_SPECIAL_TASK_DETAIL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                    }

                    @Override
                    public void Error(VolleyError volleyError) {

                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_SPECIAL_REPORT_DETAIL_FILED,msg,error);
                    }
                });

    }


    public final static int INT_DEILAY_DETAIL_SUCCESS=0x52;
    public final static int INT_DEILAY_DETAIL_FILED=0x53;
    /**
     * 获取日常维修详情
     * @param id
     */
    public void getDeilayDetail(String id){
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_GET_DEILAY_TASK_DETAIL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject data=result.getJSONObject("body").getJSONObject("data");
                            Task task=new Task();
                            HiddenTroubleDetail hidden = new HiddenTroubleDetail();
                            if (!data.isNull("accidentMain")) {
                                JSONObject acc = data.getJSONObject("accidentMain");
                                hidden.setTroubleId(acc.getString("id"));
                                hidden.setState(acc.getString("state"));
                                hidden.setContent(acc.getString("description"));
                                hidden.setQuantity(acc.getString("quantity"));
                                hidden.setUnit(acc.getString("unit"));
                                hidden.setType(acc.getJSONObject("diseaseType").getString("id"));
                                hidden.setTypeName(acc.getJSONObject("diseaseType").getString("name"));
                                hidden.setNameId(acc.getJSONObject("diseaseName").getString("id"));
                                hidden.setName(acc.getJSONObject("diseaseName").getString("name"));

                                if(!acc.isNull("diseaseStock")) {
                                    hidden.setStockId(acc.getJSONObject("diseaseStock").getString("id"));
                                    hidden.setStockName(acc.getJSONObject("diseaseStock").getString("name"));
                                }
                                hidden.setProcInsId(acc.getJSONObject("act").getString("procInsId"));
                                hidden.setInspectionName(acc.getJSONObject("createBy").getString("name"));
                                hidden.setDate(acc.getString("createDate"));
                            }

                            if(!data.isNull("contractMain")){
                                ContractManager manager=new ContractManager();
                                manager.setContractId(data.getJSONObject("contractMain").getString("id"));
                            }

                            task.setContactName(data.getJSONObject("tuser").getString("name"));
                            task.setContactId(data.getJSONObject("tuser").getString("id"));
                            if(!data.isNull("maintenanceState")) {
                                KLog.d(data.getString("maintenanceState"));
                                task.setMaintenanceState(Integer.valueOf(data.getString("maintenanceState")));
                            }
                            if(!data.isNull("yearMonth")) {
                                task.setYearMonth(Integer.valueOf(data.getString("yearMonth")));
                            }
                            task.setHiddenTroubleDetail(hidden);
                            task.setTaskId(data.getString("id"));
                            task.setTaskContent(data.getString("description"));
                            task.setDate(data.optString("maintenanceTime"));
                            task.setStatus(Integer.valueOf(data.getString("state")));

                            ArrayList<String> fuj=new ArrayList<String>();
                            if(data.has("programDescription")){
                                task.setReportContent(data.getString("programDescription"));
                            }
                            if(!data.isNull("dailyMaintenanceProgramList")){
                                task.setMaintenace(false);
                                JSONArray fj=data.getJSONArray("dailyMaintenanceProgramList");
                                for(int i=0;i<fj.length();i++){
                                    KLog.d(fj.getJSONObject(i).getString("path"));
                                    fuj.add(fj.getJSONObject(i).getString("path"));
                                    task.setReportContent(fj.getJSONObject(i).optString("description"));
                                }
                            }else {
                                task.setMaintenace(true);
                            }
                            task.setFujian(fuj);

                            resultMessage(handler,INT_DEILAY_DETAIL_SUCCESS,task,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_DEILAY_DETAIL_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_DEILAY_DETAIL_FILED,msg,error);
                    }
                });

    }

    /**
     * 获取计划维修详情
     * @param id
     */
    public void getPlanMainDetail(String id){
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_GET_DEILAY_TASK_DETAIL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject data=result.getJSONObject("body").getJSONObject("data");
                            Task task=new Task();
                            HiddenTroubleDetail hidden = new HiddenTroubleDetail();
                            if (!data.isNull("accidentMain")) {
                                JSONObject acc = data.getJSONObject("accidentMain");
                                hidden.setTroubleId(acc.getString("id"));
                                hidden.setState(acc.getString("state"));
                                hidden.setContent(acc.getString("description"));
                                hidden.setQuantity(acc.getString("quantity"));
                                hidden.setUnit(acc.getString("unit"));
                                hidden.setType(acc.getJSONObject("diseaseType").getString("id"));
                                hidden.setTypeName(acc.getJSONObject("diseaseType").getString("name"));
                                hidden.setNameId(acc.getJSONObject("diseaseName").getString("id"));
                                hidden.setName(acc.getJSONObject("diseaseName").getString("name"));

                                if(!acc.isNull("diseaseStock")) {
                                    hidden.setStockId(acc.getJSONObject("diseaseStock").getString("id"));
                                    hidden.setStockName(acc.getJSONObject("diseaseStock").getString("name"));
                                }
                                hidden.setProcInsId(acc.getJSONObject("act").getString("procInsId"));
                                hidden.setInspectionName(acc.getJSONObject("createBy").getString("name"));
                                hidden.setDate(acc.getString("createDate"));
                            }

                            if(!data.isNull("contractMain")){
                                ContractManager manager=new ContractManager();
                                manager.setContractId(data.getJSONObject("contractMain").getString("id"));
                            }

                            task.setContactName(data.getJSONObject("tuser").getString("name"));
                            task.setContactId(data.getJSONObject("tuser").getString("id"));
                            if(!data.isNull("maintenanceState")) {
                                KLog.d(data.getString("maintenanceState"));
                                task.setMaintenanceState(Integer.valueOf(data.getString("maintenanceState")));
                            }
                            if(!data.isNull("yearMonth")) {
                                task.setYearMonth(Integer.valueOf(data.getString("yearMonth")));
                            }
                            task.setHiddenTroubleDetail(hidden);
                            task.setTaskId(data.getString("id"));
                            task.setTaskContent(data.getString("description"));
                            task.setDate(data.optString("maintenanceTime"));
                            task.setStatus(Integer.valueOf(data.getString("state")));

                            ArrayList<HiddenType> hiddenTypes=new ArrayList<HiddenType>();
                            if(!data.isNull("dailyMaintenanceDetailList")){
                                JSONArray jsonArray=data.getJSONArray("dailyMaintenanceDetailList");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jb=jsonArray.getJSONObject(i);
                                    HiddenType ht=new HiddenType();
                                    ht.setTypeName(jb.getString("name"));
                                    ht.setTroubleContent(jb.getString("num"));
                                    ht.setTypeUnit(jb.getString("unitstr"));
                                    ht.setShow(true);
                                    ht.setTypeId(jb.getString("id"));
                                    hiddenTypes.add(ht);
                                }
                            }
                            task.setHidden(hiddenTypes);
                            /////
                            resultMessage(handler,INT_DEILAY_DETAIL_SUCCESS,task,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_DEILAY_DETAIL_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_DEILAY_DETAIL_FILED,msg,error);
                    }
                });

    }

    public final static int INT_SPECIAL_DETAIL_SUCCESS=0x54;
    public final static int INT_SPECIAL_DETAIL_FILED=0x55;
    /**
     * 获取专项维修详情
     * @param id
     */
    public void getSpecialDetail(String id){
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        KLog.d(id);
        new JSONReauest(context,ConnectionURL.STR_GET_SPECIAL_TASK_DETAIL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject data=result.getJSONObject("body").getJSONObject("data");
                            Task task=new Task();
                            if(data.has("accidentMain")) {
                                JSONObject acc = data.getJSONObject("accidentMain");
                                if (acc != null) {
                                    HiddenTroubleDetail hidden = new HiddenTroubleDetail();
                                    hidden.setTroubleId(acc.getString("id"));
                                    hidden.setTitle(acc.getString("title"));
                                    hidden.setLatitude(acc.getDouble("latitude"));
                                    hidden.setLongitude(acc.getDouble("longtitude"));
                                    hidden.setState(acc.getString("state"));
                                    hidden.setContent(acc.getString("description"));
                                    ArrayList<String> img = new ArrayList<String>();
                                    JSONArray array = acc.getJSONArray("accidentAttachmentList");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jb = array.getJSONObject(i);
                                        img.add(jb.getString("path"));
                                    }
                                    hidden.setImage(img);
                                    List<HiddenType> hiddenTypes = new ArrayList<HiddenType>();
                                    JSONArray jsonArray = acc.getJSONArray("accidentDetailList");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        HiddenType hiddenType = new HiddenType();
                                        JSONObject jht = jsonArray.getJSONObject(j);
                                        hiddenType.setNum(jht.getDouble("num"));
                                        hiddenType.setTroubleContent(String.valueOf(jht.getDouble("num")));
                                        hiddenType.setTypeName(jht.getJSONObject("unit").getString("name"));
                                        hiddenType.setTypeUnit(jht.getJSONObject("unit").getString("measures"));
                                        hiddenType.setShow(true);
                                        hiddenTypes.add(hiddenType);
                                    }
                                    hidden.setHiddenTypes(hiddenTypes);
                                    hidden.setProcInsId(acc.getJSONObject("act").getString("procInsId"));
                                    hidden.setInspectionName(acc.getJSONObject("createBy").getString("name"));
                                    hidden.setDate(acc.getString("createDate"));
                                    task.setHiddenTroubleDetail(hidden);
                                }
                            }

                            task.setTaskId(data.getString("id"));
                            task.setTaskName(data.getString("title"));
                            task.setTaskContent(data.getString("description"));
                            JSONObject header=data.getJSONObject("header");
                            task.setContactId(header.getString("id"));
                            task.setContactName(header.getString("name"));
                            task.setDate(data.getString("maintenanceTime"));

                            task.setStatus(Integer.valueOf(data.getString("state")));
                            JSONArray attach=data.getJSONArray("specialMaintenanceAttachmentList");
                            ArrayList<String> attachs=new ArrayList<String>();
                            if(attach!=null&&attach.length()>0){
                                for(int i=0;i<attach.length();i++){
                                    JSONObject job=attach.getJSONObject(i);
                                    attachs.add(job.getString("path"));
                                }
                            }
                            task.setFujian(attachs);
                            JSONArray detail=data.getJSONArray("specialMaintenanceDetailList");
                            ArrayList<HiddenType> hiddent=new ArrayList<HiddenType>();
                            if(detail!=null&&detail.length()>0){
                                for(int j=0;j<detail.length();j++){
                                    HiddenType hiddenType=new HiddenType();
                                    JSONObject jht=detail.getJSONObject(j);
                                    hiddenType.setNum(jht.getDouble("num"));
                                    hiddenType.setTroubleContent(String.valueOf(jht.getDouble("num")));
                                    hiddenType.setTypeName(jht.getJSONObject("unit").getString("name"));
                                    hiddenType.setTypeUnit(jht.getJSONObject("unit").getString("measures"));
                                    hiddenType.setShow(true);
                                    hiddent.add(hiddenType);
                                }
                            }
                            task.setHidden(hiddent);

                            /////
                            resultMessage(handler,INT_SPECIAL_DETAIL_SUCCESS,task,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_SPECIAL_DETAIL_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg,int error) {
                        resultMessage(handler,INT_SPECIAL_DETAIL_FILED,msg,error);
                    }
                });

    }

    public final static int INT_DEILAY_HAS_REPORTED_TRUE=0x15;
    public final static int INT_DEILAY_HAS_REPORTED_FALSE=0x16;

    /**
     * 日常上报详情
     * @param id
     */
    public void getDeilayHasReported(String id){
        Map<String,String> map=new HashMap<String,String>();
        map.put("dailyMaintenanceMain.id",id);
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_SAVE_DEILAY_TASK_IS_REPORTL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject data=result.getJSONObject("body").getJSONObject("data");
                            Task task=new Task();
                            if(data.has("accidentMain")) {
                                JSONObject acc = data.getJSONObject("accidentMain");
                                if (acc != null) {
                                    HiddenTroubleDetail hidden = new HiddenTroubleDetail();
                                    hidden.setTroubleId(acc.getString("id"));
                                    hidden.setTitle(acc.getString("title"));
                                    hidden.setLatitude(acc.getDouble("latitude"));
                                    hidden.setLongitude(acc.getDouble("longtitude"));
                                    hidden.setState(acc.getString("state"));
                                    hidden.setContent(acc.getString("description"));
                                    ArrayList<String> img = new ArrayList<String>();
                                    JSONArray array = acc.getJSONArray("accidentAttachmentList");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jb = array.getJSONObject(i);
                                        img.add(jb.getString("path"));
                                    }
                                    hidden.setImage(img);
                                    List<HiddenType> hiddenTypes = new ArrayList<HiddenType>();
                                    JSONArray jsonArray = acc.getJSONArray("accidentDetailList");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        HiddenType hiddenType = new HiddenType();
                                        JSONObject jht = jsonArray.getJSONObject(j);
                                        hiddenType.setNum(jht.getDouble("num"));
                                        hiddenType.setTroubleContent(String.valueOf(jht.getDouble("num")));
                                        hiddenType.setTypeName(jht.getJSONObject("unit").getString("name"));
                                        hiddenType.setTypeUnit(jht.getJSONObject("unit").getString("measures"));
                                        hiddenType.setShow(true);
                                        hiddenTypes.add(hiddenType);
                                    }
                                    hidden.setHiddenTypes(hiddenTypes);
                                    hidden.setProcInsId(acc.getJSONObject("act").getString("procInsId"));
                                    hidden.setInspectionName(acc.getJSONObject("createBy").getString("name"));
                                    hidden.setDate(acc.getString("createDate"));
                                    task.setHiddenTroubleDetail(hidden);
                                }
                            }

                            task.setTaskId(data.getString("id"));
//                            task.setTaskName(data.getString("title"));\
                            if(data.has("description")) {
                                task.setTaskContent(data.getString("description"));
                            }
//                            JSONObject header=data.getJSONObject("header");
//                            task.setContactId(header.getString("id"));
//                            task.setContactName(header.getString("name"));
                            task.setDate(data.getString("createDate"));

                            JSONArray attach=data.getJSONArray("dailyMaintenanceReportAttachmentList");
                            ArrayList<String> attachs=new ArrayList<String>();
                            if(attach!=null&&attach.length()>0){
                                for(int i=0;i<attach.length();i++){
                                    JSONObject job=attach.getJSONObject(i);
                                    attachs.add(job.getString("path"));
                                }
                            }
                            task.setFujian(attachs);
                            JSONArray detail=data.getJSONArray("dailyMaintenanceReportDetailList");
                            ArrayList<HiddenType> hiddent=new ArrayList<HiddenType>();
                            if(detail!=null&&detail.length()>0){
                                for(int j=0;j<detail.length();j++){
                                    HiddenType hiddenType=new HiddenType();
                                    JSONObject jht=detail.getJSONObject(j);
                                    hiddenType.setTypeId(jht.getString("id"));
                                    hiddenType.setNum(jht.getDouble("num"));
                                    hiddenType.setTroubleContent(String.valueOf(jht.getDouble("num")));
//                                    hiddenType.setTypeName(jht.getJSONObject("unit").optString("name"));
//                                    hiddenType.setTypeUnit(jht.getJSONObject("unit").optString("measures"));
                                    hiddenType.setShow(true);
                                    hiddent.add(hiddenType);
                                }
                            }
                            task.setHidden(hiddent);
                            task.setContactId(data.getJSONObject("createBy").getString("id"));
                            /////
                            resultMessage(handler,INT_DEILAY_HAS_REPORTED_TRUE,task,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {

                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler,INT_DEILAY_HAS_REPORTED_FALSE,"",errorcode);
                    }
                });

    }

    public final static int INT_SPECIAL_HAS_REPORTED_TRUE=0x3562;
    public final static int INT_SPECIAL_HAS_REPORTED_FALSE=0x3563;

    /**
     * 专项上报详情
     * @param id
     */
    public void getSpecialHasReported(String id){
        Map<String,String> map=new HashMap<String,String>();
        map.put("specialMaintenanceMain.id",id);
        map.put("title","");
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_SAVE_SPECIAL_TASK_IS_REPORTL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject data=result.getJSONObject("body").getJSONObject("data");
                            Task task=new Task();
                            if(data.has("accidentMain")) {
                                JSONObject acc = data.getJSONObject("accidentMain");
                                if (acc != null) {
                                    HiddenTroubleDetail hidden = new HiddenTroubleDetail();
                                    hidden.setTroubleId(acc.getString("id"));
                                    hidden.setTitle(acc.getString("title"));
                                    hidden.setLatitude(acc.getDouble("latitude"));
                                    hidden.setLongitude(acc.getDouble("longtitude"));
                                    hidden.setState(acc.getString("state"));
                                    hidden.setContent(acc.getString("description"));
                                    ArrayList<String> img = new ArrayList<String>();
                                    JSONArray array = acc.getJSONArray("accidentAttachmentList");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jb = array.getJSONObject(i);
                                        img.add(jb.getString("path"));
                                    }
                                    hidden.setImage(img);
                                    List<HiddenType> hiddenTypes = new ArrayList<HiddenType>();
                                    JSONArray jsonArray = acc.getJSONArray("accidentDetailList");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        HiddenType hiddenType = new HiddenType();
                                        JSONObject jht = jsonArray.getJSONObject(j);
                                        hiddenType.setTypeId(jht.getString("id"));
                                        hiddenType.setNum(jht.getDouble("num"));
                                        hiddenType.setTroubleContent(String.valueOf(jht.getDouble("num")));
                                        hiddenType.setTypeName(jht.getJSONObject("unit").getString("name"));
                                        hiddenType.setTypeUnit(jht.getJSONObject("unit").getString("measures"));
                                        hiddenType.setShow(true);
                                        hiddenTypes.add(hiddenType);
                                    }
                                    hidden.setHiddenTypes(hiddenTypes);
                                    hidden.setProcInsId(acc.getJSONObject("act").getString("procInsId"));
                                    hidden.setInspectionName(acc.getJSONObject("createBy").getString("name"));
                                    hidden.setDate(acc.getString("createDate"));
                                    task.setHiddenTroubleDetail(hidden);
                                }
                            }

                            task.setTaskId(data.getString("id"));
                            task.setTaskName(data.optString("title"));
                            task.setTaskContent(data.optString("description"));
                            task.setDate(data.getString("createDate"));

                            JSONArray attach=data.getJSONArray("specialMaintenanceReportAttachmentList");
                            ArrayList<String> attachs=new ArrayList<String>();
                            if(attach!=null&&attach.length()>0){
                                for(int i=0;i<attach.length();i++){
                                    JSONObject job=attach.getJSONObject(i);
                                    attachs.add(job.getString("path"));
                                }
                            }
                            task.setFujian(attachs);
                            JSONArray detail=data.getJSONArray("specialMaintenanceReportDetailList");
                            ArrayList<HiddenType> hiddent=new ArrayList<HiddenType>();
                            if(detail!=null&&detail.length()>0){
                                for(int j=0;j<detail.length();j++){
                                    HiddenType hiddenType=new HiddenType();
                                    JSONObject jht=detail.getJSONObject(j);
                                    hiddenType.setTypeId(jht.getString("id"));
                                    hiddenType.setNum(jht.getDouble("num"));
                                    hiddenType.setTroubleContent(String.valueOf(jht.getDouble("num")));
                                    hiddenType.setTypeName(jht.getJSONObject("unit").getString("name"));
                                    hiddenType.setTypeUnit(jht.getJSONObject("unit").getString("measures"));
                                    hiddenType.setShow(true);
                                    hiddent.add(hiddenType);
                                }
                            }
                            task.setHidden(hiddent);
                            task.setContactId(data.getJSONObject("createBy").getString("id"));
                            /////
                            resultMessage(handler,INT_SPECIAL_HAS_REPORTED_TRUE,task,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {

                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler,INT_SPECIAL_HAS_REPORTED_FALSE,msg,errorcode);
                    }
                });

    }




}
