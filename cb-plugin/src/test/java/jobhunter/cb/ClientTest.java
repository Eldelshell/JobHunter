package jobhunter.cb;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import jobhunter.cb.Client;
import jobhunter.models.Job;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ClientTest {

//	@Test
	public void test() throws IOException, URISyntaxException {
		Job job = Client.of("http://www.careerbuilder.com/jobseeker/jobs/jobdetails.aspx?Job_DID=JHL7YS5Y2BZCP2TKT1X&showNewJDP=yes&ipath=HPRJ").execute();
		assertNotNull(job);
		assertEquals("JHL7YS5Y2BZCP2TKT1X", job.getExtId());
		assertTrue(StringUtils.isNotEmpty(job.getDescription()));
	}

}
