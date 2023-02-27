package io.mcc.mobile.common.exception;

public class CashtreeException extends Exception {

	private String code;
	
	public CashtreeException() {
		super();
	}
	
	public CashtreeException(String code) {
		super(code);
	}

}
