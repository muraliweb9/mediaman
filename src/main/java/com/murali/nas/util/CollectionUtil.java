package com.murali.nas.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public final class CollectionUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionUtil.class);
	
	
	private CollectionUtil() {
	}
	
	
	public static List<String> parseAllowedExtensions(final String allowedExtString) {
		List<String> allowedExtensions = new ArrayList<>();
		StringTokenizer strTok = new StringTokenizer(allowedExtString, ",");
		while (strTok.hasMoreTokens()) {
			String tok = strTok.nextToken().trim();
			LOGGER.info(String.format("Allowed extension used %s", tok));
			allowedExtensions.add(tok);
		}
		return allowedExtensions;
	}
	
}
