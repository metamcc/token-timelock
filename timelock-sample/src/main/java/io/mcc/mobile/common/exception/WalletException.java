package io.mcc.mobile.common.exception;

public class WalletException extends Exception {

	private String args1;
	
	public WalletException() {
		super();
	}
	
	public WalletException(String code) {
		super(code);
	}
	
	public WalletException(String code, String args1) {
		super(code);
		this.args1 = args1;
	}
	
	public String getArgs1() {
		return this.args1;
	}

}
