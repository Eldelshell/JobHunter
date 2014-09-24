package jobhunter.utils;

import jobhunter.models.Job;
import jobhunter.models.Profile;
import jobhunter.models.SubscriptionItem;

import org.junit.Test;

public class FreeMarkerRendererTest {

	@Test
	public void renderJob() {
		FreeMarkerRenderer r = new FreeMarkerRenderer();
		Job j = Random.Job();
		r.render(j).orElseThrow(RuntimeException::new);
	}
	
	@Test
	public void renderProfile() {
		FreeMarkerRenderer r = new FreeMarkerRenderer();
		Profile j = Random.Profile();
		r.render(j).orElseThrow(RuntimeException::new);
	}
	
	@Test
	public void renderSubscription() {
		FreeMarkerRenderer r = new FreeMarkerRenderer();
		SubscriptionItem j = Random.SubscriptionItem();
		r.render(j).orElseThrow(RuntimeException::new);
	}

}
