package jobhunter.monster;

import java.io.IOException;
import java.net.URISyntaxException;

import jobhunter.models.Job;
import jobhunter.plugin.MonsterPlugin;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

	private static final Logger l = LoggerFactory.getLogger(Client.class);
	
	public static Job get(final String url) throws IOException, URISyntaxException {
		l.debug("Connecting to {}", url);
		
		final Document doc = Jsoup.connect(url).get();
		
		final Job job = Job.of();
		job.setPortal(MonsterPlugin.portal);
		job.setLink(url);
		
		final Element form = doc.getElementById("forApply");
		
		job.setPosition(form.getElementById("jobPosition").attr("value"));
		job.setAddress(form.getElementById("jobLocation").attr("value"));
		job.setExtId(form.getElementById("jobID").attr("value"));
		job.getCompany().setName(form.getElementById("jobCompany").attr("value"));
		
		job.setDescription(
			StringEscapeUtils.unescapeHtml4(
				doc.getElementById("TrackingJobBody").html()
			)
		);
		
		return job;
	}
	
}
