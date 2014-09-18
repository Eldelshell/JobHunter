/*
 * Copyright (C) 2014 Alejandro Ayuso
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jobhunter.rss;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import jobhunter.gui.Localizable;
import jobhunter.models.Subscription;
import jobhunter.persistence.SubscriptionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledFeedService extends ScheduledService<Integer> implements Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(ScheduledFeedService.class);
	
	private final ResourceBundle bundle;
	
	private final Duration delay = Duration.minutes(1);
	private final Duration period = Duration.minutes(30);
	
	public ScheduledFeedService(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
		this.setDelay(delay);
		this.setRestartOnFailure(true);
		this.setPeriod(period);
	}
	
	@Override
	protected Task<Integer> createTask() {
		return new FeedTask(this.bundle);
	}

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}

	public static class FeedTask extends Task<Integer> implements Localizable {
		
		private final ResourceBundle bundle;
		
		public FeedTask(ResourceBundle bundle) {
			super();
			this.bundle = bundle;
		}

		private void update(String message, int position) {
			updateMessage(message);
			updateProgress(position, SubscriptionRepository.getSubscriptions().size());
		}

		@Override
		protected Integer call() throws Exception {
			l.info("Updating feeds");
			int counter = 0;
			
			final List<Subscription> neu = new ArrayList<>();
			
			for(Subscription subscription : SubscriptionRepository.getSubscriptions()){
				l.debug("Updating subscription {}", subscription.getTitle());
				if(subscription.isUpdatable()){
					update("", ++counter);
					
					Optional<Root> rss = Client.create(subscription.getURI()).execute();
					
					if(!rss.isPresent()){
						l.error("Failed to update {}", subscription.getTitle());
						subscription.setFailed(true);
						neu.add(subscription);
						continue;
					}
					
					Channel channel = rss.get().getChannel();
					
					subscription.setLastUpdate(LocalDateTime.now())
						.setLink(channel.getLink())
						.setFailed(false);
					
					l.debug("Adding to collection");
					for(Item i : channel.getItems()){
						if(i.isValid()) {
							l.debug("Adding item {}", i.getLink());
							subscription.addItem(i);
						}
					}
					neu.add(subscription);
				}else{
					l.debug("No need to update feed {}", subscription.getTitle());
					l.debug("Last update was on {}", subscription.getLastUpdate());
				}
			}
			
			neu.forEach(s -> {
				SubscriptionRepository.add(s);
			});
			
			return counter;
		}

		@Override
		public ResourceBundle getBundle() {
			return bundle;
		}
		
	}
	
}
