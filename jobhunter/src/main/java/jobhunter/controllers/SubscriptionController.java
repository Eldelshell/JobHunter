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

package jobhunter.controllers;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import jobhunter.gui.Localizable;
import jobhunter.gui.dialog.SubscriptionForm;
import jobhunter.models.Subscription;
import jobhunter.models.SubscriptionItem;
import jobhunter.persistence.SubscriptionRepository;
import jobhunter.rss.FeedService;
import jobhunter.rss.ScheduledFeedService;
import jobhunter.utils.ApplicationState;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SubscriptionController implements Localizable {
	
	private static final Logger l = LoggerFactory.getLogger(SubscriptionController.class);
	
	private final ResourceBundle bundle;
	private final ScheduledFeedService feedService;
	
	private EventHandler<ActionEvent> onUpdate;
	
	public SubscriptionController(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
		feedService = new ScheduledFeedService(bundle);
		feedService.start();
	}

	public void addFeed() {
		l.debug("Opening Subscription Dialog");
    	SubscriptionForm dialog = SubscriptionForm.create(getBundle())
			.setSubscription(Subscription.create());
    	
    	Optional<Action> action = dialog.show();
    	
    	if(action.isPresent() && action.get() != Dialog.Actions.CANCEL) {
    		l.debug("Got response from dialog");
    		Subscription sub = dialog.getSubscription();
    		SubscriptionRepository.instanceOf().add(sub);
			
    		l.debug("Updating the new feed");
    		updateFeeds();
    	}
    	
	}
	
	public void updateFeeds() {
		FeedService fs = new FeedService(getBundle());
    	
    	fs.setOnFailed(err -> {
    		Dialogs.create()
				.message(getTranslation("message.updating.feed.failed"))
				.showException(err.getSource().getException());
    	});
    	
    	fs.setOnSucceeded(ev -> {
    		this.onUpdate.handle(new ActionEvent(null, null));
    	});
    	
    	Dialogs.create()
    		.message(getTranslation("message.updating.feed"))
    		.showWorkerProgress(fs);
    	
    	fs.start();
	}
	
	public void deleteFeed() {
		SubscriptionRepository repo = SubscriptionRepository.instanceOf();
		
    	Dialogs.create()
			.lightweight()
			.title(getTranslation("menu.delete.feed"))
			.message(getTranslation("message.select.feed.delete"))
			.showChoices(
				repo.getSubscriptions()
					.stream()
					.map(sub -> sub.getTitle())
					.collect(Collectors.toList())
			).ifPresent(response -> {
				l.debug("Delete {}", response);
				repo.findByTitle(response)
					.ifPresent(repo::delete);
			});
    }
	
	public void readAll() {
		l.debug("Marking all feeds as read");
		SubscriptionRepository repo = SubscriptionRepository.instanceOf();
		for(Subscription s : repo.getSubscriptions()) {
			for(SubscriptionItem item : s.getItems()) {
				if(item.getActive())
					item.setActive(Boolean.FALSE);
			}
		}
		ApplicationState.instanceOf().changesPending(true);
	}

	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}
	
	public SubscriptionController setOnUpdate(EventHandler<ActionEvent> event) {
		this.onUpdate = (event);
		return this;
	}
	
}
