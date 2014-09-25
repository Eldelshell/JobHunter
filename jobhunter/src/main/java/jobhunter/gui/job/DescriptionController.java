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

package jobhunter.gui.job;

import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import jobhunter.gui.Localizable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DescriptionController implements Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(DescriptionController.class);
	
	private AnchorPane pane;
	private HTMLEditor editor;
	private final ResourceBundle bundle;
	
	public static DescriptionController create(ResourceBundle bundle){
    	return new DescriptionController(bundle);
    }
    
    private DescriptionController(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
		this.pane = new AnchorPane();
		this.editor = new HTMLEditor();
		AnchorPane.setBottomAnchor(editor, 0.0);
		AnchorPane.setLeftAnchor(editor, 0.0);
		AnchorPane.setTopAnchor(editor, 0.0);
		AnchorPane.setRightAnchor(editor, 0.0);
		pane.setPrefSize(520, 430);
		pane.getChildren().add(editor);
	}

	
	public Optional<Parent> show() {
		l.debug("Showing");
		return Optional.of(pane);
	}

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}

	public String getDescription() {
		return this.editor.getHtmlText();
	}

	public DescriptionController setDescription(String description) {
		editor.setHtmlText(description);
		return this;
	}

}
