package jobhunter.rss;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jobhunter.models.Subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedService extends Service<Subscription> {
	
	private static final Logger l = LoggerFactory.getLogger(FeedService.class);
	
	private final Subscription subscription;
	
	private FeedService(Subscription subscription) {
		super();
		this.subscription = subscription;
	}
	
	public static FeedService create(Subscription subscription){
		return new FeedService(subscription);
	}

	public Subscription getSubscription() {
		return subscription;
	}

	@Override
	protected Task<Subscription> createTask() {
		return new FeedTask(subscription);
	}
	
	static class FeedTask extends Task<Subscription> implements Observer {
		
		private final Subscription subscription;
		
		public FeedTask(Subscription subscription) {
			super();
			this.subscription = subscription;
		}

		public void update(Observable o, Object arg) {
			l.debug("Update progress");
		}

		@Override
		protected Subscription call() throws Exception {
			Optional<Root> rss = Client.create(subscription.getURI()).execute();
			
			if(!rss.isPresent()) return null; //Or something!
			
			subscription.setLastUpdate(LocalDateTime.now());
			
			Channel channel = rss.get().getChannel();
			
			l.debug("Adding to collection");
			for(Item i : channel.getItems()){
				l.debug("Adding item {}", i.getGuid());
				subscription.addItem(i);
			}
			
			l.debug("Return subscription with new elements");
			return subscription;
		}
		
	}

}
