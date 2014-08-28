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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jobhunter.controllers.PreferencesController;
import jobhunter.utils.JavaFXUtils;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.PropertySheet.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreferencesDialog {

	private static final Logger l = LoggerFactory.getLogger(PreferencesDialog.class);
	private static final int WIDTH 	= 400;
	private static final int HEIGHT = 300;
	
	private ResourceBundle bundle;
	
	private PreferencesController preferencesController = PreferencesController.instanceOf();
	
	public static abstract class Property implements PropertySheet.Item {

		protected EventHandler<ActionEvent> handler;
		protected String category, name, description;
		
		protected Property(String category, String name, String description) {
			super();
			this.category = category;
			this.name = name;
			this.description = description;
		}
		
		public void setOnChange(EventHandler<ActionEvent> handler) {
			this.handler = handler;
		}

		@Override
		public String getCategory() {
			return this.category;
		}

		@Override
		public String getDescription() {
			return this.description;
		}

		@Override
		public String getName() {
			return this.name;
		}

	}
	
	public static class StringProperty extends Property {
		
		protected StringProperty(String name, String description, String value) {
			super(null, name, description);
			this.value = value;
		}
		
		protected StringProperty(String category, String name, String description, String value) {
			super(category, name, description);
			this.value = value;
		}

		private String value;

		@Override
		public Class<?> getType() {
			return String.class;
		}

		@Override
		public Object getValue() {
			return this.value;
		}

		@Override
		public void setValue(Object arg0) {
			this.value = (String)arg0;
			this.handler.handle(new ActionEvent(this, null));
		}
		
	}
	
	public static class PortalsProperty extends StringProperty {

		protected PortalsProperty(String value) {
			super("Portals", "Comma separated list", value);
		}
		
	}
	
	private ObservableList<Item> getItems() {
		final Property portals = new PortalsProperty(preferencesController.getPortals());
		
		portals.setOnChange(event -> {
			final StringProperty p = (StringProperty)event.getSource();
			preferencesController.setPortals((String)p.getValue());
		});
		
		final ObservableList<Item> list = FXCollections.observableArrayList();
		list.add(portals);
		return list;
	}
	
	private void show() {
		l.debug("Showing Preferences Dialog");
		final PropertySheet s = new PropertySheet(getItems());
    	s.setSearchBoxVisible(false);
    	s.setModeSwitcherVisible(false);
    	
    	final Button closeButton = new Button("Close");
    	
    	closeButton.setOnAction(event -> {
    		JavaFXUtils.closeWindow(event);
    	});
    	
    	final ToolBar toolbar = new ToolBar(closeButton);
    	toolbar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
    	
    	final BorderPane infoPane = new BorderPane();
    	infoPane.setBottom(toolbar);
    	infoPane.setCenter(s);
    	infoPane.setMaxHeight(Double.MAX_VALUE);
    	
    	final Stage stage = new Stage();
		stage.setTitle("Preferences");
        stage.setScene(new Scene(infoPane, WIDTH, HEIGHT));
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
	}
	
	public static void show(final ResourceBundle bundle) {
		PreferencesDialog dialog = new PreferencesDialog();
		dialog.bundle = bundle;
		dialog.show();
	}
	
}
