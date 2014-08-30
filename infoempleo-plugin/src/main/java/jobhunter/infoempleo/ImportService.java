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

import java.net.URI;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jobhunter.models.Job;
import jobhunter.plugin.InfoEmpleoWebPlugin;

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
	
	static class ImportTask extends Task<Job> {
		
		private final String url;
		
		public ImportTask(String url) {
			super();
			this.url = url;
		}

		@Override
		protected Job call() throws Exception {
			try{
				final Document doc = Jsoup.connect(url).get();
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
				
				job.getCompany().setName(
					doc.getElementById("ctl00_CPH_Body_Logo_Empresa").attr("title")
				);
				
				final String href = doc.getElementById("ctl00_CPH_Body_lnkEnviarAmigoI").attr("href");
				
				final String extId = URLEncodedUtils.parse(new URI(href), "UTF-8")
					.stream()
					.filter(nvp -> nvp.getName().equalsIgnoreCase("Id_Oferta"))
					.findFirst().get()
					.getValue();
				
				job.setExtId(extId);
				return job;
			}catch(Exception e) {
				throw new InfoEmpleoAPIException("Failed to import");
			}
		}
		
	}
	
}
