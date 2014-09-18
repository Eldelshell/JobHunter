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

import java.util.ResourceBundle;

import jobhunter.gui.Localizable;
import jobhunter.utils.JavaFXUtils;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.dialog.Dialogs.CommandLink;

/**
 * Dialog to show an user different options to report a bug
 */
public class BugReportDialog implements Localizable{
	
	public static final String GITHUB_URI = "https://github.com/Eldelshell/JobHunter/issues";
	private static final String FORM_URI = "https://docs.google.com/forms/d/1vEt80-0MdBFjG9EymogRDRiQtwJA2wI7ghtp0LPQrI8/viewform";
	
	private ResourceBundle bundle;
	
	public static BugReportDialog create() {
		return new BugReportDialog();
	}
	
	public void show() {
		CommandLink github = new CommandLink(
			getTranslation("label.report.github"),
			getTranslation("message.report.github")
		);
		
		CommandLink form = new CommandLink(
			getTranslation("label.report.form"),
			getTranslation("message.report.form")
		);
		
		Action response = Dialogs.create()
		        .title(getTranslation("menu.bug"))
		        .masthead(null)
		        .message(getTranslation("label.select.option"))
		        .showCommandLinks(form, github, form);
		
		if(!response.equals(Dialog.Actions.CANCEL)){
			if(response.equals(github)){
				JavaFXUtils.openWebpage(GITHUB_URI);
			}else{
				JavaFXUtils.openWebpage(FORM_URI);
			}
		}
	}

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}

	public BugReportDialog setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
		return this;
	}
	
}
