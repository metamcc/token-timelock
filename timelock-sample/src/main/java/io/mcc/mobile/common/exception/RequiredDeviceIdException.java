package io.mcc.mobile.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Header device-id 없을경우,
 * 시스템 접근 차단 목적
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredDeviceIdException extends RuntimeException{

	private Error[] errors;
	
	
	public RequiredDeviceIdException() {
        super();
    }
	
	public Error[] getErrors() {
        return errors;
    }
	
	public RequiredDeviceIdException(String code) {
		super(code);
	}
	
}
