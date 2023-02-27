package io.mcc.mobile.test.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import io.mcc.common.Const;
import io.mcc.common.exception.BizException;
import io.mcc.common.resolver.UserAgent;
import io.mcc.common.util.CommonUtils;
import io.mcc.common.vo.ResultCode;
import io.mcc.goodmorn.fruit.service.CommFruitService;
import io.mcc.goodmorn.repository.CommonRepository;
import io.mcc.mcctoken.service.MCCTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.mcc.common.vo.CommonVO;
import io.mcc.mobile.test.mapper.TestMapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.utils.Convert;

@Slf4j
@Service
public class TestService {

	@Autowired
	private MCCTokenService mCCTokenService;
	
	@Autowired
	private TestMapper testMapper;

	@Autowired
	private CommFruitService commFruitService;

	private final BigInteger fruitTxMax = new BigInteger("10000000000000000000000000"); //10000000 mcc


	private void notifyError(String errMsg) {
		//~~ remove..
	}
	/**
	 * x2ho.add. 2023.02.08.
	 * @param userSeq - 사용자 키
	 * @param balaceOfMCC - 토큰 balance
	 * @param rstCVO
	 */
	private void checkAvailableFruit(long userSeq, BigDecimal balaceOfMCC, CommonVO rstCVO) {
		log.debug("========== checkAvailableFruit:balaceOfMCC={}", balaceOfMCC);

		rstCVO.put("totalfruitQty", balaceOfMCC.toPlainString());

		BigDecimal avFruitQty = balaceOfMCC.subtract(new BigDecimal(rstCVO.getString("transferFruitQty","0")))
				.subtract(new BigDecimal(rstCVO.getString("lockAmount","0")));
		if (avFruitQty.compareTo(BigDecimal.ZERO) < 0) {
			String errMsg = String.format("Invalid wallet amount(<0):[user=%ld]", userSeq);
			log.warn(errMsg);
			//관리자 notify
			notifyError(errMsg);

			avFruitQty = BigDecimal.ZERO;
		}
		rstCVO.put("availabilityFruitQty", avFruitQty);
	}

	/**
	 * 실제 토큰 정보 조회
	 *
	 * @param userSeq : 사용자일련번호
	 * @param paramCVO : 파라메터
	 * @return
	 */
	public CommonVO getAvailableFruitData(long userSeq, CommonVO paramCVO)
	{

		CommonVO rstCVO = testMapper.selectMyFruitData(userSeq);
		BigDecimal kctTotalQty = null;

		if(!CommonUtils.isEmpty(rstCVO))
		{
			String mccWalletAddr = rstCVO.getString("mcc_wallet_addr");
			String hfbnWalletAddr = rstCVO.getString("hlf_wallet_addr");
			if(!CommonUtils.isEmpty(mccWalletAddr) && !CommonUtils.isEmpty(hfbnWalletAddr))
			{
				int discordCnt = 0;
				try
				{
					// 가장 작은 값을 보유 수량으로 보여줌
					BigDecimal minQty = null;


					// kct 총량
					kctTotalQty = new BigDecimal(mCCTokenService.getBalanceOf(mccWalletAddr));

					// kct를 기본으로
					minQty = kctTotalQty;

					//x2ho.mody.2023.02.08
					checkAvailableFruit(userSeq, minQty, rstCVO);
				}
				catch(Exception e)
				{
					log.error("mcc balanceof error = {}",e);

				}
			}

			if(!CommonUtils.isEmpty(paramCVO)) // hfbn 전달용
			{
				paramCVO.put("senderAddr", hfbnWalletAddr);
			}
		}

		return rstCVO;
	}

	/**
	 * 열매이체 - 유저가 유저에게 열매를 보내는 경우
	 *
	 * @param userAgent - 로그인 유저 : 보내는 이
	 * @param paramCVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = { Exception.class })
	public CommonVO transferUserFruit(UserAgent userAgent, CommonVO paramCVO) throws Exception {

		CommonVO rstCVO = null;

		// 1.1 기본 유효성체크 - 시스템 활성화 유무
		commFruitService.chkBase();

		// 파라메터
		boolean bAuthCheck = paramCVO.getBoolean("isAuthChk", true); // 인증 여부 체크 true,false
		boolean bSecureCheck = paramCVO.getBoolean("bSecureCheck", true); // 보안 체크 true,false

		boolean bFriendsCheck = paramCVO.getBoolean("isFriendChk", true); // 친구 여부 체크 true,false
		boolean bTrDup = paramCVO.getBoolean("isTrDup", true); // 중복 여부 체크 true,false


		long sndUserSeq = userAgent.getToken_user_seq(); // 보내는이
		long rcvUserSeq= paramCVO.getLong("reciveUserSeq"); // 받는이

		String trnsmisQty = paramCVO.getString("trnsmisQty", "0"); // 이체량

		// 권한 체크
		UserAgent rcvUserAgent = commFruitService.setRedisUserAgent(rcvUserSeq);
		if(bAuthCheck)
		{
			//rcvUserAgent = commFruitService.chkInnerAuth(userAgent, rcvUserAgent);
		}

		if(sndUserSeq == rcvUserSeq) {
			log.error("BizException M409 user_seq {}, in_out_user_seq {}", sndUserSeq, rcvUserSeq);
			throw new BizException(ResultCode.M409.getCode());
		}

		// 전송하려는 사용자의 사용가능 열매를 체크..
		//전송중이거나 lockup된 물량이상은 전송 불가능하도록 처리..
		CommonVO myDataCVO = getAvailableFruitData(sndUserSeq, paramCVO);

		log.debug("myDataCVO = {}", myDataCVO);

		// 1.4 인증체크 - 시스템 지급 제외
		if(bSecureCheck)
		{
			commFruitService.chkSecurity(userAgent, myDataCVO, paramCVO);
			trnsmisQty = paramCVO.getString("trnsmisQty");
		}

		// 이체금액 토큰형식으로 변환
		BigInteger trnsmisFruitQtyBi = Convert.toWei(trnsmisQty, Convert.Unit.ETHER).toBigInteger();
		paramCVO.put("trnsmis_fruit_qty_bi_str", trnsmisFruitQtyBi.toString());

		// 0 이상
		if( Double.valueOf(trnsmisQty) <= 0) {
			log.error("BizException M402 trnsmis_fruit_qty {}", trnsmisQty);
			throw new BizException(ResultCode.M402.getCode());
		}

		// 보유 열매 이상
		BigInteger availabilityFruitQtyBi = new BigInteger(myDataCVO.getString("availabilityFruitQty"));
		if(availabilityFruitQtyBi.compareTo(trnsmisFruitQtyBi) == -1 ) {
			log.error("BizException M403 availabilityFruitQty {}, trnsmisFruitQtyBi {}", availabilityFruitQtyBi, trnsmisFruitQtyBi);
			throw new BizException(ResultCode.M403.getCode());
		}

		// 1회 한도 아래
		if(trnsmisFruitQtyBi.compareTo(fruitTxMax) == 1) {
			log.error("BizException M420 fruitTxMax {}, trnsmisFruitQtyBi {}", fruitTxMax, trnsmisFruitQtyBi);
			throw new BizException(ResultCode.M420.getCode());
		}

		// 최근 중복 이체 여부 체크
		if(bTrDup)
		{
			commFruitService.chkTrDup(sndUserSeq, rcvUserSeq);
		}

		// 이체 데이터와 친구 관계 체크 및 구성
		if(bFriendsCheck)
		{
			commFruitService.chkFriendship(userAgent, paramCVO);
		}

		// 열매유통이력 기록
		// 이하 생략

		return rstCVO;

	}

}
