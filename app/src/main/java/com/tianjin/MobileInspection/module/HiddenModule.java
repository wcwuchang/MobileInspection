package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.PlanManage;
import com.tianjin.MobileInspection.until.ConnectionURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 吴昶 on 2017/2/28.
 */
public class HiddenModule extends BaseModule {

    private Handler handler;

    public HiddenModule(Handler handler, Context context){
        this.handler=handler;
        this.context=context;
    }

    public final static int INT_GET_TROUBLES_LIST_SUCCESS=0x34;
    public final static int INT_GET_TROUBLES_LIST_Filed=0x35;

    /**
     * 获取隐患列表
     * @param state
     * @param maintenance
     */
    public void getTroubles(String state,String maintenance){
        Map<String,String> map=new HashMap<String, String>();
        map.put("state",state);
        map.put("maintenance",maintenance);
        new JSONReauest(context, ConnectionURL.STR_GET_HIDDEN_TROUBLE_LIST, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        KLog.json(result.toString());
                        try {
                            JSONObject data=result.getJSONObject("body");
                            List<PlanManage> hiddenTypes=new ArrayList<PlanManage>();
                            JSONArray jsonArray=data.getJSONArray("list");
                            if(jsonArray!=null&&jsonArray.length()>0){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jb=jsonArray.getJSONObject(i);
                                    PlanManage hiddenType=new PlanManage();
                                    hiddenType.setPlanName(jb.getString("title"));
                                    hiddenType.setPlanId(jb.getString("id"));
                                    hiddenType.setPlanStartDate(jb.getJSONObject("createBy").getString("name"));
                                    hiddenType.setPlanEndDate(jb.getString("createDate"));
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
}
