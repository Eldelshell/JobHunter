/*
 * Copyright (C) 2014-2015 Alejandro Ayuso and Alison C.
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

package jobhunter.craigslist;

import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jobhunter.models.Job;

public class ImportService extends Service<Job> {

private final String url;

	private static final Logger l = LoggerFactory.getLogger(ImportService.class);
	
	public ImportService(String url) {
		super();
		this.url = url;
	}

	@Override
	protected Task<Job> createTask() {
		return new ImportTask(url);
	}
	
	static class ImportTask extends Task<Job> implements Observer {
		
		private final String url;
		
		public ImportTask(String url) {
			super();
			this.url = url;
		}

		@Override
		protected Job call() throws Exception {
			try{
				return Client.of(url).observe(this).execute();
			}catch(Exception e) {
				throw new CraigslistAPIException("Failed to import");
			}
		}
		
		@Override
		public void update(Observable o, Object arg) {
			l.debug("Update progress");
			Client.Event event = (Client.Event) arg;
			updateMessage(event.message);
			updateProgress(event.position, event.total);
		}
		
	}
	
}
