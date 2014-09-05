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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jobhunter.controllers.PreferencesController;
import jobhunter.gui.FormChangeListener;
import jobhunter.gui.Localizable;
import jobhunter.gui.dialog.EditorDialog;
import jobhunter.models.Job;
import jobhunter.persistence.ProfileRepository;

import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationFormController implements Initializable, JobFormChild<Job>, Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(ApplicationFormController.class);
	private static final String PATH = "/fxml/ApplicationForm.fxml";
	
	private ResourceBundle bundle;
	
	@FXML
    private TextField salaryTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField positionTextField;

    @FXML
    private ComboBox<String> portalCombo;

    @FXML
    private ChoiceBox<String> statusCombo;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Slider ratingSlider;

    @FXML
    private TextField linkTextField;
    
    private Job job;
    private final PreferencesController preferencesController = PreferencesController.instanceOf();
    private final ProfileRepository profileController = ProfileRepository.instanceOf();
    private FormChangeListener<Job> listener;
    
    public static ApplicationFormController of(final Job job) {
    	ApplicationFormController instance = new ApplicationFormController();
    	instance.setJob(job);
    	return instance;
    }
    
    @Override
	public String getFXMLPath() {
		return PATH;
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle bundle) {
		l.debug("Init me {}", this.getClass().getCanonicalName());
		
		this.bundle = bundle;

		ObservableList<String> portals = FXCollections.observableArrayList(
			preferencesController.getPortalsList()
		);

		ObservableList<String> jobStates = FXCollections.observableArrayList(
			Job.Status.asList()
		);

		portalCombo.setItems(portals);
		statusCombo.setItems(jobStates);
		
		bindEvents();

		if (this.job == null) {
			initializeEmpty();
		} else {
			initializeFull();
		}
		
	}
	
	@FXML
	void onOpenEditorAction(ActionEvent event) {
		EditorDialog
			.create(bundle)
			.setHtml(this.job.getDescription())
			.setOnSaveEvent((e) -> {
				EditorDialog dialog = (EditorDialog)e.getSource();
				this.descriptionTextArea.setText(dialog.getHtml());
			}).show();
	}
	
	private void bindEvents() {
		positionTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setPosition(neu);
				changed();
			}
		});
		
		addressTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setAddress(neu);
				changed();
			}
		});
		
		salaryTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setSalary(neu);
				changed();
			}
		});
		
		linkTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setLink(neu);
				changed();
			}
		});
		
		descriptionTextArea.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setDescription(neu);
				changed();
			}
		});
		
		ratingSlider.valueProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setRating(neu.intValue());
				changed();
			}
		});
		
		statusCombo.valueProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setStatus(Job.Status.valueOf(neu.toUpperCase()));
				changed();
			}
		});
		
		portalCombo.valueProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setPortal(neu);
				preferencesController.addNewPortal(neu);
				changed();
			}
		});
	}
	
	private void initializeEmpty() {
		TextFields.bindAutoCompletion(
			positionTextField, 
			profileController.getAutocompletePositions()
		);
		
		statusCombo.getSelectionModel().selectFirst();
	}
	
	private void initializeFull() {
		statusCombo.getSelectionModel().select(job.getStatus().capitalize());
		positionTextField.setText(job.getPosition());
		addressTextField.setText(job.getAddress());
		salaryTextField.setText(job.getSalary());
		portalCombo.getSelectionModel().select(job.getPortal());
		linkTextField.setText(job.getLink());
		descriptionTextArea.setText(job.getDescription());
		ratingSlider.setValue(job.getRating().doubleValue());
		
		if(!job.getActive())
			initializeInactive();
	}
	
	private void initializeInactive() {
		positionTextField.setDisable(true);
		addressTextField.setDisable(true);
		salaryTextField.setDisable(true);
		portalCombo.setDisable(true);
		linkTextField.setDisable(true);
		descriptionTextArea.setDisable(true);
		ratingSlider.setDisable(true);
	}
	
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
	
	@Override
	public void changed(){
		if(this.listener != null)
			this.listener.changed(this.job);
	}
	
	@Override
	public FormChangeListener<Job> getListener() {
		return listener;
	}
	
	@Override
	public void setListener(FormChangeListener<Job> listener) {
		this.listener = listener;
	}

	@Override
	public ResourceBundle getBundle() {
		return this.bundle;
	}
	
}
