package com.murali.nas.model.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
@XmlRootElement(name = "grouping")
public class Grouping {
	
	private String name;
	private List<Group> groups;
	
	
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlElement(name = "name")
	public final String getName() {
		return this.name;
	}
	
	
	public final void setName(final String name) {
		this.name = name;
	}
	
	
	@XmlElement(name = "group")
	public final List<Group> getGroups() {
		return groups;
	}
	
	
	public final void setGroups(final List<Group> groups) {
		this.groups = groups;
	}
	
}