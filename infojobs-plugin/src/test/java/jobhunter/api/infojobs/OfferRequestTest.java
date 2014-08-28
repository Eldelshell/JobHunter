package jobhunter.api.infojobs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

public class OfferRequestTest {

	@Test
	public void of() {
		List<NameValuePair> valids = Arrays.asList(new BasicNameValuePair[]{
			new BasicNameValuePair("191f3a359148338652264596490a9c","https://www.infojobs.net/madrid/jefe-proyecto-desarrollo-web-con-java/of-i191f3a359148338652264596490a9c"),
			new BasicNameValuePair("191f3a359148338652264596490a9c", "https://www.infojobs.net/ver-oferta-inscripcion.xhtml?dgv=2647582832808061917&of_codigo=191f3a359148338652264596490a9c"),
			new BasicNameValuePair("e258722fe746dd97a3762545bc2046", "https://www.infojobs.net/enviar.empleo/administrador-oracle/e258722fe746dd97a3762545bc2046")
		});
		
		valids.forEach(nvp -> testValidURL(nvp.getValue(), nvp.getName()));
	}
	
	private void testValidURL(final String url, final String expected) {
		OfferRequest or;
		try {
			or = OfferRequest.of(url);
			assertNotNull(or);
			assertTrue(
				String.format("%s doesnt match %s", or.getKey(), expected), 
				or.getKey().equals(expected)
			);
		} catch (InfoJobsAPIException e) {
			e.printStackTrace();
		}
		
	}
	

}
