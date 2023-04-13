package com.murali.nas.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtil.class)
@PowerMockIgnore("javax.management.*")
public class UtilEasyMockTest {
	
	private static final Logger LOGGER = LoggerFactory
		.getLogger(UtilEasyMockTest.class);
	
	
	@Before
	public void setUp() throws Exception {
	}
	
	
	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testCreateSymLink() throws Exception {
		
		/*
		TestObject obj = createMockBuilder(TestObject.class).addMockedMethod("greater").createStrictMock();
		obj.greater(11);
		expectLastCall().times(0, 1);
		obj.greater(13);
		expectLastCall().times(1);

		replay(obj);
		//obj.doSome(11);
		obj.doSome(13);
		verify(obj);
		*/
		
		// FileUtil fileUtil =
		// createMockBuilder(FileUtil.class).addMockedMethod("createSymbolicLink").createStrictMock();
		
	}
	
}


/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
class TestObject {
	
	private static final Logger LOGGER = LoggerFactory
		.getLogger(TestObject.class);
	
	
	public int doSome(int i) {
		LOGGER.info(String.format("doSome [%s]", i));
		if (i > 10) {
			LOGGER.info(String.format("[%s] is greater than 10", i));
			greater(i);
		}
		return i;
	}
	
	
	public void greater(int i) {
		LOGGER.info(String.format("Call to greater with [%s]", i));
		
	}
}
