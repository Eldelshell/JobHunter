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

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jobhunter.models.Job;
import jobhunter.persistence.ObjectId;
import jobhunter.persistence.ProfileRepository;
import jobhunter.utils.JavaFXUtils;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFormController extends Observable implements Initializable {
	
	private static final Logger l = LoggerFactory.getLogger(JobFormController.class);
	private static final String PATH = "/fxml/JobForm.fxml";
	private static final int WIDTH 	= 700;
	private static final int HEIGHT = 500;
	
	private Job job;
	
	@FXML
    private BorderPane mainPanel;
	
	@FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<String> jobFormListView;
    
	private final ProfileRepository profileController = ProfileRepository.instanceOf();
	
	private ApplicationFormController applicationForm;
	private CompanyFormController companyForm;
	private ContactsFormController contactsForm;
	private ActivityLogController logController;

	@FXML
	void cancelButtonHandler(ActionEvent event) {
		l.debug("Cancel!");
		close(event, false);
	}

	@FXML
	void saveButtonHandler(ActionEvent event) {
		l.debug("Save: {}", job.toString());
		profileController.saveJob(job);
		close(event, true);
	}
	
	@FXML
	void deleteButtonHandler(ActionEvent event) {
		Action response = Dialogs.create()
		        .title("Delete job")
		        .lightweight()
		        .masthead("You're about to delete this job.")
		        .message("Do you want to continue?")
		        .showConfirm();

		if (response == Dialog.Actions.YES) {
			profileController.deleteJob(this.job);
			close(event, true);
		}
	}
	
	@FXML
	void jobFormListViewOnMouseClickedHandler(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY)){
			final int index = jobFormListView.getSelectionModel().getSelectedIndex();
			
			switch(index){
			case 0:
				applicationForm.load().ifPresent(this::drawForm);
				break;
			case 1:
				companyForm.load().ifPresent(this::drawForm);
				break;
			case 2:
				contactsForm.load().ifPresent(this::drawForm);
				break;
			case 3:
				logController.load().ifPresent(this::drawForm);
				break;
			}
		}
		
	}
	
	void drawForm(final Node node) {
		mainPanel.setCenter(node);
	}
	
	public static JobFormController of(final ObjectId jobId) {
		return of(ProfileRepository.instanceOf().getJob(jobId));
    }
	
	public static JobFormController of(final Optional<Job> job) {
		JobFormController instance = new JobFormController();
		if(job.isPresent()) instance.setJob(job.get());
		return instance;
		
	}
    
    public void load() {
    	FXMLLoader fxmlLoader = new FXMLLoader(ApplicationFormController.class.getResource(PATH));
    	fxmlLoader.setController(this);
    	
    	Scene scene = null;
		try {
			Parent root = (Parent)fxmlLoader.load();
			scene = new Scene(root, WIDTH, HEIGHT);
			
		} catch (IOException e) {
			l.error("Failed to open file {}", PATH, e);
		}
		
		if(scene != null){
			Stage stage = new Stage();
			
			if(this.job != null){
				stage.setTitle("Edit Job " + job.getPosition());
			}else{
				stage.setTitle("Add Job");
			}
			
	        stage.setScene(scene);
	        stage.initStyle(StageStyle.UTILITY);
	        stage.initModality(Modality.WINDOW_MODAL);
	        stage.showAndWait();
		}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		if(this.job == null){
			this.job = Job.of();
			this.deleteButton.setVisible(false);
		}
		
		applicationForm = ApplicationFormController.of(job);
		
		applicationForm.setListener(e -> {
			this.job = e;
		});
		
		companyForm = CompanyFormController.of(job.getCompany());
		
		companyForm.setListener(e -> {
			this.job.setCompany(e);
		});
		
		contactsForm = ContactsFormController.of(job.getContacts());
		
		logController = ActivityLogController.of(job.getLogs());

		//FIXME: I don't like how this works by using the index. Fix later.
		jobFormListView.setItems(FXCollections.observableArrayList(
			"Job", "Company", "Contacts", "Log"
		));
		
		jobFormListView.getSelectionModel().selectFirst();
		
		applicationForm.load().ifPresent(this::drawForm);
	}
	
	private void close(Event event, Boolean changed) {
		if(changed){
			setChanged();
			notifyObservers(job);
		}
		JavaFXUtils.closeWindow(event);
	}

	public Optional<Job> getJob() {
		return Optional.of(job);
	}

	public JobFormController setJob(Job job) {
		this.job = job;
		return this;
	}
	
	public JobFormController setObserver(Observer obs){
		addObserver(obs);
		return this;
	}
	
}
