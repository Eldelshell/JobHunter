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

package jobhunter.gui;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public interface Localizable {

	public ResourceBundle getBundle();
	
	default String getTranslation(String str) {
		try {
			return getBundle().getString(str);
		} catch (Exception e) {
			return str;
		}
	}
	
	default String getTranslation(String str, Object...objects) {
		try {
			final String i18n = getBundle().getString(str);
			return MessageFormat.format(i18n, objects);
		} catch (Exception e) {
			return str;
		}
	}
	
	default String[] getTranslationArray(String str) {
		return getBundle().getString(str).split(",");
	}
}
