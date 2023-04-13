package com.murali.nas.model;

/**
 * 
 * @author Murali Karunanithy
 * 
 */
public final class KeyValueMeta {
	
	private final String key;
	
	private final String value;
	
	
	public KeyValueMeta(final String key, final String value) {
		this.key = key;
		this.value = value;
	}
	
	
	public String getKey() {
		return this.key;
		
	}
	
	
	public String getValue() {
		return this.value;
		
	}
	
	
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder = strBuilder.append("Key[").append(key).append("]").append("Value[").append(value).append("]");
		return strBuilder.toString();
	}
	
}
