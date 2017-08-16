package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.customview.TreeListView.TreeElement;
import com.tianjin.MobileInspection.entity.HiddenTroubleDetail;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.InspectionChoose;
import com.tianjin.MobileInspection.entity.InspectionDetail;
import com.tianjin.MobileInspection.entity.Office;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.entity.Yinhuan;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.FlowUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuchang on 2016-12-9.
 */
public class InspectionModule extends BaseModule {

    public final static int INT_GET_ROAD_SUCCESS=0x5634;
    public final static int INT_GET_ROAD_FILED=0x5635;
    public final static int INT_SAVE_NEW_INSPECTION_SUCCESS=0x5636;
    public final static int INT_SAVE_NEW_INSPECTION_FILED=0x5637;
    public final static int INT_GET_HIDDENTROUBLETYPE_FILED=0x5638;
    public final static int INT_GET_HIDDENTROUBLETYPE_SUCCESS=0x5639;
    public final static int INT_GET_OFFICE_LIST_FILED=0x5640;
    public final static int INT_GET_OFFICE_LIST_SUCCESS=0x5641;
    public final static int INT_GET_INSPECTION_CHOSE_FILED=0x5642;
    public final static int INT_GET_INSPECTION_CHOSE_SUCCESS=0x5643;

    private Handler handler;

    public InspectionModule(Handler handler,Context context){
        this.handler=handler;
        this.context=context;
    }

    /**
     * 获取道路列表
     * @param pageno
     * @param pagesize
     */
    public void getRoads(final int pageno, final int pagesize){

        Map<String,String> map=new HashMap<String, String>();
        map.put("pageNo", String.valueOf(pageno));
        map.put("pageSize", String.valueOf(pagesize));

        JSONReauest reauest=new JSONReauest(context,ConnectionURL.STR_GET_ROADS, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONArray list=body.getJSONArray("list");
                            List<Road> roads=new ArrayList<Road>();
                            for(int i=0;i<list.length();i++){
                                JSONObject jb=list.getJSONObject(i);
                                Road road=new Road();
                                road.setRoadId(jb.getString("id"));
                                road.setRoadName(jb.getString("name"));
                                roads.add(road);
                            }
                        resultMessage(handler,INT_GET_ROAD_SUCCESS,roads,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_ROAD_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_GET_ROAD_FILED, msg, errorcode);
                    }
                });
    }

    /**
     * 获取巡检选择列表
     * @param pageNo
     * @param pageSize
     * @param userId
     * @param state
     */
    public void getInspectionChoseLList(String pageNo,String pageSize,String userId,String state){
        Map<String,String> map=new HashMap<String, String>();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        KLog.d(map.toString());
        final JSONReauest reauest=new JSONReauest(context,ConnectionURL.STR_GET_INSPECTION_TOINSPECTINGLIST, MyApplication.CONNECTION_TIME_OUT,map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            ArrayList<InspectionChoose> insplist=new ArrayList<InspectionChoose>();
                            JSONObject body=result.getJSONObject("body");
                            JSONArray array=body.getJSONArray("list");
                            for(int i=0;i<array.length();i++){
                                InspectionChoose insp=new InspectionChoose();
                                JSONObject jb=array.getJSONObject(i);
                                insp.setInspectionId(jb.getString("id"));
                                insp.setInspectionName(jb.getString("title"));
                                insp.setContent(jb.getString("content"));
                                insp.setDate(jb.getString("inspectingTime"));
                                insp.setState(Integer.parseInt(jb.getString("state")));
                                insp.setTraffic(FlowUtil.traffic(jb.getString("inspectingType")));
                                insp.setTrafficId(Integer.valueOf(jb.getString("inspectingType")));
//                                insp.setPersonId(jb.getJSONObject("tuser").getString("id"));
//                                insp.setPerson(jb.getJSONObject("tuser").getString("name"));
//                                insp.setUnitId(jb.getJSONObject("office").getString("id"));
//                                insp.setUnitName(jb.getJSONObject("office").getString("name"));
                                insp.setFlowState(FlowUtil.inspectionState(insp.getState()));
                                insp.setProcInsId(jb.getJSONObject("act").getString("procInsId"));
                                insp.setTaskId(jb.getJSONObject("act").optString("taskId"));
                                insp.setTaskDefKey(jb.getJSONObject("act").getString("taskDefKey"));
                                insp.setProcDefKey(jb.getJSONObject("act").getString("procDefKey"));
                                insp.setPersonId(jb.getJSONObject("createBy").getString("id"));
//                                List<String> roads=new ArrayList<String>();
                                List<Road> roads=new ArrayList<Road>();
                                JSONArray roadarray=jb.getJSONArray("inspectingRangeList");
                                for(int m=0;m<roadarray.length();m++){
                                    JSONObject jbs=roadarray.getJSONObject(m);
                                    Road road=new Road();
                                    road.setRoadName(jbs.getJSONObject("road").getString("name"));
                                    road.setRoadId(jbs.getJSONObject("road").getString("id"));
                                    road.setLightCount(Integer.valueOf(jbs.getJSONObject("road").getString("lightCount")));
                                    roads.add(road);
                                }
                                insp.setRoads(roads);
                                insplist.add(insp);
                            }
                            resultMessage(handler,INT_GET_INSPECTION_CHOSE_SUCCESS,insplist,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_INSPECTION_CHOSE_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_GET_INSPECTION_CHOSE_FILED, msg, errorcode);
                    }
                });
    }

    /**
     * 获取巡检列表
     * @param pageNo
     * @param pageSize
     * @param state （1：待巡检;2：巡检中;3：巡检结束；4验收结束）
     */
    public void getInspectionList(String pageNo,String pageSize,String state){
        Map<String,String> map=new HashMap<String, String>();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("state",state);
        KLog.d(map.toString());
        final JSONReauest reauest=new JSONReauest(context,ConnectionURL.STR_GET_INSPECTION_LIST, MyApplication.CONNECTION_TIME_OUT,map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            ArrayList<InspectionChoose> insplist=new ArrayList<InspectionChoose>();
                            JSONObject body=result.getJSONObject("body");
                            JSONArray array=body.getJSONArray("list");
                            for(int i=0;i<array.length();i++){
                                InspectionChoose insp=new InspectionChoose();
                                JSONObject jb=array.getJSONObject(i);
                                insp.setInspectionId(jb.getString("id"));
                                insp.setInspectionName(jb.getString("title"));
                                insp.setContent(jb.getString("content"));
                                insp.setDate(jb.getString("inspectingTime"));
                                insp.setTraffic(FlowUtil.traffic(jb.getString("inspectingType")));
//                                insp.setPersonId(jb.getJSONObject("tuser").getString("id"));
//                                insp.setPerson(jb.getJSONObject("tuser").getString("name"));
//                                insp.setUnitId(jb.getJSONObject("office").getString("id"));
//                                insp.setUnitName(jb.getJSONObject("office").getString("name"));
                                List<String> roads=new ArrayList<String>();
                                JSONArray roadarray=jb.getJSONArray("inspectingRangeList");
                                for(int m=0;m<roadarray.length();m++){
                                    JSONObject jbs=roadarray.getJSONObject(m);
                                    roads.add(jbs.getJSONObject("road").getString("name"));
                                }
                                insp.setNodes(roads);
                                insplist.add(insp);
                            }
                            resultMessage(handler,INT_GET_INSPECTION_CHOSE_SUCCESS,insplist,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_INSPECTION_CHOSE_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_GET_INSPECTION_CHOSE_FILED, msg, errorcode);
                    }
                });
    }

    /**
     * 保存新建巡检
     * @param inspection
     * @param contractId
     * @param unitName
     * @param roadId
     * @param trafficId
     * @param content
     * @param personId
     * @param date
     */
    public void saveNewInspection(String inspection,String contractId,String unitName,List<String> roadId,String trafficId,String content,String personId,String date){
        Map<String,String> map=new HashMap<String, String>();
        map.put("title",inspection);
        map.put("contractMain.id",contractId);
        map.put("office.id",unitName);
        map.put("tuser.id",personId);
        map.put("inspectingType",trafficId);
        map.put("inspectingTime",date);
        map.put("content",content);
        for(int i=0;i<roadId.size();i++){
            map.put("inspectingRangeList["+i+"].road.id",roadId.get(i));
        }
        KLog.d(map.toString());
        JSONReauest reauest=new JSONReauest(context,ConnectionURL.STR_POST_INSPECTION_SAVE, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        String str="";
                        try {
                            str=result.getString("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        resultMessage(handler,INT_SAVE_NEW_INSPECTION_SUCCESS,str,-1);
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        resultMessage(handler,INT_SAVE_NEW_INSPECTION_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_SAVE_NEW_INSPECTION_FILED, msg, errorcode);
                    }
                });

    }

    /**
     * 获取隐患的具体详情类型
     */
    public void getHiddenTroubleType(){
        new JSONReauest(context,ConnectionURL.STR_GET_TROUBLE_TYPES, MyApplication.CONNECTION_TIME_OUT,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONArray jsonArray=body.getJSONArray("list");
                            ArrayList<HiddenType> data=new ArrayList<HiddenType>();
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jb=jsonArray.getJSONObject(i);
                                HiddenType ht=new HiddenType();
                                ht.setTypeId(jb.getString("id"));
                                ht.setTypeName(jb.getString("name"));
                                ht.setTypeUnit(jb.getString("measures"));
                                data.add(ht);
                            }
                            resultMessage(handler,INT_GET_HIDDENTROUBLETYPE_SUCCESS,data,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        KLog.d(volleyError.getMessage());
                        String str=volleyError.getMessage();
                        resultMessage(handler,INT_GET_HIDDENTROUBLETYPE_FILED,str,-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_GET_HIDDENTROUBLETYPE_FILED, msg, errorcode);
                    }
                });
    }

    /**
     * 获取单位列表
     */
    public void getOfficeList(){
        new JSONReauest(context,ConnectionURL.STR_GET_OFFICE_LIST, MyApplication.CONNECTION_TIME_OUT,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONArray list=body.getJSONArray("list");
                            ArrayList<Office> offices=new ArrayList<Office>();
                            for(int i=0;i<list.length();i++){
                                JSONObject jb=list.getJSONObject(i);
                                JSONObject jar=jb.getJSONObject("area");
                                Office off=new Office();
                                off.setOfficeId(jb.getString("id"));
                                off.setOfficeName(jb.getString("name"));
                                off.setOfficeParentId(jb.getString("parentIds"));
                                off.setOfficeAreaId(jar.getString("id"));
                                off.setOfficeAreaName(jar.getString("name"));
                                offices.add(off);
                            }
                            resultMessage(handler,INT_GET_OFFICE_LIST_SUCCESS,offices,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        KLog.d(volleyError.getMessage());
                        String str=volleyError.getMessage();
                        resultMessage(handler,INT_GET_OFFICE_LIST_FILED,str,-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_GET_OFFICE_LIST_FILED, msg, errorcode);
                    }
                });
    }


    public static final int INT_HAS_DOING_TRUE_SUCCESS=0x890;
    public static final int INT_HAS_DOING_FALSE_SUCCESS=0x891;
    public static final int INT_HAS_DOING_FILED=0x892;

    /**
     * 获取是否存在正在巡检中的巡检任务
     */
    public void getHasDoingInspection(final String id){
        new JSONReauest(context,ConnectionURL.STR_GET_HAS_DOING_INSPECTION, MyApplication.CONNECTION_TIME_OUT,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
//                                JSONObject data=body.getJSONObject("data");
                                JSONArray jsonArray=body.getJSONArray("list");
                                InspectionChoose insp=new InspectionChoose();
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data=jsonArray.getJSONObject(i);
                                if(data.getString("id").equals(id)){
                                    insp.setTrafficId(Integer.valueOf(data.getString("inspectingType")));
                                    insp.setInspectionId(data.getString("id"));
                                    JSONObject act=data.getJSONObject("act");
                                    insp.setTaskId(act.getString("taskId"));
                                    insp.setTaskDefKey(act.getString("taskDefKey"));
                                    insp.setProcInsId(act.getString("procInsId"));
                                    insp.setProcDefKey(act.getString("procDefKey"));
                                    insp.setDate(act.getString("beginDate"));
                                    break;
                                }
                            }
                            resultMessage(handler,INT_HAS_DOING_TRUE_SUCCESS,insp,-1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_HAS_DOING_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_HAS_DOING_FALSE_SUCCESS, msg, errorcode);
                    }
                });
    }


    public final static int INT_SAVE_CLOCK_SUCCESS=0x770;
    public final static int INT_SAVE_CLOCK_FILED=0x771;

    /**
     * 提交打卡信息
     * @param inspectionManid
     * @param code
     */
    public void saveClock(String inspectionManid,String code,String lat,String lon){
        Map<String,String> map=new HashMap<String, String>();
        map.put("inspectingMain.id",inspectionManid);
        map.put("clock.id",code);
        map.put("latitude",lat);
        map.put("longtitude",lon);
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_ERCODE_SAVE_CLOCK, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        String msg="";
                        try {
                            msg=result.getString("msg");
                            resultMessage(handler,INT_SAVE_CLOCK_SUCCESS,msg,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_SAVE_CLOCK_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_SAVE_CLOCK_FILED, msg, errorcode);
                    }
                });

    }


    public final static int INT_COMMIT_HIDDEN_TROUBLE_SUCCESS=0x780;
    public final static int INT_COMMIT_HIDDEN_TROUBLE_FILED=0x781;
    /**
     * 隐患上报
     * @param description
     * @param longtitude
     * @param latitude
     * @param inspectingMain
     */
    public void commitHiddenTrouble(String description,String longtitude,String latitude ,String inspectingMain
            ,String emergency,String type,String roadId,String lightid,String quantity,String hiddenName,String stockid,String unit){
        Map<String,String> map=new HashMap<String, String>();
        map.put("description",description);
        map.put("longtitude",longtitude);
        map.put("latitude",latitude);
        map.put("inspectingMain.id",inspectingMain);
        map.put("emergencyTypeId",emergency);
        map.put("typeId",type);
        map.put("roadId",roadId);
        map.put("lightId",lightid);
        map.put("quantity",quantity);
        map.put("nameId",hiddenName);
        map.put("stockId",stockid);
        map.put("unit",unit);
        KLog.d(map.toString());

        new JSONReauest(context,ConnectionURL.STR_HIDDEN_TROUBLE_COMMIT, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            String str=body.getString("id");
                            resultMessage(handler,INT_COMMIT_HIDDEN_TROUBLE_SUCCESS,str,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_COMMIT_HIDDEN_TROUBLE_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_COMMIT_HIDDEN_TROUBLE_FILED, msg, errorcode);
                    }
                });
    }


    public final static int INT_FINISH_INSPECTION_SUCCESS=0x670;
    public final static int INT_FINISH_INSPECTION_Filed=0x671;

    /**
     * 巡检结束提交接口
     * @param id
     * @param tasmkid
     * @param taskDefKey
     * @param procInsId
     * @param comment
     * @param flag
     */
    public void inspectionFinishCommint(String id,String tasmkid,String taskDefKey,String procInsId,String comment,String flag){
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        map.put("act.taskId",tasmkid);
        map.put("act.taskDefKey","inspecting");
        map.put("act.procInsId",procInsId);
        map.put("act.comment",comment);
        map.put("act.flag",flag);
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_POST_INSPECTION_AUDIT, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        handler.sendEmptyMessage(INT_FINISH_INSPECTION_SUCCESS);
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        resultMessage(handler,INT_FINISH_INSPECTION_Filed,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_FINISH_INSPECTION_Filed, msg, errorcode);
                    }
                });

    }

    public final static int INT_GET_TROUBLES_LIST_SUCCESS=0x34;
    public final static int INT_GET_TROUBLES_LIST_Filed=0x35;
    /**
     * 获取隐患列表
     * @param pageNo
     * @param pageSize
     */
    public void getTroubles(String pageNo,String pageSize){
        Map<String,String> map=new HashMap<String, String>();
        map.put("pageNo", String.valueOf(pageNo));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("state",String.valueOf("1"));
        map.put("maintenance",String.valueOf("0"));
        new JSONReauest(context,ConnectionURL.STR_GET_HIDDEN_TROUBLE_LIST, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject data=result.getJSONObject("body");
                            List<HiddenTroubleDetail> hiddenTypes=new ArrayList<HiddenTroubleDetail>();
                            JSONArray jsonArray=data.getJSONArray("list");
                            if(jsonArray!=null&&jsonArray.length()>0){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jb=jsonArray.getJSONObject(i);
                                    HiddenTroubleDetail hiddenType=new HiddenTroubleDetail();
                                    hiddenType.setTitle(jb.getString("title"));
                                    hiddenType.setTroubleId(jb.getString("id"));
                                    hiddenType.setContent(jb.getString("description"));
                                    hiddenType.setLatitude(jb.getDouble("latitude"));
                                    hiddenType.setLongitude(jb.getDouble("longtitude"));
                                    hiddenType.setInspectionName(jb.getJSONObject("createBy").getString("name"));
                                    hiddenType.setDate(jb.getString("createDate"));
                                    hiddenTypes.add(hiddenType);
                                }
                            }
                            resultMessage(handler,INT_GET_TROUBLES_LIST_SUCCESS,hiddenTypes,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_TROUBLES_LIST_Filed,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_GET_TROUBLES_LIST_Filed, msg, errorcode);
                    }
                });
    }

    public  final static int INT_GET_INSPECTION_SUCCESS=0x745;
    public  final static int INT_GET_INSPECTION_FILED=0x746;

    /**
     * 获取巡检详情
     * @param id
     */
    public void getInspectionDetail(String id){
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        KLog.d(map.toString());
        new JSONReauest(context,ConnectionURL.STR_GET_INSPECTION_DETAIL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONObject data=body.getJSONObject("data");
                            InspectionDetail detail=new InspectionDetail();
                            detail.setInspectionId(data.getString("id"));

                            JSONObject contractMain=data.getJSONObject("contractMain");
                            detail.setContractName(contractMain.getString("name"));
                            detail.setContractId(contractMain.getString("id"));

                            detail.setState(data.getString("state"));
                            detail.setTraffic(data.getString("inspectingType"));
                            detail.setDate(data.getString("inspectingTime"));
                            detail.setStartTime(data.getString("startTime"));
                            if(data.has("finishTime")) {
                                detail.setEndTime(data.getString("finishTime"));
                            }
                            detail.setContent(data.getString("content"));

                            JSONArray jsroads=data.getJSONArray("inspectingRangeList");
                            ArrayList<Road> roads=new ArrayList<Road>();
                            if(roads!=null&&jsroads.length()>0){
                                for(int i=0;i<jsroads.length();i++){
                                    Road road=new Road();
                                    JSONObject jb=jsroads.getJSONObject(i);
                                    road.setRoadName(jb.getJSONObject("road").getString("name"));
                                    road.setRoadId(jb.getJSONObject("road").getString("id"));
                                    roads.add(road);
                                }
                            }
                            detail.setRoads(roads);

                            ArrayList<Yinhuan> yinhuans=new ArrayList<Yinhuan>();
                            if(data.has("accidentMainList")) {
                                JSONArray list = data.getJSONArray("accidentMainList");
                                if (list != null && list.length() > 0) {
                                    for (int j = 0; j < list.length(); j++) {
                                        Yinhuan yinhuan = new Yinhuan();
                                        JSONObject jb = list.getJSONObject(j);
                                        KLog.json(jb.toString());
                                        yinhuan.setYhId(jb.getString("id"));
                                        yinhuan.setYhContent(jb.getString("description"));
                                        yinhuan.setPersonName(jb.getJSONObject("createBy").getString("name"));
                                        yinhuan.setYhdate(jb.getString("createDate"));
                                        yinhuans.add(yinhuan);
                                    }
                                }
                            }
                            detail.setYinhuans(yinhuans);
                            ArrayList<LatLng> clock=new ArrayList<LatLng>();
                            if(data.has("inspectingClockList")){
                                JSONArray jsonArray=data.getJSONArray("inspectingClockList");
                                if(jsonArray!=null&&jsonArray.length()>0){
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jb=jsonArray.getJSONObject(i);
                                        String str=jb.getString("isClock");
                                        if(!str.equals("否")) {
                                            LatLng latLng = new LatLng(Double.valueOf(jb.getString("latitude")), Double.valueOf(jb.getString("longtitude")));
                                            clock.add(latLng);
                                        }
                                    }
                                }
                            }
                            detail.setClocllist(clock);
                            resultMessage(handler,INT_GET_INSPECTION_SUCCESS,detail,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_INSPECTION_FILED,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler,INT_GET_INSPECTION_FILED,msg,errorcode);
                    }
                });
    }


    public final static int INT_GET_HIDDEN_TROUBLE_SUCCESS=0x876;
    public final static int INT_GET_HIDDEN_TROUBLE_FILED=0x877;
    public  void getHiddenTroubleDetail(String id){
        Map<String,String> map=new HashMap<String, String>();
        map.put("id",id);
        KLog.d(map.toString());
        new JSONReauest(context, ConnectionURL.STR_GET_HIDDEN_TROUBLE_DETAIL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        HiddenTroubleDetail detail = new HiddenTroubleDetail();
                        try {
                            JSONObject body = result.getJSONObject("body");
                            JSONObject data = body.getJSONObject("data");
                            detail.setTroubleId(data.getString("id"));
                            detail.setLatitude(data.getDouble("latitude"));
                            detail.setLongitude(data.getDouble("longtitude"));
                            detail.setState(data.getString("state"));
                            detail.setContent(data.getString("description"));
                            detail.setEmergencyType(data.getString("emergencyTypeId"));
                            detail.setType(data.getString("typeId"));
                            detail.setRoadId(data.getString("roadId"));
                            detail.setLightId(data.getString("lightId"));
                            detail.setQuantity(data.getString("quantity"));
                            detail.setUnit(data.getString("unit"));
                            detail.setNameId(data.getString("nameId"));
                            detail.setStockId(data.getString("stockId"));
                            ArrayList<String> img=new ArrayList<String>();
                            JSONArray array=data.getJSONArray("accidentAttachmentList");
                            for(int i=0;i<array.length();i++){
                                JSONObject jb=array.getJSONObject(i);
                                KLog.json(jb.toString());
                                img.add(jb.getString("path"));
                            }
                            detail.setImage(img);
//                            List<HiddenType> hiddenTypes=new ArrayList<HiddenType>();
//                            JSONArray jsonArray=data.getJSONArray("accidentDetailList");
//                            for(int j=0;j<jsonArray.length();j++){
//                                HiddenType hiddenType=new HiddenType();
//                                JSONObject jht=jsonArray.getJSONObject(j);
//                                hiddenType.setNum(jht.getDouble("num"));
//                                hiddenType.setTroubleContent(String.valueOf(jht.getDouble("num")));
//                                hiddenType.setTypeName(jht.getJSONObject("unit").getString("name"));
//                                hiddenType.setTypeUnit(jht.getJSONObject("unit").getString("measures"));
//                                hiddenType.setShow(true);
//                                hiddenTypes.add(hiddenType);
//                            }
//                            detail.setHiddenTypes(hiddenTypes);
                            detail.setProcInsId(data.getJSONObject("act").optString("procInsId"));
                            detail.setInspectionName(data.getJSONObject("createBy").getString("name"));
                            detail.setDate(data.getString("createDate"));
                            resultMessage(handler,INT_GET_HIDDEN_TROUBLE_SUCCESS,detail,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Error(VolleyError volleyError) {

                    }

                    @Override
                    public void Filed(String msg, int errorcode) {

                    }
                });

    }

    public final static int INT_GET_DISEASE_OR_EXPERT_SUCCESS=0x562;
    public final static int INT_GET_DISEASE_OR_EXPERT_FILED=0x563;
    public void getDiseaseList(String url){
        new JSONReauest(context, url, MyApplication.CONNECTION_TIME_OUT,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject body=result.getJSONObject("body");
                            JSONArray list=body.getJSONArray("list");
                            List<TreeElement> data=new ArrayList<TreeElement>();
                            if(list!=null&&list.length()>0){
                                for (int i=0;i<list.length();i++){
                                    JSONObject jb=list.getJSONObject(i);
                                    TreeElement dad=new TreeElement();
                                    dad.setId(jb.getString("id"));
                                    dad.setTitle(jb.getString("name"));
                                    dad.setParentId(jb.getString("parentId"));
                                    dad.setContent(CheckUntil.checkEditext(jb.getString("description")));
                                    String parents=jb.getString("parentIds");
                                    String[] info=parents.split(",");
                                    dad.setLevel(info.length);
                                    dad.setChosed(false);
                                    if(info.length==1){
                                        dad.setFold(false);
                                    }else {
                                        dad.setFold(true);
                                    }
                                    data.add(dad);
                                }
                            }
                            resultMessage(handler,INT_GET_DISEASE_OR_EXPERT_SUCCESS,data,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_DISEASE_OR_EXPERT_FILED,"网络连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler,INT_GET_DISEASE_OR_EXPERT_FILED,msg,errorcode);
                    }
                });

    }

}
