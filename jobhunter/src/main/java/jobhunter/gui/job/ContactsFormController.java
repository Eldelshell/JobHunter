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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import jobhunter.gui.FormChangeListener;
import jobhunter.models.Contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactsFormController implements Initializable, JobFormChild<Contact> {
	
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
    private ContextMenu tableMenu;
    
    private Set<Contact> contacts;
    
    private FormChangeListener<Contact> listener;
    
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
    	
    	table.setItems(FXCollections.observableArrayList(contacts));
	}
    
    public static ContactsFormController of(final Set<Contact> contacts) {
    	ContactsFormController ctrl = new ContactsFormController();
    	ctrl.setContacts(contacts);
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
	
	@FXML
	void onAddRowMenuItemAction(ActionEvent e) {
		l.debug("Menu");
		
		final Contact contact = Contact.of();
		this.contacts.add(contact);
		table.getItems().add(contact);
		table.getSelectionModel().selectLast();
	}
	
	private void updateContact(final Contact contact) {
		this.contacts.stream()
			.filter(c -> c.getId().equals(contact.getId()))
			.forEach(c -> c = contact);
	}

	@Override
	public void changed() {
		
	}

	@Override
	public FormChangeListener<Contact> getListener() {
		return this.listener;
	}

	@Override
	public void setListener(FormChangeListener<Contact> listener) {
		this.listener = listener;
	}

	public Set<Contact> getContacts() {
		return contacts;
	}

	public ContactsFormController setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
		return this;
	}
	
}
