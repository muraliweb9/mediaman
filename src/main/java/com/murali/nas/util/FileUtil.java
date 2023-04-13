package com.murali.nas.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murali.nas.MediaManager;
import com.murali.nas.model.MetaData;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public final class FileUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	
	
	private FileUtil() {
	}
	
	
	public static void createSymLink(final File inputFile,
		final String metaGroupKey,
		final MetaData metaData,
		final Map<Path, MetaData> createdLinks) {
		
		Path link = PropertyHolder.getOutputPath(metaGroupKey, metaData.getTitle(), inputFile);
		Path target = Paths.get(inputFile.toURI());
		
		if (!createdLinks.containsKey(link)) {
			
			try {
				Files.createSymbolicLink(link, target);
				createdLinks.put(link, metaData);
				LOGGER.info(String.format("Link: [%s] --> [%s]", link, target));
				
			} catch (IOException ioe) {
				LOGGER.error(String.format("Error creating link: [%s] --> [%s]", link, target));
			}
		}
		else {
			LOGGER.warn(String.format("Skipping creating link [%s] to [%s]. Link exists for [%s]", link, target,
				createdLinks.get(link)));
		}
	}
}
