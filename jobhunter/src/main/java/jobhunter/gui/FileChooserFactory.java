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
import java.util.Optional;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class FileChooserFactory {

	private static final ExtensionFilter JHF_FILTER = new ExtensionFilter("JobHunter (.jhf)", "*.jhf");
	private static final ExtensionFilter HTM_FILTER = new ExtensionFilter("HTML", "*.html");
	private static final ExtensionFilter PDF_FILTER = new ExtensionFilter("PDF", "*.pdf");
	
	public static Optional<File> open(final Window window) {
		final FileChooser fc = new FileChooser();
    	fc.setTitle("Open JobHunter File");
    	fc.setSelectedExtensionFilter(JHF_FILTER);
    	fc.getExtensionFilters().add(JHF_FILTER);
    	return Optional.ofNullable(fc.showOpenDialog(window));
	}
	
	public static Optional<File> saveAs(final Window window) {
		final FileChooser fc = new FileChooser();
    	fc.setTitle("Save JobHunter File");
    	fc.setSelectedExtensionFilter(JHF_FILTER);
    	fc.getExtensionFilters().add(JHF_FILTER);
    	return Optional.ofNullable(fc.showSaveDialog(window));
	}
	
	public static Optional<File> exportHTML(final Window window) {
		final FileChooser fc = new FileChooser();
    	fc.setTitle("Export as HTML");
    	fc.setSelectedExtensionFilter(HTM_FILTER);
    	fc.getExtensionFilters().add(HTM_FILTER);
    	return Optional.ofNullable(fc.showSaveDialog(window));
	}
	
	public static Optional<File> exportPDF(final Window window) {
		final FileChooser fc = new FileChooser();
    	fc.setTitle("Export as PDF");
    	fc.setSelectedExtensionFilter(PDF_FILTER);
    	fc.getExtensionFilters().add(PDF_FILTER);
    	return Optional.ofNullable(fc.showSaveDialog(window));
	}
	
}
