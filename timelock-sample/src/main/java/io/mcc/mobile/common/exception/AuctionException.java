package io.mcc.mobile.common.exception;

public class AuctionException extends Exception {

	private String code;
	
	public AuctionException() {
		super();
	}
	
	public AuctionException(String code) {
		super(code);
	}

}
