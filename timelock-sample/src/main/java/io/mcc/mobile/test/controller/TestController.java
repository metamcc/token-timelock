package io.mcc.mobile.test.controller;

import io.mcc.common.util.CommonUtils;
import io.mcc.common.vo.ResultCode;
import io.mcc.mobile.common.config.WebMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.mcc.mobile.common.config.version.ApiVersion;
import io.mcc.mobile.common.vo.ResponseVO;
import io.mcc.mobile.test.service.TestService;

import io.mcc.common.resolver.UserAgent;
import io.mcc.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(value = "/test/")
@RestController
public class TestController {

	@Autowired
	private TestService testService;


	/**
	 * 내 열매 정보를 반환
	 * - 총열매, 사용가능한 열매 (이체중,lockup된 물량 제외)
	 *
	 * @param userAgent
	 * @return
	 * @throws Exception
	 */
	@ApiVersion({WebMvcConfig.API_VERSION_2})
	@RequestMapping(value = "/fruit/user", method = RequestMethod.GET)
	public ResponseVO getMyFruitInfo(UserAgent userAgent) throws Exception {

		ResponseVO resVO = null;


		// 2. 서비스 처리
		CommonVO rstCVO = testService.getAvailableFruitData(userAgent.getToken_user_seq());


		// 3. 응답처리
		resVO = new ResponseVO(ResultCode.SUCCESS);
		resVO.setResultObject(rstCVO);

		log.debug("setUserLinkage ResponseVO = {} ",resVO);

		return resVO;
	}

	/**
	 * 열매 이체하기
	 * 이체시 사용가능한 열매를 확인 ...요청한량이 이체가능한지 체크 후 가능할 경우... 이체 처리
	 * 열매량은 MCCX토큰 balance를 기준으로 계산
	 *
	 * @param userAgent - userAgent 정보
	 * @param param - 연동 파라미터
	 * @return ResponseVO - 결과 값
	 * @throws Exception
	 */
	@ApiVersion({WebMvcConfig.API_VERSION, WebMvcConfig.API_VERSION_2})
	@RequestMapping(value = "/fruit/{friends_seq}", method = RequestMethod.POST)
	public ResponseVO sendFruitToFriend(UserAgent userAgent, @RequestBody CommonVO param) throws Exception {
		ResponseVO res = null;

		CommonVO paramCVO = new CommonVO();
		paramCVO.put("isBaseChk", true);
		paramCVO.put("isAuthChk", true);
		paramCVO.put("isFriendChk", true);
		paramCVO.put("isTrDup", true);
		paramCVO.put("reciveUserSeq", param.getLong("recvUser"));
		paramCVO.putAll(param);

		CommonVO rstCVO = testService.transferUserFruit(userAgent, paramCVO);

		if(!CommonUtils.isEmpty(rstCVO))
		{
			res = new ResponseVO(ResultCode.SUCCESS);
			res.setResultObject(rstCVO);
		}
		else
		{
			res = new ResponseVO(ResultCode.H404);
		}
		return res;
	}

}
