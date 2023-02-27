package io.mcc.mobile.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 시스템 긴급 공지 등 접근 차단 목적
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SystemEmergencyException extends RuntimeException{

	private Error[] errors;
	
	private String messages;
	
	public SystemEmergencyException() {
        super();
    }
	
	
	public SystemEmergencyException(String messages) {
		super(messages);
	}
	
	public Error[] getErrors() {
        return errors;
    }
	
	
}
