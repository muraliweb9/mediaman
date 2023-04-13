package com.murali.nas.services;

import static com.murali.nas.model.MetaDataType.PLAYLIST;
import static com.murali.nas.util.FileUtil.createSymLink;
import static com.murali.nas.util.perf.PerfMonitor.startPerf;
import static com.murali.nas.util.perf.PerfMonitor.stopPerf;
import static java.lang.Boolean.TRUE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.murali.nas.MediaManagerException;
import com.murali.nas.filter.NamePlaylistFilter.Match;
import com.murali.nas.filter.NamePlaylistOperation.Operation;
import com.murali.nas.model.MetaData;
import com.murali.nas.model.MetaDataType;
import com.murali.nas.model.xml.itunes.Playlist;
import com.murali.nas.util.ITunesLibraryHolder;
import com.murali.nas.util.MetaDataParser;
import com.murali.nas.util.PropertyHolder;


/**
 *
 * A media catalog service implementation.
 *
 * @author Murali Karunanithy
 *
 */
@Component
public final class CatalogServiceImpl implements CatalogService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogServiceImpl.class);
	
	
	public static void main(final String[] args) throws IOException {
		
		List<String> files = new ArrayList<String>();
		files.add("/home/karumur/media_prep/input/In Outhose 3-1.mp4");
		files.add("/home/karumur/media_prep/input/In Outhouse 1-2.m4v");
		files.add("/home/karumur/media_prep/input/In Outhouse 1-2.m4v");
		
		new CatalogServiceImpl().append("", files);
		
	}
	
	
	@Override
	public void catalog(final String iTunesLibFile) throws IOException, SAXException, MediaManagerException,
		InterruptedException {
		
		Optional<ITunesLibraryHolder> holder = Optional
			.ofNullable(iTunesLibFile != null ? new ITunesLibraryHolder(iTunesLibFile) : null);
		
		String tag = startPerf();
		
		List<Playlist> playlists = null;
		
		if (holder.isPresent()) {
			
			String playlistMatchKey = PropertyHolder.getITunesPlaylistMatchKey();
			Match playlistMatchCriteria = PropertyHolder.getITunesPlaylistMatchCriteriaKey();
			Boolean smartplaylist = PropertyHolder.getITunesPlaylistUseSmart();
			String renameKey = PropertyHolder.getITunesPlaylistRenameKey();
			String renameVal = PropertyHolder.getITunesPlaylistRenameVal();
			Operation operation = PropertyHolder.getITunesPlaylistOperation();
			
			playlists = holder.get().getPlaylists(playlistMatchKey,
				playlistMatchCriteria, smartplaylist, operation, renameKey, renameVal);
		}
		
		MetaDataType metaTypeBeingSortedBy = holder.isPresent() ? PLAYLIST : PropertyHolder
			.getMediaGroupTag();
		String inputfolder = PropertyHolder.getInputDir();
		
		List<String> allowedExtensions = PropertyHolder.getAllowedExtensions();
		
		LOGGER
			.info(String.format("Catalog started for folder [%s] with extensions %s", inputfolder, allowedExtensions));
		Map<File, MetaData> metaMap = new MetaDataParser(inputfolder, allowedExtensions).parseMetaData();
		
		LOGGER.info(String.format("Total files [%d] for linking", metaMap.size()));
		
		// Count the items with missing titles
		long countMissingTitles =
			metaMap
				.values()
				.stream()
				.filter(m -> m.isTitleBlank())
				.count();
		
		LOGGER.info(String.format("Total files [%d] with [%d] missing titles", metaMap.size(), countMissingTitles));
		
		// Log missing titles
		metaMap
			.values()
			.stream()
			.filter(m -> m.isTitleBlank())
			.forEach(
				meta -> LOGGER.error(String.format("Missing title for file [%s]", meta.getFile().getAbsolutePath())));
		
		LOGGER.info(String.format("Cleaning output directory [%s]", PropertyHolder.getOutputDir()));
		FileUtils.cleanDirectory(PropertyHolder.getOutputDirFile());
		
		LOGGER.info(String.format("Cleaning work list directory [%s]", PropertyHolder.getWorkDir()));
		FileUtils.cleanDirectory(PropertyHolder.getWorkDirFile());
		
		if (!holder.isPresent()) {
			createLink(null, metaMap);
		}
		else {
			LOGGER.info(String.format("Playlists being DONE are:"));
			
			playlists
				.stream()
				.forEachOrdered(p -> LOGGER.info(String.format("*** %s ***", p.getName())));
			
			for (Playlist p : playlists) {
				
				String reduceTag = startPerf(String.format("MetaMap::reduce:%s", p.getName()));
				
				Map<File, MetaData> reducedMetaMap = holder.get().filterMetaData(p.getName(), metaMap);
				
				stopPerf(reduceTag);
				
				LOGGER.info(String.format("Creating [%d] links for playlist:[%s]", reducedMetaMap.size(), p.getName()));
				Thread t = new Thread(() -> createLink(p, reducedMetaMap));
				t.start();
				t.join();
			}
		}
		if (PropertyHolder.isDoCompilations()) {
			createCompilations(metaTypeBeingSortedBy, playlists, metaMap);
		}
		stopPerf(tag);
		
	}
	
	
	@Override
	public void append(final String groupBy, final List<String> files) throws IOException {
		List<Movie> inMovies = filesToMovies(files);
		
		List<Track> videoTracks = new LinkedList<>();
		List<Track> audioTracks = new LinkedList<>();
		
		inMovies
			.stream()
			.forEach(
				m -> audioTracks.addAll(
					m.getTracks()
						.stream()
						.filter(t -> t.getHandler().equals("soun"))
						.collect(Collectors.toList())));
		
		inMovies
			.stream()
			.forEach(
				m -> videoTracks.addAll(
					m.getTracks()
						.stream()
						.filter(t -> t.getHandler().equals("vide"))
						.collect(Collectors.toList())));
		
		Movie output = new Movie();
		
		if (audioTracks.size() > 0) {
			output.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
		}
		if (videoTracks.size() > 0) {
			output.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
		}
		
		Container out = new DefaultMp4Builder().build(output);
		
		String outputFile = PropertyHolder.getOutputDirCompilations(groupBy);
		
		FileChannel fc = new RandomAccessFile(String.format(outputFile), "rw").getChannel();
		out.writeContainer(fc);
		fc.close();
		
	}
	
	
	private void createLink(final Playlist playlist, Map<File, MetaData> metaMap) {
		
		LOGGER.info(String.format("Processing playlist [%s]", playlist));
		String tag = startPerf(playlist.getName() + "(" + playlist.getTracks().size() + ")");
		
		Path outputSortDir = PropertyHolder.getOutputDirPath(playlist.getTransformedName());
		LOGGER.info(String.format("Creating group directory [%s]", outputSortDir));
		try {
			Files.createDirectories(outputSortDir);
		} catch (IOException ioe) {
			LOGGER.error(String.format("Error creating directory: [%s]", outputSortDir));
			return;
		}
		
		Map<Path, MetaData> createdLinks = new HashMap<>();
		
		for (Map.Entry<File, MetaData> meta : metaMap.entrySet()) {
			
			if (meta.getValue().hasNotHandled()) {
				LOGGER.warn(String.format("File: (with not handled): [%s],  Meta [%s]",
					meta.getKey().getAbsolutePath(),
					meta.getValue()));
			}
			else {
				LOGGER.info(String.format("File: [%s],  Meta [%s]", meta.getKey().getAbsolutePath(), meta.getValue()));
			}
			
			createSymLink(meta.getKey(), playlist.getTransformedName(), meta.getValue(), createdLinks);
		}
		stopPerf(tag);
	}
	
	
	private void createCompilations(final MetaDataType metaTypeBeingSortedBy, final List<Playlist> playlists,
		final Map<File, MetaData> metaMap)
		throws MediaManagerException, IOException {
		
		String tag = startPerf();
		
		Boolean playlistsUsed = !CollectionUtils.isEmpty(playlists);
		Map<String, List<MetaData>> sortedByMetaType = null;
		if (playlistsUsed) {
			LOGGER.info(String.format("Compilations using playlists of size [%d]", playlists.size()));
			sortedByMetaType = MetaData.groupBy(playlists, metaMap);
		}
		else {
			LOGGER.info(String.format("Compilations NOT using playlists"));
			sortedByMetaType = MetaData.groupBy(metaTypeBeingSortedBy, metaMap);
		}
		
		Files.createDirectories(Paths.get(PropertyHolder.getOutputDirCompilations()));
		
		File workDirScript = new File(PropertyHolder.getWorkDirScriptFile());
		workDirScript.setExecutable(TRUE);
		Writer workDirScriptWriter = new BufferedWriter(new FileWriter(workDirScript));
		
		workDirScriptWriter.write(String.format("#!/bin/bash"));
		
		workDirScriptWriter.write(String.format(System.getProperty("line.separator")));
		workDirScriptWriter.write(String.format(System.getProperty("line.separator")));
		
		workDirScriptWriter.write(String.format("INPATH='%s'", PropertyHolder.getInputDir()));
		workDirScriptWriter.write(String.format(System.getProperty("line.separator")));
		
		workDirScriptWriter.write(String.format("OUTPATH='%s'", PropertyHolder.getOutputDirCompilations()));
		workDirScriptWriter.write(String.format(System.getProperty("line.separator")));
		
		workDirScriptWriter.write(String.format("WPATH='%s'", PropertyHolder.getWorkDir()));
		workDirScriptWriter.write(String.format(System.getProperty("line.separator")));
		
		workDirScriptWriter.write(String.format(System.getProperty("line.separator")));
		workDirScriptWriter.write(String.format(System.getProperty("line.separator")));
		
		for (String groupBy : sortedByMetaType.keySet()) {
			
			String compilationWorkFile = PropertyHolder.getWorkDirFile(groupBy);
			
			LOGGER.info(String.format("Creating compilation work file [%s]", compilationWorkFile));
			
			Writer output = new BufferedWriter(new FileWriter(compilationWorkFile));
			output.write(String.format("# List of files for [%s]", groupBy));
			output.write(String.format(System.getProperty("line.separator")));
			output.write(String.format(System.getProperty("line.separator")));
			
			output.write(String.format("# List of files for [%s]", groupBy));
			output.write(String.format(System.getProperty("line.separator")));
			
			workDirScriptWriter.write(String
				.format("ffmpeg -f concat -i \"$WPATH/%s." + PropertyHolder.getWorkDirExt()
					+ "\" -c copy \"$OUTPATH/%s.mp4\"", groupBy, groupBy));
			workDirScriptWriter.write(String.format(System.getProperty("line.separator")));
			
			try {
				List<String> filesInGroup = new ArrayList<>();
				for (MetaData meta : sortedByMetaType.get(groupBy)) {
					String fileInGroup = meta.getFile().getAbsolutePath();
					LOGGER.info(String.format("Group by [%s] for meta [%s]", groupBy, meta));
					output.write(String.format("file '%s'", fileInGroup));
					output.write(String.format(System.getProperty("line.separator")));
					filesInGroup.add(fileInGroup);
				}
				append(groupBy, filesInGroup);
			} catch (Exception e) {
				LOGGER.error(String.format("Unable to create compilations for (%s)"
					+ " due to exception (%s)", groupBy, e.getMessage()));
			} finally {
				output.close();
			}
		}
		workDirScriptWriter.close();
		new File(PropertyHolder.getWorkDirScriptFile()).setExecutable(TRUE);
		
		stopPerf(tag);
		
	}
	
	
	private List<Movie> filesToMovies(final List<String> files) {
		
		List<Movie> movies = new ArrayList<>();
		for (String file : files) {
			try {
				movies.add(MovieCreator.build(file));
			} catch (IOException ioe) {
				
				LOGGER.error(String.format(
					"Failed to create movie from file [%s], IOException , so skipping", file), ioe);
			} catch (Exception e) {
				LOGGER.error(String.format(
					"Failed to create movie from file [%s], general exception , so skipping", file), e);
			}
		}
		return movies;
	}
	
}
