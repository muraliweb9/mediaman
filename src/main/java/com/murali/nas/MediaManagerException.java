package com.murali.nas;

/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class MediaManagerException extends Exception {
	
	private static final long serialVersionUID = 696785673878162572L;
	
	
	public MediaManagerException(final String message) {
		super(message);
	}
	
	
	public MediaManagerException(final Throwable throwable) {
		super(throwable);
	}
	
	
	public MediaManagerException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
}
