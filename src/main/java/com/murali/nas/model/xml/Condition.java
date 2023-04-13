package com.murali.nas.model.xml;

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
public class Condition {
	
	private String tag;
	
	private String value;
	private Boolean include;
	
	
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlElement(name = "tag")
	public final String getTag() {
		return tag;
	}
	
	
	public final void setTag(final String tag) {
		this.tag = tag;
	}
	
	
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlElement(name = "value")
	public final String getValue() {
		return value;
	}
	
	
	public final void setValue(final String value) {
		this.value = value;
	}
	
	
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	@XmlElement(name = "include")
	public final Boolean isInclude() {
		return include;
	}
	
	
	public final void setInclude(final Boolean include) {
		this.include = include;
	}
	
}
