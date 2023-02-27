package io.mcc.mobile.common.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	
	
	/**
	 * mobilePhoneNumber 
	 * 변환
	 * @param mobilePhoneNumber 파라미터로 전달 받은 전화번호
	 */
	public static String getValidMobilePhoneNumber(String mobilePhoneNumber) {
		if (mobilePhoneNumber == null) {
			return null;
		}

		if (mobilePhoneNumber.startsWith("0")) {
			return mobilePhoneNumber.substring(1);
		} else {
			return mobilePhoneNumber;
		}
	}

	public static boolean isEquals(String src, String dest, boolean bIgnoreCase) {
		if (src == null && dest == null) return true;
		if (src == null && dest != null && dest.trim().length() == 0) return true;
		if (dest == null && src != null && src.trim().length() == 0) return true;

		if (src!=null && dest!=null) {
			if (bIgnoreCase && src.equalsIgnoreCase(dest)) return true;
			else if (src.equals(dest)) return true;
		}
		return false;
	}

	public static String fillZero(String value, String fillString) {
		if (StringUtils.isBlank(value)) {
			return value;
		} else {
			if(StringUtils.contains(value, '.')) {
				String [] _value = StringUtils.split(value,'.');
				String integer = _value[0];
				String decimal = (_value[1] + fillString).substring(0, fillString.length());
				return integer.concat(".").concat(decimal);			
			} else {
				return value.concat(".").concat(fillString);
			}	
		}
	}
	
	
	public static String objStrTobk(Object obj){
		if(obj == null){
			return "";
		}else{
			String s = String.valueOf(obj);
			if("null".equals(s.toLowerCase())){
				return "";
			} else {
				return s;
			}
		}
	}
	public static int objToInt(Object obj) {
		return objToInt(obj, 0);
	}
	public static int objToInt(Object obj, int defV){
		if(obj == null){
			return defV;
		} else {
			try
			{
				return Integer.parseInt(String.valueOf(obj));
			}
			catch(Exception e)
			{
				return defV;
			}
		}
	}
	
	public static long objToLong(Object obj) {
		return objToLong(obj, 0);
	}
	public static long objToLong(Object obj, int defV){
		if(obj == null){
			return defV;
		} else {
			try
			{
				return Long.parseLong(String.valueOf(obj));
			}
			catch(Exception e)
			{
				return defV;
			}
		}
	}
	
	public static String getNationCd(String  nationCd){
		if(nationCd == null){
			return "USA";
		} else {
			if( !"IDN".equals(nationCd) && !"KOR".equals(nationCd)){
				return "USA";
			}
		}
		
		return nationCd;
	}	

	public static int getInt(String v, int defV) {
		if (v != null) {
			try {
				int iv = Integer.parseInt(v);

				return iv;
			} catch (Exception e) {
			}
		}
		return defV;
	}
	public static long getLong(String v, long defV) {
		if (v != null) {
			try {
				long iv = Long.parseLong(v);

				return iv;
			} catch (Exception e) {
			}
		}
		return defV;
	}
	
	public static String replaceChars(String str) {
	    str = StringUtils.replaceChars(str, "-_+=!@#$%^&*()[]{}|\\;:'\"<>,.?/~`） ","");
	    if(str.length() < 1) {
	    	return null;
	    }
	    return str;
	}

	public static String objToStr(Object obj){
	
		if(obj == null){
			return "";
		}else{
			String s = String.valueOf(obj);
			if("null".equals(s.toLowerCase())){
					return "";
			} else {
				return s;
			}
		}
	}
	
	
	public static String camel2Snake(String camelStr)
	{
		String snakeStr = "";
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        snakeStr = camelStr.replaceAll(regex, replacement).toLowerCase();
        return snakeStr;
	}
}