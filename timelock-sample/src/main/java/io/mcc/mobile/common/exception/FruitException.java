package io.mcc.mobile.common.exception;

public class FruitException extends Exception {

	private String code;
	
	public FruitException() {
		super();
	}
	
	public FruitException(String code) {
		super(code);
	}

}
