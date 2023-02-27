package io.mcc.mobile.common.exception;

public class HandlerException extends RuntimeException {

	private String code;
	
	public HandlerException() {
		super();
	}
	
	public HandlerException(String code) {
		super(code);
	}

}
