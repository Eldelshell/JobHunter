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

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jobhunter.gui.Localizable;
import jobhunter.models.ActivityLog;
import jobhunter.utils.JavaFXUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEventDialogController implements Initializable, Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(LogEventDialogController.class);
	private static final String PATH = "/fxml/LogEventDialog.fxml";
	private static final int WIDTH 	= 420;
	private static final int HEIGHT = 170;
	
	private ResourceBundle bundle;

	@FXML
    private Button cancelButton;
	
	@FXML
    private Button saveButton;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField descriptionText;

    @FXML
    private ChoiceBox<String> typeCombo;
    
    private ActivityLog log;

    @FXML
    void onCancelButtonAction(ActionEvent event) {
    	this.log = null;
    	JavaFXUtils.closeWindow(event);
    }

    @FXML
    void onSaveButtonAction(ActionEvent event) {
    	this.log.setCreated(dateField.getValue());
    	this.log.setType(ActivityLog.Type.valueOf(typeCombo.getValue().toUpperCase()));
    	this.log.setDescription(descriptionText.getText());
    	JavaFXUtils.closeWindow(event);
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle bundle) {
		this.bundle = bundle;
		ObservableList<String> types = FXCollections.observableArrayList(
			ActivityLog.Type.asList()
		);
		
		typeCombo.setItems(types);
	}
	
	public static LogEventDialogController of(final ActivityLog log) {
		LogEventDialogController ctrl = new LogEventDialogController();
		ctrl.setLog(log);
    	return ctrl;
    }
	
	public Optional<ActivityLog> show() {
		Optional<Parent> root = JavaFXUtils.loadFXML(this, PATH);
		
		if(!root.isPresent()) return Optional.empty();
		
		Stage stage = new Stage();
		stage.setTitle(getTranslation("message.add.event"));
		
		Scene scene = new Scene(root.get(), WIDTH, HEIGHT);
		stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
        l.debug("Returning model");
		return Optional.ofNullable(this.log);
	}

	public ActivityLog getLog() {
		return log;
	}

	public LogEventDialogController setLog(ActivityLog log) {
		this.log = log;
		return this;
	}

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}
	
}
