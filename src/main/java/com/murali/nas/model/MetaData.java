package com.murali.nas.model;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.apple.AppleItemListBox;
import com.googlecode.mp4parser.boxes.apple.Utf8AppleDataBox;
import com.murali.nas.MediaManagerException;
import com.murali.nas.model.xml.itunes.Playlist;
import com.murali.nas.model.xml.itunes.Track;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class MetaData {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MetaData.class);
	
	private File file;
	
	private String title;
	
	private String artist;
	
	private String album;
	
	private String grouping;
	
	private String genre;
	
	private String tool;
	
	private String season;
	
	private String episode;
	
	private String playlist;
	
	private String tvsh;
	
	private String cpil;
	
	private List<KeyValueMeta> notHandled = new ArrayList<KeyValueMeta>();
	
	
	public MetaData(final AppleItemListBox appleListBox, final File file) {
		
		this.file = file;
		List<Box> boxes = appleListBox.getBoxes();
		for (Box box : boxes) {
			if (box instanceof Utf8AppleDataBox) {
				parseMeta((Utf8AppleDataBox) box);
			}
		}
	}
	
	
	private void parseMeta(final Utf8AppleDataBox box) {
		Utf8AppleDataBox appleBox = (Utf8AppleDataBox) box;
		String value = appleBox.getValue();
		switch (MetaDataType.forValue(appleBox.getType())) {
		case TITLE:
			this.title = value;
			break;
		case ARTIST:
			this.artist = value;
			break;
		case ALBUM:
			this.album = value;
			break;
		case GROUPING:
			this.grouping = value;
			break;
		case GENRE:
			this.genre = value;
			break;
		case TOOL:
			this.tool = value;
			break;
		case SEASON:
			this.season = value;
			break;
		case EPISODE:
			this.episode = value;
			break;
		case TVSH:
			this.tvsh = value;
			break;
		case CPIL:
			this.cpil = value;
			break;
		case NOT_HANDLED:
			this.notHandled.add(new KeyValueMeta(appleBox.getType(), value));
			break;
		default:
			break;
		}
	}
	
	
	public final File getFile() {
		return file;
	}
	
	
	public final String getFileName() {
		return getFile().getName();
	}
	
	
	public final String getTitle() {
		return this.title;
	}
	
	
	public final Boolean isTitleBlank() {
		return StringUtils.isBlank(getTitle());
	}
	
	
	public final String getArtist() {
		return this.artist;
	}
	
	
	public final String getAlbum() {
		return this.album;
	}
	
	
	public final String getGrouping() {
		return this.grouping;
	}
	
	
	public final String getTool() {
		return this.tool;
	}
	
	
	public final String getGrouping(final MetaDataType metaType) throws MediaManagerException {
		switch (metaType) {
		case TITLE:
			return title;
		case ARTIST:
			return artist;
		case ALBUM:
			return album;
		case GROUPING:
			return grouping;
		case GENRE:
			return genre;
		case TOOL:
			return tool;
		case SEASON:
			return season;
		case EPISODE:
			return episode;
		case PLAYLIST:
			return playlist;
		case NOT_HANDLED:
		default:
			throw new MediaManagerException(String.format("Not a valid or handled MetaDataType %s", metaType));
		}
	}
	
	
	public final String getGenre() {
		return this.genre;
	}
	
	
	public final String getEpisode() {
		return this.episode;
	}
	
	
	public final String getSeason() {
		return this.season;
	}
	
	
	public final String getPlaylist() {
		return this.playlist;
	}
	
	
	public final Boolean hasNotHandled() {
		return notHandled != null && !notHandled.isEmpty();
	}
	
	
	public static Map<String, List<MetaData>> groupBy(final MetaDataType metaTypeBeingGroupedBy,
		final Map<File, MetaData> metaMap) throws MediaManagerException {
		
		Map<String, List<MetaData>> groupedByMap = new HashMap<>();
		for (MetaData metaData : metaMap.values()) {
			String groupBy = metaData.getGrouping(metaTypeBeingGroupedBy);
			if (groupedByMap.get(groupBy) == null) {
				groupedByMap.put(groupBy, new ArrayList<MetaData>());
			}
			groupedByMap.get(groupBy).add(metaData);
		}
		
		return groupedByMap;
	}
	
	
	public static Map<String, List<MetaData>> groupBy(final List<Playlist> playlists,
		final Map<File, MetaData> metaMap) throws MediaManagerException, UnsupportedEncodingException {
		
		Map<String, List<MetaData>> groupedByMap = new HashMap<>();
		for (Playlist playlist : playlists) {
			String groupBy = playlist.getName();
			LOGGER.info(String.format("Playlist name [%s]", groupBy));
			List<MetaData> playlistMeta = new ArrayList<MetaData>();
			groupedByMap.put(groupBy, playlistMeta);
			if (playlist.getTracks() != null && !playlist.getTracks().isEmpty()) {
				outer: for (Track track : playlist.getTracks()) {
					for (MetaData meta : metaMap.values()) {
						LOGGER.debug(String.format("Comparing filenames track[%s] and meta[%s]",
							track.getLocationFileName(), meta.getFile().getName()));
						if (track.equals(meta)) {
							LOGGER.debug("Matched !!");
							playlistMeta.add(meta);
							continue outer;
						}
					}
				}
			}
			LOGGER.info(String.format("Playlist [%s] has [%d] matches", groupBy, playlistMeta.size()));
		}
		
		return groupedByMap;
	}
	
	
	public final String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("Title[" + getTitle() + "]");
		strBuilder.append("Artist[" + getArtist() + "]");
		strBuilder.append("Album[" + getAlbum() + "]");
		strBuilder.append("Grouping[" + getGrouping() + "]");
		strBuilder.append("Genre[" + getGenre() + "]");
		
		if (StringUtils.isNotBlank(getTool())) {
			strBuilder.append("Tool[" + getTool() + "]");
		}
		
		if (StringUtils.isNotBlank(getSeason())) {
			strBuilder.append("Season[" + getSeason() + "]");
		}
		
		if (StringUtils.isNotBlank(getEpisode())) {
			strBuilder.append("Episode[" + getEpisode() + "]");
		}
		
		if (StringUtils.isNotBlank(playlist)) {
			strBuilder.append("Playlist[" + this.playlist + "]");
		}
		
		if (StringUtils.isNotBlank(tvsh)) {
			strBuilder.append("tvsh[" + this.tvsh + "]");
		}
		
		if (StringUtils.isNotBlank(cpil)) {
			strBuilder.append("cpil[" + this.cpil + "]");
		}
		
		if (this.notHandled != null && !this.notHandled.isEmpty()) {
			strBuilder.append("  ****  ");
			strBuilder.append("Not Hanled").append("[");
			for (KeyValueMeta keyVal : this.notHandled) {
				strBuilder.append(keyVal);
			}
			strBuilder.append("]");
		}
		
		return strBuilder.toString();
	}
}
