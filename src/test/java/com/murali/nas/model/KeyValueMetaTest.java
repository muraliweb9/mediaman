package com.murali.nas.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(KeyValueMeta.class)
public class KeyValueMetaTest {
	
	@Test
	public void testMockKeyValueMeta() {
		KeyValueMeta meta = mock(KeyValueMeta.class);
		when(meta.getKey()).thenReturn("mockKey");
		when(meta.getValue()).thenReturn("mockValue");
		
		assertEquals("mockKey", meta.getKey());
		assertEquals("mockValue", meta.getValue());
	}
	
	
	@Test
	public void tetsKeyValueMeta() {
		KeyValueMeta keyValue1 = new KeyValueMeta(null, null);
		KeyValueMeta keyValue2 = new KeyValueMeta(null, "val2");
		KeyValueMeta keyValue3 = new KeyValueMeta("key3", null);
		KeyValueMeta keyValue4 = new KeyValueMeta("key4", "val4");
		
		assertNull(keyValue1.getKey());
		assertNull(keyValue1.getValue());
		
		assertNull(keyValue2.getKey());
		assertEquals("val2", keyValue2.getValue());
		
		assertEquals("key3", keyValue3.getKey());
		assertNull(keyValue3.getValue());
		
		assertEquals("key4", keyValue4.getKey());
		assertEquals("val4", keyValue4.getValue());
		
		assertEquals("Key[key4]Value[val4]", keyValue4.toString());
		
	}
	
}
