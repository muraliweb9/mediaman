package com.murali.nas.model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public final class BooleanAdapter extends XmlAdapter<Boolean, Boolean> {
	
	@Override
	public Boolean unmarshal(final Boolean v) throws Exception {
		if (v != null) {
			return Boolean.TRUE.equals(v);
		}
		return null;
		
	}
	
	
	@Override
	public Boolean marshal(final Boolean v) throws Exception {
		if (v != null) {
			return Boolean.TRUE.equals(v);
		}
		return null;
	}
	
}