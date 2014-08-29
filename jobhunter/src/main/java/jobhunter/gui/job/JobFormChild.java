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
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import jobhunter.gui.FormChangeListener;

public interface JobFormChild<T> {
	
	static final Logger l = LoggerFactory.getLogger(JobFormChild.class);

	abstract void changed();
	
	FormChangeListener<T> getListener();
	
	void setListener(FormChangeListener<T> listener);
	
	String getFXMLPath();
	
	default Optional<Parent> load() {
		FXMLLoader fxmlLoader = new FXMLLoader(JobFormChild.class.getResource(getFXMLPath()));
    	fxmlLoader.setController(this);
		try {
			return Optional.of((Parent)fxmlLoader.load());
		} catch (IOException e) {
			l.error("Failed to open file {}", getFXMLPath(), e);
		}
		return Optional.empty();
	}
	
}
