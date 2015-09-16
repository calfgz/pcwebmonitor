package org.jwebap.core;

/**
 * Component×é¼þ×¢²áÒì³£
 * 
 * @author leadyu
 * @since Jwebap 0.5
 * @date Aug 7, 2007
 */
@SuppressWarnings("serial")
public class RegisterException extends Exception {

	public RegisterException(String msg) {
		super(msg);
	}

	public RegisterException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
