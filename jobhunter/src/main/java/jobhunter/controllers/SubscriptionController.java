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

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import jobhunter.gui.Localizable;
import jobhunter.gui.dialog.DialogFactory;
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
	private final ActionEvent dumbEvent = new ActionEvent();
	
	private EventHandler<ActionEvent> onUpdate;
	private EventHandler<ActionEvent> onAdd;
	
	public SubscriptionController(ResourceBundle bundle) {
		super();
		this.bundle = bundle;
		feedService = new ScheduledFeedService();
		feedService.start();
		
		feedService.setOnSucceeded(ev -> {
			this.onUpdate.handle(dumbEvent);
		});
	}

	public void addFeed() {
		l.debug("Adding a new subscription feed");
		editFeed(Optional.empty());
		
//		Shall we update the feeds?
//		l.debug("Updating the new feed");
//		updateFeeds();
	}
	
	public void updateFeeds() {
		FeedService fs = new FeedService();
    	
    	fs.setOnFailed(err -> {
    		Dialogs.create()
				.message(getTranslation("message.updating.feed.failed"))
				.showException(err.getSource().getException());
    	});
    	
    	fs.setOnSucceeded(ev -> {
    		this.onUpdate.handle(dumbEvent);
    	});
    	
    	Dialogs.create()
    		.message(getTranslation("message.updating.feed"))
    		.showWorkerProgress(fs);
    	
    	fs.start();
	}
	
	public void deleteFeed() {
    	Dialogs.create()
			.lightweight()
			.title(getTranslation("menu.delete.feed"))
			.message(getTranslation("message.select.feed.delete"))
			.showChoices(
				SubscriptionRepository.getSubscriptions()
					.stream()
					.map(sub -> sub.getTitle())
					.collect(Collectors.toList())
			).ifPresent(response -> {
				l.debug("Delete {}", response);
				SubscriptionRepository
					.findByTitle(response)
					.ifPresent(SubscriptionRepository::delete);
				this.onAdd.handle(dumbEvent);
			});
    }
	
	public void deleteFeed(Optional<Subscription> feed) {
		if(feed.isPresent()){
			if(DialogFactory.deleteFeed()){
				SubscriptionRepository.delete(feed.get());
				this.onAdd.handle(dumbEvent);
			}
		}else{
			deleteFeed();
		}
		
	}
	
	public void deleteItems(ObservableList<SubscriptionItem> items) {
		if(items.isEmpty()) return;
		
		if(!DialogFactory.deleteItems()) return;
		
		SubscriptionRepository.findByItem(items.get(0)).ifPresent(sub -> {
			for(SubscriptionItem item : items){
				l.debug("Removing item {}", item.getPosition());
				sub.getItems().remove(item);
			}
		});
		
		ApplicationState.changesPending(true);
		this.onUpdate.handle(dumbEvent);
		
	}
	
	public void readAll() {
		l.debug("Marking all feeds as read");
		for(Subscription s : SubscriptionRepository.getSubscriptions()) {
			for(SubscriptionItem item : s.getItems()) {
				if(item.getActive())
					item.setActive(Boolean.FALSE);
			}
		}
		ApplicationState.changesPending(true);
		this.onUpdate.handle(dumbEvent);
	}
	
	public void readAll(final Optional<Subscription> option) {
		option.ifPresent(s -> {
			l.debug("Marking all items as read for subscription {}", s.getTitle());
			for(SubscriptionItem item : s.getItems()) {
				if(item.getActive())
					item.setActive(Boolean.FALSE);
			}
			ApplicationState.changesPending(true);
			this.onUpdate.handle(dumbEvent);
		});
	}
	
	public void editFeed(Optional<Subscription> feed) {
		SubscriptionForm dialog = SubscriptionForm.create(getBundle())
				.setSubscription(feed.orElse(Subscription.create()));
	    	
    	Optional<Action> action = dialog.show();
		if(action.isPresent() && action.get() != Dialog.Actions.CANCEL){
    		l.debug("Got response from dialog");
    		Subscription sub = dialog.getSubscription();
    		SubscriptionRepository.add(sub);
		}
		
		this.onAdd.handle(dumbEvent);
	}
	
	@Override
	public ResourceBundle getBundle() {
		return bundle;
	}
	
	public void setOnUpdate(EventHandler<ActionEvent> event) {
		this.onUpdate = (event);
	}

	public void setOnAdd(EventHandler<ActionEvent> onAdd) {
		this.onAdd = onAdd;
	}
	
}
