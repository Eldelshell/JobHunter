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

package jobhunter.dice;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;

import jobhunter.models.Job;
import jobhunter.plugin.DicePlugin;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client extends Observable {

	private static final Logger l = LoggerFactory.getLogger(Client.class);
	
	private final String url;
	
	private Client(String url) {
		super();
		this.url = url;
	}
	
	public static Client of(final String url) {
		return new Client(url);
	}
	
	public Client observe(final Observer o) {
		addObserver(o);
		return this;
	}
	
	public Job execute() throws IOException, URISyntaxException {
		l.debug("Connecting to {}", url);
		
		update("Connecting", 1L);
		final Document doc = Jsoup.connect(url).get();
		
		update("Parsing HTML", 2L);
		final Job job = Job.of();
		job.setPortal(DicePlugin.portal);
		job.setLink(url);
		
		StringBuilder description = new StringBuilder();
		
		for(Element meta : doc.getElementsByTag("meta")){
			l.debug("Checking {}", meta.toString());
			if(meta.attr("name").equals("twitter:text:job_title"))
				job.setPosition(meta.attr("content"));
			
			if(meta.attr("name").equals("twitter:text:company"))
				job.getCompany().setName(meta.attr("content"));
			
			if(meta.attr("name").equals("twitter:text:city"))
				job.setAddress(meta.attr("content"));
			
			if(meta.attr("name").equals("twitter:text:salary"))
				job.setSalary(meta.attr("content"));
			
			if(meta.attr("name").equals("twitter:text:job_description_web")){
				description.append(StringEscapeUtils.unescapeHtml4(
					meta.attr("content")
				));
			}
			
			if(meta.attr("name").equals("twitter:text:skills")){
				description.append(StringEscapeUtils.unescapeHtml4(
					meta.attr("content")
				));
			}
		}
		
		job.setDescription(description.toString());
		
		update("Done", 3L);
		return job;
	}
	
	private void update(String message, Long position) {
		l.debug("Update with [{}, {}]", message, position);
		setChanged();
		notifyObservers(Event.of(message, position));
		clearChanged();
	}
	
	public static class Event {
		
		public final String message;
		public final Long position;
		public final Long total = 3L;
		
		private Event(String message, Long position) {
			super();
			this.message = message;
			this.position = position;
		}
		
		public static Event of(String message, Long position) {
			return new Event(message, position);
		}
		
	}
	
}
