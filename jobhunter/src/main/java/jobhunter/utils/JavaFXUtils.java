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

package jobhunter.utils;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import javafx.util.Duration;

import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some commonly used code-fragments
 */
public class JavaFXUtils {
	
	private static final Logger l = LoggerFactory.getLogger(JavaFXUtils.class);
	
	public static Boolean isDoubleClick(MouseEvent event) {
		return isLeftButton(event) && event.getClickCount() == 2;
	}
	
	public static Boolean isLeftButton(MouseEvent event) {
		return event != null && event.getButton().equals(MouseButton.PRIMARY);
	}

	public static void closeWindow(Event event) {
		getWindow(event).hide();
	}

	public static void openWebpage(String url) {
		try {
			new ProcessBuilder("x-www-browser", url).start();
		} catch (Exception e) {
			Dialogs
				.create()
				.lightweight()
				.message(e.getLocalizedMessage())
				.title("Failed to open link")
				.showException(e);
		}
	}
	
	public static Window getWindow(Event event) {
		return ((Node) event.getSource()).getScene().getWindow();
	}
	
	public static Optional<Parent> loadFXML(Initializable clazz, String path, ResourceBundle bundle) {
		FXMLLoader fxmlLoader = new FXMLLoader(clazz.getClass().getResource(path));
		fxmlLoader.setResources(bundle);
    	fxmlLoader.setController(clazz);
		try {
			return Optional.of((Parent)fxmlLoader.load());
		} catch (IOException e) {
			l.error("Failed to open file {}", path, e);
		}
		return Optional.empty();
	}
	
	public static void toast(final Label label, final String message) {
		label.setText(message);
		FadeTransition trans = new FadeTransition();
		trans.setDuration(Duration.seconds(3));
		trans.setFromValue(0);
		trans.setToValue(1);
		trans.setNode(label);
		trans.playFromStart();
		
		trans.setOnFinished((e) -> {
			label.setOpacity(0);
		});
	}
}
