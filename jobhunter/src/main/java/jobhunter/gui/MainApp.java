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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jobhunter.controllers.PreferencesController;
import jobhunter.utils.ApplicationState;
import jobhunter.utils.JavaFXUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {
	
	private static final Logger l = LoggerFactory.getLogger(MainApp.class);
	
    @Override
    public void start(Stage stage) throws Exception {
    	l.debug("Running in debug {}", System.getProperty("debug"));
    	
    	PreferencesController.instanceOf().init();
    	
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"), ApplicationState.instanceOf().getBundle());
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle(ApplicationState.APP_STRING);
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/logo.png")));
        stage.setScene(scene);
        stage.show();
        
        scene.getWindow().setOnCloseRequest(e -> {
        	JavaFXUtils.confirmExit(e);
        });
        
    }
    
    @Override
	public void stop() throws Exception {
		super.stop();
		l.debug("Quitting");
	}

	/**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
