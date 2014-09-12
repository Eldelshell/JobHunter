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

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jobhunter.gui.FormChangeListener;
import jobhunter.models.Company;
import jobhunter.persistence.ProfileRepository;

import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanyFormController implements JobFormChild<Company> {
	
	private static final Logger l = LoggerFactory.getLogger(CompanyFormController.class);
	private static final String PATH = "/fxml/CompanyForm.fxml";

	@FXML
    private TextField addressTextField;

    @FXML
    private TextField linkTextField;

    @FXML
    private TextField companyTextField;
    
    @FXML
    private TextArea descriptionTextArea;
    
    private Company company;
    private final ProfileRepository profileController = ProfileRepository.instanceOf();
    
    private FormChangeListener<Company> listener;
    
    private final ResourceBundle bundle;
    
    public static CompanyFormController create(ResourceBundle bundle){
    	return new CompanyFormController(bundle);
    }
    
    private CompanyFormController(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
	}
    
    @Override
	public String getFXMLPath() {
		return PATH;
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		l.debug("Initializing");
		bindEvents();
		if(this.company == null){
			initializeEmpty();
		}else{
			initializeFull();
		}
		
	}
	
	private void initializeEmpty() {
		TextFields.bindAutoCompletion(companyTextField, profileController.getAutocompleteCompanies());
	}
	
	private void initializeFull() {
		companyTextField.setText(company.getName());
		addressTextField.setText(company.getAddress());
		linkTextField.setText(company.getUrl());
	}
	
	private void bindEvents() {
		
		companyTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.company.setName(neu);
				changed();
			}
		});
		
		addressTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.company.setAddress(neu);
				changed();
			}
		});
		
		linkTextField.textProperty().addListener((obs,old,neu) -> {
			if(neu != null){
				this.company.setUrl(neu);
				changed();
			}
		});
		
	}
	
	public Company getCompany() {
		return company;
	}

	public CompanyFormController setCompany(Company company) {
		this.company = company;
		return this;
	}
	
	@Override
	public void changed(){
		if(this.listener != null)
			this.listener.changed(this.company);
	}

	@Override
	public FormChangeListener<Company> getListener() {
		return listener;
	}
	
	@Override
	public void setListener(FormChangeListener<Company> listener) {
		this.listener = listener;
	}

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}
	
}
