package io.mcc.mobile.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private Object[] arg;
	
	public ValidationException() {
        super();
    }

    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ValidationException(final String message) {
        super(message);
    }

    public ValidationException(final Throwable cause) {
        super(cause);
    }

    
    public ValidationException(final String message, Object[] arg) {
        super(message);
		this.arg = arg;
	}
    
	
	public Object[] getArgs() {
		return this.arg;
	}
}
