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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import jobhunter.models.Contact;
import jobhunter.models.Job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller to edit a job's contacts data.
 */
public class ContactsFormController implements JobFormChild<Contact> {
	
	private static final Logger l = LoggerFactory.getLogger(ApplicationFormController.class);
	private static final String PATH = "/fxml/ContactsForm.fxml";
	
	@FXML
    private TableView<Contact> table;
	
	@FXML
    private TableColumn<Contact, String> nameColumn;

    @FXML
    private TableColumn<Contact, String> phoneColumn;

    @FXML
    private TableColumn<Contact, String> emailColumn;
    
    @FXML
    private TableColumn<Contact, String> positionColumn;
    
    private Job job;
    
    private final ResourceBundle bundle;
    
    public static ContactsFormController create(ResourceBundle bundle){
    	return new ContactsFormController(bundle);
    }
    
    private ContactsFormController(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
	}
    
    @Override
	public void initialize(URL arg0, ResourceBundle bundle) {
    	nameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("name"));
    	nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    	nameColumn.setOnEditCommit(e -> {
    		final int index = e.getTablePosition().getRow();
    		final Contact selected = e.getTableView().getItems().get(index);
    		selected.setName(e.getNewValue());
    		updateContact(selected);
    	});
    	
    	phoneColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone"));
    	phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    	phoneColumn.setOnEditCommit(e -> {
    		final int index = e.getTablePosition().getRow();
    		final Contact selected = e.getTableView().getItems().get(index);
    		selected.setPhone(e.getNewValue());
    		updateContact(selected);
    	});
    	
    	emailColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("email"));
    	emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    	emailColumn.setOnEditCommit(e -> {
    		final int index = e.getTablePosition().getRow();
    		final Contact selected = e.getTableView().getItems().get(index);
    		selected.setEmail(e.getNewValue());
    		updateContact(selected);
    	});
    	
    	positionColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("position"));
    	positionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    	positionColumn.setOnEditCommit(e -> {
    		final int index = e.getTablePosition().getRow();
    		final Contact selected = e.getTableView().getItems().get(index);
    		selected.setPosition(e.getNewValue());
    		updateContact(selected);
    	});
    	
    	table.setItems(FXCollections.observableArrayList(job.getContacts()));
	}
    
	@Override
	public String getFXMLPath() {
		return PATH;
	}

	@FXML
	void onAddRowMenuItemAction(ActionEvent e) {
		l.debug("Menu");
		
		final Contact contact = Contact.of();
		this.job.addContact(contact);
		table.getItems().add(contact);
		table.getSelectionModel().selectLast();
	}
	
	@FXML
    void onDeleteRowMenuItemAction(ActionEvent event) {
    	Contact selected = table.getSelectionModel().getSelectedItem();
    	int index = table.getSelectionModel().getSelectedIndex();
    	
    	if(selected == null) return;
    	
    	this.job.getContacts().remove(selected);
    	table.getItems().remove(index);
    }
	
	private void updateContact(final Contact contact) {
		this.job.getContacts().stream()
			.filter(c -> c.getId().equals(contact.getId()))
			.forEach(c -> c = contact);
	}

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}

	@Override
	public Job getJob() {
		return job;
	}

	public ContactsFormController setJob(Job job) {
		this.job = job;
		return this;
	}
	
}
