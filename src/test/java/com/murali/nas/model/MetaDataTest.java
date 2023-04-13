package com.murali.nas.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.murali.nas.MediaManagerException;
import com.murali.nas.model.xml.itunes.Playlist;
import com.murali.nas.model.xml.itunes.Track;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ Playlist.class, Track.class, MetaData.class })
@PowerMockIgnore("javax.management.*")
public class MetaDataTest {
	
	@Test
	public void testMetaDataGroupBy() throws UnsupportedEncodingException, MediaManagerException {
		List<Playlist> playlists = new ArrayList<>();
		Map<File, MetaData> metaMap = new HashMap<>();
		
		Playlist playlist1 = mock(Playlist.class);
		Playlist playlist2 = mock(Playlist.class);
		Playlist playlist3 = mock(Playlist.class);
		Playlist playlist4 = mock(Playlist.class);
		Playlist playlist5 = mock(Playlist.class);
		
		playlists.add(playlist1);
		playlists.add(playlist2);
		playlists.add(playlist3);
		playlists.add(playlist4);
		playlists.add(playlist5);
		
		File file1 = mock(File.class);
		File file2 = mock(File.class);
		File file3 = mock(File.class);
		File file4 = mock(File.class);
		File file5 = mock(File.class);
		File file6 = mock(File.class);
		File file7 = mock(File.class);
		
		MetaData meta1 = mock(MetaData.class);
		MetaData meta2 = mock(MetaData.class);
		MetaData meta3 = mock(MetaData.class);
		MetaData meta4 = mock(MetaData.class);
		MetaData meta5 = mock(MetaData.class);
		MetaData meta6 = mock(MetaData.class);
		MetaData meta7 = mock(MetaData.class);
		
		metaMap.put(file1, meta1);
		metaMap.put(file2, meta2);
		metaMap.put(file3, meta3);
		metaMap.put(file4, meta4);
		metaMap.put(file5, meta5);
		metaMap.put(file6, meta6);
		metaMap.put(file7, meta7);
		
		Track track1 = mock(Track.class);
		Track track2 = mock(Track.class);
		Track track3 = mock(Track.class);
		Track track4 = mock(Track.class);
		Track track5 = mock(Track.class);
		
		when(track1.getLocationFileName()).thenReturn("fileName1.mp4");
		when(track2.getLocationFileName()).thenReturn("fileName2.mp4");
		when(track3.getLocationFileName()).thenReturn("fileName3.mp4");
		when(track4.getLocationFileName()).thenReturn("fileName4.mp4");
		when(track5.getLocationFileName()).thenReturn("fileName5.mp4");
		
		when(file1.getName()).thenReturn("fileName1.mp4");
		when(file2.getName()).thenReturn("fileName2.mp4");
		when(file3.getName()).thenReturn("fileName3.mp4");
		when(file4.getName()).thenReturn("fileName4.mp4");
		when(file5.getName()).thenReturn("fileName5.mp4");
		when(file6.getName()).thenReturn("fileName6.mp4");
		when(file7.getName()).thenReturn("fileName7.mp4");
		
		when(meta1.getFile()).thenReturn(file1);
		when(meta2.getFile()).thenReturn(file2);
		when(meta3.getFile()).thenReturn(file3);
		when(meta4.getFile()).thenReturn(file4);
		when(meta5.getFile()).thenReturn(file5);
		when(meta6.getFile()).thenReturn(file6);
		when(meta7.getFile()).thenReturn(file7);
		
		when(playlist1.getName()).thenReturn("Playlist1");
		when(playlist2.getName()).thenReturn("Playlist2");
		when(playlist3.getName()).thenReturn("Playlist3");
		when(playlist4.getName()).thenReturn("Playlist4");
		when(playlist5.getName()).thenReturn("Playlist5");
		
		when(playlist1.getTracks()).thenReturn(Arrays.asList(track1, track2));
		when(playlist2.getTracks()).thenReturn(Arrays.asList(track1, track2, track3));
		when(playlist3.getTracks()).thenReturn(null);
		when(playlist4.getTracks()).thenReturn(Arrays.asList(track3, track4));
		when(playlist5.getTracks()).thenReturn(Arrays.asList(track5));
		
		when(track1.equals(any(MetaData.class))).thenCallRealMethod();
		when(track2.equals(any(MetaData.class))).thenCallRealMethod();
		when(track3.equals(any(MetaData.class))).thenCallRealMethod();
		when(track4.equals(any(MetaData.class))).thenCallRealMethod();
		when(track5.equals(any(MetaData.class))).thenCallRealMethod();
		
		Map<String, List<MetaData>> sortedMetaMap = MetaData.groupBy(playlists, metaMap);
		
		assertEquals(2, sortedMetaMap.get(playlist1.getName()).size());
		assertEquals(3, sortedMetaMap.get(playlist2.getName()).size());
		assertEquals(0, sortedMetaMap.get(playlist3.getName()).size());
		assertEquals(2, sortedMetaMap.get(playlist4.getName()).size());
		assertEquals(1, sortedMetaMap.get(playlist5.getName()).size());
		
		assertTrue(sortedMetaMap.get(playlist1.getName()).containsAll(Arrays.asList(meta1, meta2)));
		assertTrue(sortedMetaMap.get(playlist2.getName()).containsAll(Arrays.asList(meta1, meta2, meta3)));
		assertTrue(sortedMetaMap.get(playlist3.getName()).isEmpty());
		assertTrue(sortedMetaMap.get(playlist4.getName()).containsAll(Arrays.asList(meta3, meta4)));
		assertTrue(sortedMetaMap.get(playlist5.getName()).containsAll(Arrays.asList(meta5)));
		
	}
	
	
	@Test
	public void testToString() {
		MetaData meta = mock(MetaData.class);
		
		when(meta.getTitle()).thenReturn("my-title");
		when(meta.getArtist()).thenReturn("my-artist");
		when(meta.getAlbum()).thenReturn("my-album");
		when(meta.getGrouping()).thenReturn("my-grouping");
		when(meta.getGenre()).thenReturn("my-genre");
		when(meta.getTool()).thenReturn("my-tool");
		when(meta.getSeason()).thenReturn("my-season");
		when(meta.getEpisode()).thenReturn("my-episode");
		
		when(meta.toString()).thenCallRealMethod();
		
		assertEquals("Title[my-title]Artist[my-artist]Album[my-album]Grouping[my-grouping]"
			+ "Genre[my-genre]Tool[my-tool]Season[my-season]Episode[my-episode]",
			meta.toString());
	}
	
	
	@Test
	public void testToStringEmpty() {
		MetaData meta = mock(MetaData.class);
		
		when(meta.getTitle()).thenReturn("my-title");
		when(meta.getArtist()).thenReturn("my-artist");
		when(meta.getAlbum()).thenReturn("my-album");
		when(meta.getGrouping()).thenReturn("my-grouping");
		when(meta.getGenre()).thenReturn("my-genre");
		when(meta.getTool()).thenReturn("");
		when(meta.getSeason()).thenReturn("my-season");
		when(meta.getEpisode()).thenReturn("");
		
		when(meta.toString()).thenCallRealMethod();
		
		assertEquals("Title[my-title]Artist[my-artist]Album[my-album]Grouping[my-grouping]"
			+ "Genre[my-genre]Season[my-season]",
			meta.toString());
	}
}
