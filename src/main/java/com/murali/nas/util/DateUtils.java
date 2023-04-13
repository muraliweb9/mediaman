package com.murali.nas.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.murali.nas.MediaManagerException;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public final class DateUtils {
	
	private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	
	private DateUtils() {
	}
	
	
	public static Date fromTimeStamp(final String timeStamp) throws MediaManagerException {
		
		try {
			return new SimpleDateFormat(TIMESTAMP_FORMAT).parse(timeStamp);
		} catch (ParseException e) {
			throw new MediaManagerException(String.format("Unable to parse [%s] as timestamp, format used is [%s]",
				timeStamp, TIMESTAMP_FORMAT), e);
		}
		
	}
	
	
	public static String toTimeStamp(final Date date) throws MediaManagerException {
		
		return new SimpleDateFormat(TIMESTAMP_FORMAT).format(date);
		
	}
}
