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

package jobhunter.utils;

import impl.org.controlsfx.i18n.Localization;

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ApplicationState {
	
	_INSTANCE;
	
	private Boolean changesPending = Boolean.FALSE;
	private static final Boolean debug = (System.getProperty("debug") != null);
	private static final Boolean development = (System.getProperty("development") != null);
	private final Locale locale = getLocaleOption();
	private final ResourceBundle bundle = loadBundle();
	
	private static final Logger l = LoggerFactory.getLogger(ApplicationState.class);
	public static final String APP_STRING = "JobHunter 0.1.2";
	
	private static ApplicationState self() {
		return _INSTANCE;
	}
	
	public static Boolean changesPending() {
		return self().changesPending;
	}
	
	public static void changesPending(Boolean val){
		l.debug("Changes pending: {}", val);
		self().changesPending = val;
	}
	
	public static Boolean isDebug() {
		return debug;
	}
	
	public static Boolean isDevelopment() {
		return development;
	}
	
	public static Locale getLocale() {
		return self().locale;
	}
	
	public static ResourceBundle getBundle() {
		return self().bundle;
	}

	private static Locale getLocaleOption() {
		final String cliLocale = System.getProperty("locale");
		
		if(cliLocale != null){
			//locale should come as a en_US, en_UK, es_ES, fr_FR
			final String [] str = cliLocale.split("_");
			final Locale local = new Locale(str[0], str[1]);
			Localization.setLocale(local);
			return local;
		}else{
			return Locale.getDefault();
		}
	}
	
	private static ResourceBundle loadBundle() {
		final Locale local = getLocaleOption();
		return ResourceBundle.getBundle("i18n.jobhunter", local);
	}
	
}
