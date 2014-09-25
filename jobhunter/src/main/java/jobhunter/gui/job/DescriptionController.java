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

import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;

/**
 * Custom AnchorPane with a HTMLEditor to modify a job's description.
 */
public class DescriptionController extends AnchorPane {
	
	private final HTMLEditor editor;
	
	public static DescriptionController create(){
    	return new DescriptionController();
    }
    
    private DescriptionController() {
		super();
		this.editor = new HTMLEditor();
		AnchorPane.setBottomAnchor(editor, 0.0);
		AnchorPane.setLeftAnchor(editor, 0.0);
		AnchorPane.setTopAnchor(editor, 0.0);
		AnchorPane.setRightAnchor(editor, 0.0);
		setPrefSize(520, 430);
		getChildren().add(editor);
	}

	
	public String getDescription() {
		return this.editor.getHtmlText();
	}

	public DescriptionController setDescription(String description) {
		editor.setHtmlText(description);
		return this;
	}

}
