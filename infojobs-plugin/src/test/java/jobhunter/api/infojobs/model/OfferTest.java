package jobhunter.api.infojobs.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OfferTest {

	@Test
	public void parseFromJSON() {
		ObjectMapper mapper = new ObjectMapper();
		
		try(InputStream io = this.getClass().getResourceAsStream("/json/Offer.json")){
			Offer entity = mapper.readValue(io, Offer.class);
			assertNotNull("Failed to parse JSON", entity);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to parse JSON");
		}
		
	}

}
