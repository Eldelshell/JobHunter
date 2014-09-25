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

import java.io.File;
import java.util.Optional;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import jobhunter.gui.Localizable;
import jobhunter.utils.ApplicationState;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 * Factory to create different dialogs for the application.
 */
public enum DialogFactory implements Localizable {
	
	_INSTANCE;
	
	private static final ExtensionFilter JHF_FILTER = new ExtensionFilter("JobHunter (.jhf)", "*.jhf");
	private static final ExtensionFilter HTM_FILTER = new ExtensionFilter("HTML", "*.html");
	private static final ExtensionFilter PDF_FILTER = new ExtensionFilter("PDF", "*.pdf");
	
	private Boolean _delete(final String message) {
		Action deleteAction = Dialogs.create()
			.title(getTranslation(message))
			.message(getTranslation("message.confirmation"))
			.showConfirm();
		
		return deleteAction.equals(Dialog.Actions.YES);
	}
	
	private Boolean _deleteJob(final String position) {
		Action response = Dialogs.create()
		        .title(getTranslation("message.delete.job", position))
		        .lightweight()
		        .masthead(getTranslation("message.delete.job.confirmation"))
		        .message(getTranslation("message.confirmation"))
		        .showConfirm();
		return response.equals(Dialog.Actions.YES);
	}

	private Action _pendingChanges() {
		Action response = Dialogs.create()
			.masthead(getTranslation("message.pending.changes"))
			.message(getTranslation("message.save.changes"))
			.lightweight()
			.showConfirm();
		
		return response;
	}
	
	private Boolean _openFile(final String filename){
		Action response = Dialogs.create()
			.title(getTranslation("message.open.file", filename))
			.message(getTranslation("message.changes.lost"))
			.lightweight()
			.showConfirm();
		
		return response.equals(Dialog.Actions.YES);
	}
	
	private Optional<File> _open(final Window window) {
		final FileChooser fc = new FileChooser();
    	fc.setTitle(getTranslation("message.open.jhf"));
    	fc.setSelectedExtensionFilter(JHF_FILTER);
    	fc.getExtensionFilters().add(JHF_FILTER);
    	return Optional.ofNullable(fc.showOpenDialog(window));
	}
	
	private Optional<File> _saveAs(final Window window) {
		final FileChooser fc = new FileChooser();
    	fc.setTitle(getTranslation("message.save.jhf"));
    	fc.setSelectedExtensionFilter(JHF_FILTER);
    	fc.getExtensionFilters().add(JHF_FILTER);
    	return Optional.ofNullable(fc.showSaveDialog(window));
	}
	
	private Optional<File> _exportHTML(final Window window) {
		final FileChooser fc = new FileChooser();
    	fc.setTitle(getTranslation("message.export.html"));
    	fc.setSelectedExtensionFilter(HTM_FILTER);
    	fc.getExtensionFilters().add(HTM_FILTER);
    	return Optional.ofNullable(fc.showSaveDialog(window));
	}
	
	private Optional<File> _exportPDF(final Window window) {
		final FileChooser fc = new FileChooser();
    	fc.setTitle(getTranslation("message.export.pdf"));
    	fc.setSelectedExtensionFilter(PDF_FILTER);
    	fc.getExtensionFilters().add(PDF_FILTER);
    	return Optional.ofNullable(fc.showSaveDialog(window));
	}
	
	private Action _quit(final Window window){
		return Dialogs.create()
			.owner(window)
			.masthead(getTranslation("message.pending.changes"))
			.message(getTranslation("message.save.changes"))
			.lightweight()
			.showConfirm();
	}
	
	private void _about(final Window window) {
		Dialogs.create()
			.owner(window)
			.title(getTranslation("message.about"))
			.lightweight()
			.masthead(ApplicationState.APP_STRING)
			.message(getTranslation("copyright"))
			.showInformation();
	}
	
	private void _error(final String message){
		Dialogs.create().message(getTranslation(message)).showError();
	}
	
	public static Boolean deleteItems() {
		return _INSTANCE._delete("message.delete.item.confirmation");
	}
	
	public static Boolean deleteFeed() {
		return _INSTANCE._delete("menu.delete.feed");
	}
	
	public static Boolean deleteJob(final String position) {
		return _INSTANCE._deleteJob(position);
	}
	
	public static Action pendingChanges() {
		return _INSTANCE._pendingChanges();
	}
	
	public static Boolean openFile(final String filename) {
		return _INSTANCE._openFile(filename);
	}
	
	public static Optional<File> open(final Window window){
		return _INSTANCE._open(window);
	}
	
	public static Optional<File> saveAs(final Window window){
		return _INSTANCE._saveAs(window);
	}
	
	public static Optional<File> exportHTML(final Window window){
		return _INSTANCE._exportHTML(window);
	}
	
	public static Optional<File> exportPDF(final Window window){
		return _INSTANCE._exportPDF(window);
	}
	
	public static void error(final String message){
		_INSTANCE._error(message);
	}
	
	public static Action quit(final Window window){
		return _INSTANCE._quit(window);
	}
	
	public static void about(final Window window) {
		_INSTANCE._about(window);
	}
}
