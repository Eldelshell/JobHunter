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
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
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
import jobhunter.gui.Localizable;
import jobhunter.models.Job;
import jobhunter.persistence.ProfileRepository;
import jobhunter.utils.JavaFXUtils;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFormController extends Observable implements Initializable, Localizable {
	
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
    private ListView<String> jobFormListView;
    
	private final ProfileRepository profileController = ProfileRepository.instanceOf();
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
		        .title(getTranslation("message.delete.job", this.job.getPosition()))
		        .lightweight()
		        .masthead(getTranslation("message.delete.job.confirmation"))
		        .message(getTranslation("message.confirmation"))
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
				applicationForm.show().ifPresent(this::drawForm);
				break;
			case 1:
				companyForm.show().ifPresent(this::drawForm);
				break;
			case 2:
				contactsForm.show().ifPresent(this::drawForm);
				break;
			case 3:
				logController.show().ifPresent(this::drawForm);
				break;
			}
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
		
		applicationForm = ApplicationFormController.create(bundle).setJob(job);
		
		applicationForm.setListener(e -> {
			this.job = e;
		});
		
		companyForm = CompanyFormController.create(bundle).setCompany(job.getCompany());
		
		companyForm.setListener(e -> {
			this.job.setCompany(e);
		});
		
		contactsForm = ContactsFormController.create(bundle).setContacts(job.getContacts());
		
		logController = ActivityLogController.create(bundle).setLogs(job.getLogs());

		jobFormListView.setItems(FXCollections.observableArrayList(
			getTranslationArray("job.form.items")
		));
		
		jobFormListView.getSelectionModel().selectFirst();
		
		applicationForm.show().ifPresent(this::drawForm);
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

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}
	
}
