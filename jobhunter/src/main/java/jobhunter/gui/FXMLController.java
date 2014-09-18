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
import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import jobhunter.controllers.PreferencesController;
import jobhunter.controllers.SubscriptionController;
import jobhunter.gui.dialog.AboutDialog;
import jobhunter.gui.dialog.BugReportDialog;
import jobhunter.gui.dialog.ConcurrentFileModificationDialog;
import jobhunter.gui.dialog.DebugDialog;
import jobhunter.gui.dialog.PreferencesDialog;
import jobhunter.gui.job.JobFormController;
import jobhunter.models.Job;
import jobhunter.models.Subscription;
import jobhunter.models.SubscriptionItem;
import jobhunter.persistence.Persistence;
import jobhunter.persistence.ProfileRepository;
import jobhunter.persistence.SubscriptionRepository;
import jobhunter.plugins.PlugInLoader;
import jobhunter.utils.ApplicationState;
import jobhunter.utils.HTMLRenderer;
import jobhunter.utils.JavaFXUtils;
import jobhunter.utils.Random;
import jobhunter.utils.WebViewRenderer;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FXMLController implements Initializable, Observer, Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(FXMLController.class);
	
    @FXML
    private ListView<Job> jobsListView;
    
    @FXML
    private ListView<Subscription> feedListView;
    
    @FXML
    private VBox feedsTableViewContainer;
    
    @FXML
    private CheckMenuItem autoSaveMenuItem;

    @FXML
    private RadioMenuItem deletedMenuItem;
    
    @FXML
    private RadioMenuItem orderByDateMenuItem;
    
    @FXML
    private RadioMenuItem orderByRatingMenuItem;
    
    @FXML
    private RadioMenuItem orderByActivityMenuItem;
    
    @FXML
    private RadioMenuItem orderByStatusMenuItem;

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
    
    @FXML
    private TableView<SubscriptionItem> subscriptionTable;
    
    @FXML
    private TableColumn<SubscriptionItem, LocalDateTime> dateColumn;
    
    @FXML
    private TableColumn<SubscriptionItem, String> positionColumn;
    
    private WebViewRenderer webViewRenderer;
    
    /**
     * This label is used to display an error when a Subscription is
     * in failed state. Check initilize for more info
     */
    private Label feedErrorLabel = new Label();
    
    private ResourceBundle bundle;
    
    private final ProfileRepository profileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionController subscriptionController;
    private final PreferencesController preferencesController;
    private final ApplicationState state;
    
    public FXMLController() {
    	this.profileRepository = ProfileRepository.instanceOf();
    	this.subscriptionRepository = SubscriptionRepository.instanceOf();
    	this.preferencesController = PreferencesController.instanceOf();
    	this.state = ApplicationState.instanceOf();
    	this.subscriptionController = new SubscriptionController(this.state.getBundle());
    }
    
    @FXML
    void onActionNewMenuItemHandler(ActionEvent e) {
    	if(state.changesPending()) {
    		Action response = Dialogs.create()
    			.masthead(getTranslation("message.pending.changes"))
    			.message(getTranslation("message.save.changes"))
    			.lightweight()
    			.showConfirm();
    		
    		if (response == Dialog.Actions.YES){
    			onActionSaveMenuItemHandler(e);
    		}else if (response == Dialog.Actions.CANCEL){
    			return;
    		}
    		
    	}
    	
    	preferencesController.setLastFilePath("");
    	profileRepository.clear();
    	subscriptionRepository.clear();
    	refresh();
    }

    @FXML
    void onActionOpenMenuItemHandler(ActionEvent event) {
    	Optional<File> fopen = FileChooserFactory
    			.create(bundle)
    			.open(JavaFXUtils.getWindow(mainWebView));
    	
    	if(fopen.isPresent()) {
    		Action response = Dialogs.create()
    			.title(getTranslation("message.open.file", fopen.get().getName()))
    			.message(getTranslation("message.changes.lost"))
    			.lightweight()
    			.showConfirm();
    		
    		if (response == Dialog.Actions.YES) {
    			profileRepository.load(fopen.get());
    			subscriptionRepository.load(fopen.get());
    			preferencesController.setLastFilePath(fopen.get().getAbsolutePath());
    			refresh();
    		}
    	}
    }

    @FXML
    void onActionSaveMenuItemHandler(ActionEvent event) {
    	if(preferencesController.isLastFilePathSet()){
    		final File fout = new File(preferencesController.getLastFilePath());
    		try {
				Persistence.save(fout);
				JavaFXUtils.toast(statusLabel, getTranslation("message.changes.saved"));
			} catch (ConcurrentModificationException e) {
				handleConcurrentFileModification(fout);
			}
    		
    	}else{
    		onActionSaveAsMenuItemHandler(event);
    	}
    }

    @FXML
    void onActionSaveAsMenuItemHandler(ActionEvent event) {
    	final Optional<File> fopen = FileChooserFactory
    			.create(bundle)
    			.saveAs(JavaFXUtils.getWindow(mainWebView));
    	
    	if(fopen.isPresent()){
    		Persistence.rewrite(fopen.get());
    		preferencesController.setLastFilePath(fopen.get().getAbsolutePath());
    		JavaFXUtils.toast(statusLabel, getTranslation("message.changes.saved"));
    	}
    }
    
    @FXML
    void onAutoSaveAction(ActionEvent event) {
    	// If a user selects this, a save is expected, right?
    	autosave();
    	preferencesController.setAutosave(autoSaveMenuItem.isSelected());
    }
    
    @FXML
    void onExportHTML(ActionEvent event) {
    	final Optional<File> fopen = FileChooserFactory
    			.create(bundle)
    			.exportHTML(JavaFXUtils.getWindow(mainWebView));
    	
    	if(fopen.isPresent() && HTMLRenderer.of().export(fopen.get(), profileRepository.getProfile())){
    		JavaFXUtils.toast(statusLabel, getTranslation("message.exported.html"));
    	}else{
    		Dialogs
    			.create()
    			.lightweight()
    			.message(getTranslation("message.failed.export.html"))
    			.showError();
    	}
    	
    }

    @FXML
    void onActionQuitMenuItemHandler(ActionEvent event) {
    	JavaFXUtils.confirmExit(event);
    }

    @FXML
    void onRefreshAction(ActionEvent event) {
    	l.debug("onActionDeletedMenuItemHandler");
    	refresh();
    }
    
    @FXML
    void onActionAboutMenuItemHandler(ActionEvent event) {
    	AboutDialog.create(getBundle()).show();
    }

    @FXML
    void jobListViewOnMouseClickedHandler(MouseEvent e){
    	Job selectedJob = jobsListView.getSelectionModel().getSelectedItem();
    	if(selectedJob != null){
	    	if(JavaFXUtils.isDoubleClick(e)){
    			openJobForm(Optional.of(selectedJob));
	    	}else{
	    		webViewRenderer.render(selectedJob);
	    	}
    	}
    }
    
    @FXML
    void feedListViewOnMouseClickedHandler(MouseEvent e){
    	Subscription selected = feedListView.getSelectionModel().getSelectedItem();
    	if(selected != null){
	    	subscriptionRepository.findById(selected.getId())
		    	.ifPresent(sub -> {
		    		if(sub.getFailed()){
		    			if(!feedsTableViewContainer.getChildren().contains(feedErrorLabel))
		    				feedsTableViewContainer.getChildren().add(feedErrorLabel);
		    		}else{
		    			feedsTableViewContainer.getChildren().remove(feedErrorLabel);
		    		}
		    		subscriptionTable.setItems(
	    				FXCollections.observableArrayList(
							sub.getSortedItems()
	    				)
					);
		    		
		    		subscriptionTable.getSelectionModel().clearSelection();
    		    	subscriptionTable.requestFocus();
		    		subscriptionTable.getSelectionModel().selectFirst();
		    		subscriptionTable.getFocusModel().focus(0);
		    		subscriptionTableOnClick(null);
		    	});
    	}else{
    		subscriptionTable.setItems(null);
    	}
    }
    
    @FXML
    void subscriptionTableOnClick(MouseEvent e){
    	SubscriptionItem item = subscriptionTable.getSelectionModel().getSelectedItem();
    	if(item != null){
	    	webViewRenderer.render(item);
	    	item.setActive(false);
    	}
    }
    
    @FXML
    void deleteItems(ActionEvent e){
    	subscriptionController.deleteItems(
			subscriptionTable.getSelectionModel().getSelectedItems()
		);
    	refresh();
    }
    
    @FXML
    void subscriptionTableOnKeyPress(KeyEvent e){
    	if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN)
    		subscriptionTableOnClick(null);
    }
    
    @FXML
    void feedListViewOnKeyPress(KeyEvent e){
    	if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN)
    		feedListViewOnMouseClickedHandler(null);
    }
    
    @FXML
    void jobsListViewOnKeyPress(KeyEvent e){
    	if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN)
    		jobListViewOnMouseClickedHandler(null);
    }
    
    @FXML
    void addJobButtonActionHandler(ActionEvent e){
    	openJobForm(Optional.empty());
    }
    
    @FXML
    void addFeedHandler(ActionEvent e){
    	subscriptionController.addFeed();
    	refresh();
    }
    
    @FXML
    void deleteFeedHandler(ActionEvent e){
    	l.debug("Deleting selected items");
    	subscriptionController.deleteFeed();
    	refresh();
    }
    
    @FXML
    void updateFeedHandler(ActionEvent e){
    	subscriptionController.updateFeeds();
    }
    
    @FXML
    void readAllFeedHandler(ActionEvent e){
    	subscriptionController.readAll();
    	autosave();
    	refresh();
    }
    
	@FXML
    void onLoadPlugIns(ActionEvent e) {
    	l.debug("Loading plugins");
    	
    	PlugInLoader pl = new PlugInLoader();
    	
    	Dialogs.create()
    		.message(getTranslation("message.loading.plugins"))
    		.showWorkerProgress(pl);
    	
    	pl.start(importMenu);
    }
    
    @FXML
    void onActionPreferencesMenuItemHandler(ActionEvent e) {
    	PreferencesDialog.create(getBundle()).show();
    }
    
    @FXML
    void onActionReportBugMenuItemHandler(ActionEvent event) {
    	BugReportDialog.create().setBundle(bundle).show();
    }
    
    @FXML
    void onActionClearDataMenuItemHandler(ActionEvent e) {
    	File dataFile = new File(preferencesController.getLastFilePath());
    	dataFile.delete();
    	preferencesController.setLastFilePath("");
    }
    
    @FXML
    void onInsertRandomJob(ActionEvent e) {
    	profileRepository.getProfile().addJob(Random.Job());
    	refresh();
    }
    
    @FXML
    void onInsertRandomSubscription(ActionEvent e) {
    	subscriptionRepository.add(Random.Subscription());
    	refresh();
    }
    
    @FXML
    void onShowJobs(ActionEvent e){
    	DebugDialog.create(getBundle()).show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	this.bundle = rb;
    	
    	feedErrorLabel.setText(getTranslation("message.feed.failed"));
    	feedErrorLabel.getStyleClass().add("error-label");
		
    	developmentMenu.setVisible(state.isDebug());
    	
    	if(preferencesController.isLastFilePathSet()){
    		final File fout = new File(preferencesController.getLastFilePath());
    		profileRepository.load(fout);
    		subscriptionRepository.load(fout);
    	}else{
    		profileRepository.getProfile();
    	}
    	
    	profileRepository.setListener(() -> {
    		refresh();
    	});
    	
    	subscriptionRepository.setListener(() -> {
    		refresh();
    	});
    	
    	subscriptionController.setOnUpdate(e -> {
    		l.debug("Subscription Controller onUpdate");
    		JavaFXUtils.toast(statusLabel, getTranslation("message.all.feeds.updated"));
    		autosave();
    	});
    	
    	autoSaveMenuItem.setSelected(preferencesController.isAutosave());
    	
    	jobsListView.setCellFactory(new JobCell.JobCellCallback());
    	feedListView.setCellFactory(new SubscriptionListCell.CellCallback());
    	
    	// Initialize the table
    	dateColumn.setCellValueFactory(SubscriptionRow.DATE_VALUE);
    	dateColumn.setCellFactory(SubscriptionRow::getCellFactory);
    	positionColumn.setCellValueFactory(SubscriptionRow.POSITION_VALUE);
    	subscriptionTable.setRowFactory(SubscriptionRow::create);
    	subscriptionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
		this.webViewRenderer = new WebViewRenderer(mainWebView);
		WebViewOnClickListener.set(mainWebView);
		
		// Load Plugins
    	onLoadPlugIns(null);
    	
    	refresh();
    }
    
	@Override
	public void update(Observable o, Object arg) {
		l.debug("Update");
		refresh();
		autosave();
	}
	
	private void openJobForm(final Optional<Job> job) {
		JobFormController
			.create(bundle)
			.setJob(job.orElse(null))
			.setObserver(this)
			.show();
	}
	
	private void refresh() {
		l.debug("Refreshing View");
		mainWebView.getEngine().loadContent("");
		jobsListView.getSelectionModel().clearSelection();
    	jobsListView.setItems(FXCollections.observableArrayList(getJobs()));
    	feedListView.setItems(getSubscriptions());
    	feedListViewOnMouseClickedHandler(null);
	}
	
	private void autosave() {
		if(!autoSaveMenuItem.isSelected()) return;
		if(!state.changesPending()) return;
		if(StringUtils.isEmpty(preferencesController.getLastFilePath())) return;
		l.debug("Autosaving");
		onActionSaveMenuItemHandler(null);
	}

	@Override
	public ResourceBundle getBundle() {
		return this.bundle;
	}
	
	private List<Job> getJobs() {
		if(orderByRatingMenuItem.isSelected()){
			return profileRepository.getJobsByRating(deletedMenuItem.isSelected());
		}else if(orderByActivityMenuItem.isSelected()){
			return profileRepository.getJobsByActivity(deletedMenuItem.isSelected());
		}else if(orderByStatusMenuItem.isSelected()){
			return profileRepository.getJobsByStatus(deletedMenuItem.isSelected());
		}else{
			orderByDateMenuItem.setSelected(true);
			return profileRepository.getJobsByDate(deletedMenuItem.isSelected());
		}
	}
	
	private ObservableList<Subscription> getSubscriptions() {
		l.debug("Getting subscriptions");
		return FXCollections.observableArrayList(
			subscriptionRepository.getSubscriptions()
		);
	}
	
	private void handleConcurrentFileModification(final File file) {
		ConcurrentFileModificationDialog.Actions act = ConcurrentFileModificationDialog.create()
			.setBundle(getBundle())
			.show(file);
		
		switch(act){
		case SAVE:
			onActionSaveAsMenuItemHandler(null);
			break;
		case OVERWRITE:
			Persistence.rewrite(file);
			JavaFXUtils.toast(statusLabel, getTranslation("message.changes.saved"));
			break;
		case RELOAD:
			profileRepository.clear();
	    	subscriptionRepository.clear();
	    	profileRepository.load(file);
    		subscriptionRepository.load(file);
    		break;
		default: break;
		}
		
	}
	
}
