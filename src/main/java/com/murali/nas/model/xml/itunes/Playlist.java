package com.murali.nas.model.xml.itunes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.murali.nas.MediaManagerException;
import com.murali.nas.filter.ToStringFunction;
import com.murali.nas.util.ITunesDictParser;
import com.murali.nas.util.ITunesLibraryHolder;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class Playlist {
	
	private final ToStringFunction<Playlist> playlistOperation;
	private final ITunesLibraryHolder library;
	private final ITunesDictParser parser;
	
	private List<BigInteger> cachedTrackIds = new ArrayList<BigInteger>();
	
	
	public Playlist(final ITunesLibraryHolder library, final Dict playlistDict,
		final ToStringFunction<Playlist> playlistOperation) {
		this.playlistOperation = playlistOperation;
		this.library = library;
		this.parser = new ITunesDictParser(playlistDict);
	}
	
	
	public final Object getValue(final ITunesKey key) {
		return parser.getValue(key);
	}
	
	
	public final String getName() {
		return (String) parser.getValue(ITunesKey.NAME);
	}
	
	
	public final String getTransformedName() {
		if (playlistOperation != null) {
			return playlistOperation.applyAsString(this);
		}
		return getName();
	}
	
	
	public final Boolean getMaster() {
		return (Boolean) parser.getValue(ITunesKey.MASTER);
	}
	
	
	public final BigInteger getPlaylistId() {
		return (BigInteger) parser.getValue(ITunesKey.PLAYLIST_ID);
	}
	
	
	public final String getPlaylisPersistentId() {
		return (String) parser.getValue(ITunesKey.PLAYLIST_PERSISTENT_ID);
	}
	
	
	public final Boolean getVisible() {
		return (Boolean) parser.getValue(ITunesKey.VISIBLE);
	}
	
	
	public final Boolean getAllItems() {
		return (Boolean) parser.getValue(ITunesKey.ALL_ITEMS);
	}
	
	
	public final ArrayList<ITunesDictParser> getPlaylistItems() {
		return (ArrayList<ITunesDictParser>) parser.getValue(ITunesKey.PLAYLIST_ITEMS);
	}
	
	
	public final Boolean isSmartPlaylist() {
		if (getValue(ITunesKey.SMART_INFO) != null) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	
	public final List<BigInteger> getTrackIds() {
		if (cachedTrackIds.isEmpty()) {
			ArrayList<ITunesDictParser> playlistItems = getPlaylistItems();
			if (playlistItems != null && !playlistItems.isEmpty()) {
				for (ITunesDictParser playlistItem : playlistItems) {
					ITunesDictParser localParser = new ITunesDictParser(playlistItem.getDict());
					BigInteger trackId = (BigInteger) localParser.getValue(ITunesKey.TRACK_ID);
					cachedTrackIds.add(trackId);
				}
			}
		}
		
		return cachedTrackIds;
	}
	
	
	public final List<Track> getTracks() { 
	return library.getTracks(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName())
		.append("/")
		.append(getTracks()==null?"null":getTracks().size());
		
		return sb.toString();
	}
	
}
