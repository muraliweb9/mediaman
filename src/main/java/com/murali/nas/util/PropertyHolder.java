package com.murali.nas.util;

import static com.murali.nas.util.perf.PerfMonitor.startPerf;
import static com.murali.nas.util.perf.PerfMonitor.stopPerf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murali.nas.filter.NamePlaylistFilter.Match;
import com.murali.nas.filter.NamePlaylistOperation.Operation;
import com.murali.nas.model.MetaDataType;


/**
 * 
 * This is a singleton class that holds all the properties. This class must be initialised with a property file before
 * use. Typically this should be the first step of the application
 * 
 * @author Murali Karunanithy
 * 
 */
public final class PropertyHolder {
	
	private static final Logger LOGGER = LoggerFactory
		.getLogger(PropertyHolder.class);
	
	private static final String BASE_DIR_KEY = "base.dir";
	private static final String BASE_APP_KEY = "base.app.dir";
	private static final String INPUT_APP_KEY = "input.app.dir";
	private static final String ABS_INPUT_APP_KEY = "abs.input.app.dir";
	private static final String OUTPUT_APP_KEY = "output.app.dir";
	private static final String ABS_OUTPUT_APP_KEY = "abs.output.app.dir";
	
	private static final String WORK_APP_KEY = "work.app.dir";
	private static final String ABS_WORK_APP_KEY = "abs.work.app.dir";
	private static final String WORK_DIR_EXT_KEY = "work.dir.ext";
	private static final String ALLOWED_MEDIA_KEY = "allowed.media.ext";
	private static final String MEDIA_GROUP_TAG_KEY = "media.group.tag";
	
	private static final String PERF_LOG_THRESHOLD_KEY = "perf.logging.threshold";
	
	private static final String ITUNES_PLAYLIST_MATCH_KEY = "media.playlist.group.name";
	private static final String ITUNES_PLAYLIST_MATCH_CRIETRIA_KEY = "media.playlist.group.name.condition";
	private static final String ITUNES_PLAYLIST_SMART_MATCH_KEY = "media.playlist.group.smart";
	private static final String DO_COMPILATIONS_KEY = "media.catalog.do.compilations";
	
	private static final String ITUNES_PLAYLIST_RENAME_CRIETRIA_KEY = "media.playlist.group.rename.key";
	private static final String ITUNES_PLAYLIST_RENAME_CRIETRIA_VALUE = "media.playlist.group.rename.value";
	private static final String ITUNES_PLAYLIST_RENAME_OPERATION = "media.playlist.group.rename.operation";
	
	private static Properties properties;
	
	private static String baseAppDir;
	private static String inputDir;
	private static String outputDir;
	private static String workDir;
	private static List<String> allowedExt;
	private static MetaDataType mediaGroupTag;
	private static String workDirExt;
	
	// Playlist match criteria
	private static String iTunesPlaylistMatchKey = "";
	private static Match iTunesPlaylistMatchCriteriaKey;
	
	// Playlist operation ex. rename operation
	private static String iTunesPlaylistRenameKey = "";
	private static String iTunesPlaylistRenameVal = "";
	private static Operation iTunesPlaylistOperation;
	
	private static Boolean iTunesPlaylistUseSmart;
	private static Boolean isDoCompilations;
	
	
	private PropertyHolder() {
		
	}
	
	
	public static void initialise(final String configFile) throws IOException {
		initialise(new FileInputStream(configFile));
	}
	
	
	public static void initialise(final File configFile) throws IOException {
		initialise(new FileInputStream(configFile));
	}
	
	
	public static void initialise(final InputStream input) throws IOException {
		properties = new Properties();
		String tag = startPerf();
		properties.load(input);
		initialise();
		stopPerf(tag);
	}
	
	
	// CHECKSTYLE:OFF (To bypass NPath complexity)
	private static void initialise() {
		String baseDir = properties.getProperty(BASE_DIR_KEY);
		String appDir = properties.getProperty(BASE_APP_KEY);
		
		baseAppDir = baseDir + File.separator + appDir;
		
		if (StringUtils.isNotEmpty(properties.getProperty(ABS_INPUT_APP_KEY))) {
			inputDir = properties.getProperty(ABS_INPUT_APP_KEY);
			LOGGER.info(String.format(
				"An absolute input directory [%s] specified", inputDir));
		} else {
			inputDir = baseDir + File.separator + appDir + File.separator
				+ properties.getProperty(INPUT_APP_KEY);
		}
		
		if (StringUtils.isNotEmpty(properties.getProperty(ABS_OUTPUT_APP_KEY))) {
			outputDir = properties.getProperty(ABS_OUTPUT_APP_KEY);
			LOGGER.info(String.format(
				"An absolute output directory [%s] specified", outputDir));
		} else {
			outputDir = baseDir + File.separator + appDir + File.separator
				+ properties.getProperty(OUTPUT_APP_KEY);
		}
		
		if (StringUtils.isNotEmpty(properties.getProperty(ABS_WORK_APP_KEY))) {
			workDir = properties.getProperty(ABS_WORK_APP_KEY);
			LOGGER.info(String.format(
				"A absolute work directory extension of [%s] specified", workDir));
		} else {
			workDir = baseDir + File.separator + appDir + File.separator
				+ properties.getProperty(WORK_APP_KEY);
		}
		
		if (StringUtils.isNotEmpty(properties.getProperty(WORK_DIR_EXT_KEY))) {
			workDirExt = properties.getProperty(WORK_DIR_EXT_KEY);
			LOGGER.info(String.format(
				"A work directory extension of [%s] specified", workDirExt));
		} else {
			workDirExt = "txt";
		}
		
		// Playlist Match criteria
		if (StringUtils.isNotEmpty(properties.getProperty(ITUNES_PLAYLIST_MATCH_KEY))) {
			iTunesPlaylistMatchKey = properties.getProperty(ITUNES_PLAYLIST_MATCH_KEY);
			LOGGER.info(String.format(
				"ITunes playlist match key of [%s] specified", iTunesPlaylistMatchKey));
		}
		
		if (StringUtils.isNotEmpty(properties.getProperty(ITUNES_PLAYLIST_MATCH_CRIETRIA_KEY))) {
			iTunesPlaylistMatchCriteriaKey = Match.valueOf(properties.getProperty(ITUNES_PLAYLIST_MATCH_CRIETRIA_KEY));
			LOGGER.info(String.format(
				"ITunes playlist match criteria of [%s] specified", iTunesPlaylistMatchCriteriaKey));
		}
		
		// Playlist rename criteria
		if (StringUtils.isNotEmpty(properties.getProperty(ITUNES_PLAYLIST_RENAME_CRIETRIA_KEY))) {
			iTunesPlaylistRenameKey = properties.getProperty(ITUNES_PLAYLIST_RENAME_CRIETRIA_KEY);
			LOGGER.info(String.format(
				"ITunes playlist rename key of [%s] specified", iTunesPlaylistRenameKey));
		}
		
		if (StringUtils.isNotEmpty(properties.getProperty(ITUNES_PLAYLIST_RENAME_CRIETRIA_VALUE))) {
			iTunesPlaylistRenameVal = properties.getProperty(ITUNES_PLAYLIST_RENAME_CRIETRIA_VALUE);
			LOGGER.info(String.format(
				"ITunes playlist rename key of [%s] specified", iTunesPlaylistRenameVal));
		}
		
		if (StringUtils.isNotEmpty(properties.getProperty(ITUNES_PLAYLIST_RENAME_OPERATION))) {
			iTunesPlaylistOperation = Operation.valueOf(properties.getProperty(ITUNES_PLAYLIST_RENAME_OPERATION));
			LOGGER.info(String.format(
				"ITunes playlist rename operation of [%s] specified", iTunesPlaylistOperation));
		}
		
		if (StringUtils.isNotEmpty(properties.getProperty(ITUNES_PLAYLIST_SMART_MATCH_KEY))) {
			iTunesPlaylistUseSmart = Boolean.valueOf(properties.getProperty(ITUNES_PLAYLIST_SMART_MATCH_KEY));
			LOGGER.info(String.format(
				"ITunes playlist use smartlist of [%s] specified", iTunesPlaylistUseSmart));
			
		}
		
		if (StringUtils.isNotEmpty(properties.getProperty(DO_COMPILATIONS_KEY))) {
			isDoCompilations = Boolean.valueOf(properties.getProperty(DO_COMPILATIONS_KEY));
			LOGGER.info(String.format(
				"Do compilations set to [%s]", String.valueOf(isDoCompilations)));
			
		}
		
		allowedExt = CollectionUtil.parseAllowedExtensions(properties
			.getProperty(ALLOWED_MEDIA_KEY));
		mediaGroupTag = MetaDataType.valueOf(properties
			.getProperty(MEDIA_GROUP_TAG_KEY));
		
		LOGGER.info(String.format("Input directory [%s]", inputDir));
		LOGGER.info(String.format("Output directory [%s]", outputDir));
		LOGGER.info(String.format("Work directory [%s]", workDir));
		LOGGER.info(String.format("Work directory extension [%s]", workDirExt));
		LOGGER.info(String.format("Allowed extenstions [%s]", allowedExt));
		LOGGER.info(String.format("Media sort tag [%s]", mediaGroupTag));
	}
	
	
	// CHECKSTYLE:ON
	
	public static String getInputDir() {
		return inputDir;
	}
	
	
	public static String getOutputDir() {
		return outputDir;
	}
	
	
	public static String getOutputDirCompilations() {
		return outputDir + File.separator + "Compilations";
	}
	
	
	public static String getOutputDirCompilations(final String groupBy) {
		return getOutputDirCompilations() + File.separator + groupBy + ".mp4";
		
	}
	
	
	public static File getOutputDirFile() {
		return new File(outputDir);
	}
	
	
	public static Path getOutputDirPath() {
		return Paths.get(outputDir);
	}
	
	
	public static Path getOutputDirPath(final String folder) {
		String localFolder = folder;
		if (folder == null || "null".equals(folder.trim())
			|| "".equals(folder.trim())) {
			localFolder = "UNKNOWN";
		}
		return Paths.get(outputDir + File.separator + localFolder);
	}
	
	
	public static String getWorkDir() {
		return workDir;
	}
	
	
	public static File getWorkDirFile() {
		return new File(workDir);
	}
	
	
	public static String getWorkDirFile(final String groupBy) {
		return workDir + File.separator + groupBy + "." + workDirExt;
	}
	
	
	public static String getWorkDirExt() {
		return workDirExt;
	}
	
	
	public static String getWorkDirScriptFile() {
		return baseAppDir + File.separator + "create-comp.sh";
	}
	
	
	public static List<String> getAllowedExtensions() {
		return allowedExt;
	}
	
	
	public static String getProperty(final String key) {
		return properties.getProperty(key);
	}
	
	
	public static MetaDataType getMediaGroupTag() {
		return mediaGroupTag;
	}
	
	
	public static Path getOutputPath(final String groupingKey, final String name, final File inputFile) {
		String localGroupingKey = groupingKey;
		String localName = name;
		if (groupingKey == null || "null".equals(groupingKey.trim())
			|| "".equals(groupingKey.trim())) {
			localGroupingKey = "UNKNOWN";
		}
		if (name == null || "null".equals(name.trim())
			|| "".equals(name.trim())) {
			localName = "UNKNOWN";
		}
		Integer lastIndex = inputFile.getAbsolutePath().lastIndexOf(".");
		String extension = inputFile.getAbsolutePath().substring(lastIndex + 1);
		return Paths.get(outputDir + File.separator + localGroupingKey
			+ File.separator + localName + "." + extension);
	}
	
	
	public static Path getInputPath(final String name) {
		return Paths.get(inputDir + File.separator + name);
	}
	
	
	// CHECKSTYLE:OFF (the below methods can't be final as used by mock tests
	public static String getITunesPlaylistMatchKey() {
		return iTunesPlaylistMatchKey;
	}
	
	
	public static Match getITunesPlaylistMatchCriteriaKey() {
		return iTunesPlaylistMatchCriteriaKey;
	}
	
	
	public static String getITunesPlaylistRenameKey() {
		return iTunesPlaylistRenameKey;
	}
	
	
	public static String getITunesPlaylistRenameVal() {
		return iTunesPlaylistRenameVal;
	}
	
	
	public static Operation getITunesPlaylistOperation() {
		return iTunesPlaylistOperation;
	}
	
	
	public static Boolean getITunesPlaylistUseSmart() {
		return iTunesPlaylistUseSmart;
	}
	
	
	public static Boolean isDoCompilations() {
		return isDoCompilations;
	}
	
	
	// CHECKSTYLE:ON
	
	public static Long getPerfLogThreshold() {
		return Long.valueOf(properties.getProperty(PERF_LOG_THRESHOLD_KEY) == null ? "0" : properties
			.getProperty(PERF_LOG_THRESHOLD_KEY));
	}
	
}
