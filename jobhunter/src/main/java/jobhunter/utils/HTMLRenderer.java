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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jobhunter.models.Job;
import jobhunter.models.Profile;

import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLRenderer {
	
	private static final Logger l = LoggerFactory.getLogger(HTMLRenderer.class);
	
	public static HTMLRenderer of() {
		return new HTMLRenderer();
	}
	
	public Map<String, String> getValues() {
		final Map<String, String> vals = new HashMap<>();
		if(ApplicationState.instanceOf().isDevelopment()){
			vals.put("styles_path", "src/main/resources/templates/styles.html");
		}else{
			vals.put("styles_path", "templates/styles.html");
		}
		vals.put("generated", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
		return vals;
	}
	
	public Boolean export(final File output, final Profile profile) {
		l.debug("Exporting to HTML");
		
		Path path = Paths.get(output.getAbsolutePath());
		Charset charset = Charset.forName("utf-8");
		
		final Optional<Object> obj = render(profile);
		
		if(!obj.isPresent()) return false;
		
		try {
			final String str = (String)obj.get();
			Files.write(path, str.getBytes(charset));
		} catch (IOException e) {
			l.error("Failed to save HTML file", e);
			return false;
		}
		return true;

	}
	
	public Optional<Object> render(final Job job) {
		String path = "templates/job.html";
		
		if(ApplicationState.instanceOf().isDevelopment())
			path = "src/main/resources/templates/job.html";
		
		try(InputStream io = new FileInputStream(new File(path))){
			return Optional.of(TemplateRuntime.eval(io, job, getValues()));
		} catch (IOException e) {
			l.error("Failed to generate HTML", e);
		}
		
		return Optional.empty();
	}
	
	private Optional<Object> render(final Profile profile) {
		l.debug("Rendering for Export");
		String path = "templates/export.html";
		
		if(ApplicationState.instanceOf().isDevelopment())
			path = "src/main/resources/templates/export.html";
		
		try(InputStream io = new FileInputStream(new File(path))){
			return Optional.of(TemplateRuntime.eval(io, profile, getValues()));
		} catch (IOException e) {
			l.error("Failed to generate HTML", e);
		}
		
		return Optional.empty();
	}
	
}
