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

package jobhunter.gui.dialog;

import jobhunter.utils.ApplicationState;

import org.controlsfx.dialog.Dialogs;

public class AboutDialog {

	public static void show() {
		Dialogs
			.create()
			.title("About JobHunter")
			.lightweight()
			.masthead(ApplicationState.APP_STRING)
			.message("Copyright © Alejandro Ayuso\nJobHunter is licensed under the GPLv3 Open Source License")
			.showInformation();
	}
	
}
