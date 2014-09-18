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

import java.io.File;
import java.util.ResourceBundle;

import jobhunter.gui.Localizable;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.dialog.Dialogs.CommandLink;

/**
 * A dialog to handle concurrent file modification situations. We offer
 * the user the following options to act upon:
 * - Cancel
 * - Overwrite saved file
 * - Reload and lose current changes
 * - Save as new file
 */
public class ConcurrentFileModificationDialog implements Localizable {
	
	public enum Actions {
		OVERWRITE, RELOAD, SAVE, CANCEL;
	}

	private ResourceBundle bundle;
	
	public static ConcurrentFileModificationDialog create() {
		return new ConcurrentFileModificationDialog();
	}
	
	public Actions show(final File file) {
		CommandLink overwrite = new CommandLink(
			getTranslation("label.overwrite"),
			getTranslation("message.overwrite")
		);
		
		CommandLink reload = new CommandLink(
			getTranslation("label.reload"),
			getTranslation("message.reload")
		);
		
		CommandLink save = new CommandLink(
			getTranslation("label.save.as"),
			getTranslation("message.save.as")
		);
		
		CommandLink cancel = new CommandLink(
			getTranslation("label.cancel"),
			getTranslation("message.cancel")
		);
		
		Action response = Dialogs.create()
		        .title(getTranslation("label.file.modified"))
		        .masthead(getTranslation("message.file.modified", file.toString()))
		        .message(getTranslation("label.select.option"))
		        .actions(Dialog.Actions.CANCEL)
		        .showCommandLinks(cancel, cancel, save, overwrite, reload);
		
		if(response.equals(overwrite)) return Actions.OVERWRITE;
		if(response.equals(reload)) return Actions.RELOAD;
		if(response.equals(save)) return Actions.SAVE;
		
		return Actions.CANCEL;
	}

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}

	public ConcurrentFileModificationDialog setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
		return this;
	}

}
