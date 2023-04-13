package com.murali.nas.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.murali.nas.MediaManagerException;
import com.murali.nas.model.xml.Condition;
import com.murali.nas.model.xml.Group;
import com.murali.nas.model.xml.Grouping;


public class ParseTest {
	
	@Test
	public void testParsingConfigFile() throws IOException, JAXBException, MediaManagerException {
		
		GroupingHolder groupingHolder = new GroupingHolder(new File("src/main/config/properties/grouping-config.xml"));
		
		Grouping grouping = groupingHolder.getGrouping();
		
		assertEquals("my-grouping", grouping.getName());
		assertEquals(2, grouping.getGroups().size());
		
		Boolean conditionChecked_1 = Boolean.FALSE;
		Boolean conditionChecked_2 = Boolean.FALSE;
		for (Group group : grouping.getGroups()) {
			if ("my-group-1".equals(group.getName())) {
				for (Condition condition : group.getConditions()) {
					if ("my-tag-1".equals(condition.getTag())) {
						assertEquals("my-value-1", condition.getValue());
						assertTrue(condition.isInclude());
						conditionChecked_1 = Boolean.TRUE;
					}
				}
				
			}
			if ("my-group-2".equals(group.getName())) {
				for (Condition condition : group.getConditions()) {
					if ("my-tag-2".equals(condition.getTag())) {
						assertEquals("my-value-2", condition.getValue());
						assertFalse(condition.isInclude());
						conditionChecked_2 = Boolean.TRUE;
					}
				}
				
			}
		}
		assertTrue(String.format("Not all condiitons checked [%s, %s]", conditionChecked_1, conditionChecked_2),
			conditionChecked_1 && conditionChecked_2);
	}
	
}
