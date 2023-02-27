package io.mcc.mobile.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale.LanguageRange;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import uap_clj.java.api.Browser;
import uap_clj.java.api.Device;
import uap_clj.java.api.OS;

public class HttpHeaderUtil {
	
	private HttpHeaderUtil(){}
	
	public static String getLanguage(HttpHeaders reqHeader) {
		String lang = "en";
		List<LanguageRange> acceptLanguage = reqHeader.getAcceptLanguage();
		if (acceptLanguage != null && acceptLanguage.size() > 0) {
			LanguageRange range = acceptLanguage.get(0);
			if (range.getRange().indexOf("ko") != -1) {
				lang = "ko";
			} else {
				lang = "en";
			}
		}
		return lang;
	}

	public static String getIpAddr(HttpHeaders httpHeaders) {
		String ip = httpHeaders.get("X-Forwarded-For").get(0);
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { ip = httpHeaders.get("Proxy-Client-IP").get(0); }
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { ip = httpHeaders.get("WL-Proxy-Client-IP").get(0); }
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { ip = httpHeaders.get("HTTP_CLIENT_IP").get(0); }
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { ip = httpHeaders.get("HTTP_X_FORWARDED_FOR").get(0); }
		return ip;
	}
	
	public static String getClientIp(HttpServletRequest request) {
    	String clientIp = request.getHeader("X-Forwarded-For");
        
    	if (StringUtils.isEmpty(clientIp)|| "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        
    	return clientIp;
    }
	
	
	

	public static String getBrowser(HttpHeaders httpHeaders){
		String userAgent = httpHeaders.get("User-Agent").get(0);
		String client = userAgent.toLowerCase();
		String browser = "";
		if (client.contains("msie")){
			String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
			browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
		} else if (client.contains("safari") && client.contains("version")){
			browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
		} else if ( client.contains("opr") || client.contains("opera")){
			if(client.contains("opera")){
				browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
			} else if(client.contains("opr")){
				browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
			}
		} else if (client.contains("chrome")) {
			browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		} else if ((client.indexOf("mozilla/7.0") > -1) || (client.indexOf("netscape6") != -1)  || (client.indexOf("mozilla/4.7") != -1) || (client.indexOf("mozilla/4.78") != -1) || (client.indexOf("mozilla/4.08") != -1) || (client.indexOf("mozilla/3") != -1) ){
			//browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
			browser = "Netscape-?";
		} else if (client.contains("firefox")){
			browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
		} else if(client.contains("rv")){
			browser="IE";
		} else {
			browser = "UnKnown, More-Info: "+userAgent;
		}
		return browser;
	}

	public static String getOs(HttpHeaders httpHeaders){
		String userAgent = httpHeaders.get("User-Agent").get(0);
		String os = "";
		if (userAgent.toLowerCase().indexOf("windows") >= 0 ){
			os = "Windows";
		} else if(userAgent.toLowerCase().indexOf("mac") >= 0){
			os = "Mac";
		} else if(userAgent.toLowerCase().indexOf("x11") >= 0){
			os = "Unix";
		} else if(userAgent.toLowerCase().indexOf("android") >= 0){
			os = "Android";
		} else if(userAgent.toLowerCase().indexOf("iphone") >= 0){
			os = "IPhone";
		} else {
			os = "UnKnown, More-Info: "+userAgent;
		}
		return os;
	}

	public static String getOsVer(String userAgent){
		String osVer = "";

		if (userAgent!=null)
			userAgent = userAgent.toLowerCase();

		int idxF = -1;
		if((idxF=userAgent.toLowerCase().indexOf("android")) >= 0){
			int idxSep = userAgent.lastIndexOf(";");
			int idxstr = userAgent.lastIndexOf("android")+7;
			osVer = userAgent.substring(idxstr, idxSep).trim();
		} else if((idxF=userAgent.toLowerCase().indexOf("ios")) >= 0){
			int idxSep = userAgent.indexOf(";", idxF+4);
			osVer = userAgent.substring(idxF+4, idxSep).trim();
		} else {
			osVer = "UnKnown";
		}
		return osVer;
	}
	

	public static String getDiviceName(String userAgent){
		String diviceName = "";

		if (userAgent!=null)
			userAgent = userAgent.toLowerCase();

		if((userAgent.indexOf("ios")) >= 0){
			diviceName = "ios";
		} else if(userAgent.indexOf("android") >= 0){
			//GoodMorn/1.2.0 (SM-G930S; Android 26;)"; ==> SM-G930S
			int idx    = userAgent.indexOf("(");		
			int idxEnd = userAgent.indexOf(";");
			if(idx > -1 &&  idxEnd > 0) {
				diviceName = userAgent.substring(idx+1, idxEnd);	
			}
		} else {
			diviceName = "UnKnown";
		}
		return diviceName;
	}

	public static String getAppVersion(String userAgent){
		String appVersionStr = "";

		if (userAgent!=null)
			userAgent = userAgent.toLowerCase();

		if(!StringUtils.isEmpty(userAgent) && (userAgent.contains("good morn") || userAgent.contains("goodmorn"))) {
			appVersionStr = userAgent.split("\\(")[0].trim().split("\\/")[1];
			
			if(appVersionStr.contains("-"))
			{
				appVersionStr = appVersionStr.substring(0, appVersionStr.indexOf("-"));
			}
		}
		
		return appVersionStr;
	}
	
	
	public static String getAppName(String userAgent){
		String appNameStr = "";

		if (userAgent!=null)
			userAgent = userAgent.toLowerCase();

		if(!StringUtils.isEmpty(userAgent) && (userAgent.contains("good morn") || userAgent.contains("goodmorn"))) {
			appNameStr = userAgent.split("\\(")[0].trim().split("\\/")[0];

		}
		
		return appNameStr;
	}
	

	
	
	public static String getUserAgentInfo( String userAgent, String key) throws IOException {

		String value = "";
		
		if(!StringUtils.isEmpty(userAgent)) {

			HashMap b = Browser.lookup(userAgent);
		    HashMap o = OS.lookup(userAgent);
		    HashMap d = Device.lookup(userAgent);

		    String ua_device = d.get("family").toString();
		    String ua_os = o.get("family").toString();
		    String ua_browser = b.get("family").toString();

			if("os".equals(key)) {
				value = ua_os;
			} else if("device".equals(key)) {
				value = ua_device;
			} else if("brwsr".equals(key)) {
				value = ua_browser;
			}
		}
		
		return value;
	}
	
}
