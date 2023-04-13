package com.murali.nas.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.MetaBox;
import com.coremedia.iso.boxes.apple.AppleItemListBox;
import com.murali.nas.model.FileHolder;
import com.murali.nas.model.MetaData;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class MetaDataParser {
	
	private static final Logger LOGGER = LoggerFactory
		.getLogger(MetaDataParser.class);
	
	private Collection<FileHolder> files;
	
	
	public MetaDataParser(final String folderName,
		final List<String> allowedExt) {
		initialise(folderName, allowedExt);
	}
	
	
	public MetaDataParser(final Collection<FileHolder> files) {
		this.files = files;
	}
	
	
	private void initialise(final String folderName, final List<String> allowedExt) {
		LOGGER.info(String.format(
			"Parsing folder [%s] for files with extension [%s]",
			folderName, allowedExt));
		this.files = new ArrayList<>();
		File folder = new File(folderName);
		for (File file : FileUtils.listFiles(folder, allowedExt.toArray(new String[0]), Boolean.TRUE)) {
			files.add(new FileHolder(file));
		}
	}
	
	
	public final Map<File, MetaData> parseMetaData() {
		
		Map<File, MetaData> fileMetaMap = new ConcurrentHashMap<>();
		for (FileHolder file : files) {
			try {
				
				LOGGER.info(String.format("Reading meta for file [%s]", file));
				MetaBox meta = file.getMetaBox();
				
				MetaData metaData = null;
				for (Box metaBox : meta.getBoxes()) {
					if (metaBox instanceof AppleItemListBox) {
						LOGGER.info(String.format("Parsing meta class %s.",
							metaBox.getClass().getName()));
						metaData = getAppleMetaData((AppleItemListBox) metaBox,
							file.getFile());
						if (!fileMetaMap.containsKey(file)) {
							fileMetaMap.put(file.getFile(), metaData);
						} else {
							LOGGER.error(String
								.format("Duplicate AppleItemListBox in file. Existing [%s] and new [%s]",
									fileMetaMap.get(file), metaData));
						}
					} else {
						LOGGER.warn(String.format(
							"Meta class %s is not parsed.", metaBox
								.getClass().getName()));
					}
				}
				
				file.close();
			} catch (Exception e) {
				LOGGER.error(String.format(
					"Failed to parse file [%s], so skipping", file), e);
			}
		}
		
		return fileMetaMap;
	}
	
	
	private static MetaData getAppleMetaData(final AppleItemListBox appleListBox, final File file) {
		MetaData metaData = new MetaData(appleListBox, file);
		if (metaData.hasNotHandled()) {
			LOGGER.warn(String.format(
				"Created MetaData (with not handled): [%s]",
				metaData.toString()));
		} else {
			LOGGER.info(String.format("Created MetaData: [%s]",
				metaData.toString()));
		}
		return metaData;
		
	}
	
}
