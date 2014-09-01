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

package jobhunter.infoempleo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;

import jobhunter.models.Job;
import jobhunter.plugin.InfoEmpleoWebPlugin;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.utils.URLEncodedUtils;
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
		
		job.setPortal(InfoEmpleoWebPlugin.portal);
		job.setLink(url);
		job.setExtId("");
		
		final StringBuilder description = new StringBuilder();
		
		doc.getElementsByClass("linea").forEach(td -> {
			td.getElementsByTag("p").forEach(p -> {
				description
					.append(StringEscapeUtils.unescapeHtml4(p.html()))
					.append(System.lineSeparator());
			});
		});
		
		job.setDescription(description.toString());
		
		job.setPosition(
			doc.getElementById("ctl00_CPH_Body_Link_Subtitulo").attr("title")
		);
		
		job.getCompany().setName(getCompany(doc));
		
		final String href = doc.getElementById("ctl00_CPH_Body_lnkEnviarAmigoI").attr("href");
		
		final String extId = URLEncodedUtils.parse(new URI(href), "UTF-8")
			.stream()
			.filter(nvp -> nvp.getName().equalsIgnoreCase("Id_Oferta"))
			.findFirst().get()
			.getValue();
		
		job.setExtId(extId);
		
		update("Done", 3L);
		return job;
	}
	
	private void update(String message, Long position) {
		l.debug("Update with [{}, {}]", message, position);
		setChanged();
		notifyObservers(Event.of(message, position));
		clearChanged();
	}
	
	private String getCompany(final Document doc) {
		Element el = doc.getElementById("ctl00_CPH_Body_Logo_Empresa");
		return el != null ? el.attr("title") : "";
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
