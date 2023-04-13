package com.murali.nas.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murali.nas.model.xml.itunes.Array;
import com.murali.nas.model.xml.itunes.Dict;
import com.murali.nas.model.xml.itunes.False;
import com.murali.nas.model.xml.itunes.ITunesKey;
import com.murali.nas.model.xml.itunes.True;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class ITunesDictParser {
	
	private static final Logger LOGGER = LoggerFactory
		.getLogger(ITunesDictParser.class);
	
	private Dict dict;
	
	private Map<ITunesKey, Object> dictKeyValues = new HashMap<>();
	private Map<String, Object> dictNotHandledKeyValues = new HashMap<>();
	private Map<ITunesKey, Class<?>> dictValueTypes = new HashMap<>();
	
	
	public ITunesDictParser(final Dict dict) {
		this.dict = dict;
		List<Object> objs = dict.getDictOrArrayOrData();
		for (int i = 0; i < objs.size(); i++) {
			JAXBElement<?> keyItem = (JAXBElement<?>) objs.get(i);
			String keyValue = (String) keyItem.getValue();
			
			ITunesKey iTunesKey = ITunesKey.forValue(keyValue);
			i++;
			
			if (iTunesKey != null) {
				
				Object valueItem = objs.get(i);
				Object valueValue = null;
				Class<?> valueClass = null;
				if (valueItem instanceof JAXBElement) {
					JAXBElement<?> jaxbValueItem = (JAXBElement<?>) valueItem;
					valueValue = jaxbValueItem.getValue();
					valueClass = jaxbValueItem.getDeclaredType();
				}
				else if (valueItem instanceof True) {
					valueValue = Boolean.TRUE;
					valueClass = Boolean.class;
				}
				else if (valueItem instanceof False) {
					valueValue = Boolean.FALSE;
					valueClass = Boolean.class;
				}
				else if (valueItem instanceof Array) {
					List<Dict> dicts = ((Array) valueItem).getDict();
					
					List<ITunesDictParser> dict2Parser = new ArrayList<ITunesDictParser>();
					for (Dict dict2 : dicts) {
						dict2Parser.add(new ITunesDictParser(dict2));
					}
					valueValue = dict2Parser;
					valueClass = Array.class;
				}
				else {
					valueValue = valueItem;
					valueClass = Object.class;
				}
				
				if (iTunesKey != ITunesKey.NOT_HANDLED) {
					dictKeyValues.put(iTunesKey, valueValue);
				}
				else {
					dictNotHandledKeyValues.put(keyValue, valueValue);
				}
				dictValueTypes.put(iTunesKey, valueClass);
			}
			else {
				i = i + 2;
				LOGGER.error(String.format(
					"ITunes key [%s] is not handled, so not added to set",
					keyValue));
			}
			
		}
	}
	
	
	public final Dict getDict() {
		return this.dict;
	}
	
	
	public final Object getValue(final Object key) {
		if (dictKeyValues.isEmpty()) {
			return dictNotHandledKeyValues.get(key);
		}
		else {
			return dictKeyValues.get(key);
		}
	}
	
	
	public final Set<?> getKeys() {
		if (dictKeyValues.isEmpty()) {
			return dictNotHandledKeyValues.keySet();
		}
		else {
			return dictKeyValues.keySet();
		}
		
	}
	
}
