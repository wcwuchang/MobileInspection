package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.InspectionDetail;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.entity.Task;
import com.tianjin.MobileInspection.entity.Todo;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DialogUnit;
import com.tianjin.MobileInspection.until.FlowUtil;
import com.tianjin.MobileInspection.until.JsonAndMap;
import com.tianjin.MobileInspection.until.SuperScriptUnit;
import com.tianjin.MobileInspection.until.VolleyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 吴昶 on 2016/12/15.
 */
public class TodoModule extends BaseModule {

    public final static int INT_GET_TODO_INSPECTION_SUCCESS=0x5560;
    public final static int INT_GET_TODO_INSPECTION_FILED=0x5561;
    public final static int INT_GET_FORM_URL_SUCCESS=0x5562;
    public final static int INT_GET_FORM_URL_FILED=0x5563;
    public final static int INT_GET_TODO_DETAIL_SUCCESS=0x5564;
    public final static int INT_GET_TODO_DETAIL_FILED=0x5565;
    public final static int INT_GET_CLIAM_SUCCESS=0x5566;
    public final static int INT_GET_CLIAM_FILED=0x5567;

    private Handler handler;

    public TodoModule(Handler handler,Context context){
        this.handler=handler;
        this.context=context;
    }
    /**
     * 获取待办列表
     */
    public void getTodoList(){
        JSONReauest reauest=new JSONReauest(context,ConnectionURL.STR_GET_ACT_TASK_TODO, MyApplication.CONNECTION_TIME_OUT,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            MyApplication.DBM.gettTodo().dropTable();
                            String logName=MyApplication.getStringSharedPreferences("username","");
                            JSONObject body=result.getJSONObject("body");
                            JSONArray jsonArray=body.getJSONArray("list");
                            List<Todo> data=new ArrayList<Todo>();
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jb=jsonArray.getJSONObject(i);
                                Todo todo=new Todo();
                                todo.setTaskId(jb.getString("taskId"));
                                todo.setTaskName(jb.getString("taskName"));
                                todo.setProcInsId(jb.getString("procInsId"));
                                todo.setStatus(FlowUtil.flowStatus(jb.getString("status")));
                                todo.setTaskCreateDate(jb.getString("taskCreateDate"));
                                todo.setTaskDefKey(jb.getString("taskDefKey"));
                                todo.setProcDefId(jb.getString("procDefId"));
                                todo.setProcDefKey(jb.getString("procDefKey"));
                                todo.setTodoType(FlowUtil.todotype(jb.getString("procDefKey")));
                                JSONObject vars=jb.getJSONObject("vars");
                                JSONObject map=vars.getJSONObject("map");
                                if(map==null || map.isNull("title")) continue;
                                    todo.setTitle(map.optString("title"));
                                if(!map.isNull("紧急类型")){
                                    int t=Integer.valueOf(map.getString("紧急类型"));
                                    todo.setTypeName("紧急类型:");
                                    if(t==0){
                                        todo.setType("一般");
                                    }else if(t==1){
                                        todo.setType("紧急");
                                    }else if(t==2){
                                        todo.setType("管理");
                                    }
                                }else  if(!map.isNull("inspecting_man")){
                                    String insp=map.getString("inspecting_man");
                                    if(!insp.equals(logName)){
                                        continue;
                                    }
                                    todo.setTypeName("巡检状态:");
                                    todo.setType(todo.getTaskName());
                                }else {
                                    todo.setType(todo.getTaskName());
                                }
                                data.add(todo);
                            }
                            MyApplication.DBM.gettTodo().insertList(data);
                            SuperScriptUnit.resetBadgeCount(context,data.size());
                            resultMessage(handler,INT_GET_TODO_INSPECTION_SUCCESS,data,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_TODO_INSPECTION_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_GET_TODO_INSPECTION_FILED, msg, errorcode);
                    }
                });
    }

    //签收
    public void claim(String taskId){
        Map<String,String> map=new HashMap<String, String>();
        map.put("taskId",taskId);
        JSONReauest reauest=new JSONReauest(context,ConnectionURL.STR_GET_ACT_TASK_CLAIM, MyApplication.CONNECTION_TIME_OUT,map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        handler.sendEmptyMessage(0x77);
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_CLIAM_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_GET_CLIAM_FILED, msg, errorcode);
                    }
                });
    }

    //待办巡检详情
    public void getTodoInspectionDetail(String url){
        DialogUnit.showDialog(context);
        KLog.d(ConnectionURL.HTTP_SERVER_IP+url + ConnectionURL.HTTP_SERVER_END_Y);
        StringRequest request=new StringRequest(Request.Method.GET, ConnectionURL.HTTP_SERVER_IP+url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogUnit.closeDialog();
                        KLog.json(s);
                        try {
                            JSONObject job = new JSONObject(s);
                            if(job.getBoolean("success")) {
                                InspectionDetail detail = new InspectionDetail();
                                JSONObject body = job.getJSONObject("body");
                                JSONObject data = body.getJSONObject("data");
                                detail.setInspectionId(data.getString("id"));
                                detail.setTitle(data.getString("title"));
                                detail.setContractName(data.getJSONObject("contractMain").getString("name"));
//                                detail.setUnitName(data.getJSONObject("office").getString("name"));
//                                detail.setContactName(data.getJSONObject("tuser").getString("name"));
                                detail.setContent(data.getString("content"));
                                detail.setTraffic(data.getString("inspectingType"));
                                detail.setDate(data.getString("inspectingTime"));
                                detail.setStartTime(data.optString("startTime"));
                                if(data.has("finishTime")) {
                                    detail.setEndTime(data.getString("finishTime"));
                                }
                                detail.setState(data.getString("state"));
                                ArrayList<Road> roads = new ArrayList<Road>();
                                JSONArray array = data.getJSONArray("inspectingRangeList");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jb=array.getJSONObject(i);
                                    Road road=new Road();
                                    road.setRoadName(jb.getJSONObject("road").getString("name"));
                                    road.setRoadId(jb.getJSONObject("road").getString("id"));
                                    KLog.d(road.getRoadName());
                                    roads.add(road);
                                }
                                if(data.has("act")){
                                    JSONObject jact=data.getJSONObject("act");
                                    if(jact.has("taskDefKey")){
                                        detail.setTaskDefKey(jact.getString("taskDefKey"));
                                    }
                                }
                                detail.setRoads(roads);
                                KLog.d(body.getJSONObject("button").toString());
                                KLog.d(JsonAndMap.getMapForJson(body.getJSONObject("button").toString()).toString());
                                detail.setOption(JsonAndMap.getMapForJson(body.getJSONObject("button").toString()).toString());
                                resultMessage(handler,INT_GET_TODO_DETAIL_SUCCESS,detail,-1);
                            }else {
                                String str=job.getString("msg");
                                resultMessage(handler,INT_GET_TODO_DETAIL_FILED,str,Integer.valueOf(job.getString("erroeCode")));
                            }

                        } catch (JSONException e) {
                            KLog.d(e.getMessage());
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogUnit.closeDialog();
                resultMessage(handler,INT_GET_TODO_DETAIL_FILED,"服务器连接失败",-1);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(MyApplication.CONNECTION_TIME_OUT, 1, 1.0f));//自定义超时时间
        VolleyUtil.getRequestQueue().add(request);
    }

    //待办隐患详情
    public void getTodoHiddenTroubleDetail(String url){
        DialogUnit.showDialog(context);
        KLog.d(ConnectionURL.HTTP_SERVER_IP+url + ConnectionURL.HTTP_SERVER_END_Y);
        StringRequest request=new StringRequest(Request.Method.GET, ConnectionURL.HTTP_SERVER_IP+url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        DialogUnit.closeDialog();
                        KLog.json(s);
                        try {
                            JSONObject job = new JSONObject(s);
                            if(job.getBoolean("success")) {
                                HiddenTroubleDetail detail = new HiddenTroubleDetail();
                                JSONObject body = job.getJSONObject("body");
                                JSONObject data = body.getJSONObject("data");
                                detail.setTroubleId(data.getString("id"));
                                detail.setLatitude(data.getDouble("latitude"));
                                detail.setLongitude(data.getDouble("longtitude"));
//                                detail.setTitle(data.getString("title"));
                                detail.setState(data.getString("state"));
                                detail.setContent(data.getString("description"));
                                detail.setEmergencyType(data.getString("emergencyTypeId"));
                                detail.setType(data.getString("typeId"));
                                detail.setRoadId(data.getString("roadId"));
                                detail.setLightId(data.getString("lightId"));
                                detail.setQuantity(data.optString("quantity"));
                                detail.setUnit(data.getString("unit"));
                                detail.setNameId(data.getString("nameId"));
                                detail.setStockId(data.getString("stockId"));
                                ArrayList<String> img=new ArrayList<String>();
                                JSONArray array=data.getJSONArray("accidentAttachmentList");
                                for(int i=0;i<array.length();i++){
                                    JSONObject jb=array.getJSONObject(i);
                                    img.add(jb.getString("path"));
                                }
                                detail.setImage(img);
                                List<HiddenType> hiddenTypes=new ArrayList<HiddenType>();
                                JSONArray jsonArray=data.getJSONArray("accidentDetailList");
                                for(int j=0;j<jsonArray.length();j++){
                                    HiddenType hiddenType=new HiddenType();
                                    JSONObject jht=jsonArray.getJSONObject(j);
                                    hiddenType.setNum(jht.getDouble("num"));
                                    hiddenType.setTroubleContent(String.valueOf(jht.getDouble("num")));
                                    hiddenType.setTypeName(jht.getJSONObject("unit").getString("name"));
                                    hiddenType.setTypeUnit(jht.getJSONObject("unit").getString("measures"));
                                    hiddenType.setShow(true);
                                    hiddenTypes.add(hiddenType);
                                }
                                detail.setHiddenTypes(hiddenTypes);
                                detail.setTaskId(data.getJSONObject("act").getString("taskId"));
                                detail.setTaskDefKey(data.getJSONObject("act").getString("taskDefKey"));
                                detail.setTaskName(data.getJSONObject("act").getString("taskName"));
                                detail.setProcInsId(data.getJSONObject("act").getString("procInsId"));
                                detail.setProcDefId(data.getJSONObject("act").getString("procDefId"));
                                detail.setInspectionName(data.getJSONObject("createBy").getString("name"));
                                detail.setDate(data.getString("createDate"));
                                detail.setOption(JsonAndMap.getMapForJson(body.getJSONObject("button").toString()).toString());
                                resultMessage(handler,INT_GET_TODO_DETAIL_SUCCESS,detail,-1);
                            }else {
                                String str=job.getString("msg");
                                resultMessage(handler,INT_GET_TODO_DETAIL_FILED,str,Integer.valueOf(job.getString("erroeCode")));
                            }

                        } catch (JSONException e) {
                            KLog.d(e.getMessage());
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogUnit.closeDialog();
                resultMessage(handler,INT_GET_TODO_DETAIL_FILED,"服务器连接失败",-1);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(MyApplication.CONNECTION_TIME_OUT, 1, 1.0f));//自定义超时时间
        VolleyUtil.getRequestQueue().add(request);
    }

    public void getGormUrl(String taskId,String taskName,String taskDefKey,String procInsId,String procDefId){
        Map<String,String> map=new HashMap<String, String>();
        map.put("taskId",taskId);
        map.put("taskName",taskName);
        map.put("taskDefKey",taskDefKey);
        map.put("procInsId",procInsId);
        map.put("procDefId",procDefId);
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_GET_FORM_URL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            String url=result.getJSONObject("body").getString("url");
                            resultMessage(handler,INT_GET_FORM_URL_SUCCESS,url,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_FORM_URL_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_GET_FORM_URL_FILED, msg, errorcode);
                    }
                });


    }

    public final static int INT_GET_DEILYDEILY_SUCCESS=0x569;
    public final static int INT_GET_DEILYDEILY_FILED=0x566;
    /**
     * 待办日常详情
     * @param url
     */
    public void getDeilayDetail(String url){

        KLog.d(ConnectionURL.HTTP_SERVER_IP+url);
        DialogUnit.showDialog(context);
        StringRequest request=new StringRequest(Request.Method.GET, ConnectionURL.HTTP_SERVER_IP+url,
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
                                task.setTaskName(data.optString("title"));
                                task.setTaskId(data.getString("id"));
                                task.setTaskContent(data.getString("description"));
                                task.setContactName(data.getJSONObject("header").getString("name"));
                                task.setContactId(data.getJSONObject("header").getString("id"));
                                task.setContractName(data.getJSONObject("contractMain").getString("name"));
                                task.setContractId(data.getJSONObject("contractMain").getString("id"));
                                task.setDate(data.getString("maintenanceTime"));

                                task.setTroubleId(data.getJSONObject("accidentMain").getString("id"));
                                task.setTroubleName(data.getJSONObject("accidentMain").getString("title"));

                                ArrayList<String> fujian = new ArrayList<String>();
                                if (data.has("dailyMaintenanceAttachmentList")) {
                                    JSONArray array = data.getJSONArray("dailyMaintenanceAttachmentList");
                                    for (int i = 0; i < array.length(); i++) {
                                        fujian.add(array.getJSONObject(i).getString("path"));
                                    }
                                }
                                task.setFujian(fujian);

                                ArrayList<HiddenType> hiddenTypes = new ArrayList<HiddenType>();
                                if (data.has("dailyMaintenanceDetailList")) {
                                    JSONArray array = data.getJSONArray("dailyMaintenanceDetailList");
                                    for (int i = 0; i < array.length(); i++) {
                                        HiddenType hiddenType = new HiddenType();
                                        JSONObject jb = array.getJSONObject(i);
                                        hiddenType.setTypeName(jb.getJSONObject("unit").getString("name"));
                                        hiddenType.setTypeUnit(jb.getJSONObject("unit").getString("measures"));
                                        hiddenType.setTroubleContent(jb.getDouble("num")+"");
                                        hiddenType.setShow(true);
                                        hiddenTypes.add(hiddenType);
                                    }
                                }
                                task.setHidden(hiddenTypes);
                                task.setStatus(Integer.valueOf(data.getString("state")));
                                task.setActprocDefId(data.getJSONObject("act").getString("procDefId"));
                                task.setActprocInsId(data.getJSONObject("act").getString("procInsId"));
                                task.setActtaskDefKey(data.getJSONObject("act").getString("taskDefKey"));
                                task.setActtaskId(data.getJSONObject("act").getString("taskId"));
                                task.setActtaskName(data.getJSONObject("act").getString("taskName"));
                                task.setStatus(FlowUtil.deilaytype(data.getJSONObject("act").getString("taskDefKey")));

                                HiddenTroubleDetail detail = new HiddenTroubleDetail();
                                if (data.has("accidentMain")) {
                                    JSONObject jb = data.getJSONObject("accidentMain");
                                    detail.setTroubleId(jb.getString("id"));
                                    detail.setLatitude(jb.getDouble("latitude"));
                                    detail.setLongitude(jb.getDouble("longtitude"));
                                    detail.setTitle(jb.getString("title"));
                                    detail.setState(jb.getString("state"));
                                    detail.setContent(jb.getString("description"));
                                    ArrayList<String> img=new ArrayList<String>();
                                    JSONArray array=jb.getJSONArray("accidentAttachmentList");
                                    for(int i=0;i<array.length();i++){
                                        JSONObject jsb=array.getJSONObject(i);
                                        img.add(jsb.getString("path"));
                                    }
                                    detail.setImage(img);
                                    List<HiddenType> hiddentypes=new ArrayList<HiddenType>();
                                    JSONArray jsonArray=jb.getJSONArray("accidentDetailList");
                                    for(int j=0;j<jsonArray.length();j++){
                                        HiddenType hiddenType=new HiddenType();
                                        JSONObject jht=jsonArray.getJSONObject(j);
                                        hiddenType.setNum(jht.getDouble("num"));
                                        hiddenType.setTroubleContent(String.valueOf(jht.getDouble("num")));
                                        hiddenType.setTypeName(jht.getJSONObject("unit").getString("name"));
                                        hiddenType.setTypeUnit(jht.getJSONObject("unit").getString("measures"));
                                        hiddenType.setShow(true);
                                        hiddentypes.add(hiddenType);
                                    }
                                    detail.setHiddenTypes(hiddentypes);
                                    detail.setProcInsId(jb.getJSONObject("act").getString("procInsId"));
                                    detail.setInspectionName(jb.getJSONObject("createBy").getString("name"));
                                    detail.setDate(jb.getString("createDate"));
                                }
                                task.setHiddenTroubleDetail(detail);
                                if(body.has("button")){
                                    task.setOption(JsonAndMap.getMapForJson(body.getJSONObject("button").toString()).toString());
                                }
                                resultMessage(handler, INT_GET_DEILYDEILY_SUCCESS, task, -1);
                            }else {
                                resultMessage(handler, INT_GET_DEILYDEILY_FILED, job.getString("msg"), Integer.valueOf(job.getString("errorCode")));
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
                resultMessage(handler,INT_GET_DEILYDEILY_FILED,"服务器连接失败",-1);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(MyApplication.CONNECTION_TIME_OUT, 1, 1.0f));//自定义超时时间
        VolleyUtil.getRequestQueue().add(request);
    }


    public final static int INT_GET_SPECIAL_DETAIL_SUCCESS=0x570;
    public final static int INT_GET_SPECIAL_DETAIL_FILED=0x571;
    /**
     * 待办专项详情
     * @param url
     */
    public void getSpecialDetail(String url){

        KLog.d(ConnectionURL.HTTP_SERVER_IP+url + ConnectionURL.HTTP_SERVER_END_Y);
        KLog.d(ConnectionURL.HTTP_SERVER_IP+url);
        DialogUnit.showDialog(context);
        StringRequest request=new StringRequest(Request.Method.GET, ConnectionURL.HTTP_SERVER_IP+url,
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
                                task.setTaskName(data.getString("title"));
                                task.setTaskId(data.getString("id"));
                                task.setTaskContent(data.getString("description"));
                                task.setContactName(data.getJSONObject("header").getString("name"));
                                task.setContactId(data.getJSONObject("header").getString("id"));
//                                task.setContractName(data.getJSONObject("contractMain").getString("name"));
//                                task.setContractId(data.getJSONObject("contractMain").getString("id"));
                                task.setDate(data.getString("maintenanceTime"));

                                if(data.has("accidentMain")) {
                                    task.setTroubleId(data.getJSONObject("accidentMain").getString("id"));
                                    task.setTroubleName(data.getJSONObject("accidentMain").getString("title"));
                                }

                                ArrayList<String> fujian = new ArrayList<String>();
                                if (data.has("specialMaintenanceAttachmentList")) {
                                    JSONArray array = data.getJSONArray("specialMaintenanceAttachmentList");
                                    for (int i = 0; i < array.length(); i++) {
                                        fujian.add(array.getJSONObject(i).getString("path"));
                                    }
                                }
                                task.setFujian(fujian);

                                ArrayList<HiddenType> hiddenTypes = new ArrayList<HiddenType>();
                                if (data.has("specialMaintenanceDetailList")) {
                                    JSONArray array = data.getJSONArray("specialMaintenanceDetailList");
                                    for (int i = 0; i < array.length(); i++) {
                                        HiddenType hiddenType = new HiddenType();
                                        JSONObject jb = array.getJSONObject(i);
                                        hiddenType.setTypeName(jb.getJSONObject("unit").getString("name"));
                                        hiddenType.setTypeUnit(jb.getJSONObject("unit").getString("measures"));
                                        hiddenType.setTroubleContent(jb.getDouble("num")+"");
                                        hiddenType.setShow(true);
                                        hiddenTypes.add(hiddenType);
                                    }
                                }
                                task.setHidden(hiddenTypes);
                                task.setState(Integer.valueOf(data.getString("state")));
                                task.setActprocDefId(data.getJSONObject("act").getString("procDefId"));
                                task.setActprocInsId(data.getJSONObject("act").getString("procInsId"));
                                task.setActtaskDefKey(data.getJSONObject("act").getString("taskDefKey"));
                                task.setActtaskId(data.getJSONObject("act").getString("taskId"));
                                task.setActtaskName(data.getJSONObject("act").getString("taskName"));
                                task.setStatus(FlowUtil.deilaytype(data.getJSONObject("act").getString("taskDefKey")));

                                HiddenTroubleDetail detail = new HiddenTroubleDetail();
                                if (data.has("accidentMain")) {
                                    JSONObject jb = data.getJSONObject("accidentMain");
                                    detail.setTroubleId(jb.getString("id"));
                                    detail.setLatitude(jb.getDouble("latitude"));
                                    detail.setLongitude(jb.getDouble("longtitude"));
                                    detail.setTitle(jb.getString("title"));
                                    detail.setState(jb.getString("state"));
                                    detail.setContent(jb.getString("description"));
                                    ArrayList<String> img=new ArrayList<String>();
                                    JSONArray array=jb.getJSONArray("accidentAttachmentList");
                                    for(int i=0;i<array.length();i++){
                                        JSONObject jsb=array.getJSONObject(i);
                                        img.add(jsb.getString("path"));
                                    }
                                    detail.setImage(img);
                                    List<HiddenType> hiddentypes=new ArrayList<HiddenType>();
                                    JSONArray jsonArray=jb.getJSONArray("accidentDetailList");
                                    for(int j=0;j<jsonArray.length();j++){
                                        HiddenType hiddenType=new HiddenType();
                                        JSONObject jht=jsonArray.getJSONObject(j);
                                        hiddenType.setNum(jht.getDouble("num"));
                                        hiddenType.setTroubleContent(String.valueOf(jht.getDouble("num")));
                                        hiddenType.setTypeName(jht.getJSONObject("unit").getString("name"));
                                        hiddenType.setTypeUnit(jht.getJSONObject("unit").getString("measures"));
                                        hiddenType.setShow(true);
                                        hiddentypes.add(hiddenType);
                                    }
                                    detail.setHiddenTypes(hiddentypes);
                                    detail.setProcInsId(jb.getJSONObject("act").getString("procInsId"));
                                    detail.setInspectionName(jb.getJSONObject("createBy").getString("name"));
                                    detail.setDate(jb.getString("createDate"));
                                }
                                task.setHiddenTroubleDetail(detail);
                                if(body.has("button")){
                                    task.setOption(JsonAndMap.getMapForJson(body.getJSONObject("button").toString()).toString());
                                }
                                resultMessage(handler, INT_GET_SPECIAL_DETAIL_SUCCESS, task, -1);
                            }else {
                                resultMessage(handler, INT_GET_SPECIAL_DETAIL_FILED, job.getString("msg"), Integer.valueOf(job.getString("errorCode")));
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
                resultMessage(handler,INT_GET_SPECIAL_DETAIL_FILED,"服务器连接失败",-1);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(MyApplication.CONNECTION_TIME_OUT, 1, 1.0f));//自定义超时时间
        VolleyUtil.getRequestQueue().add(request);
    }

    /**
     * 待办方案上报详情
     * @param url
     */
    public void getReportDetail(String url){

        KLog.d(ConnectionURL.HTTP_SERVER_IP+url + ConnectionURL.HTTP_SERVER_END_Y);
        KLog.d(ConnectionURL.HTTP_SERVER_IP+url);
        DialogUnit.showDialog(context);
        StringRequest request=new StringRequest(Request.Method.GET, ConnectionURL.HTTP_SERVER_IP+url+ ConnectionURL.HTTP_SERVER_END_Y,
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
                                if(data.has("accidentMain")) {
                                    JSONObject acc = data.getJSONObject("accidentMain");
                                    htd.setStockName(acc.getString("description"));
                                    htd.setQuantity(acc.getString("quantity"));
                                    htd.setUnit(acc.getString("unit"));
                                    htd.setStockId(acc.optString("stockId"));
                                    if(acc.has("diseaseType")) {
                                        htd.setTypeName(acc.getJSONObject("diseaseType").getString("name"));
                                    }
                                    if(acc.has("diseaseName")) {
                                        htd.setName(acc.getJSONObject("diseaseName").getString("name"));
                                    }
                                    htd.setTroubleId(acc.getString("id"));
                                }
                                task.setHiddenTroubleDetail(htd);
                                if(!data.isNull("yearMonth")) {
                                    task.setYearMonth(Integer.valueOf(data.optString("yearMonth")));
                                }
                                task.setMaintenanceState(Integer.valueOf(data.optString("maintenanceState")));
                                task.setDate(data.optString("maintenanceTime"));

                                if(data.has("contractMain")){
                                    KLog.d(data.getJSONObject("contractMain").getString("name"));
                                    task.setContractName(data.getJSONObject("contractMain").getString("name"));
                                }

                                if(data.has("tuser")){
                                    task.setContactName(data.getJSONObject("tuser").getString("name"));
                                }

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
                                ArrayList<HiddenType> hiddenTypes=new ArrayList<HiddenType>();
                                if(!data.isNull("dailyMaintenanceDetailList")){
                                   JSONArray jsonArray=data.getJSONArray("dailyMaintenanceDetailList");
                                    for(int i=0;i<jsonArray.length();i++){
                                        HiddenType ht=new HiddenType();
                                        JSONObject jb=jsonArray.getJSONObject(i);
                                        ht.setShow(true);
                                        ht.setTypeName(jb.getString("name"));
                                        ht.setTroubleContent(jb.getString("num"));
                                        ht.setTypeUnit(jb.getString("unitstr"));
                                        ht.setTypeId(jb.getString("id"));
                                        hiddenTypes.add(ht);
                                    }
                                }
                                task.setHidden(hiddenTypes);
                                JSONObject act=data.getJSONObject("act");
                                task.setActtaskId(act.getString("taskId"));
                                task.setActtaskDefKey(act.getString("taskDefKey"));
                                task.setActprocInsId(act.getString("procInsId"));
                                task.setActprocDefId(act.getString("procDefId"));
                                if(body.has("button")){
                                    task.setOption(JsonAndMap.getMapForJson(body.getJSONObject("button").toString()).toString());
                                }
                                resultMessage(handler, INT_GET_SPECIAL_DETAIL_SUCCESS, task, -1);
                            }else {
                                resultMessage(handler, INT_GET_SPECIAL_DETAIL_FILED, job.getString("msg"), Integer.valueOf(job.getString("errorCode")));
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
                resultMessage(handler,INT_GET_SPECIAL_DETAIL_FILED,"服务器连接失败",-1);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(MyApplication.CONNECTION_TIME_OUT, 1, 1.0f));//自定义超时时间
        VolleyUtil.getRequestQueue().add(request);
    }


    public final static int COMMIT_METERAGE_SUCCESS=0x78;
    public final static int COMMIT_METERAGE_FILED=0x79;

    /**
     * 专项计量
     * @param id
     * @param taskId
     * @param procInsId
     * @param taskDefKey
     * @param comment
     * @param flag
     * @param specId
     * @param troubleId
     * @param troubleNum
     */
    public void commitSpecialMeterage(String id,String taskId,String procInsId,String taskDefKey,String comment,String flag,String specId,ArrayList<String> troubleId,ArrayList<String> troubleNum){
        Map<String,String> map=new HashMap<String,String>();
        map.put("specialMaintenanceMain.id",id);
        map.put("specialMaintenanceMain.act.taskId",taskId);
        map.put("specialMaintenanceMain.act.procInsId",procInsId);
        map.put("specialMaintenanceMain.act.taskDefKey",taskDefKey);
        map.put("specialMaintenanceMain.act.comment",comment);
        map.put("specialMaintenanceMain.act.flag",flag);
        map.put("id",specId);
        if(flag.equals("yes")) {
            int i = 0;
            for (; i < troubleId.size(); i++) {
                map.put("specialMaintenanceReportDetailList[" + i + "].approvalNum", troubleNum.get(i));
                map.put("specialMaintenanceReportDetailList[" + i + "].id", troubleId.get(i));
                map.put("specialMaintenanceReportDetailList[" + i + "].approval.id", MyApplication.getStringSharedPreferences("userId", ""));
            }
        }
        KLog.d(map.toString());
        new JSONReauest(context, ConnectionURL.STR_SAVE_SPECIAL_TASK_REPORT, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            resultMessage(handler,COMMIT_METERAGE_SUCCESS,result.getString("msg"),-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,COMMIT_METERAGE_FILED,"网络错误",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler,COMMIT_METERAGE_FILED,msg,errorcode);
                    }
                });
    }


    /**
     * 日常计量
     * @param id
     * @param taskId
     * @param procInsId
     * @param taskDefKey
     * @param comment
     * @param flag
     * @param specId
     * @param troubleId
     * @param troubleNum
     */
    public void commitDeliayMeterage(String id,String taskId,String procInsId,String taskDefKey,String comment,String flag,String specId,ArrayList<String> troubleId,ArrayList<String> troubleNum){
        Map<String,String> map=new HashMap<String,String>();
        map.put("dailyMaintenanceMain.id",id);
        map.put("dailyMaintenanceMain.act.taskId",taskId);
        map.put("dailyMaintenanceMain.act.procInsId",procInsId);
        map.put("dailyMaintenanceMain.act.taskDefKey",taskDefKey);
        map.put("dailyMaintenanceMain.act.comment",comment);
        map.put("dailyMaintenanceMain.act.flag",flag);
        map.put("id",specId);
        if(flag.equals("yes")) {
            int i = 0;
            for (; i < troubleId.size(); i++) {
                map.put("dailyMaintenanceReportDetailList[" + i + "].approvalNum", troubleNum.get(i));
                map.put("dailyMaintenanceReportDetailList[" + i + "].id", troubleId.get(i));
                map.put("dailyMaintenanceReportDetailList[" + i + "].approval.id", MyApplication.getStringSharedPreferences("userId", ""));
            }
        }

        KLog.d(map.toString());
        new JSONReauest(context, ConnectionURL.STR_SAVE_DEILAY_TASK_REPORT, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            resultMessage(handler,COMMIT_METERAGE_SUCCESS,result.getString("msg"),-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,COMMIT_METERAGE_FILED,"网络错误",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler,COMMIT_METERAGE_FILED,msg,errorcode);
                    }
                });
    }
}
