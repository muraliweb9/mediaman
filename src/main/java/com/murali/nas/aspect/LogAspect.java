package com.murali.nas.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


/**
 * A logging aspect.
 * 
 * @author Murali Karunanithy
 * 
 */
@Component
@Aspect
public class LogAspect {
	
	@Before("execution(* com.murali.nas.services.CatalogServiceImpl.catalog(..))")
	public final void logBefore(final JoinPoint joinPoint) {
		System.out.println("Test Log aspect");
	}
	
}
