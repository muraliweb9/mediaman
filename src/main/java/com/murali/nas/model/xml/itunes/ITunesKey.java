package com.murali.nas.model.xml.itunes;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public enum ITunesKey {
	
	MAJOR_VERSION("Major Version"),
	MINOR_VERSION("Minor Version"),
	DATE("Date"),
	APP_VERSION("Application Version"),
	FEATURES("Features"),
	SHOW_CONTENT_RATING("Show Content Ratings"),
	MUSIC_FOLDER("Music Folder"),
	LIB_PERSISTENT_ID("Library Persistent ID"),
	TRACKS("Tracks"),
	PLAYLISTS("Playlists"),
	TRACK_ID("Track ID"),
	NAME("Name"),
	ARTIST("Artist"),
	ALBUM("Album"),
	GROUPING("Grouping"),
	KIND("Kind"),
	SIZE("Size"),
	TOTAL_TIME("Total Time"),
	DATE_MODIFIED("Date Modified"),
	DATE_ADDED("Date Added"),
	BIT_RATE("Bit Rate"),
	ARTWORK_COUNT("Artwork Count"),
	PERSISTENT_ID("Persistent ID"),
	TRACK_TYPE("Track Type"),
	HAS_VIDEO("Has Video"),
	HD("HD"),
	VIDEO_WIDTH("Video Width"),
	VIDEO_HEIGHT("Video Height"),
	LOCATION("Location"),
	FILE_FOLDER_COUNT("File Folder Count"),
	TRACK_NUMBER("Track Number"),
	SAMPLE_RATE("Sample Rate"),
	GENRE("Genre"),
	LIBRARY_FOLDER_COUNT("Library Folder Count"),
	COMPOSER("Composer"),
	YEAR("Year"),
	COMMENTS("Comments"),
	PLAY_DATE("Play Date"),
	PLAY_DATE_UTC("Play Date UTC"),
	BPM("BPM"),
	PLAY_COUNT("Play Count"),
	SORT_NAME("Sort Name"),
	SORT_ARTIST("Sort Artist"),
	SORT_ALBUM("Sort Album"),
	SORT_ALBUM_ARTIST("Sort Album Artist"),
	SORT_COMPOSER("Sort Composer"),
	COMPILATION("Compilation"),
	ALBUM_ARTIST("Album Artist"),
	NORMALIZATION("Normalization"),
	TRACK_COUNT("Track Count"),
	DISC_COUNT("Disc Count"),
	VOLUME_ADJUSTMENT("Volume Adjustment"),
	DISC_NUMBER("Disc Number"),
	SERIES("Series"),
	PART_OF_GAPLESS_ALBUM("Part Of Gapless Album"),
	MASTER("Master"),
	PLAYLIST_ID("Playlist ID"),
	ALL_ITEMS("All Items"),
	PLAYLIST_PERSISTENT_ID("Playlist Persistent ID"),
	VISIBLE("Visible"),
	PLAYLIST_ITEMS("Playlist Items"),
	SMART_INFO("Smart Info"),
	SMART_CRITERIA("Smart Criteria"),
	PARENT_PERSISTENT_ID("Parent Persistent ID"),
	FOLDER("Folder"),
	DISTINGUISHED_KIND("Distinguished Kind"),
	MOVIES("Movies"),
	TV_SHOWS("TV Shows"),
	MUSIC("Music"),
	NOT_HANDLED("NOT_HANDLED");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ITunesKey.class);
	
	private static final Map<String, ITunesKey> ENUM_MAP = new HashMap<>();
	
	private static String knownKeys = null;
	
	private String value;
	
	
	private ITunesKey(final String value) {
		this.value = value;
		
	}
	
	
	public String getValue() {
		return this.value;
	}
	
	
	public boolean hasValue(final String value) {
		return this.value.equalsIgnoreCase(value);
	}
	
	
	private static void init() {
		StringBuilder strBuilder = new StringBuilder();
		for (final ITunesKey key : values()) {
			ENUM_MAP.put(key.value, key);
			
			strBuilder.append("[");
			strBuilder.append(key.value);
			strBuilder.append("]");
			strBuilder.append(" ");
		}
		knownKeys = strBuilder
			.toString()
			.trim();
	}
	
	
	public static ITunesKey forValue(final String value) {
		
		if (ENUM_MAP.isEmpty()) {
			init();
		}
		
		ITunesKey key = ENUM_MAP.get(value);
		
		if (key != null) {
			return key;
		}
		
		// LOGGER.info(String.format("ITunesKey value not recognised [%s] must be one of [%s]", value, knownKeys));
		return NOT_HANDLED;
	}
}
