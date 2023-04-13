package com.murali.nas.filter;

import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;

import com.murali.nas.model.xml.itunes.Playlist;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class NamePlaylistFilter implements Predicate<Playlist> {
	
	/**
	 * 
	 * @author Murali Karunanithy
	 * 
	 */
	public static enum Match {
		BEGINS_WITH, CONTAINS, ENDS_WITH, EQUALS, REGEX
	};
	
	
	private String criteriaString = null;
	private Match match = Match.EQUALS;
	
	
	public NamePlaylistFilter(final String criteriaString, final Match match) {
		this.criteriaString = criteriaString;
		this.match = match;
	}
	
	
	@Override
	public final boolean test(final Playlist playlist) {
		Boolean accept = Boolean.FALSE;
		if (playlist != null && StringUtils.isNotBlank(playlist.getName())) {
			String playlistName = playlist.getName();
			if (this.match != null) {
				// CHECKSTYLE:OFF (To prevent having to add a default: as its an enum)
				switch (this.match) {
				case BEGINS_WITH:
					accept = playlistName.indexOf(criteriaString) == 0;
					break;
				case CONTAINS:
					accept = playlistName.contains(criteriaString);
					break;
				case ENDS_WITH:
					accept = playlistName.endsWith(criteriaString);
					break;
				case EQUALS:
					accept = playlistName.equals(criteriaString);
					break;
				case REGEX:
					accept = playlistName.matches(criteriaString);
					break;
				}
			}
			// CHECKSTYLE:ON
		}
		return accept;
	}
	
}
