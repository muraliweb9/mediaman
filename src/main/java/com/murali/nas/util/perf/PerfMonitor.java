package com.murali.nas.util.perf;

import java.util.HashMap;
import java.util.Map;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

import com.murali.nas.util.PropertyHolder;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public final class PerfMonitor {
	
	private static final Integer LOC_OF_CALLING_METHOD = 3;
	
	private static Map<String, StopWatch> stopWatches = new HashMap<>();
	
	
	private PerfMonitor() {
	}
	
	public static String startPerf() {
		String tag = tag("");
		if (!stopWatches.containsKey(tag)) {
			stopWatches.put(tag, new Slf4JStopWatch(tag).setTimeThreshold(PropertyHolder.getPerfLogThreshold()));
		}
		else {
			stopWatches.get(tag).start();
		}
		return tag;
	}
	
	public static String startPerf(String info) {
		String tag = tag(info);
		if (!stopWatches.containsKey(tag)) {
			stopWatches.put(tag, new Slf4JStopWatch(tag).setTimeThreshold(PropertyHolder.getPerfLogThreshold()));
		}
		else {
			stopWatches.get(tag).start();
		}
		return tag;
	}
	
	
	
	
	public static void stopPerf(final String tag) {
		stopWatches.get(tag).stop();
	}
	
	
	private static String tag(String info) {
		
		StackTraceElement stackElem = Thread.currentThread().getStackTrace()[LOC_OF_CALLING_METHOD];
		
		String clazz = stackElem.getClassName();
		String method = stackElem.getMethodName();
		Integer line = stackElem.getLineNumber();
		
		StringBuilder strBuild = new StringBuilder();
		
		strBuild.append(clazz);
		strBuild.append(":");
		strBuild.append(method);
		strBuild.append(":");
		strBuild.append(line);
		strBuild.append(":");
		strBuild.append(info);
		return strBuild.toString();
	}
}
