package jobhunter.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jobhunter.gui.Localizable;
import jobhunter.models.Job;
import jobhunter.models.Profile;
import jobhunter.models.SubscriptionItem;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Class to render FreeMarker templates.
 */
public class FreeMarkerRenderer implements Localizable {

	private static final Logger l = LoggerFactory.getLogger(FreeMarkerRenderer.class);
	
	private final Configuration cfg;
	
	public static FreeMarkerRenderer create() {
		return new FreeMarkerRenderer();
	}
	
	public FreeMarkerRenderer() {
		cfg = new Configuration();
		
		if(ApplicationState.isDevelopment()){
			try {
				cfg.setDirectoryForTemplateLoading(new File("src/main/resources/templates/"));
			} catch (IOException e) {
				l.error("Failed to access path", e);
			}
		}else{
			cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
		}
		
		cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
	}
	
	public Boolean export(final File output, final Profile profile) {
		l.debug("Exporting to HTML");
		final Map<String, Object> vals = getValues();
		vals.put("jobs", profile.getJobs());
		
		try(Writer io = new OutputStreamWriter(new FileOutputStream(output))){
			Template temp = cfg.getTemplate("export.ftl");
			temp.process(vals, io);
			return true;
		} catch (IOException e) {
			l.error("Failed to open template file export.ftl", e);
		} catch (TemplateException e) {
			l.error("Failed to process template", e);
		}
		
		return false;
	}
	
	public Optional<String> render(final Job job) {
		final Map<String, Object> vals = getValues();
		vals.put("job", job);
		return render("job.ftl", vals);
	}
	
	public Optional<String> render(final Profile profile) {
		final Map<String, Object> vals = getValues();
		vals.put("jobs", profile.getJobs());
		return render("export.ftl", vals);
	}
	
	public Optional<String> render(final SubscriptionItem item) {
		final Map<String, Object> vals = getValues();
		vals.put("subscription", item);
		return render("subscription.ftl", vals);
	}
	
	public static String sanitizeURL(String url) {
		if(!StringUtils.startsWith(url, "http"))
			url = "http://" + url;
		
		if(!StringUtils.endsWith(url, "/"))
			url = url + "/";
		
		return url;
	}
	
	private Optional<String> render(final String file, final Map<String, Object> vals){
		try(StringWriter io = new StringWriter()){
			Template temp = cfg.getTemplate(file);
			temp.process(vals, io);
			return Optional.of(io.toString());
		} catch (IOException e) {
			l.error("Failed to open template file {}", file, e);
		} catch (TemplateException e) {
			l.error("Failed to process template", e);
		}
		
		return Optional.empty();
	}
	
	private Map<String, Object> getValues() {
		final Map<String, Object> vals = new HashMap<>();
		vals.put("generated", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
		vals.put("label_contacts", getTranslation("label.contacts"));
		vals.put("label_back_to_top", getTranslation("label.back.to.top"));
		vals.put("label_your_job_applications", getTranslation("label.your.job.applications"));
		vals.put("label_generated_on", getTranslation("label.generated.on"));
		vals.put("label_salary", getTranslation("label.salary"));
		return vals;
	}
	
}
