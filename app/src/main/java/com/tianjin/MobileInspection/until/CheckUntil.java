package com.tianjin.MobileInspection.until;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据的格式与标准化校验
 */
public class CheckUntil {
	
	public static String checkEditext(Object obj){
		if(obj==null){
			return "";
		}
		if(obj.equals("null")) return "";
		return obj.toString();
	}

	public static String getJsonIsNull(String str){
		if(str==null||str.equals("null")){
			return "";
		}
		return str;
	}

	/**
	 * 判断字符串是否有效
	 * @param s
	 * @return
     */
	public static boolean isNull(String s){
		if(s==null){
			return true;
		}
		if(s.equals("null")){
			return true;
		}
		if(s.equals("")){
			return true;
		}
		return false;
	}

	//判断手机格式是否正确
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[7,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	//判断email格式是否正确
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}
	//判断是否全是数字
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

}
