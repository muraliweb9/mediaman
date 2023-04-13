package com.murali.nas.filter;

import org.apache.commons.lang.StringUtils;

import com.murali.nas.model.xml.itunes.Playlist;


/**
 * 
 * @author Murali Karunanithy
 * 
 */
public class NamePlaylistOperation implements ToStringFunction<Playlist> {
	
	/**
	 * 
	 * @author Murali Karunanithy
	 * 
	 */
	public static enum Operation {
		REPLACE_WITH
	};
	
	
	private Operation operation = Operation.REPLACE_WITH;
	private String criteriaKey = null;
	private String criteriaValue = null;
	
	
	public NamePlaylistOperation(final String criteriaKey, final String criteriaValue) {
		this.criteriaKey = criteriaKey != null ? criteriaKey : "";
		this.criteriaValue = criteriaValue != null ? criteriaValue : "";
		this.operation = Operation.REPLACE_WITH;
	}
	
	
	public NamePlaylistOperation(final Operation operation, final String criteriaKey, final String criteriaValue) {
		this(criteriaKey, criteriaValue);
		this.operation = operation;
	}
	
	
	@Override
	public final String applyAsString(final Playlist playlist) {
		String rename = "";
		if (playlist != null && StringUtils.isNotBlank(playlist.getName())) {
			String playlistName = playlist.getName();
			if (this.operation != null) {
				// CHECKSTYLE:OFF (To prevent having to add a default: as its an enum)
				switch (this.operation) {
				case REPLACE_WITH:
					rename = playlistName.replace(criteriaKey, criteriaValue);
					break;
				}
			}
			// CHECKSTYLE:ON
		}
		if (rename == null) {
			rename = "";
		}
		return rename.trim();
	}
	
}
