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

package jobhunter.plugin;

import java.util.Optional;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import jobhunter.infoempleo.ImportService;
import jobhunter.models.Job;
import jobhunter.persistence.ProfileRepository;
import jobhunter.plugins.PlugIn;

import org.controlsfx.dialog.Dialogs;

public class InfoEmpleoWebPlugin implements PlugIn {
	
	public static final String portal = "InfoEmpleo";

	@Override
	public String getPortal() {
		return portal;
	}

	@Override
	public Worker<Job> getService(String url) {
		return new ImportService(url);
	}

	@Override
	public MenuItem getMenuItem() {
		MenuItem item = new MenuItem(portal);
		item.setOnAction(new MenuItemHandler());
		return item;
	}
	
	static class MenuItemHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			Optional<String> result = Dialogs
	    		.create()
	    		.title("Add job from " + portal)
	    		.masthead("Copy and paste the URL")
	    		.message("URL (with http)")
	    		.lightweight()
	    		.showTextInput();
			
			if(result.isPresent()){
				ImportService s = new ImportService(result.get());
	    		
	    		s.setOnSucceeded((event) -> {
	    			Job job = (Job) event.getSource().getValue();
	    			ProfileRepository.save(job);
	    		});
	    		
	    		s.setOnFailed((event) -> {
	    			Dialogs
						.create()
						.masthead("Failed to import from " + portal)
						.message(event.getSource().getException().getMessage())
						.showException(event.getSource().getException());
	    		});
	    		
	    		Dialogs.create().message("Importing...").lightweight().showWorkerProgress(s);
	    		s.start();
			}
		}
		
	}

}
