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

public enum ApplicationState {
	
	_INSTANCE;
	
	private Boolean changesPending = Boolean.FALSE;
	private Boolean debug = (System.getProperty("debug") != null);
	private Boolean development = (System.getProperty("development") != null);
	
	public static ApplicationState instanceOf() {
		return _INSTANCE;
	}
	
	public Boolean changesPending() {
		return changesPending;
	}
	
	public void changesPending(Boolean val){
		this.changesPending = val;
	}
	
	public Boolean isDebug() {
		return this.debug;
	}
	
	public Boolean isDevelopment() {
		return this.development;
	}
	
}
