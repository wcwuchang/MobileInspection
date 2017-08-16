package com.scanner.smpcapture;

/**
 * Created by wuchang on 2016/8/31.
 */
public class UrlUtil {

    /**
     * @param str 待验证的字符串
     * @return 如果是符合网址格式的字符串,返回<b>true</b>,否则为<b>false</b>
     */
    public static boolean isUri( String str ){
        int d=str.indexOf("http://");
        int c=str.indexOf("https://");
        if(c==0||d==0){
           return true;
        }
        return false;
    }
}
