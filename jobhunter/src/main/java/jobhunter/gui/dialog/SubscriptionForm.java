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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jobhunter.controllers.PreferencesController;
import jobhunter.gui.Localizable;
import jobhunter.models.Subscription;
import jobhunter.utils.JavaFXUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionForm implements Initializable, Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(SubscriptionForm.class);
	private static final String PATH = "/fxml/SubscriptionForm.fxml";
	private static final int WIDTH 	= 420;
	private static final int HEIGHT = 170;
	
	private ResourceBundle bundle;
	private Subscription subscription;
	
	@FXML
    private TextField urlField;

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<String> portalField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    void onCancelButtonAction(ActionEvent event) {
    	JavaFXUtils.closeWindow(event);
    }

    @FXML
    void onSaveButtonAction(ActionEvent event) {
    	this.subscription.setTitle(titleField.getText());
		this.subscription.setPortal(portalField.getValue());
		this.subscription.setURI(urlField.getText());
    	JavaFXUtils.closeWindow(event);
    }
    
    public static SubscriptionForm create(){
    	return new SubscriptionForm();
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle b) {
		ObservableList<String> portals = FXCollections.observableArrayList(
			PreferencesController.instanceOf().getPortalsList()
		);
		
		this.portalField.setItems(portals);
	}
	
	public Optional<Subscription> show() {
		Optional<Parent> root = JavaFXUtils.loadFXML(this, PATH, bundle);
		
		if(!root.isPresent()) return Optional.empty();
		
		Stage stage = new Stage();
		stage.setTitle(getTranslation("message.add.subscription"));
		
		Scene scene = new Scene(root.get(), WIDTH, HEIGHT);
		stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
        l.debug("Returning model");
		return Optional.ofNullable(this.subscription);
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public SubscriptionForm setSubscription(Subscription subscription) {
		this.subscription = subscription;
		return this;
	}
	
	@Override
	public ResourceBundle getBundle() {
		return this.bundle;
	}

	public SubscriptionForm setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
		return this;
	}
	
}
