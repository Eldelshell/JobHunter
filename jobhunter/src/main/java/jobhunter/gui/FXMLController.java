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

package jobhunter.gui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import jobhunter.controllers.PreferencesController;
import jobhunter.gui.dialog.AboutDialog;
import jobhunter.gui.dialog.DebugDialog;
import jobhunter.gui.dialog.PreferencesDialog;
import jobhunter.gui.job.JobFormController;
import jobhunter.models.Job;
import jobhunter.persistence.ProfileRepository;
import jobhunter.plugins.PlugIn;
import jobhunter.plugins.PlugInLoader;
import jobhunter.utils.ApplicationState;
import jobhunter.utils.HTMLRenderer;
import jobhunter.utils.JavaFXUtils;
import jobhunter.utils.Random;
import jobhunter.utils.WebViewRenderer;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FXMLController implements Initializable, Observer {
	
	private static final Logger l = LoggerFactory.getLogger(FXMLController.class);
	
    @FXML
    private ListView<Job> jobsListView;
    
    @FXML
    private MenuItem addJobButton;
    
    @FXML
    private MenuItem saveAsMenuItem;

    @FXML
    private MenuItem openMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private MenuItem saveMenuItem;

    @FXML
    private RadioMenuItem deletedMenuItem;

    @FXML
    private MenuItem quitMenuItem;
    
    @FXML
    private MenuItem insertRandomJobMenuItem;
    
    @FXML
    private VBox mainContainer;
    
    @FXML
    private WebView mainWebView;
    
    @FXML
    private Menu developmentMenu;
    
    @FXML
    private Menu importMenu;
    
    @FXML
    private ComboBox<String> sortCombo;
    
    @FXML
    private Label statusLabel;
    
    private FadeTransition mainWebViewFadeTransition;
    
    private ResourceBundle bundle;
    
    private ObservableList<Job> jobs;
    
    private final ProfileRepository profileController;
    private final PreferencesController preferencesController;
    private final ApplicationState state;
    
    public FXMLController() {
    	this.profileController = ProfileRepository.instanceOf();
    	
    	profileController.setListener(() -> {
    		refresh();
    	});
    	
    	this.preferencesController = PreferencesController.instanceOf();
    	this.state = ApplicationState.instanceOf();
    }
    
    @FXML
    void onActionNewMenuItemHandler(ActionEvent e) {
    	if(state.changesPending()) {
    		Action response = Dialogs.create()
    			.masthead("There are pending changes")
    			.message("Do you want to save your changes?")
    			.lightweight()
    			.showConfirm();
    		
    		if (response == Dialog.Actions.YES){
    			onActionSaveMenuItemHandler(e);
    		}else if (response == Dialog.Actions.CANCEL){
    			return;
    		}
    		
    	}
    	
    	preferencesController.setLastFilePath("");
    	profileController.clear();
    	refresh();
    }

    @FXML
    void onActionOpenMenuItemHandler(ActionEvent event) {
    	Optional<File> fopen = FileChooserFactory.open(JavaFXUtils.getWindow(mainWebView));
    	
    	if(fopen.isPresent()) {
    		Action response = Dialogs.create()
    			.title("Open file " + fopen.get().getName())
    			.message("All changes will be lost. Do you want to continue?")
    			.lightweight()
    			.showConfirm();
    		
    		if (response == Dialog.Actions.YES) {
    			profileController.load(fopen.get());
    			preferencesController.setLastFilePath(fopen.get().getAbsolutePath());
    			refresh();
    		}
    	}
    }

    @FXML
    void onActionSaveMenuItemHandler(ActionEvent event) {
    	if(preferencesController.isLastFilePathSet()){
    		final File fout = new File(preferencesController.getLastFilePath());
    		profileController.save(fout);
    		JavaFXUtils.toast(statusLabel, "Changes saved");
    	}else{
    		onActionSaveAsMenuItemHandler(event);
    	}
    }

    @FXML
    void onActionSaveAsMenuItemHandler(ActionEvent event) {
    	final Optional<File> fopen = FileChooserFactory.saveAs(JavaFXUtils.getWindow(mainWebView));
    	if(fopen.isPresent()){
    		profileController.save(fopen.get());
    		preferencesController.setLastFilePath(fopen.get().getAbsolutePath());
    		JavaFXUtils.toast(statusLabel, "Changes saved");
    	}
    }
    
    @FXML
    void onExportHTML(ActionEvent event) {
    	final Optional<File> fopen = FileChooserFactory.exportHTML(JavaFXUtils.getWindow(mainWebView));
    	
    	if(fopen.isPresent() && HTMLRenderer.of().export(fopen.get(), profileController.getProfile())){
    		JavaFXUtils.toast(statusLabel, "Exported to HTML");
    	}else{
    		Dialogs
    			.create()
    			.lightweight()
    			.message("Failed to export to HTML")
    			.showError();
    	}
    	
    }

    @FXML
    void onActionQuitMenuItemHandler(ActionEvent event) {
    	JavaFXUtils.confirmExit(event);
    }

    @FXML
    void onActionDeletedMenuItemHandler(ActionEvent event) {
    	l.debug("onActionDeletedMenuItemHandler");
    	refresh();
    }

    @FXML
    void onActionAboutMenuItemHandler(ActionEvent event) {
    	AboutDialog.show();
    }

    @FXML
    void jobListViewOnMouseClickedHandler(MouseEvent e){
    	ListView<?> parent = (ListView<?>) e.getSource();
    	Job selectedJob = (Job) parent.getSelectionModel().getSelectedItem();
    	if(selectedJob != null){
	    	if(JavaFXUtils.isDoubleClick(e)){
    			openJobForm(Optional.of(selectedJob));
	    	}else{
	    		WebViewRenderer.render(mainWebView, selectedJob);
	    		mainWebViewFadeTransition.playFromStart();
	    	}
    	}
    }
    
    @FXML
    void addJobButtonActionHandler(ActionEvent e){
    	openJobForm(Optional.empty());
    }
    
	@FXML
	@SuppressWarnings("unchecked")
    void onLoadPlugIns(ActionEvent e) {
    	l.debug("Loading plugins");
    	
    	PlugInLoader pl = new PlugInLoader();
    	
    	pl.setOnFailed(event -> {
    		l.error("Failed to load plugins", event.getSource().getException());
    	});
    	
    	pl.setOnSucceeded(event -> {
    		List<PlugIn> plugins = (List<PlugIn>) event.getSource().getValue();
    		plugins.forEach(plugin -> {
    			l.debug("Adding Plugin {} to menu", plugin.getPortal());
    			MenuItem item = plugin.getMenuItem();
    			importMenu.getItems().add(item);
    		});
    	});
    	
    	Dialogs.create().message("Loading Plugins").showWorkerProgress(pl);
    	
    	pl.start();
    }
    
    @FXML
    void onActionPreferencesMenuItemHandler(ActionEvent e) {
    	PreferencesDialog.show(bundle);
    }
    
    @FXML
    void onActionClearDataMenuItemHandler(ActionEvent e) {
    	File dataFile = new File(preferencesController.getLastFilePath());
    	dataFile.delete();
    	preferencesController.setLastFilePath("");
    }
    
    @FXML
    void onInsertRandomJob(ActionEvent e) {
    	profileController.getProfile().addJob(Random.Job());
    	refresh();
    }
    
    @FXML
    void onShowJobs(ActionEvent e){
    	DebugDialog.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	this.bundle = rb;
    	
		developmentMenu.setVisible(state.isDebug());
    	
    	if(preferencesController.isLastFilePathSet()){
    		final File fout = new File(preferencesController.getLastFilePath());
    		profileController.load(fout);
    	}else{
    		profileController.getProfile();
    	}
    	
    	jobs = FXCollections.observableArrayList(profileController.getActiveJobs());
    	jobsListView.setCellFactory(new JobCell.JobCellCallback());
    	jobsListView.setItems(jobs);
    	
    	mainWebViewFadeTransition = new FadeTransition();
		mainWebViewFadeTransition.setDuration(Duration.millis(300));
		mainWebViewFadeTransition.setFromValue(0);
		mainWebViewFadeTransition.setToValue(1);
		mainWebViewFadeTransition.setNode(mainWebView);
		
		WebViewOnClickListener.set(mainWebView);
		
		// Load Plugins
    	onLoadPlugIns(null);
    }
    
	@Override
	public void update(Observable o, Object arg) {
		l.debug("Update");
		refresh();
	}
	
	private void openJobForm(final Optional<Job> job) {
		JobFormController
			.of(job)
			.setObserver(this)
			.load();
	}
	
	private void refresh() {
		mainWebView.getEngine().loadContent("");
		if(deletedMenuItem.isSelected()){
			jobs = FXCollections.observableArrayList(profileController.getAllJobs());
    	}else{
    		jobs = FXCollections.observableArrayList(profileController.getActiveJobs());
    	}
		jobsListView.getSelectionModel().clearSelection();
    	jobsListView.setItems(jobs);
	}

}
