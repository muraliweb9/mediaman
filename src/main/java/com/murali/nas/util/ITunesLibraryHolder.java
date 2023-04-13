package com.murali.nas.util;

import static com.murali.nas.util.perf.PerfMonitor.startPerf;
import static com.murali.nas.util.perf.PerfMonitor.stopPerf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.murali.nas.MediaManagerException;
import com.murali.nas.filter.NamePlaylistFilter;
import com.murali.nas.filter.NamePlaylistFilter.Match;
import com.murali.nas.filter.NamePlaylistOperation;
import com.murali.nas.filter.NamePlaylistOperation.Operation;
import com.murali.nas.filter.ToStringFunction;
import com.murali.nas.model.MetaData;
import com.murali.nas.model.xml.itunes.Dict;
import com.murali.nas.model.xml.itunes.ITunesKey;
import com.murali.nas.model.xml.itunes.Playlist;
import com.murali.nas.model.xml.itunes.Plist;
import com.murali.nas.model.xml.itunes.Track;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class ITunesLibraryHolder {
	
	private BigDecimal version;
	
	private ITunesDictParser dictParser = null;
	
	private List<Track> cachedTracks = new ArrayList<>();
	private List<Track> cachedVideoTracks = new ArrayList<>();
	private List<Playlist> cachedPlaylists = new ArrayList<>();
	
	
	public ITunesLibraryHolder(final String iTunesLibraryFile)
		throws IOException, MediaManagerException {
		this(new FileInputStream(iTunesLibraryFile));
	}
	
	
	public ITunesLibraryHolder(final File iTunesLibraryFile)
		throws IOException, MediaManagerException {
		this(new FileInputStream(iTunesLibraryFile));
	}
	
	
	public ITunesLibraryHolder(final InputStream input) throws IOException,
		MediaManagerException {
		
		String tag = startPerf();
		
		Plist iTunesLibrary = null;
		JAXBContext context;
		
		try {
			context = JAXBContext.newInstance(Plist.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			iTunesLibrary = (Plist) unmarshaller.unmarshal(input);
			initialise(iTunesLibrary);
		} catch (JAXBException e) {
			throw new MediaManagerException(String.format(
				"Unable to parse iTunes library file", e));
		} finally {
			stopPerf(tag);
		}
	}
	
	
	private void initialise(final Plist iTunesLibrary)
		throws MediaManagerException {
		String tag = startPerf();
		if (iTunesLibrary == null) {
			throw new MediaManagerException(
				String.format("iTunes library is not initlaised "));
		}
		version = iTunesLibrary.getVersion();
		dictParser = new ITunesDictParser(iTunesLibrary.getDict());
		stopPerf(tag);
	}
	
	
	public final BigDecimal getVersion() {
		return version;
	}
	
	
	public final BigInteger getMajorVersion() {
		return (BigInteger) dictParser.getValue(ITunesKey.MAJOR_VERSION);
	}
	
	
	public final BigInteger getMinorVersion() {
		return (BigInteger) dictParser.getValue(ITunesKey.MINOR_VERSION);
	}
	
	
	public final Date getDate() throws MediaManagerException {
		return DateUtils.fromTimeStamp((String) dictParser
			.getValue(ITunesKey.DATE));
	}
	
	
	public final String getApplicationVersion() {
		return (String) dictParser.getValue(ITunesKey.APP_VERSION);
	}
	
	
	public final BigInteger getFeatures() {
		return (BigInteger) dictParser.getValue(ITunesKey.FEATURES);
	}
	
	
	public final Boolean isShowContentRatings() {
		return (Boolean) dictParser.getValue(ITunesKey.SHOW_CONTENT_RATING);
	}
	
	
	public final String getMusicFolder() {
		return (String) dictParser.getValue(ITunesKey.MUSIC_FOLDER);
	}
	
	
	public final String getLibPersistentId() {
		return (String) dictParser.getValue(ITunesKey.LIB_PERSISTENT_ID);
	}
	
	
	public final List<Track> getTracks() {
		String tag = startPerf();
		if (cachedTracks.isEmpty()) {
			Dict dict = (Dict) dictParser.getValue(ITunesKey.TRACKS);
			ITunesDictParser localParser = new ITunesDictParser(dict);
			for (Object key : localParser.getKeys()) {
				cachedTracks.add(new Track((Dict) localParser.getValue(key)));
			}
		}
		stopPerf(tag);
		return cachedTracks;
	}
	
	
	public final List<Track> getVideoTracks() {
		String tag = startPerf();
		if (cachedVideoTracks.isEmpty()) {
			List<Track> videoTracks = getTracks()
				.stream()
				.filter(t -> t.getHasVideo())
				.collect(Collectors.toList());
			cachedVideoTracks.addAll(videoTracks);
		}
		stopPerf(tag);
		return cachedVideoTracks;
	}
	
	
	public final List<Playlist> getPlaylists(
		final ToStringFunction<Playlist> playlistOp) {
		
		String tag = startPerf();
		if (cachedPlaylists.isEmpty()) {
			List<ITunesDictParser> array = (ArrayList<ITunesDictParser>) dictParser
				.getValue(ITunesKey.PLAYLISTS);
			
			for (ITunesDictParser localParser : array) {
				cachedPlaylists.add(new Playlist(this, localParser.getDict(),
					playlistOp));
			}
		}
		stopPerf(tag);
		return cachedPlaylists;
	}
	
	
	public final List<Playlist> getPlaylists(final String nameKey,
		final Match match, final Boolean smart, final Operation operation,
		final String renameKey, final String renameVal)
		throws MediaManagerException {
		
		String tag = startPerf();
		
		List<Playlist> playlists = null;
		Predicate<Playlist> nameFilter = new NamePlaylistFilter(nameKey, match);
		ToStringFunction<Playlist> playlistOp = new NamePlaylistOperation(
			operation, renameKey, renameVal);
		
		if (smart) {
			playlists = getPlaylists(playlistOp)
				.stream()
				.filter(p -> p.isSmartPlaylist())
				.filter(nameFilter)
				.collect(Collectors.toList());
		} else {
			playlists = getPlaylists(playlistOp)
				.stream()
				.filter(nameFilter)
				.collect(Collectors.toList());
		}
		
		stopPerf(tag);
		
		return playlists;
		
	}
	
	
	public final Map<File, MetaData> filterMetaData(final String playlistName, Map<File, MetaData> metaData) {
		
		Playlist pList = getPlaylists(null)
			.stream()
			.filter(p -> playlistName.equals(p.getName()))
			.findFirst()
			.get();
		
		List<Track> tracks = getVideoTracks(pList);
		
		return metaData
			.entrySet()
			.stream()
			.filter(e -> tracks.stream().anyMatch(t -> t.isLocationMatch(e.getValue())))
			.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
		
	}
	
	
	public final List<MetaData> filterMetaData(final String playlistName, List<MetaData> metaData) {
		Playlist pList = getPlaylists(null)
			.stream()
			.filter(p -> playlistName.equals(p.getName()))
			.findFirst()
			.get();
		
		List<Track> tracks = getVideoTracks(pList);
		
		return metaData
			.stream()
			.filter(m -> tracks.stream().anyMatch(t -> t.isLocationMatch(m)))
			.collect(Collectors.toList());
	}
	
	
	public Boolean match(MetaData meta, List<Track> tracks) {
		return tracks.stream().anyMatch(t -> t.isLocationMatch(meta));
	}
	
	
	public final List<Track> getVideoTracks(final String playlistName) {
		Playlist pList = getPlaylists(null)
			.stream()
			.filter(p -> playlistName.equals(p.getName()))
			.findFirst()
			.get();
		
		return getVideoTracks(pList);
	}
	
	
	public final Boolean playlistContainsVideoTrack(final String playlistName,
		final MetaData meta) {
		
		Playlist pList = getPlaylists(null)
			.stream()
			.filter(p -> playlistName.equals(p.getName()))
			.findFirst()
			.get();
		
		return getVideoTracks(pList)
			.stream()
			.anyMatch(t -> t.isLocationMatch(meta));
		
	}
	
	
	public final List<Track> getVideoTracks(final Playlist playlist) {
		
		List<BigInteger> trackIds = playlist.getTrackIds();
		return getVideoTracks()
			.stream()
			.filter(t -> trackIds.contains(t.getTrackId()))
			.collect(Collectors.toList());
	}
	
	
	public final Boolean playlistContains(final String playlistName,
		final MetaData meta) {
		
		Playlist pList = getPlaylists(null)
			.stream()
			.filter(p -> playlistName.equals(p.getName()))
			.findFirst()
			.get();
		
		return getTracks(pList)
			.stream()
			.anyMatch(t -> t.isLocationMatch(meta));
		
	}
	
	
	public final List<Track> getTracks(final Playlist playlist) {
		
		List<BigInteger> trackIds = playlist.getTrackIds();
		return getTracks()
			.stream()
			.filter(t -> trackIds.contains(t.getTrackId()))
			.collect(Collectors.toList());
	}
}
