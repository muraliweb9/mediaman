package com.murali.nas.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.mp4parser.boxes.apple.AppleAlbumBox;
import com.googlecode.mp4parser.boxes.apple.AppleArtistBox;
import com.googlecode.mp4parser.boxes.apple.AppleGenreBox;
import com.googlecode.mp4parser.boxes.apple.AppleNameBox;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public enum MetaDataType {
	
	TITLE(new AppleNameBox().getType()),
	ARTIST(new AppleArtistBox().getType()),
	ALBUM(new AppleAlbumBox().getType()),
	GROUPING(new AppleGenreBox().getType()),
	GENRE(new AppleGenreBox().getType()),
	TOOL("\u00a9too"),
	SEASON("tvsn"),
	EPISODE("tves"),
	TVSH("tvsh"),
	CPIL("cpil"),
	PLAYLIST("Playlist"),
	NOT_HANDLED("NOT_HANDLED");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataType.class);
	
	private String type;
	
	
	private MetaDataType(final String type) {
		this.type = type;
	}
	
	
	public final String getType() {
		return this.type;
	}
	
	
	public final boolean hasValue(final String type) {
		return this.type.equalsIgnoreCase(type);
	}
	
	
	public static MetaDataType forValue(final String type) {
		for (final MetaDataType meta : values()) {
			if (meta.hasValue(type.trim())) {
				return meta;
			}
		}
		
		StringBuilder strBuilder = new StringBuilder();
		for (final MetaDataType meta : values()) {
			strBuilder.append(meta.type);
			strBuilder.append(" ");
		}
		
		LOGGER.info(String.format("MetaDataType not handled [%s] must be one of [%s]", type, strBuilder.toString()
			.trim()));
		
		return NOT_HANDLED;
	}
	
}
