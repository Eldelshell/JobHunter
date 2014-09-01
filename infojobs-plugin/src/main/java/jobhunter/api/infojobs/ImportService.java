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

package jobhunter.api.infojobs;

import java.util.Optional;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jobhunter.api.infojobs.model.ModelMapper;
import jobhunter.api.infojobs.model.Offer;
import jobhunter.models.Job;

public class ImportService extends Service<Job> {
	
	private final String url;
	
	public ImportService(String url) {
		super();
		this.url = url;
	}

	@Override
	protected Task<Job> createTask() {
		return new ImportTask(url);
	}
	
	static class ImportTask extends Task<Job>  {
		
		private final String url;
		
		public ImportTask(String url) {
			super();
			this.url = url;
		}

		@Override
		protected Job call() throws Exception {
			
			update("Connecting", 1L);
			Optional<Offer> offer = OfferRequest.of(url).execute();
			
			if(offer.isPresent()){
				update("Parsing response", 2L);
				Job job = ModelMapper.map(offer.get());
				
				update("Done", 3L);
				return job;
			}
			return null;
		}
		
		private void update(String message, Long position) {
			updateMessage(message);
			updateProgress(position, 5);
		}
		
	}

}
