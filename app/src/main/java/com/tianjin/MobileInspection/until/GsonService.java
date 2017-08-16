package com.tianjin.MobileInspection.until;

import com.google.gson.Gson;

/**
 * json 格式数据与类对象的转换
 */
public class GsonService {

    public static <T> T parseJson(String jsonString, Class<T> clazz) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, clazz);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("解析json失败");
        }
        return t;

    }
}
