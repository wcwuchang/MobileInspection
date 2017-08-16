package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.HiddenSpinner;
import com.tianjin.MobileInspection.entity.Road;
import com.tianjin.MobileInspection.entity.Weather;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;
import com.tianjin.MobileInspection.until.VolleyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 吴昶 on 2017/6/5.
 */
public class ConfigModule extends BaseModule {

    public static void getConfiguration(final Context context){
        KLog.d(ConnectionURL.STR_GET_CONGIGURATION+MyApplication.getStringSharedPreferences("JSESSIONID", "")+ConnectionURL.HTTP_SERVER_END);
        StringRequest request=new StringRequest(Request.Method.GET, ConnectionURL.STR_GET_CONGIGURATION+MyApplication.getStringSharedPreferences("JSESSIONID", "")+ConnectionURL.HTTP_SERVER_END,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            KLog.d(s);
                            if(jsonObject.getBoolean("success")) {
                                //获取新数据成功，清除旧数据
                                MyApplication.DBM.gettHiddenSpinner().dropTable();
                                JSONObject body = jsonObject.getJSONObject("body");
                                JSONArray jsonArray = body.getJSONArray("list");
                                List<HiddenSpinner> hsp=new ArrayList<HiddenSpinner>();
                                KLog.d(jsonArray.length()+"");
                                for(int i=0;i<jsonArray.length();i++){
                                    HiddenSpinner sp=new HiddenSpinner();
                                    JSONObject job=jsonArray.getJSONObject(i);
                                    sp.setSpid(job.getString("id"));
                                    sp.setRemarks(CheckUntil.getJsonIsNull(job.getString("remarks")));
                                    sp.setName(job.getString("name"));
                                    sp.setParentId(job.getString("parentId"));
                                    sp.setParentIds(job.getString("parentIds"));
                                    sp.setUnit(job.getString("description"));
                                    hsp.add(sp);
                                }
                                MyApplication.DBM.gettHiddenSpinner().insertList(hsp);
                            }else {
                                Toast.makeText(context,"获取配置数据失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1.0f));//自定义超时时间,最大请求次数，退避乘数
        VolleyUtil.getRequestQueue().add(request);
    }


    public static final int GET_EMERGENCY_SUCCESS=0x547;
    public static final int GET_EMERGENCY_FALID=0x547;
    public static void getEmergencyType(Context context, final Handler handler){
        final Map<String,String> map=new HashMap<String,String>();
        map.put("type","accident_type");
        KLog.d(ConnectionURL.STR_GET_EMERGENCY+MyApplication.getStringSharedPreferences("JSESSIONID", "")+ConnectionURL.HTTP_SERVER_END);
        StringRequest request=new StringRequest(Request.Method.POST, ConnectionURL.STR_GET_EMERGENCY+MyApplication.getStringSharedPreferences("JSESSIONID", "")+ConnectionURL.HTTP_SERVER_END,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            KLog.d(s);
                            if(jsonObject.getBoolean("success")) {
                                JSONObject body=jsonObject.getJSONObject("body");
                                JSONArray jsonArray=body.getJSONArray("list");
                                List<HiddenSpinner> hiddenSpinners=new ArrayList<HiddenSpinner>();
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject job=jsonArray.getJSONObject(i);
                                    HiddenSpinner hiddenSpinner=new HiddenSpinner();
                                    hiddenSpinner.setSpid(job.getString("id"));
                                    hiddenSpinner.setName(job.getString("description"));
                                    hiddenSpinner.setId(job.getInt("sort"));
                                    hiddenSpinners.add(0,hiddenSpinner);
                                }
                                Message msg=handler.obtainMessage();
                                msg.what=GET_EMERGENCY_SUCCESS;
                                msg.obj=hiddenSpinners;
                                handler.sendMessage(msg);
                            }else {

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

    public static void getRoadList(final Context context){
        KLog.d(ConnectionURL.STR_GET_ROADS+MyApplication.getStringSharedPreferences("JSESSIONID", "")+ConnectionURL.HTTP_SERVER_END);
        StringRequest request=new StringRequest(Request.Method.GET, ConnectionURL.STR_GET_ROADS+MyApplication.getStringSharedPreferences("JSESSIONID", "")+ConnectionURL.HTTP_SERVER_END,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            KLog.d(s);
                            if(jsonObject.getBoolean("success")) {
                                //获取新数据成功，清除旧数据
                                MyApplication.DBM.gettRoad().dropTable();
//                                JSONObject body = jsonObject.getJSONObject("body");
//                                JSONArray jsonArray = body.getJSONArray("list");
//                                List<HiddenSpinner> hsp=new ArrayList<HiddenSpinner>();
//                                KLog.d(jsonArray.length()+"");
//                                for(int i=0;i<jsonArray.length();i++){
//                                    HiddenSpinner sp=new HiddenSpinner();
//                                    JSONObject job=jsonArray.getJSONObject(i);
//                                    sp.setSpid(job.getString("id"));
//                                    sp.setRemarks(CheckUntil.getJsonIsNull(job.getString("remarks")));
//                                    sp.setName(job.getString("name"));
//                                    sp.setParentId(job.getString("parentId"));
//                                    sp.setParentIds(job.getString("parentIds"));
//                                    sp.setUnit(job.getString("description"));
//                                    hsp.add(sp);
//                                }
//                                MyApplication.DBM.gettHiddenSpinner().insertList(hsp);
                            }else {
                                Toast.makeText(context,"获取配置数据失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1.0f));//自定义超时时间,最大请求次数，退避乘数
        VolleyUtil.getRequestQueue().add(request);
    }

    public final static int INT_GET_ROAD_SUCCESS=0x5634;
    public final static int INT_GET_ROAD_FILED=0x5635;
    public static void getRoad(final Handler handler,String id){
        final Map<String,String> map=new HashMap<String,String>();
        map.put("id", id);
        KLog.d(map.toString());
        StringRequest request=new StringRequest(Request.Method.POST, ConnectionURL.STR_GET_ROADINFO+MyApplication.getStringSharedPreferences("JSESSIONID", "")+ConnectionURL.HTTP_SERVER_END,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            KLog.d(s);
                            if(jsonObject.getBoolean("success")) {
                                Road road=new Road();
                                JSONObject body=jsonObject.getJSONObject("body");
                                JSONObject data=body.getJSONObject("data");
                                road.setRoadName(data.getString("name"));
                                Message msg=handler.obtainMessage();
                                msg.what=INT_GET_ROAD_SUCCESS;
                                msg.obj=road;
                                handler.sendMessage(msg);
                            }else {

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


    public static void getWeatherInfo(final Handler handler, String city){
        String url="http://api.map.baidu.com/telematics/v3/weather?location="+city+"&output=json&ak=yhd0Rlq1mFX5McDfhxS874jvqFWIyX3e&mcode=1C:19:94:E9:E2:09:8C:89:C8:D9:06:D0:20:C8:5E:10:4E:BA:8A:90;com.tianjin.MobileInspection";
        //location=北京&output=json&ak=E4805d16520de693a3fe707cdc962045
//        final Map<String,String> map=new HashMap<String,String>();
//        map.put("location", city);
////        map.put("key","063d2b86ad5442abbfe6ffe967e633b3");
//        map.put("output","json");
//        map.put("ak","yhd0Rlq1mFX5McDfhxS874jvqFWIyX3e");
////        map.put("mcode","1C:19:94:E9:E2:09:8C:89:C8:D9:06:D0:20:C8:5E:10:4E:BA:8A:90;com.tianjin.MobileInspection");
//        KLog.d(map.toString());
        KLog.d(url);
        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            KLog.d(s);
                            Weather weather=new Weather();
                            if(jsonObject.getString("status").equals("success")){
                                JSONArray jsonArray=jsonObject.getJSONArray("results");
                                JSONObject we=jsonArray.getJSONObject(0);
                                weather.setCity(we.getString("currentCity"));
                                weather.setPm25(we.getString("pm25"));
                                JSONArray data=we.getJSONArray("weather_data");
                                JSONObject today=data.getJSONObject(0);
                                weather.setShishi(today.getString("date"));
                                weather.setDayPictureUrl(today.getString("dayPictureUrl"));
                                weather.setWind(today.getString("wind"));
                                weather.setWeather(today.getString("weather"));
                                weather.setTemp(today.getString("temperature"));
                                weather.setNightPictureUrl(today.getString("nightPictureUrl"));
                            }
//                            JSONArray array=jsonObject.getJSONArray("HeWeather5");
//                            if(array!=null&&array.length()>0){
//                                Weather weather=new Weather();
//                                JSONObject jb=array.getJSONObject(0);
//                                if(jb.has("alarms")){
//                                    JSONArray alarms=jb.getJSONArray("alarms");
//                                    JSONObject jb1=alarms.getJSONObject(0);
//                                    weather.setAlams(jb1.getString("title"));
//                                }else {
//                                    weather.setAlams("当前未发布相关预警信息");
//                                }
//                                if(jb.has("now")){
//                                    JSONObject now=jb.getJSONObject("now");
//                                    JSONObject cond=now.getJSONObject("cond");
//                                    weather.setState(Integer.valueOf(cond.getString("code")));
//                                    weather.setCode(cond.getString("txt"));
//                                    weather.setFleepTemp("体感温度:"+now.getString("fl")+ "℃");
//                                    weather.setTemp("温度:"+now.getString("tmp")+ "℃");
//                                    weather.setHum("相对湿度:"+now.getString("hum"));
//                                    JSONObject wind=now.getJSONObject("wind");
//                                    weather.setWind("风况:"+wind.getString("dir")+" "+wind.getString("sc"));
//                                }
                                Message msg=handler.obtainMessage();
                                msg.what=0x324;
                                msg.obj=weather;
                                handler.sendMessage(msg);
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        })
        ;
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1.0f));//自定义超时时间,最大请求次数，退避乘数
        VolleyUtil.getRequestQueue().add(request);
    }

}
