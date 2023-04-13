package com.murali.nas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murali.nas.MediaManagerException;
import com.murali.nas.model.xml.Grouping;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class GroupingHolder {
	
	private static final Logger LOGGER = LoggerFactory
		.getLogger(GroupingHolder.class);
	
	private Grouping grouping = null;
	
	
	public GroupingHolder(final String groupingConfigFile) throws IOException, MediaManagerException {
		this(new FileInputStream(groupingConfigFile));
	}
	
	
	public GroupingHolder(final File groupingConfigFile) throws IOException, MediaManagerException {
		this(new FileInputStream(groupingConfigFile));
	}
	
	
	public GroupingHolder(final InputStream input) throws IOException, MediaManagerException {
		
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Grouping.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			grouping = (Grouping) unmarshaller.unmarshal(input);
		} catch (JAXBException e) {
			throw new MediaManagerException(String.format("Unable to parse grouping config file", e));
		}
	}
	
	
	public final Grouping getGrouping() {
		return this.grouping;
	}
}
