package io.mcc.mobile.common.config;

import io.mcc.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class HandlerInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response,
							 Object handler) {
		
		/**
		 * 시스템 점검 Check!!!
		 */
		//생략...
        
        
        String userName = "";
        
    	// 시큐리티 컨텍스트 객체를 얻습니다. 
    	SecurityContext context = SecurityContextHolder.getContext();
    	// 인증 객체를 얻습니다. 
    	Authentication authentication = context.getAuthentication(); 
    	
    	Object principalObj = authentication.getPrincipal();
    	
    	CommonVO userInfo  = null;
    	if(principalObj instanceof Map)
    	{
    		Map principalMap = (Map)principalObj;
    		userInfo = new CommonVO();
    		userInfo.putAll(principalMap);
    		userName = userInfo.getString("user_name") ;
    	}
    	else
    	{
    		userName = principalObj.toString();
    	}
		
		/**git 
		 * App 강제 버젼 UP Check!!!
		 */
		//생략..

		return true;
	}
	
	@Override
	public void postHandle( HttpServletRequest request,
							HttpServletResponse response,
							Object handler,
							ModelAndView modelAndView) {
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
								HttpServletResponse response, 
								Object handler, 
								Exception ex) {
	}
}
