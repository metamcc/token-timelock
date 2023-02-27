package io.mcc.mobile.common.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.mcc.common.vo.CommonVO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendHttpClient
{
	
	public static void main (String[] args) {
		String usrl = "https://api.ctree.id/api2/ext/mcc/auth";
		//the result will be : {"code":0,"result":{"phone":"+6281260332838"},"msg":"success"}
		CommonVO<String, Object> api     = new CommonVO<String, Object>();
		CommonVO<String, Object> postDta = new CommonVO<String, Object>();
		
		api.put("Content_Type",   "application/x-www-form-urlencoded");
		api.put("Authorization",  "Basic bWNjLWFkbWluOjQzMjE=");
		api.put("Accept_Language", "in");
		api.put("Device_Id",       "84fa6b1c-b717-4e41-8d2e-034dababc77d");
		api.put("requestMethod",  "POST");
		
		postDta.put("usimid", "8962116644248641614");
		api.put("PostData", postDta);
		SendHttpClient http = new SendHttpClient();
		http.setPost(api, usrl, null, null, false);
		//setPost
	}
	
	public JsonObject setJson(CommonVO<String, Object> api, String url)
	{
        HttpHeaders headers = new HttpHeaders();
		if(api.get("Content_Type") != null) {
			log.info("Content-Type ::   ==> "+api.get("Content_Type"));
			headers.set("Content-Type", StringUtil.objStrTobk(api.get("Content_Type")));
		}
     
		if(api.get("Accept_Language") != null) {
			log.info("Accept_Language ::   ==> "+api.get("Accept_Language"));
			headers.set("Accept-Language", StringUtil.objStrTobk(api.get("Accept_Language")));
		}
     
		if(api.get("Device_Id") != null) {
			log.info("Device_Id ::   ==> "+api.get("Device_Id"));
			headers.set("Device-Id", StringUtil.objStrTobk(api.get("Device_Id")));
		}
        
		if(api.get("Authorization") != null) {
			log.info("Authorization ::   ==> "+api.get("Authorization"));
			headers.set("Authorization", StringUtil.objStrTobk(api.get("Authorization")));
		}
		
		if(api.get("X-mcc_Authentication") != null) {
			log.info("X-mcc_Authentication ::   ==> "+api.get("X-mcc_Authentication"));
			headers.set("X-mcc_Authentication", StringUtil.objStrTobk(api.get("X-mcc_Authentication")));
		}
		
		
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity param= new HttpEntity(api.getString("Data"), headers);

	    RestTemplate restTemplate = new RestTemplate();
	    String ss = restTemplate.postForObject(url, param, String.class);
	    log.info("ss ===>> "+ ss);

        
        return null;
	}
	
	public JsonObject redData(HttpsURLConnection connection) throws IOException {
		JsonObject json = null;
		InputStream in = null;
		InputStreamReader reader = null;
		String reponseLine = null;
		try{
			in = connection.getInputStream();
			reader = new InputStreamReader(in);
			reponseLine = new BufferedReader(reader).readLine();
			JsonParser o = new JsonParser();
			JsonElement je = o.parse(reponseLine);
			json = je.getAsJsonObject();
			log.info("json   :    "+json.toString());
			return json;
		} catch(Exception e){
			log.info("redData   :    "+reponseLine);
			e.printStackTrace();
		} finally {
			in.close();
			reader.close();	
		}
		log.info("json   :    "+json.toString());
		return json;
	}

	public String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
	}
	
	
	public Map<String, Object> setPost(CommonVO<String, Object> api, String url, String mobileClientId, String mobileClientSecret, boolean is_map) {
		RestOperations restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		if(api != null && api.get("Content_Type") != null) {
			log.info("Content-Type ::   ==> "+api.get("Content_Type"));
			headers.set("Content-Type", StringUtil.objStrTobk(api.get("Content_Type")));
		}
     
		if(api != null && api.get("Accept_Language") != null) {
			log.info("Accept_Language ::   ==> "+api.get("Accept_Language"));
			headers.set("Accept-Language", StringUtil.objStrTobk(api.get("Accept_Language")));
		}
     
		if(api != null && api.get("Device_Id") != null) {
			log.info("Device_Id ::   ==> "+api.get("Device_Id"));
			headers.set("Device-Id", StringUtil.objStrTobk(api.get("Device_Id")));
		}
     /*
		if(api.get("Authorization") != null) {
			log.info("Authorization ::   ==> "+api.get("Authorization"));
			headers.set("Authorization", StringUtil.objStrTobk(api.get("Authorization")));
		}
		*/
		
		//log.info("getAuthorizationHeader  : 1 "+getAuthorizationHeader(mobileClientId, mobileClientSecret));
		log.info("getAuthorizationHeader  : 2 Basic bWNjLWFkbWluOjQzMjE=");
		headers.set("Authorization",          "Basic bWNjLWFkbWluOjQzMjE=");
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		
		if(api != null &&  api.get("PostData") != null ) {
			log.info("PostData  :  "+api.get("PostData"));
			HashMap<String, String> post = (HashMap<String, String>)api.get("PostData");
			Set<String> key = post.keySet();
			for(String k : key) {
				formData.add(k, post.get(k));	
			}
        }
		
		log.info("url  :  "+url);
		if(is_map == true){
			Map map = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<MultiValueMap<String, String>>(formData, headers), Map.class).getBody();
			log.info("map ===>>> "+map);
			return map;
		} else {
			String map = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<MultiValueMap<String, String>>(formData, headers), String.class).getBody();
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("result", map == null ? "" : map);
			return obj;
		}
	}
	
	private String METHOD_NAME = "Method";
	
	public Map callHttpApi(String url, CommonVO headerCVO, CommonVO paramCVO) throws Exception {
		
		Map resMap = null;
		
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(10000); //타임아웃 설정 10초
        factory.setReadTimeout(10000);//타임아웃 설정 5초
       // RestTemplate restTemplate = new RestTemplate(factory);
		
		RestOperations restTemplate = new RestTemplate(factory);
		HttpHeaders headers = new HttpHeaders();
	//	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//headers.set("Content-Type", StringUtil.objStrTobk(headerCVO.get("Content-Type")));
		if(!CommonUtil.isEmpty(headerCVO))
		{
			Set keySet = headerCVO.keySet();
			Iterator<String> keyIt = keySet.iterator();
			while(keyIt.hasNext())
			{
				String key = keyIt.next();
				headers.set(key, headerCVO.getString(key));
			}
		}
		
		
		
		//headers.setContentType(MediaType.valueOf(StringUtil.objStrTobk(headerCVO.get("Content-Type"))));
		//headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
		
		
		/*
		log.debug("headers.getContentType() = {}", headers);
		HttpEntity httpEntity = null;
		if(MediaType.APPLICATION_JSON.equals(headers.getContentType()) || MediaType.APPLICATION_JSON_UTF8.equals(headers.getContentType()))
		{
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(paramCVO);
			
			httpEntity = new HttpEntity<String>(json, headers);
		}
		else
		{
			MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
			paramCVO.forEach((keyObj, valueObj)->{
				parameters.add((String) keyObj, (String) valueObj);
			});
			
			httpEntity = new HttpEntity<>(parameters, headers);
		}
		*/
		
		String method = headers.getFirst(METHOD_NAME);
		
		HttpEntity httpEntity = null;
		if(HttpMethod.GET.name().equals(method))
		{
			httpEntity = new HttpEntity<>(headers);
			
			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
			
			if(!CommonUtil.isEmpty(paramCVO))
			{
				MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
				paramCVO.forEach((keyObj, valueObj)->{
					parameters.add((String) keyObj, valueObj.toString());
				});

				uriBuilder.queryParams(parameters);
				
				url = uriBuilder.toUriString();
			}
		}
		else
		{
			MediaType contentType = headers.getContentType();
			
			if(MediaType.APPLICATION_FORM_URLENCODED.equals(contentType) || MediaType.MULTIPART_FORM_DATA.equals(contentType))
			{
				MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
				paramCVO.forEach((keyObj, valueObj)->{
					parameters.add((String) keyObj, valueObj);
				});
				
				httpEntity = new HttpEntity<>(parameters, headers);
			}
			else // default json
			{
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(paramCVO);
				
				httpEntity = new HttpEntity<String>(json, headers);
			}
			log.info("callHttpApi getBody = {} ", httpEntity.getBody().toString());
		}


		
		//log.debug("formEntity getBody = {} ", httpEntity.getBody().toString());
		ResponseEntity<Map> resEnt = restTemplate.exchange(url, HttpMethod.valueOf(headerCVO.getString("Method")), httpEntity, Map.class);
		//resMap = restTemplate.postForObject(url, httpEntity, Map.class);
		
		//log.debug("formEntity resMap = {} ", resMap);
		
		//ResponseEntity<Map> resEnt = restTemplate.exchange(url, HttpMethod.valueOf(headerCVO.getString("Method")), new HttpEntity(paramCVO, headers), Map.class);
		if(resEnt.getStatusCodeValue() == 200)
		{
			resMap = resEnt.getBody();
		}
		
		return resMap;
	}
	
	public Map putByMap(String url, CommonVO headerCVO, CommonVO paramCVO) {
		RestOperations restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", StringUtil.objStrTobk(headerCVO.get("Content-Type")));

		Map resMap = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity(paramCVO, headers), Map.class).getBody();

		return resMap;
	}
	
	public Map<String, Object> setGet(String url, boolean is_map) {
		RestOperations restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("text/plain;charset=utf-8"));

		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		
		log.info("url  :  "+url);
		if(is_map == true){
			Map map = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
			log.info("map ===>>> "+map);
			return map;
		} else {
			String map = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("result", map == null ? "" : map);
			return obj;
		}
	}
	

	@SuppressWarnings("unchecked")
	public Map<String, Object> sendDelete(Map<String, Object> api, String url) {
		RestOperations restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();

		log.info("url  :  "+url);
		Map<String, Object> result = new HashMap<String, Object>();
		try {

		    headers.setContentType(MediaType.APPLICATION_JSON);
		    HttpEntity param= new HttpEntity(api, headers);
		    
			String map = restTemplate.exchange(url, HttpMethod.DELETE, param, String.class).getBody();
			log.info("map  :  "+map);
			//문자열 json 데이터를 Map으로 변
			JsonParser o = new JsonParser();
			JsonElement je = o.parse(map);
			JsonObject json = je.getAsJsonObject();
			log.info("json   :    "+json.toString());
			Set<String> keys = json.keySet();
			String listKey = "list";
			int count = 0;
			String str = null;
			for(String key : keys) {
				try {
					if(json.get(key).isJsonArray()) {
						JsonArray arry = json.getAsJsonArray(key);
						if(result.containsKey(listKey)) {
							listKey = listKey+count;	
						}
						result.put(listKey , getMapData(arry));		
					} else {
						str = StringUtil.objToStr(json.get(key)).replaceAll("\"", "");
						result.put(key, str);	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//result.putAll(map);
			log.info("result ===>>> "+result);
		} catch(Exception e) {
			e.printStackTrace();
			//결제 요청 실패
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> sendDeleteWithHeader(Map<String, Object> api, String url, Map<String, String> header) {
		RestOperations restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		 if(!header.isEmpty()){
			 if(!header.isEmpty()){
				 if(header.get("Content-Type") != null) {
					 headers.set("Content-Type", header.get("Content-Type"));
				 }else {
					 headers.setContentType(MediaType.APPLICATION_JSON);
				 }
				 if(header.get("User-Agent") != null) {
					 headers.set("User-Agent", header.get("User-Agent"));
				 }
				 if(header.get("Device-id") != null) {
					 headers.set("Device-id", header.get("Device-id"));
				 }
				 if(header.get("Authorization") != null) {
					 headers.set("Authorization", header.get("Authorization"));
				 }
			 }
		 }
		 
		log.info("url  :  "+url);
		Map<String, Object> result = new HashMap<String, Object>();
		try {

		    HttpEntity param= new HttpEntity(api, headers);
		    
			String map = restTemplate.exchange(url, HttpMethod.DELETE, param, String.class).getBody();
			log.info("map  :  "+map);
			//문자열 json 데이터를 Map으로 변
			JsonParser o = new JsonParser();
			JsonElement je = o.parse(map);
			JsonObject json = je.getAsJsonObject();
			log.info("json   :    "+json.toString());
			Set<String> keys = json.keySet();
			String listKey = "list";
			int count = 0;
			String str = null;
			for(String key : keys) {
				try {
					if(json.get(key).isJsonArray()) {
						JsonArray arry = json.getAsJsonArray(key);
						if(result.containsKey(listKey)) {
							listKey = listKey+count;	
						}
						result.put(listKey , getMapData(arry));		
					} else {
						str = StringUtil.objToStr(json.get(key)).replaceAll("\"", "");
						result.put(key, str);	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//result.putAll(map);
			log.info("result ===>>> "+result);
		} catch(Exception e) {
			e.printStackTrace();
			//결제 요청 실패
		}
		
		return result;
	}

	public List<Object> getMapData(JsonArray arry){
		List<Object> ret = new ArrayList<Object>();
		JsonObject json = null;
		Map<String, Object> map = null;
		String str = null;
		for(JsonElement element : arry) {
			map = new HashMap<String, Object>();
			json = element.getAsJsonObject();
			Set<String> keys = json.keySet();
			for(String key : keys) {
				str = StringUtil.objToStr(json.get(key)).replaceAll("\"", "");
				map.put(key, str);
			}
			ret.add(map);
		}
		return ret;
	}
}