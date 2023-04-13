package com.murali.nas.model.xml;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;


public class BooleanAdapterTest {
	
	private BooleanAdapter boolAdapter = new BooleanAdapter();
	
	
	@Test
	public void testNull() throws Exception {
		
		assertNull(boolAdapter.marshal(null));
		assertNull(boolAdapter.unmarshal(null));
	}
	
	
	@Test
	public void testTrue() throws Exception {
		
		Boolean input = Boolean.TRUE;
		
		Boolean marshal = boolAdapter.marshal(input);
		
		assertSame(input, marshal);
		
		Boolean unmarshal = boolAdapter.unmarshal(marshal);
		assertSame(input, unmarshal);
		
	}
	
	
	@Test
	public void testFalse() throws Exception {
		
		Boolean input = Boolean.FALSE;
		
		Boolean marshal = boolAdapter.marshal(input);
		
		assertSame(input, marshal);
		
		Boolean unmarshal = boolAdapter.unmarshal(marshal);
		assertSame(input, unmarshal);
		
	}
}
