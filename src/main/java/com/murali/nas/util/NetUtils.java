package com.murali.nas.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public final class NetUtils {
	
	private NetUtils() {
	}
	
	
	public static String encode(final String s) throws UnsupportedEncodingException {
		return URLEncoder.encode(s, "UTF-8").replace("+", "%20");
	}
	
	
	public static String decode(final String s) throws UnsupportedEncodingException {
		return URLDecoder.decode(s, "UTF-8");
	}
}
