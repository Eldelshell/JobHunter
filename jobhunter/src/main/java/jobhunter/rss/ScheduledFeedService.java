package jobhunter.rss;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import jobhunter.gui.Localizable;
import jobhunter.models.Subscription;
import jobhunter.persistence.SubscriptionRepository;

public class ScheduledFeedService extends ScheduledService<Void> implements Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(ScheduledFeedService.class);
	
	private ResourceBundle bundle;
	
	private final Duration delay = Duration.minutes(1);
	private final Duration period = Duration.minutes(5);
	
	private ScheduledFeedService() {
		super();
		this.setDelay(delay);
		this.setRestartOnFailure(true);
		this.setPeriod(period);
	}
	
	public static ScheduledFeedService create(){
		return new ScheduledFeedService();
	}

	@Override
	protected Task<Void> createTask() {
		return new FeedTask(this.bundle);
	}

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}

	public ScheduledFeedService setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
		return this;
	}
	
	public static class FeedTask extends Task<Void> implements Localizable {
		
		private final ResourceBundle bundle;
		
		public FeedTask(ResourceBundle bundle) {
			super();
			this.bundle = bundle;
		}

		private void update(String message, Long position) {
			updateMessage(message);
			updateProgress(position, 3);
		}

		@Override
		protected Void call() throws Exception {
			l.info("Updating feeds");
			for(Subscription subscription : SubscriptionRepository.instanceOf().getSubscriptions()){
				if(subscription.isUpdatable()){
					update(getTranslation("message.connecting"), 1L);
					Optional<Root> rss = Client.create(subscription.getURI()).execute();
					
					if(!rss.isPresent()){
						l.error("Failed to update {}", subscription.getTitle());
						continue;
					}
					
					Channel channel = rss.get().getChannel();
					
					update(getTranslation("message.parsing.response"), 2L);
					subscription.setLastUpdate(LocalDateTime.now());
					subscription.setLink(channel.getLink());
					
					l.debug("Adding to collection");
					for(Item i : channel.getItems()){
						l.debug("Adding item {}", i.getLink());
						subscription.addItem(i);
					}
					
					SubscriptionRepository.instanceOf().add(subscription);
					
					update(getTranslation("message.done"), 3L);
					l.debug("Return subscription with new elements");
				}
			}
			
			return null;
		}

		@Override
		public ResourceBundle getBundle() {
			return bundle;
		}
		
	}
	
}
