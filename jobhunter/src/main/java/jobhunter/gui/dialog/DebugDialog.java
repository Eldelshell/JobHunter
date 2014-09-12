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

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jobhunter.gui.Localizable;
import jobhunter.persistence.Persistence;

public class DebugDialog implements Localizable {
	
private final ResourceBundle bundle;
	
	private DebugDialog(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
	}
	
	public static DebugDialog create(ResourceBundle bundle){
		return new DebugDialog(bundle);
	}

	public void show() {
		TextArea text = new TextArea();
		text.setText(Persistence.debugProfile());
		text.setEditable(false);
		text.setPrefSize(600, 500);
		
		VBox box = new VBox(1);
		box.getChildren().add(text);
		box.setPrefSize(600, 500);
		
		Scene scene = new Scene(box, 600, 500);
		
		Stage stg = new Stage();
        stg.setTitle("Debug!");
        stg.setScene(scene);
        stg.initStyle(StageStyle.UTILITY);
        stg.initModality(Modality.WINDOW_MODAL);
        stg.show();
		
	}
	
	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}
	
	
}
