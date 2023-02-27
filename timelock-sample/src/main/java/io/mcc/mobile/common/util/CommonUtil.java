package io.mcc.mobile.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.util.StringUtils;

import io.mcc.common.vo.CommonVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 자주 사용되는 공통 유틸 - mcc 2.0 부터 사용
 * 
 * @author supercsh
 *
 */
@Slf4j
public class CommonUtil {
	
	/**
	 * 개체가 
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) 
	{
		boolean rstBoolean = true;
		
		if(obj == null)
		{
			rstBoolean = true;
		}
		else
		{
			if (obj instanceof List)
			{
				List targetList = (List)obj;
				if (targetList != null && targetList.size() > 0)
				{
					rstBoolean = false;
				}
			}
			else if (obj instanceof Map)
			{
				Map targetMap = (Map)obj;
				if (targetMap != null && targetMap.size() > 0)
				{
					rstBoolean = false;
				}
			}
			else if (obj instanceof CommonVO)
			{
				CommonVO targetCVO = (CommonVO)obj;
				if (targetCVO != null && targetCVO.size() > 0)
				{
					rstBoolean = false;
				}
			}
			else if (obj instanceof String)
			{
				String targetStr = (String)obj;
				if (targetStr != null && !"".equals(targetStr))
				{
					rstBoolean = false;
				}
			}
			else if (obj instanceof Long)
			{
				
				long targetLong = Long.valueOf(obj.toString());
				rstBoolean = false;
			}
			else
			{
				if(obj != null)
				{
					rstBoolean = false;
				}
			}
		}
		return rstBoolean;
	}
	
	
	public static boolean null2Bool(Boolean obj, boolean defV)
	{
		if(obj == null){
			return defV;
		} else {
			return obj.booleanValue();
		}
	}
	
	  public static String getString(String str, String dfStr)
		{
			String rstStr = "";
			if(isEmpty(str))
			{
				rstStr = dfStr;
			}
			else
			{
				rstStr = str;
			}
			
			return rstStr;
		}
	
	/**
	 * 
	 * 현재날짜 또는 시간을 입력된 형식으로 리턴함
	 * ex) todayToFormat("yyyyMMdd") : 20210803
	 * */
	public static String todayToFormat(String format) {

		Date today = new Date();
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.HOUR, 9);

//		return form.format(today);
		return form.format(cal.getTime());
	}


	/**
	 * 
	 * SHA-256 암호화
	 * */
	public static String sha256(String str) throws NoSuchAlgorithmException{
		if(StringUtils.isEmpty(str)) return "";

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hash = md.digest(str.toString().getBytes());

		StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        
        System.out.println("============= sha256 : " + hexString.toString());
		
		return hexString.toString();
	}

	/**
	 * HMAC MD5 암호화
	 * @param plainText, secretKey
	 * 
	 * */
	public static String hmacMD5(String plainText, String secretKey) throws Exception {
	    
	    SecretKeySpec secret;
	    String algorithms = "HmacMD5";
	    
		try {
			secret = new SecretKeySpec(secretKey.getBytes("utf-8"), algorithms);
			Mac hasher = Mac.getInstance(algorithms);
		    hasher.init(secret);

		    byte[] hashValue = hasher.doFinal(plainText.getBytes());
		    
		    return Hex.encodeHexString(hashValue);
		    
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return null;
	}
}
