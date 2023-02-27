package io.mcc.mobile.test.service;

import io.mcc.common.resolver.UserExtraRole;
import io.mcc.common.vo.CommonVO;
import io.mcc.common.vo.ResultCode;
import io.mcc.mobile.common.config.oauth2.handler.CustomOauthException;
import io.mcc.mobile.common.config.oauth2.service.CustomRemoteTokenServices;
import io.mcc.mobile.common.util.StringUtil;
import io.mcc.mobile.test.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
public class UserAuthInfoService {


	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CustomRemoteTokenServices remoteTokenServices;

	@Autowired
	private UserMapper userMapper;
	/**
	 * 사용자 정보 취득 공용 함수
	 * 
	 * @param paramVO
	 * @return
	 */
	public CommonVO getUserInfo(CommonVO paramVO) {
		CommonVO userInfo = new CommonVO();
		//생략 ...
		return userInfo;
	}


	/**
	 * 로그인정보를 조회..
	 * @param username
	 * @return
	 */
	public CommonVO getUserInfo(String username) {

		CommonVO param = new CommonVO();
		param.put("username", username);
		CommonVO userInfo = new CommonVO();	//생략 ...
		return userInfo;
	}
	
	public CommonVO getUserInfo(String authCred, String lang) {
		String token = "";
		
		if( !authCred.isEmpty() && authCred.contains("Bearer")) {
			token = authCred.split("Bearer ")[1];
		}		
		
		if(token == null || StringUtil.objStrTobk(token).length() < 0) {
			return new CommonVO();
		}
		//get user info via token
		remoteTokenServices.setLanguage(lang);
		Authentication auth = remoteTokenServices.loadAuthentication(token, lang);
		String username = auth.getName();
		
		CommonVO param = new CommonVO();
		param.put("username", username);
		
		CommonVO userInfo = userMapper.getUserInfo(param);

		if (userInfo == null){
			log.error("getUserInfo NullPointerException param {}", param);
			//x2ho.mody. 사용자 정보가 없을 경우...
			CustomOauthException ex = new CustomOauthException("");
			ex.addAdditionalInformation("resultCode",    "M007");
			ex.addAdditionalInformation("resultMessage",  messageSource.getMessage(ResultCode.M007.getCode(), null, new Locale(lang)));
			throw  ex;
			//throw new NullPointerException();
		}
		return userInfo;
	}

	/**
	 * 해당 사용자의 별도 권한 조회
	 * @param user_seq
	 * @return
	 */
	public UserExtraRole getUserExtraRole(long user_seq) {

		return userMapper.getUserExtraRole(user_seq);
	}
	
}
