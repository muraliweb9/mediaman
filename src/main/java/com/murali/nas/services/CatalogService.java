package com.murali.nas.services;

import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

import com.murali.nas.MediaManagerException;


/**
 * 
 * A media catalog service.
 * 
 * @author Murali Karunanithy
 * 
 */
public interface CatalogService {
	
	void catalog(final String iTunesLibFile) throws IOException, SAXException, MediaManagerException, InterruptedException;
	
	
	void append(final String groupBy, final List<String> files) throws IOException;
	
}
