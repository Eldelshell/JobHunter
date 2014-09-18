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
	
	private final Preferences current;
	
	private PreferencesController() {
		this.current = ApplicationState.isDevelopment() 
			? Preferences.userRoot().node("jobhunter-dev") 
			: Preferences.userRoot().node("jobhunter");
	}

	public static void init() {
		
		Boolean isFirstTime = Boolean.TRUE;
		
		try {
			isFirstTime = getCurrent().keys().length == 0;
		} catch (BackingStoreException e) {
			l.error("Error loading preferences file", e);
		}
		
		if(isFirstTime) {
			l.debug("First time we run. Create default configuration");
			generate();
		}
		
	}
	
	private static void generate() {
		getCurrent().put(LAST_FILE_PATH_PROPERTY, "");
		getCurrent().put(PORTALS_PROPERTY, DEFAULT_PORTALS);
		getCurrent().putBoolean(AUTOSAVE_PROPERTY, false);
		getCurrent().putBoolean(AUTOUPDATE_PROPERTY, true);
	}
	
	public static void setLastFilePath(final String value) {
		getCurrent().put(LAST_FILE_PATH_PROPERTY, value);
	}
	
	public static Boolean isLastFilePathSet() {
		return !getCurrent().get(LAST_FILE_PATH_PROPERTY, "").isEmpty();
	}
	
	public static String getLastFilePath() {
		return getCurrent().get(LAST_FILE_PATH_PROPERTY, null);
	}
	
	public static void setPortals(final String value) {
		getCurrent().put(PORTALS_PROPERTY, value);
	}
	
	public static String getPortals() {
		return getCurrent().get(PORTALS_PROPERTY, DEFAULT_PORTALS);
	}
	
	public static Boolean isAutosave() {
		return getCurrent().getBoolean(AUTOSAVE_PROPERTY, false);
	}
	
	public static void setAutosave(final Boolean bool){
		getCurrent().putBoolean(AUTOSAVE_PROPERTY, bool);
	}
	
	public static Boolean isAutoupdate() {
		return getCurrent().getBoolean(AUTOUPDATE_PROPERTY, true);
	}
	
	public static void setAutoupdate(final Boolean bool){
		getCurrent().putBoolean(AUTOUPDATE_PROPERTY, bool);
	}
	
	public static List<String> getPortalsList() {
		final String portals = getCurrent().get(PORTALS_PROPERTY, DEFAULT_PORTALS);
		return Arrays.asList(portals.split(", "));
	}
	
	public static void addNewPortal(final String portal){
		final String portals = getCurrent().get(PORTALS_PROPERTY, DEFAULT_PORTALS);
		if(!StringUtils.contains(portals, portal))
			setPortals(portals + ", " + portal);
	}
	
	public static void save() {
		try {
			getCurrent().flush();
		} catch (BackingStoreException e) {
			l.error("Failed to save preferences", e);
		}
	}
	
	private static Preferences getCurrent(){
		return _INSTANCE.current;
	}
	
}
