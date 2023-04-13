package com.murali.nas.model.xml.itunes;

import static com.murali.nas.util.NetUtils.decode;
import static java.lang.Boolean.FALSE;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.murali.nas.model.MetaData;
import com.murali.nas.util.ITunesDictParser;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class Track {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Track.class);
	
	private final ITunesDictParser parser;
	
	
	public Track(final Dict trackDict)  {
		parser = new ITunesDictParser(trackDict);
	}
	
	
	public final Object getValue(final ITunesKey key) {
		return parser.getValue(key);
	}
	
	
	public final BigInteger getTrackId() {
		return (BigInteger) parser.getValue(ITunesKey.TRACK_ID);
	}
	
	
	public final String getLocation() throws UnsupportedEncodingException {
		return decode((String) parser.getValue(ITunesKey.LOCATION));
	}
	
	
	public final String getLocationFileName() throws UnsupportedEncodingException {
		String location = (String) parser.getValue(ITunesKey.LOCATION);
		return decode(location.substring(location.lastIndexOf("/") + 1));
	}
	
	
	public final String getLocationFileNameOrEmpty() {
		String location = (String) parser.getValue(ITunesKey.LOCATION);
		try {
			return decode(location.substring(location.lastIndexOf("/") + 1));
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	
	public final Boolean isLocationMatch(final MetaData metaData) {
		return getLocationFileNameOrEmpty().equals(metaData.getFileName());
	}
	
	
	public final Boolean getHasVideo() {
		Boolean hasVideo = (Boolean) parser.getValue(ITunesKey.HAS_VIDEO);
		return hasVideo != null ? hasVideo : FALSE;
	}
	
	
	public final Boolean getHD() {
		return (Boolean) parser.getValue(ITunesKey.HD);
	}
	
	
	public final Boolean equals(final MetaData metaData) throws UnsupportedEncodingException {
		if (metaData != null) {
			String locationFileName = getLocationFileName();
			String metaFileName = metaData.getFile().getName();
			LOGGER.debug(String.format("Track equals compared with this [%s] and meta [%s]",
				locationFileName, metaFileName));
			return ObjectUtils.equals(locationFileName, metaFileName);
		}
		LOGGER.debug(String.format("Null meta data"));
		return Boolean.FALSE;
	}
}
