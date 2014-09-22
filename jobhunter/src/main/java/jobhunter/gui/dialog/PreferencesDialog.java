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

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import jobhunter.controllers.PreferencesController;
import jobhunter.gui.Localizable;

import org.controlsfx.dialog.Dialog;

public class PreferencesDialog implements Localizable {

	private final ResourceBundle bundle;
	
	private final TextField portals = new TextField();
	private final CheckBox autosave = new CheckBox();
	private final CheckBox autoupdate = new CheckBox();
	
	public static PreferencesDialog create(ResourceBundle bundle){
		return new PreferencesDialog(bundle);
	}
	
	private PreferencesDialog(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
		bindEvents();
	}
	
	private void bindEvents() {
		portals.setText(PreferencesController.getPortals());
		portals.textProperty().addListener((obs, old, neu) -> {
			PreferencesController.setPortals(neu);
		});
		
		autosave.setSelected(PreferencesController.isAutosave());
		autosave.setText(getTranslation("label.autosave"));
		autosave.pressedProperty().addListener((obs, old, neu) -> {
			PreferencesController.setAutosave(neu);
		});
		
		autoupdate.setSelected(PreferencesController.isAutoupdate());
		autoupdate.setText(getTranslation("label.autoupdate"));
		autoupdate.pressedProperty().addListener((obs, old, neu) -> {
			PreferencesController.setAutoupdate(neu);
		});
	}
	
	public void show() {
		Dialog dlg = new Dialog(null, getTranslation("label.preferences"));
        
        final GridPane content = new GridPane();
        content.setHgap(10);
        content.setVgap(10);
        
        content.add(new Label(getTranslation("label.portals")), 0, 0);
        content.add(portals, 1, 0);
        GridPane.setHgrow(portals, Priority.ALWAYS);
        
        content.add(autosave, 1, 1);
        content.add(autoupdate, 1, 2);
        
        dlg.setResizable(false);
        dlg.setIconifiable(false);
        dlg.setContent(content);
        dlg.getActions().addAll(Dialog.Actions.CLOSE);
          
        dlg.show();
	}

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}
	
}
