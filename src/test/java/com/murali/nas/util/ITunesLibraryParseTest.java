package com.murali.nas.util;

import static com.murali.nas.util.perf.PerfMonitor.startPerf;
import static com.murali.nas.util.perf.PerfMonitor.stopPerf;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murali.nas.MediaManagerException;
import com.murali.nas.filter.NamePlaylistFilter.Match;
import com.murali.nas.filter.NamePlaylistOperation.Operation;
import com.murali.nas.model.MetaData;
import com.murali.nas.model.xml.itunes.Playlist;
import com.murali.nas.model.xml.itunes.Track;
import com.murali.nas.model.FileHolder;



@RunWith(PowerMockRunner.class)
@PrepareForTest(MetaData.class)
@PowerMockIgnore("javax.management.*")
public class ITunesLibraryParseTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ITunesLibraryParseTest.class);
	
	
	@Before
	public void before() throws IOException {
		PropertyHolder.initialise("src/main/config/properties/configNoOverride.properties");
	}
	
	
	@Test
	public void testParseLibraryData() throws IOException, JAXBException, MediaManagerException {
		String tag = startPerf();
		
		ITunesLibraryHolder iTunesLibraryHolder = new ITunesLibraryHolder(new File(
			"src/main/config/properties/iTunes Music Library.xml"));
		
		assertEquals(new BigDecimal("1.0"), iTunesLibraryHolder.getVersion());
		assertEquals(new BigInteger("1"), iTunesLibraryHolder.getMajorVersion());
		assertEquals(new BigInteger("1"), iTunesLibraryHolder.getMinorVersion());
		assertEquals("Fri Jan 30 22:50:10 GMT 2015", iTunesLibraryHolder.getDate().toString());
		assertEquals("12.0.1.26", iTunesLibraryHolder.getApplicationVersion());
		assertEquals(new BigInteger("5"), iTunesLibraryHolder.getFeatures());
		assertEquals(Boolean.TRUE, iTunesLibraryHolder.isShowContentRatings());
		assertEquals("file://localhost/S:/iTunes/", iTunesLibraryHolder.getMusicFolder());
		assertEquals("B40AC24284D5BDEB", iTunesLibraryHolder.getLibPersistentId());
		
		stopPerf(tag);
		
	}
	
	
	@Test
	public void testParseLibraryPlaylistSimple() throws IOException, JAXBException, MediaManagerException {
		String tag = startPerf();
		
		ITunesLibraryHolder iTunesLibraryHolder = new ITunesLibraryHolder(new File(
			"src/main/config/properties/iTunes Music Library.xml"));
		
		assertEquals(6196, iTunesLibraryHolder.getTracks().size());
		assertEquals(464, iTunesLibraryHolder.getVideoTracks().size());
		assertEquals(53, iTunesLibraryHolder.getPlaylists(null).size());
		assertEquals(17, iTunesLibraryHolder.getPlaylists("SM -", Match.BEGINS_WITH, Boolean.TRUE, null, null, null)
			.size());
		assertEquals(5, iTunesLibraryHolder.getPlaylists("Vijay", Match.CONTAINS, Boolean.FALSE, null, null, null)
			.size());
		
		stopPerf(tag);
	}
	
	
	@Test
	public void testParseLibraryTiming() throws IOException, JAXBException, MediaManagerException {
		String tag = startPerf();
		
		ITunesLibraryHolder iTunesLibraryHolder = new ITunesLibraryHolder(new File(
			"src/main/config/properties/iTunes Music Library.xml"));
		// TIMINING START
		Long start = System.currentTimeMillis();
		
		List<Playlist> playlists = iTunesLibraryHolder.getPlaylists(null);
		List<Integer> playlistSizes = new ArrayList<>();
		
		for (Playlist playlist : playlists) {
			
			Integer playlistSize = iTunesLibraryHolder.getTracks(playlist).size();
			
			playlistSizes.add(playlistSize);
			for (Track track : iTunesLibraryHolder.getTracks(playlist)) {
			}
		}
		// TIMING -STOP
		LOGGER.info(String.format("Playlist parse took [%d]ms", System.currentTimeMillis() - start));
		
		stopPerf(tag);
	}
	
	
	@Test
	public void testParseLibraryPlaylistAdvanced() throws IOException, JAXBException, MediaManagerException,
		UnsupportedEncodingException {
		String tag = startPerf();
		
		ITunesLibraryHolder iTunesLibraryHolder = new ITunesLibraryHolder(new File(
			"src/main/config/properties/iTunes Music Library.xml"));
		
		List<Playlist> playlistsForNameTest = iTunesLibraryHolder.getPlaylists("Vijay",
			Match.CONTAINS, TRUE, Operation.REPLACE_WITH, "SM -", "");
		
		List<String> expPlaylistNames = Arrays.asList("SM - Vijay", "SM - Vijay Melody", "SM - Vijay Dance");
		List<String> expPlaylistTransformedNames = Arrays.asList("Vijay", "Vijay Melody", "Vijay Dance");
		
		List<String> actualPlaylistNames = new ArrayList<>();
		List<String> actualPlaylistTransformedNames = new ArrayList<>();
		for (Playlist playlist : playlistsForNameTest) {
			actualPlaylistNames.add(playlist.getName());
			actualPlaylistTransformedNames.add(playlist.getTransformedName());
		}
		assertTrue(expPlaylistNames.containsAll(actualPlaylistNames));
		assertTrue(expPlaylistTransformedNames.containsAll(actualPlaylistTransformedNames));
		
		List<Integer> expPlaylistSizes = Arrays.asList(6196, 5642, 0, 0, 30, 0, 0, 0, 0, 0, 27, 7, 464, 37, 16, 7, 184,
			43, 1, 48, 32,
			114, 12, 4, 3, 15, 37, 24, 13, 11, 14, 29, 16, 59, 5, 5, 2, 30, 1, 2, 8, 14, 18, 9, 32, 5, 1, 7, 2, 27, 23,
			10, 4);
		
		List<Playlist> playlists = iTunesLibraryHolder.getPlaylists(null);
		List<Integer> playlistSizes = new ArrayList<>();
		
		for (Playlist playlist : playlists) {
			
			Integer playlistSize = iTunesLibraryHolder.getTracks(playlist).size();
			
			assertEquals((Integer) playlist.getTracks().size(), playlistSize);
			
			playlistSizes.add(playlistSize);
			for (Track track : iTunesLibraryHolder.getTracks(playlist)) {
			}
		}
		
		assertEquals(expPlaylistSizes, playlistSizes);
		
		MetaData meta = mock(MetaData.class);
		when(meta.getFileName()).thenReturn("Step Step.m4v");
		assertTrue(iTunesLibraryHolder.playlistContains("SM - Vijay", meta));
		
		meta = mock(MetaData.class);
		when(meta.getFileName()).thenReturn("Desi Girl.mp4");
		assertFalse(iTunesLibraryHolder.playlistContains("SM - Vijay", meta));
		
		List<MetaData> reducedMeta = iTunesLibraryHolder
			.filterMetaData("SM - Vijay Dance",
				Arrays.asList(
					mockMeta("Hey Rama Rama.mp4"),
					mockMeta("Step Step.m4v"),
					mockMeta("Heartille.mp4"),
					mockMeta("Patampuchi.m4v"),
					mockMeta("En Friend.mp4")
					));
		assertEquals(3, reducedMeta.size());
		
		stopPerf(tag);
	}
	
	@Test
	public void testParseAppleMetaData() {
		FileHolder fileHolder = new FileHolder(new File("src/test/resources/Ponmeni Uruguthe.mp4"));
		
		Map<File, MetaData> metaMap = new MetaDataParser(Arrays.asList(fileHolder)).parseMetaData();
		
		assertEquals(1, metaMap.size());
		
		MetaData meta = metaMap.values().iterator().next();
		
		assertFalse(meta.isTitleBlank());
		assertEquals("Ponmeni Uruguthe", meta.getTitle());
		assertEquals("Ponmeni Uruguthe.mp4", meta.getFileName());
		
	}
	
	
	
	private MetaData mockMeta(String name) {
		MetaData meta = mock(MetaData.class);
		when(meta.getFileName()).thenReturn(name);
		return meta;
		
	}
	
}
