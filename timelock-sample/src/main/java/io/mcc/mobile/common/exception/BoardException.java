package io.mcc.mobile.common.exception;

public class BoardException extends Exception {

	private String code;
	
	public BoardException() {
		super();
	}
	
	public BoardException(String code) {
		super(code);
	}

}
