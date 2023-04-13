package com.murali.nas.filter;

/**
 * 
 * @author Murali Karunanithy
 *
 * @param <T>
 *        the type of the input to the function
 */
@FunctionalInterface
public interface ToStringFunction<T> {
	
	String applyAsString(T t);
}
