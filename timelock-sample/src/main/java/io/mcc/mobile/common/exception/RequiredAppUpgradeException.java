package io.mcc.mobile.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 앱 강제 업데이트 시행을 위한,
 * 시스템 접근 차단 목적
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredAppUpgradeException extends RuntimeException{

	private Error[] errors;
	
	
	public RequiredAppUpgradeException() {
        super();
    }
	
	public RequiredAppUpgradeException(String s) {
        super(s);
    }
	
	public Error[] getErrors() {
        return errors;
    }
	
	
}
