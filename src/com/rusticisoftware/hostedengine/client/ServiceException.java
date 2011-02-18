package com.rusticisoftware.hostedengine.client;

public class ServiceException extends Exception {

	//TODO: Add error code to this class
	private int _errorCode = -1;
	public int getErrorCode(){
		return _errorCode;
	}
	
	public ServiceException() {
		super();
	}
	
	public ServiceException(String message) {
	    super(message);
	}

	public ServiceException(int errorCode, String arg0) {
		super(arg0);
		_errorCode = errorCode;
	}

	public ServiceException(int errorCode, Throwable arg0) {
		super(arg0);
		_errorCode = errorCode;
	}

	public ServiceException(int errorCode, String arg0, Throwable arg1) {
		super(arg0, arg1);
		_errorCode = errorCode;
	}

}
