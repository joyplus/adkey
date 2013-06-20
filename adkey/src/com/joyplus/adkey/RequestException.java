package com.joyplus.adkey;

public class RequestException extends Exception {

	private static final long serialVersionUID = 1L;

	public RequestException() {
		super();
	}

	public RequestException(final String detailMessage) {
		super(detailMessage);
	}

	public RequestException(final String detailMessage,
			final Throwable throwable) {
		super(detailMessage, throwable);
	}

	public RequestException(final Throwable throwable) {
		super(throwable);
	}

}
