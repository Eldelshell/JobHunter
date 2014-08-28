package jobhunter.gui.job;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
	public Optional<Parent> load() {
		FXMLLoader fxmlLoader = new FXMLLoader(ApplicationFormController.class.getResource(PATH));
    	fxmlLoader.setController(this);
		try {
			return Optional.of((Parent)fxmlLoader.load());
		} catch (IOException e) {
			l.error("Failed to open file {}", PATH, e);
		}
		return Optional.empty();
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
