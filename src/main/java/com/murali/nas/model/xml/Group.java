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
@XmlRootElement(name = "group")
public class Group {
	
	private String name;
	
	private List<Condition> conditions;
	
	
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlElement(name = "name")
	public final String getName() {
		return name;
	}
	
	
	public final void setName(final String name) {
		this.name = name;
	}
	
	
	@XmlElement(name = "condition")
	public final List<Condition> getConditions() {
		return conditions;
	}
	
	
	public final void setConditions(final List<Condition> conditions) {
		this.conditions = conditions;
	}
	
}
