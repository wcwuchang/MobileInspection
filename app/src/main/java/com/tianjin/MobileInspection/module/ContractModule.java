package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.ContractData;
import com.tianjin.MobileInspection.entity.ContractManager;
import com.tianjin.MobileInspection.entity.HiddenType;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同
 * Created by wuchang on 2016-12-7.
 */
public class ContractModule extends BaseModule {

    public final static int INT_CONTRACT_LIST_SUCCESS = 0x1025;
    public final static int INT_CONTRACT_LIST_FILED = 0x1026;
    public final static int INT_CONTRACT_DETAIL_SUCCESS = 0x1027;
    public final static int INT_CONTRACT_DETAIL_FILED = 0x1028;

    private Handler handler;

    public ContractModule(Handler handler,Context context){
        this.handler=handler;
        this.context=context;
    }


    //合同列表
    public void getContractList(final int pageno, final int pagesize) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNo", String.valueOf(pageno));
        map.put("pageSize", String.valueOf(pagesize));
        new JSONReauest(context,ConnectionURL.STR_GET_CONTRACTS_LIST, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
            @Override
            public void Success(JSONObject result) {
                JSONObject body = null;
                KLog.json(result.toString());
                try {
                    List<ContractManager> data = new ArrayList<ContractManager>();
                    body = result.getJSONObject("body");
                    JSONArray list = body.getJSONArray("list");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject jb = list.getJSONObject(i);
                        ContractManager cm = new ContractManager();
                        cm.setContractId(jb.getString("id"));
                        cm.setName(jb.getString("name"));
                        cm.setUnitName(jb.getJSONObject("office").getString("name"));
                        cm.setBeginDate(jb.getString("startTime"));
                        cm.setEndDate(jb.getString("endTime"));
                        cm.setType(Integer.valueOf(jb.getString("type")));
//                        cm.setFlowtype(i%4);
                        data.add(cm);
                    }
                    resultMessage(handler, INT_CONTRACT_LIST_SUCCESS, data, -1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void Error(VolleyError volleyError) {
                resultMessage(handler, INT_CONTRACT_LIST_FILED, "服务器连接失败", 1);
            }

            @Override
            public void Filed(String msg, int errorcode) {
                resultMessage(handler, INT_CONTRACT_LIST_FILED, msg, errorcode);
            }
        });
    }

    /**
     * 巡检合同详情
     * @param id
     */
    public void getContractDetail(final String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        KLog.d("data",map.toString());
        JSONReauest sd = new JSONReauest(context,ConnectionURL.STR_GET_CONTRACTS_DETAIL, 5000, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject jsonObject) {
                        KLog.json(jsonObject.toString());
                        KLog.d(jsonObject.toString());
                        try {
                            JSONObject body=jsonObject.getJSONObject("body");
                            JSONObject data=body.getJSONObject("data");
                            ContractManager cm=new ContractManager();
                            cm.setContractId(data.getString("id"));
                            cm.setName(data.getString("name"));
                            cm.setBeginDate(DateUtils.getYMD(data.getString("startTime")));
                            cm.setEndDate(DateUtils.getYMD(data.getString("endTime")));
                            cm.setUnitName(data.getJSONObject("office").getString("name"));
                            cm.setUnitId(data.getJSONObject("office").getString("id"));
                            cm.setContent(data.getString("content"));
                            ArrayList<ContractData> contractDatas=new ArrayList<ContractData>();
                            JSONArray array=data.getJSONArray("contractRoadList");
                            for(int i=0;i<array.length();i++){
                                Road road=new Road();
                                ContractData contractData=new ContractData();
                                JSONObject jb=array.getJSONObject(i);
                                contractData.setContractItemId(jb.getJSONObject("road").getString("id"));
                                contractData.setContractItem(jb.getJSONObject("road").getString("name"));
                                contractData.setContractNum(jb.getDouble("inspectingNumber"));
                                contractData.setRoad(road);
                                contractData.setMonth(Integer.parseInt(jb.getString("yearMonth")));
                                contractDatas.add(contractData);
                            }
                            cm.setDatalist(contractDatas);
                            ArrayList<HiddenType> hiddenTypes=new ArrayList<HiddenType>();
                            if(data.has("inspectingList")&&data.getJSONArray("inspectingList")!=null){
                                JSONArray jsonArray=data.getJSONArray("inspectingList");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsob=jsonArray.getJSONObject(i);
                                    JSONArray array1=jsob.getJSONArray("inspectingRangeList");
                                    for(int j=0;j<array1.length();j++){
                                        JSONObject jsonObject1=array1.getJSONObject(j);
                                        String id=jsonObject1.getJSONObject("road").getString("id");
                                        boolean has=false;
                                        for(int m=0;m<hiddenTypes.size();m++){
                                            if(id.equals(hiddenTypes.get(m).getTypeId())){
                                                hiddenTypes.get(m).setNum(hiddenTypes.get(m).getNum()+1);
                                                has=true;
                                            }
                                        }
                                        if(has) continue;
                                        HiddenType hiddenType=new HiddenType();
                                        hiddenType.setTypeId(id);
                                        hiddenType.setTypeName(jsonObject1.getJSONObject("road").getString("name"));
                                        hiddenType.setNum(1);
                                        hiddenType.setShow(true);
                                        hiddenTypes.add(hiddenType);
                                    }
                                }

                            }
                            if(data.has("dailyList")&&data.getJSONArray("dailyList")!=null){
                                JSONArray jsonArray=data.getJSONArray("dailyList");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject obj=jsonArray.getJSONObject(i);
                                    JSONArray array1=obj.getJSONArray("dailyMaintenanceDetailList");
                                    for(int j=0;j<array1.length();j++){
                                        JSONObject joby=array1.getJSONObject(j);
                                        String id=joby.getJSONObject("unit").getString("id");
                                        boolean t=false;
                                        for(int m=0;m<hiddenTypes.size();m++){
                                            if(id.equals(hiddenTypes.get(m).getTypeId())){
                                                hiddenTypes.get(m).setNum(hiddenTypes.get(m).getNum()+joby.getInt("num"));
                                                t=true;
                                            }
                                        }
                                        if (t) continue;
                                        HiddenType hiddenType=new HiddenType();
                                        hiddenType.setTypeId(id);
                                        hiddenType.setTypeUnit(joby.getJSONObject("unit").getString("measures"));
                                        hiddenType.setTypeName(joby.getJSONObject("unit").getString("name"));
                                        KLog.d(joby.getInt("num")+"");
                                        hiddenType.setNum(joby.getInt("num"));
                                        hiddenType.setShow(true);
                                        hiddenTypes.add(hiddenType);
                                    }

                                }
                            }
                            cm.setHiddenTypes(hiddenTypes);
                            resultMessage(handler,INT_CONTRACT_DETAIL_SUCCESS,cm,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        resultMessage(handler,INT_CONTRACT_DETAIL_FILED, "合同详情获取失败",1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_CONTRACT_DETAIL_FILED, msg, errorcode);
                    }
                });
    }


    /**
     * 维修合同详情
     * @param id
     */
    public void getMaintanenceContractDetail(final String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        KLog.d("data",map.toString());
        JSONReauest sd = new JSONReauest(context,ConnectionURL.STR_GET_CONTRACTS_DETAIL, 5000, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject jsonObject) {
                        KLog.json(jsonObject.toString());
                        KLog.d(jsonObject.toString());
                        try {
                            JSONObject body=jsonObject.getJSONObject("body");
                            JSONObject data=body.getJSONObject("data");
                            ContractManager cm=new ContractManager();
                            cm.setContractId(data.getString("id"));
                            cm.setName(data.getString("name"));
                            cm.setBeginDate(DateUtils.getYMD(data.getString("startTime")));
                            cm.setEndDate(DateUtils.getYMD(data.getString("endTime")));
                            cm.setUnitName(data.getJSONObject("office").getString("name"));
                            cm.setUnitId(data.getJSONObject("office").getString("id"));
                            cm.setContent(data.getString("content"));
                            ArrayList<ContractData> contractDatas=new ArrayList<ContractData>();
                            JSONArray array=data.getJSONArray("contractUnitList");
                            for(int i=0;i<array.length();i++){
                                ContractData contractData=new ContractData();
                                JSONObject jb=array.getJSONObject(i);
                                contractData.setContractItemId(jb.getString("id"));
                                contractData.setContractItem(jb.getJSONObject("road").getString("name"));
                                contractData.setContractNum(jb.getDouble("inspectingNumber"));
                                contractData.setMonth(Integer.parseInt(jb.getString("yearMonth")));
                                contractDatas.add(contractData);
                            }
                            cm.setDatalist(contractDatas);
                            ArrayList<HiddenType> hiddenTypes=new ArrayList<HiddenType>();
                            if(data.has("inspectingList")&&data.getJSONArray("inspectingList")!=null){
                                JSONArray jsonArray=data.getJSONArray("inspectingList");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsob=jsonArray.getJSONObject(i);
                                    JSONArray array1=jsob.getJSONArray("inspectingRangeList");
                                    for(int j=0;j<array1.length();j++){
                                        JSONObject jsonObject1=array1.getJSONObject(j);
                                        String id=jsonObject1.getJSONObject("road").getString("id");
                                        boolean has=false;
                                        for(int m=0;m<hiddenTypes.size();m++){
                                            if(id.equals(hiddenTypes.get(m).getTypeId())){
                                                hiddenTypes.get(m).setNum(hiddenTypes.get(m).getNum()+1);
                                                has=true;
                                            }
                                        }
                                        if(has) continue;
                                        HiddenType hiddenType=new HiddenType();
                                        hiddenType.setTypeId(id);
                                        hiddenType.setTypeName(jsonObject1.getJSONObject("road").getString("name"));
                                        hiddenType.setNum(1);
                                        hiddenType.setShow(true);
                                        hiddenTypes.add(hiddenType);
                                    }
                                }

                            }
                            if(data.has("dailyList")&&data.getJSONArray("dailyList")!=null){
                                JSONArray jsonArray=data.getJSONArray("dailyList");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject obj=jsonArray.getJSONObject(i);
                                    JSONArray array1=obj.getJSONArray("dailyMaintenanceDetailList");
                                    for(int j=0;j<array1.length();j++){
                                        JSONObject joby=array1.getJSONObject(j);
                                        String id=joby.getJSONObject("unit").getString("id");
                                        boolean t=false;
                                        for(int m=0;m<hiddenTypes.size();m++){
                                            if(id.equals(hiddenTypes.get(m).getTypeId())){
                                                hiddenTypes.get(m).setNum(hiddenTypes.get(m).getNum()+joby.getInt("num"));
                                                t=true;
                                            }
                                        }
                                        if (t) continue;
                                        HiddenType hiddenType=new HiddenType();
                                        hiddenType.setTypeId(id);
                                        hiddenType.setTypeUnit(joby.getJSONObject("unit").getString("measures"));
                                        hiddenType.setTypeName(joby.getJSONObject("unit").getString("name"));
                                        KLog.d(joby.getInt("num")+"");
                                        hiddenType.setNum(joby.getInt("num"));
                                        hiddenType.setShow(true);
                                        hiddenTypes.add(hiddenType);
                                    }

                                }
                            }
                            cm.setHiddenTypes(hiddenTypes);
                            resultMessage(handler,INT_CONTRACT_DETAIL_SUCCESS,cm,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        resultMessage(handler,INT_CONTRACT_DETAIL_FILED, "合同详情获取失败",1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_CONTRACT_DETAIL_FILED, msg, errorcode);
                    }
                });
    }

    // 合同类型（1：巡检合同 2：维修合同）
    public void getContractByType(final String type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        new JSONReauest(context,ConnectionURL.STR_GET_CONTRACTS_LIST, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        JSONObject body = null;
                        KLog.json(result.toString());
                        try {
                            List<ContractManager> data = new ArrayList<ContractManager>();
                            body = result.getJSONObject("body");
                            JSONArray list = body.getJSONArray("list");
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject jb = list.getJSONObject(i);
                                ContractManager cm = new ContractManager();
                                cm.setContractId(jb.getString("id"));
                                cm.setName(jb.getString("name"));
                                cm.setUnitName(jb.getJSONObject("office").getString("name"));
                                cm.setBeginDate(jb.getString("startTime"));
                                cm.setEndDate(jb.getString("endTime"));
                                cm.setType(Integer.valueOf(jb.getString("type")));
//                        cm.setFlowtype(i%4);
                                data.add(cm);
                            }
                            resultMessage(handler, INT_CONTRACT_LIST_SUCCESS, data, -1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler, INT_CONTRACT_LIST_FILED, "服务器连接失败", 1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_CONTRACT_LIST_FILED, msg, errorcode);
                    }
                });
    }

}
