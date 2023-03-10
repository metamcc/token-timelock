package io.mcc.mobile.common.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mcc.common.resolver.UserAgent;
import io.mcc.common.resolver.UserExtraRole;
import io.mcc.common.vo.CommonVO;
import io.mcc.mobile.common.config.oauth2.handler.CustomOauthException;
import io.mcc.mobile.common.util.HttpHeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class UserAgentWebArgumentResolver implements HandlerMethodArgumentResolver  {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserAgent.class.isAssignableFrom(parameter.getParameterType());
    }

    public UserAgent resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // Language (ko, en)
        String language = "en";
        String acceptLanguage = webRequest.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (!StringUtils.isBlank(acceptLanguage)) {
        	language = acceptLanguage;
        }
        
        // get user info from header(token)
        String authCred = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String deviceId = ""; // ??????????????????
        String userAgent = "";
        String reqSsaid = "";
        
        long user_seq = 0;
        String username = "";
        String moblphon_no = "";
        String moblphon_nation_no_cd = "";
        String nation_cd = "";
        String use_lang_cd = "";
        ZoneId zoneId = null;
        int timezoneOffset = 0;
        String pushUseYn = "";
        String sbscrb_dt = "";
        String ip = "";
        String os = "";
        String osVer = "";          //x2ho.add.
        String reqAppName = "";
        String reqAppVersion = "";
        String devicename = "";
        String gradeCd = "";        //x2ho.add.????????????
        String nicknm  = ""; //????????? ?????????
        String userTypeCd = "";//????????? ??????
        String displayName = ""; // ????????????
        String propose_no = ""; // ????????????
        UserExtraRole userRole = null; // ???????????????
        String userDeviceType = ""; // ?????????????????????
        String mccRole = "";
        
        boolean needPbsync = false;
        int reqApiVer = 1;

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        
        if(authCred != null) {

        	// ???????????? ???????????? ????????? ????????????. 
        	SecurityContext context = SecurityContextHolder.getContext();
        	// ?????? ????????? ????????????. 
        	Authentication authentication = context.getAuthentication(); 
        	
        	Object principalObj = authentication.getPrincipal();

        	// ???????????? ????????? ?????? ??????
        	CommonVO userInfo  = null;
        	if(principalObj instanceof Map)
        	{
        		Map principalMap = (Map)principalObj;
        		userInfo = new CommonVO();
        		userInfo.putAll(principalMap);
        	}


        	//????????????..

            // ????????? ????????? AcceptLanguage ??? ?????? ?????? update
            if (!language.equals(use_lang_cd)) {
            	//commonSevice.updateUserLanguage(user_seq, language);
            }
            
            // ????????? ??????
            Object userExtraRoleObj = userInfo.get("userExtraRoleCVO");
            if (userExtraRoleObj != null) {
            	LinkedHashMap userRoleLHMap = (LinkedHashMap)userExtraRoleObj;
            	if(userRoleLHMap != null && userRoleLHMap.size() > 0) {
            		ObjectMapper mapper = new ObjectMapper();
               	 	userRole = mapper.convertValue(userRoleLHMap, UserExtraRole.class);
            	}
            }
            else
            {
                //?????? role??? ????????? ??????, ??????
            	//userRole = authInfoService.getUserExtraRole(user_seq);
            }

        } else {
            log.error("----- ###  Authorization Tocken Not Found. ");
        }
        
        os = HttpHeaderUtil.getUserAgentInfo(userAgent, "os");
        osVer = HttpHeaderUtil.getOsVer(userAgent);
        devicename = HttpHeaderUtil.getDiviceName(userAgent);
        reqAppName = HttpHeaderUtil.getAppName(userAgent);
        reqAppVersion = HttpHeaderUtil.getAppVersion(userAgent);

        os = os ==null || "".equals(os) ? userDeviceType : os;
        
        //pw ?????? ?????? ????????? ?????? ??????
        ip = HttpHeaderUtil.getClientIp(httpServletRequest);

        String reqURI = httpServletRequest.getRequestURI();
        if (reqURI.indexOf("api/v2/") > 0) {
            reqApiVer = 2;
        }
        
        UserAgent resultUserAgent = new UserAgent(language, user_seq, username, moblphon_no, moblphon_nation_no_cd, nation_cd,
                zoneId, timezoneOffset, pushUseYn, sbscrb_dt, ip, os, osVer, devicename, reqAppName, reqAppVersion,
                gradeCd, userRole,  reqApiVer, nicknm,  displayName, userTypeCd, propose_no, deviceId,  reqSsaid, mccRole);
 
        resultUserAgent.setNeed_pbsync(needPbsync);
       // log.debug("-----UserAgentWebArgumentResolver  resultUserAgent = {}", resultUserAgent);
        
        return resultUserAgent;
    }

}