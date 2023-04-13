package com.murali.nas.model;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.MetaBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.UserDataBox;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class FileHolder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileHolder.class);
	
	private Optional<File> file;
	
	
	public FileHolder(final File file) {
		this.file = Optional.ofNullable(file);
	}
	
	
	public final File getFile() {
		return file.orElseGet(null);
	}
	
	
	/**
	 * Always call close() on when finished with the meta data to close the stream.
	 * 
	 * @return the Metabox
	 * @throws IOException
	 */
	public final MetaBox getMetaBox() throws IOException {
		
		IsoFile isoFile = new IsoFile(getFile().getAbsolutePath());
		MovieBox moov = isoFile.getBoxes(MovieBox.class).get(0);
		UserDataBox udta = moov.getBoxes(UserDataBox.class).get(0);
		MetaBox meta = udta.getBoxes(MetaBox.class).get(0);
		
		// DONT CLOSE i.e. isoFile.close() - MetaBox will not have data when returned.
		
		return meta;
		
	}
	
	
	/**
	 * This should be called when parsing of meta is complete.
	 * 
	 * @throws IOException
	 */
	public final void close() throws IOException {
		
	}
	
	
	@Override
	public String toString() {
		return file.isPresent() ? file.get().getAbsolutePath() : "null";
	}
	
}
