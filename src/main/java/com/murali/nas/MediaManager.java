package com.murali.nas;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.SAXException;

import com.murali.nas.services.CatalogService;
import com.murali.nas.util.PropertyHolder;


/**
 * 
 * @author Murali Karunanithy
 * 
 * 
 */
public class MediaManager {
	
	private static final Logger LOGGER = LoggerFactory
		.getLogger(MediaManager.class);
	
	
	public static void main(final String[] args) throws IOException,
		SAXException, MediaManagerException, BeansException, InterruptedException {
		String configFile = null;
		String iTunesLibFile = null;
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			LOGGER.info(String.format("Program arg %d : [%s]", i, arg));
		}
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equals("-c")) {
				configFile = args[++i];
				LOGGER.info(String.format("Config file [%s]", configFile));
			}
			
			if (arg.equals("-i")) {
				iTunesLibFile = args[++i];
				LOGGER.info(String.format("ITunes library file [%s]",
					iTunesLibFile));
			}
			
		}
		
		if (configFile == null) {
			LOGGER.error(String.format("No config file specified"));
			LOGGER.error(String
				.format("Usage: java com.murali.nas.MediaManager "
					+ "-c config/config.properties -i config/iTunes Music Library.xml"));
			System.exit(1);
		}
		
		if (iTunesLibFile == null) {
			
			LOGGER.info(String
				.format("iTunes Music Library not specified using default grouping"));
		}
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
			"media-man-spring.xml");
		
		PropertyHolder.initialise(configFile);
		
		context.getBean(CatalogService.class).catalog(iTunesLibFile);
		
		context.close();
		
	}
	
}
