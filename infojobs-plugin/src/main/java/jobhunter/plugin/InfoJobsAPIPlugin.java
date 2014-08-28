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
import jobhunter.api.infojobs.ImportService;
import jobhunter.models.Job;
import jobhunter.persistence.ProfileRepository;
import jobhunter.plugins.PlugIn;

import org.controlsfx.dialog.Dialogs;

public class InfoJobsAPIPlugin implements PlugIn {

	@Override
	public String getPortal() {
		return "InfoJobs API";
	}

	@Override
	public Worker<Job> getService(final String url) {
		return new ImportService(url);
	}

	@Override
	public MenuItem getMenuItem() {
		MenuItem item = new MenuItem("InfoJobs");
		item.setOnAction(new MenuItemHandler());
		return item;
	}
	
	static class MenuItemHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent arg0) {
			Optional<String> result = Dialogs
	    		.create()
	    		.masthead("Add job from InfoJobs")
	    		.message("Copy and paste the URL")
	    		.showTextInput();
			
			if(result.isPresent()){
				ImportService s = new ImportService(result.get());
	    		
	    		s.setOnSucceeded((event) -> {
	    			Job job = (Job) event.getSource().getValue();
	    			ProfileRepository.instanceOf().saveJob(job);
	    		});
	    		
	    		s.setOnFailed((event) -> {
	    			Dialogs
						.create()
						.masthead("Failed to import from InfoJobs")
						.message(event.getSource().getException().getMessage())
						.showException(event.getSource().getException());
	    		});
	    		
	    		Dialogs.create().message("Importing...").showWorkerProgress(s);
	    		s.start();
			}
		}
		
	}

}
