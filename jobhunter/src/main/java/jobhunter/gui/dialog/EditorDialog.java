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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditorDialog {
	
	private final TextArea html = new TextArea();
	private EventHandler<ActionEvent> handler;
	
	public static EditorDialog create() {
		return new EditorDialog();
	}
	
	public String getHtml() {
		return html.getText();
	}

	public EditorDialog setHtml(String html) {
		this.html.setText(html);
		return this;
	}

	public EditorDialog show() {
		html.setPrefSize(600, 500);
		html.setWrapText(true);
		html.setPadding(new Insets(10.0));
		VBox.setVgrow(html, Priority.ALWAYS);
		
		final VBox box = new VBox(1);
		box.getChildren().add(html);
		box.setPrefSize(600, 500);
		
		final Scene scene = new Scene(box, 600, 500);
		
		final Stage stg = new Stage();
        stg.setTitle("Editor");
        stg.setScene(scene);
        stg.initStyle(StageStyle.UTILITY);
        stg.initModality(Modality.APPLICATION_MODAL);
        
        stg.setOnCloseRequest((e) -> {
        	handler.handle(new ActionEvent(this, html));
        });
        
        stg.show();
        return this;
	}
	
	public EditorDialog setOnSaveEvent(EventHandler<ActionEvent> handler){
		this.handler = handler;
		return this;
	}
	
}
