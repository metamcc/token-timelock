package io.mcc.mobile.common.exception;

public class GuestException extends RuntimeException{

	public GuestException() {
		super();
	}
	
	String code;
	
	public GuestException(String message) {
		super(message);
	}
	
	public GuestException(String code, String message) {
		super(message);
	}

}
