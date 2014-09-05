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

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ApplicationState {
	
	_INSTANCE;
	
	private Boolean changesPending = Boolean.FALSE;
	private final Boolean debug = (System.getProperty("debug") != null);
	private final Boolean development = (System.getProperty("development") != null);
	private final Locale locale = getLocaleOption();
	private final ResourceBundle bundle = loadBundle();
	
	private static final Logger l = LoggerFactory.getLogger(ApplicationState.class);
	public static final String APP_STRING = "JobHunter 0.0.4";
	
	public static ApplicationState instanceOf() {
		return _INSTANCE;
	}
	
	public Boolean changesPending() {
		return changesPending;
	}
	
	public void changesPending(Boolean val){
		l.debug("Changes pending: {}", val);
		this.changesPending = val;
	}
	
	public Boolean isDebug() {
		return this.debug;
	}
	
	public Boolean isDevelopment() {
		return this.development;
	}
	
	public Locale getLocale() {
		return this.locale;
	}
	
	public ResourceBundle getBundle() {
		return bundle;
	}

	private static Locale getLocaleOption() {
		final String cliLocale = System.getProperty("locale");
		
		if(cliLocale != null){
			//locale should come as a en_US, en_UK, es_ES, fr_FR
			final String [] str = cliLocale.split("_");
			return new Locale(str[0], str[1]);
		}else{
			return Locale.getDefault();
		}
	}
	
	private static ResourceBundle loadBundle() {
		final Locale local = getLocaleOption();
		return ResourceBundle.getBundle("i18n.jobhunter", local);
	}
	
}
