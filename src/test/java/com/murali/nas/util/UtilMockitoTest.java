package com.murali.nas.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.MetaBox;
import com.coremedia.iso.boxes.apple.AppleItemListBox;
import com.googlecode.mp4parser.boxes.apple.AppleAlbumBox;
import com.googlecode.mp4parser.boxes.apple.AppleArtistBox;
import com.googlecode.mp4parser.boxes.apple.AppleGenreBox;
import com.googlecode.mp4parser.boxes.apple.AppleNameBox;
import com.googlecode.mp4parser.boxes.apple.Utf8AppleDataBox;
import com.murali.nas.MediaManagerException;
import com.murali.nas.filter.NamePlaylistFilter.Match;
import com.murali.nas.filter.NamePlaylistOperation.Operation;
import com.murali.nas.model.FileHolder;
import com.murali.nas.model.MetaData;
import com.murali.nas.model.MetaDataType;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ FileHolder.class, FileUtil.class, URI.class, File.class,
	Path.class, Paths.class })
@PowerMockIgnore("javax.management.*")
public class UtilMockitoTest {
	
	@Before
	public void setUp() throws Exception {
	}
	
	
	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testParseAllowableFileExtensions() {
		
		List<String> allowed = CollectionUtil
			.parseAllowedExtensions("mp3, mp4, m4v");
		assertTrue(allowed.contains("mp3"));
		assertTrue(allowed.contains("mp4"));
		assertTrue(allowed.contains("m4v"));
		assertEquals(3, allowed.size());
		assertFalse(allowed.contains("avi"));
	}
	
	
	@Test
	public void testConfigLoader() throws IOException {
		
		PropertyHolder.initialise(new File(
			"src/main/config/properties/config.properties"));
		
		assertEquals("/volume1/media/iTunes/Home Videos",
			PropertyHolder.getInputDir());
		assertEquals("/volume1/video", PropertyHolder.getOutputDir());
		
		assertEquals("/volume1/media_prep/work", PropertyHolder.getWorkDir());
		
		assertEquals(3, PropertyHolder.getAllowedExtensions().size());
		assertTrue(PropertyHolder.getAllowedExtensions().contains("mp4"));
		assertTrue(PropertyHolder.getAllowedExtensions().contains("m4v"));
		assertFalse(PropertyHolder.getAllowedExtensions().contains("mp3"));
		assertEquals(MetaDataType.GROUPING, PropertyHolder.getMediaGroupTag());
		assertTrue(PropertyHolder.getITunesPlaylistUseSmart());
		assertEquals("SM -", PropertyHolder.getITunesPlaylistMatchKey());
		assertEquals(Match.BEGINS_WITH,
			PropertyHolder.getITunesPlaylistMatchCriteriaKey());
		
		assertEquals("SM -", PropertyHolder.getITunesPlaylistRenameKey());
		assertEquals("", PropertyHolder.getITunesPlaylistRenameVal());
		assertEquals(Operation.REPLACE_WITH,
			PropertyHolder.getITunesPlaylistOperation());
		
		File myFile = mock(File.class);
		when(myFile.getAbsolutePath()).thenReturn("/home/user/music/nice-song.mp4");
		assertEquals(sysPath("/volume1/video/MyGroup/MySong.mp4"),
			PropertyHolder.getOutputPath("MyGroup", "MySong", myFile)
				.toString());
		assertEquals(sysPath("/volume1/video/MyGroup/UNKNOWN.mp4"),
			PropertyHolder.getOutputPath("MyGroup", null, myFile)
				.toString());
		assertEquals(sysPath("/volume1/video/MyGroup/UNKNOWN.mp4"),
			PropertyHolder.getOutputPath("MyGroup", "null", myFile)
				.toString());
		assertEquals(sysPath("/volume1/video/MyGroup/UNKNOWN.mp4"),
			PropertyHolder.getOutputPath("MyGroup", "  ", myFile)
				.toString());
		assertEquals(sysPath("/volume1/video/MyGroup/UNKNOWN.mp4"), PropertyHolder.getOutputPath("MyGroup", "", myFile)
			.toString());
		
		assertEquals(sysPath("/volume1/video/UNKNOWN/MySong.mp4"), PropertyHolder.getOutputPath(null, "MySong", myFile)
			.toString());
		assertEquals(sysPath("/volume1/video/UNKNOWN/MySong.mp4"),
			PropertyHolder.getOutputPath("null", "MySong", myFile)
				.toString());
		assertEquals(sysPath("/volume1/video/UNKNOWN/MySong.mp4"), PropertyHolder.getOutputPath("  ", "MySong", myFile)
			.toString());
		assertEquals(sysPath("/volume1/video/UNKNOWN/MySong.mp4"), PropertyHolder.getOutputPath("", "MySong", myFile)
			.toString());
		
		assertEquals(sysPath("/volume1/video/MyFolder"), PropertyHolder.getOutputDirPath("MyFolder").toString());
		assertEquals(sysPath("/volume1/video/UNKNOWN"), PropertyHolder.getOutputDirPath(null).toString());
		assertEquals(sysPath("/volume1/video/UNKNOWN"), PropertyHolder.getOutputDirPath("   ").toString());
		assertEquals(sysPath("/volume1/video/UNKNOWN"), PropertyHolder.getOutputDirPath("").toString());
	}
	
	
	@Test
	public void testConfigLoaderNoOverride() throws IOException {
		
		PropertyHolder.initialise(new File(
			"src/main/config/properties/configNoOverride.properties"));
		
		assertEquals(".." + File.separator + "media_prep" + File.separator
			+ "input", PropertyHolder.getInputDir());
		assertEquals(".." + File.separator + "media_prep" + File.separator
			+ "output", PropertyHolder.getOutputDir());
		
		assertEquals(".." + File.separator + "media_prep" + File.separator
			+ "work" + File.separator + "Melody.txt",
			PropertyHolder.getWorkDirFile("Melody"));
		
	}
	
	
	@Test
	public void testReadingOfAppleTags() throws IOException {
		
		Collection<FileHolder> files = new ArrayList<>();
		FileHolder fileHolder = mock(FileHolder.class);
		File file = mock(File.class);
		MetaBox metaBox = mock(MetaBox.class);
		
		List<Box> appleMetaBoxes = new ArrayList<>();
		AppleItemListBox appleMetaBox = mock(AppleItemListBox.class);
		appleMetaBoxes.add(appleMetaBox);
		
		List<Box> appleAttributeBoxes = new ArrayList<>();
		
		Utf8AppleDataBox titleBox = new AppleNameBox();
		titleBox.setValue("my-title");
		appleAttributeBoxes.add(titleBox);
		
		Utf8AppleDataBox artistBox = new AppleArtistBox();
		artistBox.setValue("my-artist");
		appleAttributeBoxes.add(artistBox);
		
		Utf8AppleDataBox albumBox = new AppleAlbumBox();
		albumBox.setValue("my-album");
		appleAttributeBoxes.add(albumBox);
		
		Utf8AppleDataBox genreBox = new AppleGenreBox();
		genreBox.setValue("my-genre");
		appleAttributeBoxes.add(genreBox);
		
		files.add(fileHolder);
		
		when(fileHolder.getFile()).thenReturn(file);
		when(fileHolder.getMetaBox()).thenReturn(metaBox);
		doNothing().when(fileHolder).close();
		when(metaBox.getBoxes()).thenReturn(appleMetaBoxes);
		when(appleMetaBox.getBoxes()).thenReturn(appleAttributeBoxes);
		
		Map<File, MetaData> fileMetaDataMap = new MetaDataParser(files)
			.parseMetaData();
		
		assertEquals(1, fileMetaDataMap.entrySet().size());
		for (Map.Entry<File, MetaData> metaData : fileMetaDataMap.entrySet()) {
			MetaData meta = metaData.getValue();
			assertEquals(file, metaData.getKey());
			assertEquals(file, meta.getFile());
			assertEquals("my-title", meta.getTitle());
			assertEquals("my-artist", meta.getArtist());
			assertEquals("my-album", meta.getAlbum());
		}
		
	}
	
	
	@Test
	public void testParseDate() throws MediaManagerException {
		String item = "2014-02-19T22:48:40Z";
		Date date = DateUtils.fromTimeStamp(item);
		String dateStr = DateUtils.toTimeStamp(date);
		
		assertEquals("Wed Feb 19 22:48:40 GMT 2014", date.toString());
		assertEquals(item, dateStr);
		
	}
	
	
	@Test(expected = MediaManagerException.class)
	public void testParseDateException() throws MediaManagerException {
		String invalidItem = "Hello World!" + "2014-02-19T22:48:40Z";
		DateUtils.fromTimeStamp(invalidItem);
	}
	
	
	@Test
	public void testEncodeDecode() throws UnsupportedEncodingException {
		String pre = "This is a string containing containing very strange characters `!\"$%^&*()-_+={[]}:;@'~#<>,.?/|\\";
		String encoded = "This%20is%20a%20string%20containing%20containing%20very%20strange%20characters%20%60%21%22%24%25%5E%26*%28%29-_%2B%3D%7B%5B%5D%7D%3A%3B%40%27%7E%23%3C%3E%2C.%3F%2F%7C%5C";
		
		assertEquals(pre, NetUtils.decode(encoded));
		assertEquals(encoded, NetUtils.encode(pre));
		
	}
	
	
	private String sysPath(String path) {
		return path.replace('/', File.separator.charAt(0));
	}
	
}
