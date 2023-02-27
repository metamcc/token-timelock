package io.mcc.mobile.common.exception;

import java.util.Locale;
import java.util.Map;

import io.mcc.mobile.common.config.oauth2.handler.CustomOauthException;
import io.mcc.mobile.common.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.mcc.mobile.board.service.BoardNoticeService;

import io.mcc.common.exception.BizException;
import io.mcc.common.exception.RollbackException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionRestControllerAdvice extends ResponseEntityExceptionHandler {
	
	@Autowired
	private BoardNoticeService noticeService;
	
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	public ResponseVO commonException(Exception ex, Locale locale) {
		log.error("===== commonException = {}", ex);
		
		
		ResponseVO vo = new ResponseVO(ResultCode.H500.getCode(), messageSource.getMessage(ResultCode.H500.getCode(), null, locale));
		return vo;
    }
	
	/**
	 * 각종 유효성 체크 관련 예외 처리
	 * 
	 * @param ex
	 * @param request
	 * @param locale
	 * @return
	 */
	@ExceptionHandler(ValidationException.class)
    public ResponseVO commonValidationException(ValidationException ex, WebRequest request, Locale locale) {
		log.error("===== ValidationException = {}", ex);
		//ResponseVO vo = new ResponseVO(ResultCode.NPE.getCode(), messageSource.getMessage(ResultCode.NPE.getCode(), null, locale));
		ResponseVO vo = null;
		if (ex.getMessage() != null) {
			vo = new ResponseVO(ex.getMessage(), messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale));
		} else {
			vo = new ResponseVO(ResultCode.VDE.getCode(), messageSource.getMessage(ResultCode.VDE.getCode(), null, locale));
		}
        return vo;
    }
	
	/**
	 * 트랜잭션 관련 예외 처리
	 * 
	 * @param ex
	 * @param request
	 * @param locale
	 * @return
	 */
	@ExceptionHandler(RollbackException.class)
    public ResponseVO commonRollbackException(RollbackException ex, WebRequest request, Locale locale) {
		log.error("===== RollbackException = {}", ex);
		//ResponseVO vo = new ResponseVO(ResultCode.NPE.getCode(), messageSource.getMessage(ResultCode.NPE.getCode(), null, locale));
		ResponseVO vo = null;
		if (ex.getMessage() != null) {
			vo = new ResponseVO(ex.getMessage(), messageSource.getMessage(ex.getMessage(), null, locale));
		} else {
			vo = new ResponseVO(ResultCode.RBE.getCode(), messageSource.getMessage(ResultCode.RBE.getCode(), null, locale));
		}
        return vo;
    }
	
	/**
	 * 메세지코드 데이터 없음
	 * 
	 * @param ex
	 * @param locale
	 * @return
	 */
	@ExceptionHandler(NoSuchMessageException.class)
	public ResponseVO noSuchMessageException(NoSuchMessageException ex, Locale locale) {
		log.error("===== NoSuchMessageException start...");
		log.error("ExceptionHandler = {}", ex);
		
		ResponseVO vo = new ResponseVO(ResultCode.NO_MESSAGE_DATA.getCode(), ex.getMessage());
		return vo;
    }
	
	
	/**
	 *  인증 서버에서 내려온 에러 세세지를 처리한다.
	 * @param ex      인증서버 예외
	 * @param request 웹 요청
	 * @param locale 지역의 [언어][나라] 등의 정보
	 * @return 응답
	 */
	@ExceptionHandler(CustomOauthException.class)
    public ResponseVO customOauthException(CustomOauthException ex, WebRequest request, Locale locale) {
		log.error("===== customOauthException is happened!");
		log.error("customOauthException = {}", ex);
		Map<String, String>  ret = ex.getAdditionalInformation();
		
		ResponseVO vo = new ResponseVO(ret.get("resultCode"), ret.get("resultMessage"));
		//ResponseVO vo = new ResponseVO(ret.get("resultCode"), messageSource.getMessage(ret.get("resultCode"), null, locale));
        return vo;
    }
	
	@ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseVO commonNullPointerException(NullPointerException ex, WebRequest request, Locale locale) {
		log.error("===== NullPointerException is happened!");
		log.error("NullPointerException = {}", ex);

		ResponseVO vo = new ResponseVO(ResultCode.NPE.getCode(), messageSource.getMessage(ResultCode.NPE.getCode(), null, locale));
        return vo;
    }
	
	@ExceptionHandler(HttpServerErrorException.class)
    public ResponseVO commonHttpServerErrorException(HttpServerErrorException ex, WebRequest request, Locale locale) {
		log.error("===== commonHttpServerErrorException is happened!");
		log.error("HttpServerErrorException", ex);

		ResponseVO vo = new ResponseVO(ResultCode.NPE.getCode(), messageSource.getMessage(ResultCode.NPE.getCode(), null, locale));
        return vo;
    }
	
//	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
//	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
//		String bodyOfResponse = "This should be application specific";
//		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
//	}
	
	

	
	
	
	@ExceptionHandler(BizException.class)
    public ResponseVO commonBizException(BizException ex, WebRequest request, Locale locale) {
		log.error("===== BizException is happened!");
		log.error("BizException = {}", ex);
		//ResponseVO vo = new ResponseVO(ResultCode.NPE.getCode(), messageSource.getMessage(ResultCode.NPE.getCode(), null, locale));
		ResponseVO vo = null;
		if (ex.getMessage()!=null) {
			vo = new ResponseVO(ex.getMessage(), messageSource.getMessage(ex.getMessage(), null, locale));
		} else {
			vo = new ResponseVO(ResultCode.H500.getCode(), ResultCode.H500.getMsg());
		}
        return vo;
    }
	
	
	@ExceptionHandler(SecurityException.class)
	public ResponseVO commonSecurityException(SecurityException ex, WebRequest request, Locale locale) {
		log.error("===== SecurityException is happened!");
		log.error("SecurityException = {}", ex);
		ResponseVO vo = null;
		String code = ex.getMessage();
		if("M021".equals(ex.getMessage())) {
			vo = new ResponseVO(ResultCode.M021.getCode(), messageSource.getMessage(ResultCode.M021.getCode(), null, locale));
		} else {
			vo = new ResponseVO(ResultCode.SCE.getCode(), messageSource.getMessage(ResultCode.SCE.getCode(), null, locale));
		}
		
        return vo;
    }
	
	
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseVO commonAccessDeniedException(AccessDeniedException ex, WebRequest request, Locale locale) {
		log.error("===== AccessDeniedException is happened!");
		log.error("AccessDeniedException = {}", ex);
		ResponseVO vo = new ResponseVO(ResultCode.SCE.getCode(), ex.getMessage());
		
        return vo;
    }
	
	@ExceptionHandler(UserDuplicationException.class)
    public ResponseVO UserDuplicationException(UserDuplicationException ex, WebRequest request, Locale locale) {
		log.error("===== UserDuplicationException is happened!");
		log.error("UserDuplicationException = {}", ex);
		ResponseVO vo = new ResponseVO(ResultCode.M107.getCode(), messageSource.getMessage(ResultCode.M107.getCode(), null, locale));
		vo.setResultObject(ex.getMessage());
        return vo;
    }
	
	
	@ExceptionHandler(SystemEmergencyException.class)
    public ResponseVO commonSystemEm(SystemEmergencyException ex, WebRequest request, Locale locale) {
		log.error("===== SystemEmException is happened!");
		log.error("SystemEmergencyException = {}", ex);
		//CommonVO param = new CommonVO();
//		param.put("language", language);
		//param.put("bbs_type_cd", BbsTypeCD.System.getTypeCd());
		
		//Notice notices = noticeService.getSystemNotice(param);
		ResponseVO vo = null;
		String message = ex.getMessage();
		if (message != null && message.length() > 0){
			vo = new ResponseVO(ResultCode.M701.getCode(), message);
		} else {
			vo = new ResponseVO(ResultCode.M701.getCode(), messageSource.getMessage(ResultCode.M701.getCode(), null, locale));
		}
		
		//vo.setResultObject(notices);
        return vo;
    }
	
	
	@ExceptionHandler(RequiredAppUpgradeException.class)
    public ResponseVO commonRequiredAppUpgrade(RequiredAppUpgradeException ex, WebRequest request, Locale locale) {
		log.error("===== RequiredAppUpgradeException is happened!");
		log.error("RequiredAppUpgradeException = {}", ex);
		/**
		 * TODO : 상세 정책 정의
		 * app version 체크 방법, 주체
		 * 업그레이드 방식  등...
		 */
		
		ResponseVO vo = new ResponseVO(ResultCode.M703.getCode(), messageSource.getMessage(ResultCode.M703.getCode(), null, locale));
		
        return vo;
    }
    
    @ExceptionHandler(RequiredDeviceIdException.class)
    public ResponseVO commonRequiredDeviceId(RequiredDeviceIdException ex, WebRequest request, Locale locale) {
		log.error("===== RequiredDeviceIdException is happened!");
		log.error("RequiredDeviceIdException = {}", ex);
		
		String code = ex.getMessage();

		ResponseVO vo = new ResponseVO(code, messageSource.getMessage(code, null, locale));
		
        return vo;
    }
	
    @ExceptionHandler(FruitException.class)
    public ResponseVO fruitException(FruitException ex, WebRequest request, Locale locale) {
    	String code = ex.getMessage();
    	log.error("===== fruitException is happened! code : {}, locale : = {}", code, locale);
    	log.error("FruitException = {}", ex);
    	log.error("FruitException = {}", messageSource.getMessage(code, null, locale));

		ResponseVO vo = new ResponseVO(code, messageSource.getMessage(code, null, locale));
		return vo;
    }
   
    @ExceptionHandler(CashtreeException.class)
    public ResponseVO cashtreeException(CashtreeException ex, WebRequest request, Locale locale) {
    	String code = ex.getMessage();
    	log.error("===== CashtreeException is happened! code :  {}", code);
    	log.error("CashtreeException = {}", ex);
		ResponseVO vo = new ResponseVO(code, messageSource.getMessage(code, null, locale));
		return vo;
    }

	@ExceptionHandler(HandlerException.class)
	public ResponseVO handlerException(HandlerException ex, WebRequest request, Locale locale) {
		String code = ex.getMessage();
		log.error("===== handlerException is happened! code : {}", code);
		log.error("HandlerException = {}", ex);
		ResponseVO vo = new ResponseVO(code, messageSource.getMessage(code, null, locale));
		return vo;
	}
    
	/**
	 * 통합 게시판 예외 처리
	 * @param ex 예외 
	 * @param request 리퀘스트
	 * @param locale 언어 코드
	 * @return
	 */
    @ExceptionHandler(BoardException.class)
    public ResponseVO boardException(BoardException ex, WebRequest request, Locale locale) {
    	String code = ex.getMessage();
    	log.error("===== BoardException is happened! code : {}", code);
    	log.error("BoardException = {}", ex);
		ResponseVO vo = new ResponseVO(code, messageSource.getMessage(code, null, locale));
		return vo;
   }
    
    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    protected ResponseVO handleConflict(MaxUploadSizeExceededException ex, WebRequest request) {
    	String code = ex.getMessage();
    	log.error("===== BoardException is happened! code : {}", code);
    	log.error("MaxUploadSizeExceededException = {}", ex);
		ResponseVO vo = new ResponseVO("M670", "첨부 파일 용량이 초과 하였습니");
		return vo;
    }
    
    
    @ExceptionHandler(value = {GuestException.class})
    protected ResponseVO guestException(GuestException ex, WebRequest request) {
    	String code = ex.getMessage();
    	//log.error("===== GuestException is happened! code : {}", code);
		ResponseVO vo = new ResponseVO(ResultCode.SCE.getCode(), ResultCode.SCE.getMsg());
		return vo;
    }

    @ExceptionHandler(WalletException.class)
    public ResponseVO walletException(WalletException ex, WebRequest request, Locale locale) {
    	String code = ex.getMessage();
    	log.error("===== WalletException is happened! code : {}, locale : = {}", code, locale);
    	log.error("WalletException = {}", ex);
    	log.error("WalletException = {}", messageSource.getMessage(code, null, locale));
		ResponseVO vo = new ResponseVO(code, messageSource.getMessage(code, null, locale));
		return vo;
    }
    
    @ExceptionHandler(AuctionException.class)
    public ResponseVO auctionException(AuctionException ex, WebRequest request, Locale locale) {
    	String code = ex.getMessage();
    	log.error("===== auctionException is happened! code : {}, locale : = {}", code, locale);
    	log.error("AuctionException = {}", ex);
    	
		ResponseVO vo = new ResponseVO(code, messageSource.getMessage(code, null, locale));
		return vo;
    }
}


