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

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobhunter.gui.FormChangeListener;
import jobhunter.gui.dialog.LogEventDialogController;
import jobhunter.models.ActivityLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityLogController implements Initializable, JobFormChild<ActivityLog> {
	
	private static final Logger l = LoggerFactory.getLogger(ActivityLogController.class);
	private static final String PATH = "/fxml/LogForm.fxml";

	@FXML
    private ContextMenu tableContextMenu;

    @FXML
    private MenuItem addRowMenuItem;
    
    @FXML
    private TableView<ActivityLog> table;

    @FXML
    private TableColumn<ActivityLog, String> dateColumn;

    @FXML
    private TableColumn<ActivityLog, String> eventColumn;

    @FXML
    private TableColumn<ActivityLog, String> descriptionColumn;
    
    private Set<ActivityLog> logs;
    
    private FormChangeListener<ActivityLog> listener;

    @FXML
    void onAddRowMenuItemAction(ActionEvent event) {
    	Optional<ActivityLog> neu = LogEventDialogController.of(ActivityLog.of()).show();
    	
    	if(neu.isPresent()){
    		this.logs.add(neu.get());
    		table.getItems().add(neu.get());
    		table.getSelectionModel().selectLast();
    	}
    	
    }
	
    @FXML
    void onDeleteRowMenuItemAction(ActionEvent event) {
    	ActivityLog selected = table.getSelectionModel().getSelectedItem();
    	int index = table.getSelectionModel().getSelectedIndex();
    	
    	if(selected == null) return;
    	
    	this.logs.remove(selected);
    	table.getItems().remove(index);
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		l.debug("Initializing");
		dateColumn.setCellValueFactory(new PropertyValueFactory<ActivityLog, String>("date"));
		eventColumn.setCellValueFactory(new PropertyValueFactory<ActivityLog, String>("event"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<ActivityLog, String>("description"));
		table.setItems(FXCollections.observableArrayList(this.logs));
	}
	
	public static ActivityLogController of(final Set<ActivityLog> logs) {
		ActivityLogController ctrl = new ActivityLogController();
		ctrl.setLogs(logs);
    	return ctrl;
    }
	
	@Override
	public String getFXMLPath() {
		return PATH;
	}

	@Override
	public void changed() {
		
	}

	@Override
	public FormChangeListener<ActivityLog> getListener() {
		return this.listener;
	}

	@Override
	public void setListener(FormChangeListener<ActivityLog> listener) {
		this.listener = listener;
	}

	public Set<ActivityLog> getLogs() {
		return logs;
	}

	public void setLogs(Set<ActivityLog> logs) {
		this.logs = logs;
	}
	
}
