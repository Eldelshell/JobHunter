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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import jobhunter.controllers.PreferencesController;
import jobhunter.models.Job;
import jobhunter.persistence.ProfileRepository;
import jobhunter.utils.LocalizedEnum;

import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationFormController implements JobFormChild<Job> {
	
	private static final Logger l = LoggerFactory.getLogger(ApplicationFormController.class);
	private static final String PATH = "/fxml/ApplicationForm.fxml";
	
	private final ResourceBundle bundle;
	
	@FXML
    private TextField salaryTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField positionTextField;

    @FXML
    private ComboBox<String> portalCombo;

    @FXML
    private ChoiceBox<LocalizedEnum<Job.Status>> statusCombo;

    @FXML
    private Slider ratingSlider;

    @FXML
    private TextField linkTextField;
    
    private Job job;
    
    public static ApplicationFormController create(ResourceBundle bundle) {
    	return new ApplicationFormController(bundle);
    }
    
    private ApplicationFormController(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
	}
    
    @Override
	public String getFXMLPath() {
		return PATH;
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle bundle) {
		l.debug("Init me {}", this.getClass().getCanonicalName());
		
		ObservableList<String> portals = FXCollections.observableArrayList(
				PreferencesController.getPortalsList()
		);
		
		List<LocalizedEnum<Job.Status>> vals = Arrays.stream(Job.Status.values())
				.map(LocalizedEnum::of)
				.collect(Collectors.toList());

		ObservableList<LocalizedEnum<Job.Status>> jobStates = FXCollections.observableArrayList(
			vals
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
	
	private void bindEvents() {
		positionTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setPosition(neu);
			}
		});
		
		addressTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setAddress(neu);
			}
		});
		
		salaryTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setSalary(neu);
			}
		});
		
		linkTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setLink(neu);
			}
		});
		
		ratingSlider.valueProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setRating(neu.intValue());
			}
		});
		
		statusCombo.valueProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setStatus((Job.Status)neu.getEnum());
			}
		});
		
		portalCombo.valueProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.job.setPortal(neu);
				PreferencesController.addNewPortal(neu);
			}
		});
	}
	
	private void initializeEmpty() {
		TextFields.bindAutoCompletion(
			positionTextField, 
			ProfileRepository.getAutocompletePositions()
		);
		
		statusCombo.getSelectionModel().selectFirst();
	}
	
	private void initializeFull() {
		statusCombo.getSelectionModel().select(LocalizedEnum.of(job.getStatus()));
		positionTextField.setText(job.getPosition());
		addressTextField.setText(job.getAddress());
		salaryTextField.setText(job.getSalary());
		portalCombo.getSelectionModel().select(job.getPortal());
		linkTextField.setText(job.getLink());
		ratingSlider.setValue(job.getRating().doubleValue());
		
		if(!job.getActive())
			initializeInactive();
	}
	
	private void initializeInactive() {
		statusCombo.setDisable(true);
		positionTextField.setDisable(true);
		addressTextField.setDisable(true);
		salaryTextField.setDisable(true);
		portalCombo.setDisable(true);
		linkTextField.setDisable(true);
		ratingSlider.setDisable(true);
	}
	
	public ApplicationFormController setJob(Job job) {
		this.job = job;
		return this;
	}
	
	@Override
	public ResourceBundle getBundle() {
		return this.bundle;
	}
	
	@Override
	public Job getJob() {
		return job;
	}
	
}
