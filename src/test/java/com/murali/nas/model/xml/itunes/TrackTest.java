package com.murali.nas.model.xml.itunes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.murali.nas.model.MetaData;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ Track.class, MetaData.class })
@PowerMockIgnore("javax.management.*")
public class TrackTest {
	
	@Test
	public void testPassEquals() throws UnsupportedEncodingException {
		File file = mock(File.class);
		Track track = mock(Track.class);
		MetaData metaData = mock(MetaData.class);
		when(track.getLocationFileName()).thenReturn("Vaada Bin Ladan.m4v");
		when(metaData.getFile()).thenReturn(file);
		when(file.getName()).thenReturn("Vaada Bin Ladan.m4v");
		when(track.equals(metaData)).thenCallRealMethod();
		assertTrue(track.equals(metaData));
	}
	
	
	@Test
	public void testFailEquals() throws UnsupportedEncodingException {
		File file = mock(File.class);
		Track track = mock(Track.class);
		MetaData metaData = mock(MetaData.class);
		when(track.getLocationFileName()).thenReturn("Vaada Bin Ladan 2.m4v");
		when(metaData.getFile()).thenReturn(file);
		when(file.getName()).thenReturn("Vaada Bin Ladan.m4v");
		assertFalse(track.equals(metaData));
	}
	
}
