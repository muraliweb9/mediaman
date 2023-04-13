package com.murali.nas;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.murali.nas.services.CatalogService;

public class GeneralTest {
	

	@Test
	public void springTest() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"media-man-spring.xml");
		
		assertNotNull(context.getBean(CatalogService.class));

	}


}
