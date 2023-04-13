package com.murali.nas.filter;

import static com.murali.nas.filter.NamePlaylistFilter.Match.BEGINS_WITH;
import static com.murali.nas.filter.NamePlaylistFilter.Match.CONTAINS;
import static com.murali.nas.filter.NamePlaylistFilter.Match.ENDS_WITH;
import static com.murali.nas.filter.NamePlaylistFilter.Match.EQUALS;
import static com.murali.nas.filter.NamePlaylistFilter.Match.REGEX;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.function.Predicate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.murali.nas.model.xml.itunes.Playlist;


@RunWith(PowerMockRunner.class)
@PrepareForTest(Playlist.class)
public class PlaylistFilterTest {
	
	@Test
	public void testFilterPlaylist() {
		Predicate<Playlist> playlistFilterName2 = new NamePlaylistFilter("name1", EQUALS);
		Predicate<Playlist> playlistFilterBegin = new NamePlaylistFilter("beginWith", BEGINS_WITH);
		Predicate<Playlist> playlistFilterContains = new NamePlaylistFilter("containWith", CONTAINS);
		Predicate<Playlist> playlistFilterEnds = new NamePlaylistFilter("endsWith", ENDS_WITH);
		Predicate<Playlist> playlistFilterRegex = new NamePlaylistFilter("[A-Za-z]*", REGEX);
		
		Predicate<Playlist> playlistFilterNull = new NamePlaylistFilter(null, null);
		
		Playlist playlist = mock(Playlist.class);
		
		when(playlist.getName()).thenReturn("name1");
		
		when(playlist.getName()).thenReturn("name2");
		
		when(playlist.getName()).thenReturn("name1");
		assertTrue(playlistFilterName2.test(playlist));
		
		when(playlist.getName()).thenReturn("name2");
		assertFalse(playlistFilterName2.test(playlist));
		
		when(playlist.getName()).thenReturn("beginWithNameOfPlaylist");
		assertTrue(playlistFilterBegin.test(playlist));
		
		when(playlist.getName()).thenReturn("xbeginWithNameOfPlaylist");
		assertFalse(playlistFilterBegin.test(playlist));
		
		when(playlist.getName()).thenReturn("dfhdasjkcontainWithhhsdhsjkasdhk");
		assertTrue(playlistFilterContains.test(playlist));
		
		when(playlist.getName()).thenReturn("dfhdasjkcontainxxxWithhhsdhsjkasdhk");
		assertFalse(playlistFilterContains.test(playlist));
		
		when(playlist.getName()).thenReturn("dfhdasjkcontainWithhhsdhsjkasdhkendsWith");
		assertTrue(playlistFilterEnds.test(playlist));
		
		when(playlist.getName()).thenReturn("dfhdasjkcontainWithhhsdhsjkasdhkendsWithf");
		assertFalse(playlistFilterEnds.test(playlist));
		
		when(playlist.getName()).thenReturn("dfhdasjkcontainWithhhsdhsjkasdhkendsWith");
		assertTrue(playlistFilterRegex.test(playlist));
		
		when(playlist.getName()).thenReturn("dfhdasjkcontainWithhhsdhsjkasdhkendsWithf1");
		assertFalse(playlistFilterRegex.test(playlist));
		
		when(playlist.getName()).thenReturn("dfhdasjkcontainWithhhsdhsjkasdhkendsWithf1");
		assertFalse(playlistFilterRegex.test(null));
		
		when(playlist.getName()).thenReturn(null);
		assertFalse(playlistFilterRegex.test(playlist));
		
		when(playlist.getName()).thenReturn(" ");
		assertFalse(playlistFilterRegex.test(playlist));
		
		when(playlist.getName()).thenReturn("dfhdasjkcontainWithhhsdhsjkasdhkendsWithf1");
		assertFalse(playlistFilterNull.test(playlist));
		
	}
	
}
