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

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jobhunter.gui.Localizable;
import jobhunter.gui.dialog.DialogFactory;
import jobhunter.models.Job;
import jobhunter.persistence.ProfileRepository;
import jobhunter.utils.JavaFXUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFormController implements Initializable, Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(JobFormController.class);
	private static final String PATH = "/fxml/JobForm.fxml";
	private static final int WIDTH 	= 700;
	private static final int HEIGHT = 500;
	
	private Job job;
	
	@FXML
    private BorderPane mainPanel;
	
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private ToggleButton companyButton;

    @FXML
    private ToggleButton contactsButton;

    @FXML
    private ToggleButton eventsButton;

    @FXML
    private ToggleButton applicationButton;

	private final ResourceBundle bundle;
	
	private ApplicationFormController applicationForm;
	private CompanyFormController companyForm;
	private ContactsFormController contactsForm;
	private ActivityLogController logController;
	
	public static JobFormController create(ResourceBundle bundle){
		return new JobFormController(bundle);
	}

	private JobFormController(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
	}

	@FXML
	void cancelButtonHandler(ActionEvent event) {
		l.debug("Cancel!");
		close(event);
	}

	@FXML
	void saveButtonHandler(ActionEvent event) {
		l.debug("Save: {}", job.toString());
		ProfileRepository.save(job);
		close(event);
	}
	
	@FXML
	void deleteButtonHandler(ActionEvent event) {
		if(DialogFactory.deleteJob(this.job.getPosition())){
			ProfileRepository.delete(this.job);
			close(event);
		}
	}
	
	@FXML
    void switchForm(ActionEvent event) {
		if(event.getSource().equals(applicationButton)){
			applicationForm.show().ifPresent(this::drawForm);
		}else if(event.getSource().equals(companyButton)){
			companyForm.show().ifPresent(this::drawForm);
		}else if(event.getSource().equals(contactsButton)){
			contactsForm.show().ifPresent(this::drawForm);
		}else if(event.getSource().equals(eventsButton)){
			logController.show().ifPresent(this::drawForm);
		}
    }
	
	void drawForm(final Node node) {
		mainPanel.setCenter(node);
	}
	
    public void show() {
    	Optional<Parent> root = JavaFXUtils.loadFXML(this, PATH, bundle);
    	
    	Scene scene = new Scene(root.get(), WIDTH, HEIGHT);
		Stage stage = new Stage();
		
		if(this.job.getPosition() != null){
			stage.setTitle(getTranslation("message.edit.job", job.getPosition()));
		}else{
			stage.setTitle(getTranslation("message.add.job"));
		}
		
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle b) {
		l.debug("Initializing {}", b);
		
		if(this.job == null){
			this.job = Job.of();
			this.deleteButton.setVisible(false);
		}
		
		if(!this.job.getActive()){
			this.deleteButton.setVisible(false);
			this.saveButton.setVisible(false);
		}
		
		applicationForm = ApplicationFormController.create(bundle).setJob(job);
		companyForm = CompanyFormController.create(bundle).setJob(job);
		contactsForm = ContactsFormController.create(bundle).setJob(job);
		logController = ActivityLogController.create(bundle).setJob(job);

		applicationForm.show().ifPresent(this::drawForm);
	}
	
	private void close(Event event) {
		JavaFXUtils.closeWindow(event);
	}

	public Optional<Job> getJob() {
		return Optional.of(job);
	}

	public JobFormController setJob(Job job) {
		this.job = job;
		return this;
	}
	
	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}
	
}
