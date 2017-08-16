package com.tianjin.MobileInspection.module;

import android.content.Context;
import android.os.Handler;

import com.android.volley.VolleyError;
import com.socks.library.KLog;
import com.tianjin.MobileInspection.configure.MyApplication;
import com.tianjin.MobileInspection.entity.Contact;
import com.tianjin.MobileInspection.until.CheckUntil;
import com.tianjin.MobileInspection.until.ConnectionURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 吴昶 on 2016/12/4.
 */
public class ContactsModule extends BaseModule{

    public static final int INT_GET_CONTACTS_SUCCESS=0X5675;
    public static final int INT_GET_CONTACTS_FILEAD=0X5676;
    public static final int INT_GET_CONTACTS_DETAIL_SUCCESS=0X5677;
    public static final int INT_GET_CONTACTS_DETAIL_FILEAD=0X5678;

    private Handler handler;
    public ContactsModule(Handler handler,Context context){
        this.handler=handler;
        this.context=context;
    }

    //获取联系人列表
    public void getContacts(){
        new JSONReauest(context,ConnectionURL.STR_GET_CONTACTS, MyApplication.CONNECTION_TIME_OUT,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        JSONArray array= null;
                        try {
                            array = result.getJSONObject("body").getJSONArray("list");
                            List<Contact> list=new ArrayList<Contact>();
                            for(int i=0;i<array.length();i++){
                                JSONObject obj=array.getJSONObject(i);
                                Contact con=new Contact();
                                con.setPersonId(obj.getString("id"));
                                con.setOfficeId(obj.getString("officeId"));
                                con.setOfficeName(obj.getString("officeName"));
                                con.setName(obj.getString("name"));
                                con.setHeadPath(obj.optString("photo"));
                                list.add(con);
                             }
                            resultMessage(handler,INT_GET_CONTACTS_SUCCESS,list,-1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler,INT_GET_CONTACTS_FILEAD,"服务器连接失败",-1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler,INT_GET_CONTACTS_FILEAD,msg,errorcode);
                    }
                });
    }

    //联系人详情
    public void getContactDetail(final String id) {
        KLog.d(id);
        String jsessionid = MyApplication.getStringSharedPreferences("JSESSIONID", "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        new JSONReauest(context,ConnectionURL.STR_GET_CONTACTS_DETAIL, MyApplication.CONNECTION_TIME_OUT, map,
                new JSONReauest.ResultListener() {
                    @Override
                    public void Success(JSONObject result) {
                        JSONObject obj = null;
                        try {
                            obj = result.getJSONObject("body").getJSONObject("date");
                            Contact con = new Contact();
                            con.setPhoneNumber(CheckUntil.getJsonIsNull(obj.getString("mobile")));
                            con.setEmail(CheckUntil.getJsonIsNull(obj.getString("email")));
                            con.setHeadPath(CheckUntil.getJsonIsNull(obj.getString("photo")));
                            resultMessage(handler, INT_GET_CONTACTS_DETAIL_SUCCESS, con, -1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Error(VolleyError volleyError) {
                        resultMessage(handler, INT_GET_CONTACTS_DETAIL_FILEAD, "网络连接失败", -1);
                    }

                    @Override
                    public void Filed(String msg, int errorcode) {
                        resultMessage(handler, INT_GET_CONTACTS_DETAIL_FILEAD, msg, errorcode);
                    }
                });
    }
}
