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
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class FXMLController implements Initializable, Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(FXMLController.class);
	
    @FXML
    private ListView<Job> jobsList;
    
    @FXML
    private ListView<Subscription> subscriptionsList;
    
    @FXML
    private VBox feedsTableViewContainer;
    
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
    private Label statusLabel;
    
    @FXML
    private TableView<SubscriptionItem> subscriptionItemsTable;
    
    @FXML
    private TableColumn<SubscriptionItem, LocalDateTime> dateColumn;
    
    @FXML
    private TableColumn<SubscriptionItem, String> positionColumn;
    
    /**
     * Renders the HTML for the mainWebView
     */
    private WebViewRenderer webViewRenderer;
    
    /**
     * This label is used to display an error when a Subscription is
     * in failed state. Check initilize for more info
     */
    private Label feedErrorLabel = new Label();
    
    private ResourceBundle bundle;
    
    private final SubscriptionController subscriptionController;
    
    public FXMLController() {
    	this.subscriptionController = new SubscriptionController(ApplicationState.getBundle());
    }
    
    @FXML
    void newFile(ActionEvent e) {
    	if(ApplicationState.changesPending()) {
    		Action response = Dialogs.create()
    			.masthead(getTranslation("message.pending.changes"))
    			.message(getTranslation("message.save.changes"))
    			.lightweight()
    			.showConfirm();
    		
    		if (response == Dialog.Actions.YES){
    			save(e);
    		}else if (response == Dialog.Actions.CANCEL){
    			return;
    		}
    		
    	}
    	
    	PreferencesController.setLastFilePath("");
    	ProfileRepository.clear();
    	SubscriptionRepository.clear();
    	refresh();
    }

    @FXML
    void openFile(ActionEvent event) {
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
    			ProfileRepository.load(fopen.get());
    			SubscriptionRepository.load(fopen.get());
    			PreferencesController.setLastFilePath(fopen.get().getAbsolutePath());
    			refresh();
    		}
    	}
    }

    @FXML
    void save(ActionEvent event) {
    	if(PreferencesController.isLastFilePathSet()){
    		final File fout = new File(PreferencesController.getLastFilePath());
    		try {
				Persistence.save(fout);
				JavaFXUtils.toast(statusLabel, getTranslation("message.changes.saved"));
			} catch (ConcurrentModificationException e) {
				handleConcurrentFileModification(fout);
			}
    		
    	}else{
    		saveAs(event);
    	}
    }

    @FXML
    void saveAs(ActionEvent event) {
    	final Optional<File> fopen = FileChooserFactory
    			.create(bundle)
    			.saveAs(JavaFXUtils.getWindow(mainWebView));
    	
    	if(fopen.isPresent()){
    		Persistence.rewrite(fopen.get());
    		PreferencesController.setLastFilePath(fopen.get().getAbsolutePath());
    		JavaFXUtils.toast(statusLabel, getTranslation("message.changes.saved"));
    	}
    }
    
    @FXML
    void exportHTML(ActionEvent event) {
    	final Optional<File> fopen = FileChooserFactory
    			.create(bundle)
    			.exportHTML(JavaFXUtils.getWindow(mainWebView));
    	
    	if(fopen.isPresent() && HTMLRenderer.of().export(fopen.get(), ProfileRepository.getProfile())){
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
    void quit(ActionEvent event) {
    	JavaFXUtils.confirmExit(event);
    }

    @FXML
    void refresh(ActionEvent event) {
    	l.debug("onActionDeletedMenuItemHandler");
    	refresh();
    }
    
    @FXML
    void showAboutDialog(ActionEvent event) {
    	AboutDialog.create(getBundle()).show();
    }

    @FXML
    void jobsListClick(MouseEvent e){
    	Job selectedJob = jobsList.getSelectionModel().getSelectedItem();
    	if(selectedJob != null){
	    	if(JavaFXUtils.isDoubleClick(e)){
    			openJobForm(Optional.of(selectedJob));
	    	}else{
	    		webViewRenderer.render(selectedJob);
	    	}
    	}
    }
    
    @FXML
    void subscriptionsListClick(MouseEvent e){
    	if(!JavaFXUtils.isLeftButton(e)) return;
    	
    	Subscription selected = subscriptionsList.getSelectionModel().getSelectedItem();
    	if(selected != null){
    		SubscriptionRepository.findById(selected.getId())
		    	.ifPresent(sub -> {
		    		if(sub.getFailed()){
		    			if(!feedsTableViewContainer.getChildren().contains(feedErrorLabel))
		    				feedsTableViewContainer.getChildren().add(feedErrorLabel);
		    		}else{
		    			feedsTableViewContainer.getChildren().remove(feedErrorLabel);
		    		}
		    		subscriptionItemsTable.setItems(
	    				FXCollections.observableArrayList(
							sub.getSortedItems()
	    				)
					);
		    		
		    		subscriptionItemsTable.getSelectionModel().clearSelection();
    		    	subscriptionItemsTable.requestFocus();
		    		subscriptionItemsTable.getSelectionModel().selectFirst();
		    		subscriptionItemsTable.getFocusModel().focus(0);
		    		subscriptionItemsTable.scrollTo(0);
		    		subscriptionTableClick(null);
		    		
		    	});
    	}else{
    		subscriptionItemsTable.setItems(null);
    	}
    }
    
    @FXML
    void subscriptionTableClick(MouseEvent e){
    	SubscriptionItem item = subscriptionItemsTable.getSelectionModel().getSelectedItem();
    	if(item != null){
	    	webViewRenderer.render(item);
	    	item.setActive(false);
	    	UpdateableListViewSkin.cast(subscriptionsList.getSkin()).refresh();
    	}
    }
    
    @FXML
    void deleteItems(ActionEvent e){
    	subscriptionController.deleteItems(
			subscriptionItemsTable.getSelectionModel().getSelectedItems()
		);
    	refresh();
    }
    
    @FXML
    void subscriptionTableKey(KeyEvent e){
    	if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN)
    		subscriptionTableClick(null);
    }
    
    @FXML
    void subscriptionsListKey(KeyEvent e){
    	if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN)
    		subscriptionsListClick(null);
    }
    
    @FXML
    void jobsListKey(KeyEvent e){
    	if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN)
    		jobsListClick(null);
    }
    
    @FXML
    void addJob(ActionEvent e){
    	openJobForm(Optional.empty());
    }
    
    @FXML
    void addFeed(ActionEvent e){
    	subscriptionController.addFeed();
    	refresh();
    }
    
    @FXML
    void deleteFeed(ActionEvent e){
    	l.debug("Show delete dialog");
    	subscriptionController.deleteFeed(
			Optional.ofNullable(
				subscriptionsList.getSelectionModel().getSelectedItem()
			)
		);
    }
    
    @FXML
    void updateFeeds(ActionEvent e){
    	subscriptionController.updateFeeds();
    }
    
    @FXML
    void readAllFeeds(ActionEvent e){
    	subscriptionController.readAll();
    	autosave();
    	refresh();
    }
    
    @FXML
    void readAllItems(ActionEvent e){
    	subscriptionController.readAll(
			Optional.ofNullable(
				subscriptionsList.getSelectionModel().getSelectedItem()
			)
		);
    	autosave();
    	refresh();
    }
    
    @FXML
    void showPreferencesDialog(ActionEvent e) {
    	PreferencesDialog.create(getBundle()).show();
    }
    
    @FXML
    void showBugReportDialog(ActionEvent event) {
    	BugReportDialog.create().setBundle(bundle).show();
    }
    
    @FXML
    void clearData(ActionEvent e) {
    	File dataFile = new File(PreferencesController.getLastFilePath());
    	dataFile.delete();
    	PreferencesController.setLastFilePath("");
    }
    
    @FXML
    void insertRandomJob(ActionEvent e) {
    	ProfileRepository.getProfile().addJob(Random.Job());
    	refresh();
    }
    
    @FXML
    void insertRandomSubscription(ActionEvent e) {
    	SubscriptionRepository.add(Random.Subscription());
    	refresh();
    }
    
    @FXML
    void showJobs(ActionEvent e){
    	DebugDialog.create(getBundle()).show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	this.bundle = rb;
    	
    	UpdateableListViewSkin<Subscription> skin = new UpdateableListViewSkin<>(this.subscriptionsList);
    	this.subscriptionsList.setSkin(skin);
    	
    	feedErrorLabel.setText(getTranslation("message.feed.failed"));
    	feedErrorLabel.getStyleClass().add("error-label");
		
    	developmentMenu.setVisible(ApplicationState.isDebug());
    	
    	if(PreferencesController.isLastFilePathSet()){
    		final File fout = new File(PreferencesController.getLastFilePath());
    		ProfileRepository.load(fout);
    		SubscriptionRepository.load(fout);
    	}else{
    		ProfileRepository.getProfile();
    	}
    	
    	ProfileRepository.setListener(() -> {
    		// Listen for change events in the repo and react
    		refresh();
    		autosave();
    	});
    	
    	SubscriptionRepository.setListener(() -> {
    		// Listen for change events in the repo and react
    		refresh();
    		autosave();
    	});
    	
    	subscriptionController.setOnUpdate(e -> {
    		l.debug("Subscription Controller onUpdate");
    		JavaFXUtils.toast(statusLabel, getTranslation("message.all.feeds.updated"));
    		autosave();
    	});
    	
    	jobsList.setCellFactory(new JobCell.JobCellCallback());
    	subscriptionsList.setCellFactory(new SubscriptionListCell.CellCallback());
    	
    	// Initialize the table
    	dateColumn.setCellValueFactory(SubscriptionRow.DATE_VALUE);
    	dateColumn.setCellFactory(SubscriptionRow::getCellFactory);
    	positionColumn.setCellValueFactory(SubscriptionRow.POSITION_VALUE);
    	subscriptionItemsTable.setRowFactory(SubscriptionRow::create);
    	subscriptionItemsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
		this.webViewRenderer = new WebViewRenderer(mainWebView);
		WebViewOnClickListener.set(mainWebView);
		
		// Load Plugins
    	loadPlugIns(null);
    	
    	refresh();
    }
    
	private void openJobForm(final Optional<Job> job) {
		JobFormController
			.create(bundle)
			.setJob(job.orElse(null))
			.show();
	}
	
	private void refresh() {
		l.debug("Refreshing View");
		mainWebView.getEngine().loadContent("");
		refreshJobsListView();
		refreshSubscriptionsListView();
	}
	
	private void autosave() {
		l.debug("Autosaving");
		if(!PreferencesController.isAutosave()) return;
		if(!ApplicationState.changesPending()) return;
		if(StringUtils.isEmpty(PreferencesController.getLastFilePath())) return;
		save(null);
	}

	@Override
	public ResourceBundle getBundle() {
		return this.bundle;
	}
	
	private void refreshJobsListView() {
		List<Job> jobs;
		if(orderByRatingMenuItem.isSelected()){
			jobs = ProfileRepository.getJobsByRating(deletedMenuItem.isSelected());
		}else if(orderByActivityMenuItem.isSelected()){
			jobs = ProfileRepository.getJobsByActivity(deletedMenuItem.isSelected());
		}else if(orderByStatusMenuItem.isSelected()){
			jobs = ProfileRepository.getJobsByStatus(deletedMenuItem.isSelected());
		}else{
			orderByDateMenuItem.setSelected(true);
			jobs = ProfileRepository.getJobsByDate(deletedMenuItem.isSelected());
		}
		
		jobsList.getSelectionModel().clearSelection();
		jobsList.getItems().clear();
		jobsList.setItems(FXCollections.observableArrayList(jobs));
	}
	
	private void refreshSubscriptionsListView() {
		subscriptionsList.getItems().clear();
		subscriptionsList.setItems(FXCollections.observableArrayList(
			SubscriptionRepository.getSubscriptions()
		));
		subscriptionsListClick(null);
	}
	
	private void loadPlugIns(ActionEvent e) {
    	l.debug("Loading plugins");
    	
    	PlugInLoader pl = new PlugInLoader();
    	
    	Dialogs.create()
    		.message(getTranslation("message.loading.plugins"))
    		.showWorkerProgress(pl);
    	
    	pl.start(importMenu);
    }
	
	private void handleConcurrentFileModification(final File file) {
		ConcurrentFileModificationDialog.Actions act = ConcurrentFileModificationDialog.create()
			.setBundle(getBundle())
			.show(file);
		
		switch(act){
		case SAVE:
			saveAs(null);
			break;
		case OVERWRITE:
			Persistence.rewrite(file);
			JavaFXUtils.toast(statusLabel, getTranslation("message.changes.saved"));
			break;
		case RELOAD:
			ProfileRepository.clear();
			SubscriptionRepository.clear();
	    	ProfileRepository.load(file);
	    	SubscriptionRepository.load(file);
    		break;
		default: break;
		}
		
	}
	
}
