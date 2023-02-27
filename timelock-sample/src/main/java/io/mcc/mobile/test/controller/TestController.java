package io.mcc.mobile.test.controller;

import io.mcc.common.util.CommonUtils;
import io.mcc.common.vo.ResultCode;
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
	 * 열매 이체하기(회원)
	 */
	@ApiVersion({WebMvcConfig.API_VERSION, WebMvcConfig.API_VERSION_2})
	@RequestMapping(value = "/fruit/{friends_seq}", method = RequestMethod.POST)
	public ResponseVO sendFruitToFriend(UserAgent userAgent, @RequestBody CommonVO param) throws Exception {
		ResponseVO res = new ResponseVO(ResultCode.SUCCESS);

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
