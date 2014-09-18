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

import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import jobhunter.controllers.PreferencesController;
import jobhunter.gui.Localizable;
import jobhunter.models.Subscription;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionForm implements Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(SubscriptionForm.class);
	
	private final ResourceBundle bundle;
    private final TextField urlField = new TextField();
    private final TextField titleField = new TextField();
    private final ComboBox<String> portalField = new ComboBox<>();
    private final Action save;
    
    private Subscription subscription;
    
    public SubscriptionForm(ResourceBundle bundle) {
		this.bundle = bundle;
		this.save = new SaveAction(getTranslation("label.save"));
		save.disabledProperty().set(true);
		
		urlField.textProperty().addListener((observable, old, neu) -> {
			subscription.setURI(neu);
			save.disabledProperty().set(!isValid());
	    });
		
		titleField.textProperty().addListener((observable, old, neu) -> {
			subscription.setTitle(neu);
			save.disabledProperty().set(!isValid());
	    });
		
		portalField.valueProperty().addListener((observable, old, neu) -> {
			subscription.setPortal(neu);
			save.disabledProperty().set(!isValid());
	    });
	}

    public static SubscriptionForm create(ResourceBundle bundle){
    	return new SubscriptionForm(bundle);
    }
    
    public Optional<Action> show() {
        Dialog dlg = new Dialog(null, getTranslation("message.add.subscription"));
        
        final GridPane content = new GridPane();
        content.setHgap(10);
        content.setVgap(10);
        
        content.add(new Label(getTranslation("label.title")), 0, 0);
        content.add(titleField, 1, 0);
        GridPane.setHgrow(titleField, Priority.ALWAYS);
        
        ObservableList<String> portals = FXCollections.observableArrayList(
			PreferencesController.getPortalsList()
		);
		
		portalField.setItems(portals);
        portalField.setPrefWidth(400.0);
        portalField.setEditable(true);
		
        content.add(new Label(getTranslation("label.portal")), 0, 1);
        content.add(portalField, 1, 1);
        
        content.add(new Label(getTranslation("label.feed.url")), 0, 2);
        content.add(urlField, 1, 2);
        GridPane.setHgrow(urlField, Priority.ALWAYS);
        
        dlg.setResizable(false);
        dlg.setIconifiable(false);
        dlg.setContent(content);
        dlg.getActions().addAll(save, Dialog.Actions.CANCEL);
          
        Platform.runLater(() -> titleField.requestFocus());
        
        l.debug("Showing dialog");
        return Optional.of(dlg.show());
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
	
	private Boolean isValid() {
		if(StringUtils.isEmpty(this.subscription.getTitle())) return false;
		if(StringUtils.isEmpty(this.subscription.getPortal())) return false;
		if(StringUtils.isEmpty(this.subscription.getURI())) return false;
		return true;
	}
	
	public class SaveAction extends AbstractAction {
		public SaveAction(String text) {
			super(text);
		}

		@Override
		public void handle(ActionEvent event) {
			Dialog d = (Dialog) event.getSource();
	        d.hide();
		}
	}
	
}
