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
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jobhunter.gui.dialog.LogEventDialogController;
import jobhunter.models.ActivityLog;
import jobhunter.models.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller to edit a job's events data. Basically it's a table.
 */
public class ActivityLogController implements JobFormChild<ActivityLog> {
	
	private static final Logger l = LoggerFactory.getLogger(ActivityLogController.class);
	private static final String PATH = "/fxml/LogForm.fxml";

    @FXML
    private TableView<ActivityLog> table;

    @FXML
    private TableColumn<ActivityLog, String> dateColumn;

    @FXML
    private TableColumn<ActivityLog, String> eventColumn;

    @FXML
    private TableColumn<ActivityLog, String> descriptionColumn;
    
    private Job job;
    
    private final ResourceBundle bundle;
    
    public static ActivityLogController create(ResourceBundle bundle){
    	return new ActivityLogController(bundle);
    }
    
    private ActivityLogController(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
	}

	@FXML
    void onAddRowMenuItemAction(ActionEvent event) {
    	LogEventDialogController.create(bundle)
			.setLog(ActivityLog.of())
			.show()
			.ifPresent(neu -> {
				this.job.addLog(neu);
	    		table.getItems().add(neu);
	    		table.getSelectionModel().selectLast();
			});
    }
	
    @FXML
    void onDeleteRowMenuItemAction(ActionEvent event) {
    	ActivityLog selected = table.getSelectionModel().getSelectedItem();
    	int index = table.getSelectionModel().getSelectedIndex();
    	
    	if(selected == null) return;
    	
    	this.job.getLogs().remove(selected);
    	table.getItems().remove(index);
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		l.debug("Initializing");
		dateColumn.setCellValueFactory(new PropertyValueFactory<ActivityLog, String>("date"));
		eventColumn.setCellValueFactory(new PropertyValueFactory<ActivityLog, String>("event"));
		eventColumn.setCellFactory(col -> {
			return new TableCell<ActivityLog, String>(){

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					
					if(!empty){
						setText(getBundle().getString("activity.log." + item.toLowerCase()));
					}else{
						setText("");
					}
				}
				
			};
		});
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<ActivityLog, String>("description"));
		table.setItems(FXCollections.observableArrayList(this.job.getLogs()));
	}
	
	@Override
	public String getFXMLPath() {
		return PATH;
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	@Override
	public Job getJob() {
		return job;
	}

	public ActivityLogController setJob(Job job) {
		this.job = job;
		return this;
	}
	
}
