package jobhunter.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jobhunter.models.Job;

import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLRenderer {
	
	private static final Logger l = LoggerFactory.getLogger(HTMLRenderer.class);

	public static Optional<Object> render(final Job job) {
		final Map<String, String> vals = new HashMap<>();
		if(ApplicationState.instanceOf().isDevelopment()){
			vals.put("styles_path", "src/main/resources/templates/styles.html");
		}else{
			vals.put("styles_path", "templates/styles.html");
		}
		return render(job, vals);
	}
	
	public static void export(final File output, final List<Job> jobs) {
		Path path = Paths.get(output.getAbsolutePath());
		Charset charset = Charset.forName("utf-8");
		
		try (BufferedWriter writer = Files.newBufferedWriter(path, charset, StandardOpenOption.CREATE)) {
			for (Job j : jobs) {
				final Optional<Object> obj = render(j);
				
				if(obj.isPresent()){
					final String str = (String)obj.get();
					writer.write(str, 0, str.length());
					writer.newLine();
				}
			}

		} catch (IOException e) {
			l.error("Failed to save HTML file", e);
		}

	}
	
	private static Optional<Object> render(final Job job, final Map<String, String> values) {
		String path = "templates/job.html";
		
		if(ApplicationState.instanceOf().isDevelopment())
			path = "src/main/resources/templates/job.html";
		
		try(InputStream io = new FileInputStream(new File(path))){
			return Optional.of(TemplateRuntime.eval(io, job, values));
		} catch (IOException e) {
			l.error("Failed to generate HTML", e);
		}
		
		return Optional.empty();
	}
	
}
