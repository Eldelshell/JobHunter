/*
 * Copyright (C) 2014 Alejandro Ayuso
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jobhunter.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import jobhunter.utils.ApplicationState;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller to handle java.util.prefs.Preferences operations
 */
public enum PreferencesController {

	_INSTANCE;
	
	private static final Logger l = LoggerFactory.getLogger(PreferencesController.class);
	
	private static final String DEFAULT_PORTALS = "Xing, LinkedIn, Monster";
	
	public static final String PORTALS_PROPERTY = "portals";
	public static final String LAST_FILE_PATH_PROPERTY = "last-file-path";
	public static final String AUTOSAVE_PROPERTY = "autosave";
	public static final String AUTOUPDATE_PROPERTY = "autoupdate";
	
	public static PreferencesController instanceOf() {
		return _INSTANCE;
	}
	
	private final Preferences current;
	
	private PreferencesController() {
		this.current = ApplicationState.instanceOf().isDevelopment() 
			? Preferences.userRoot().node("jobhunter-dev") 
			: Preferences.userRoot().node("jobhunter");
	}

	public void init() {
		
		Boolean isFirstTime = Boolean.TRUE;
		
		try {
			isFirstTime = current.keys().length == 0;
		} catch (BackingStoreException e) {
			l.error("Error loading preferences file", e);
		}
		
		if(isFirstTime) {
			l.debug("First time we run. Create default configuration");
			generate();
		}
		
	}
	
	private void generate() {
		current.put(LAST_FILE_PATH_PROPERTY, "");
		current.put(PORTALS_PROPERTY, DEFAULT_PORTALS);
		current.putBoolean(AUTOSAVE_PROPERTY, false);
		current.putBoolean(AUTOUPDATE_PROPERTY, true);
	}
	
	public void setLastFilePath(final String value) {
		current.put(LAST_FILE_PATH_PROPERTY, value);
	}
	
	public Boolean isLastFilePathSet() {
		return !current.get(LAST_FILE_PATH_PROPERTY, "").isEmpty();
	}
	
	public String getLastFilePath() {
		return current.get(LAST_FILE_PATH_PROPERTY, null);
	}
	
	public void setPortals(final String value) {
		current.put(PORTALS_PROPERTY, value);
	}
	
	public String getPortals() {
		return current.get(PORTALS_PROPERTY, DEFAULT_PORTALS);
	}
	
	public Boolean isAutosave() {
		return current.getBoolean(AUTOSAVE_PROPERTY, false);
	}
	
	public void setAutosave(final Boolean bool){
		current.putBoolean(AUTOSAVE_PROPERTY, bool);
	}
	
	public Boolean isAutoupdate() {
		return current.getBoolean(AUTOUPDATE_PROPERTY, true);
	}
	
	public void setAutoupdate(final Boolean bool){
		current.putBoolean(AUTOUPDATE_PROPERTY, bool);
	}
	
	public List<String> getPortalsList() {
		final String portals = current.get(PORTALS_PROPERTY, DEFAULT_PORTALS);
		return Arrays.asList(portals.split(", "));
	}
	
	public void addNewPortal(final String portal){
		final String portals = current.get(PORTALS_PROPERTY, DEFAULT_PORTALS);
		if(!StringUtils.contains(portals, portal))
			setPortals(portals + ", " + portal);
	}
	
	public void save() {
		try {
			current.flush();
		} catch (BackingStoreException e) {
			l.error("Failed to save preferences", e);
		}
	}
	
}
