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
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import jobhunter.gui.Localizable;
import jobhunter.models.Subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedService extends Service<Subscription> implements Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(FeedService.class);
	
	private final Subscription subscription;
	private ResourceBundle bundle;
	
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
		return new FeedTask(subscription, bundle);
	}
	
	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}

	public FeedService setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
		return this;
	}
	
	public static class FeedTask extends Task<Subscription> implements Localizable {
		
		private final Subscription subscription;
		private final ResourceBundle bundle;
		
		public FeedTask(Subscription subscription, ResourceBundle bundle) {
			super();
			this.subscription = subscription;
			this.bundle = bundle;
		}

		private void update(String message, Long position) {
			updateMessage(message);
			updateProgress(position, 3);
		}

		@Override
		protected Subscription call() throws Exception {
			update(getTranslation("message.connecting"), 1L);
			Optional<Root> rss = Client.create(subscription.getURI()).execute();
			
			if(!rss.isPresent()) failed();
			
			Channel channel = rss.get().getChannel();
			
			update(getTranslation("message.parsing.response"), 2L);
			subscription.setLastUpdate(LocalDateTime.now());
			subscription.setLink(channel.getLink());
			
			l.debug("Adding to collection");
			for(Item i : channel.getItems()){
				l.debug("Adding item {}", i.getLink());
				subscription.addItem(i);
			}
			
			update(getTranslation("message.done"), 3L);
			l.debug("Return subscription with new elements");
			return subscription;
		}

		@Override
		public ResourceBundle getBundle() {
			return bundle;
		}
		
	}

}
