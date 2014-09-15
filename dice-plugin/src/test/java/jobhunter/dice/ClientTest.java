package jobhunter.dice;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import jobhunter.models.Job;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ClientTest {

//	@Test
	public void test() throws IOException, URISyntaxException {
		Job job = Client.of("http://www.dice.com/jobsearch/servlet/JobSearch?op=302&dockey=xml/3/9/39f6fa1e18518cdaab618966ec53b0cb@endecaindex&source=19&FREE_TEXT=&rating=&src=19").execute();
		assertNotNull(job);
//		assertEquals("JHL7YS5Y2BZCP2TKT1X", job.getExtId());
		assertTrue(StringUtils.isNotEmpty(job.getDescription()));
		System.out.println(job.getDescription());
	}

}
