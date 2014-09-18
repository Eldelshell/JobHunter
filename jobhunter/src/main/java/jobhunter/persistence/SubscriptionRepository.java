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

package jobhunter.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import jobhunter.models.Subscription;
import jobhunter.models.SubscriptionItem;
import jobhunter.utils.ApplicationState;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SubscriptionRepository {

	_INSTANCE;

	private static final Logger l = LoggerFactory.getLogger(SubscriptionRepository.class);
	
	private SubscriptionRepositoryListener listener;

	private List<Subscription> subscriptions;

	public static List<Subscription> getSubscriptions() {
		if(_INSTANCE.subscriptions == null)
			_INSTANCE.subscriptions = new ArrayList<>();
		return _INSTANCE.subscriptions;
	}

	public static void setSubscriptions(List<Subscription> subscriptions) {
		_INSTANCE.subscriptions = subscriptions;
	}
	
	public static void add(final Subscription subs){
		l.debug("Adding subscription");
		
		if(getSubscriptions().contains(subs)){
			getSubscriptions().remove(subs);
		}
		
		getSubscriptions().add(subs);
		ApplicationState.instanceOf().changesPending(true);
	}
	
	public static void delete(final Subscription subs){
		l.debug("Removing subscription");
		getSubscriptions().remove(subs);
		_INSTANCE.fireEvent();
		ApplicationState.instanceOf().changesPending(true);
	}
	
	public static Optional<Subscription> findByTitle(final String title) {
		return getSubscriptions()
			.stream()
			.filter(sub -> sub.getTitle().equals(title))
			.findFirst();
	}
	
	public static Optional<Subscription> findById(final ObjectId id) {
		return getSubscriptions()
			.stream()
			.filter(sub -> sub.getId().equals(id))
			.findFirst();
	}
	
	public static Optional<Subscription> findByItem(final SubscriptionItem item) {
		return getSubscriptions()
				.stream()
				.filter(sub -> sub.getItems().contains(item))
				.findFirst();
	}
	
	public static void load(final File file) {
		Optional<List<Subscription>> profile = Persistence.readSubscriptions(file);
		_INSTANCE.subscriptions = profile.orElse(new ArrayList<>());
		_INSTANCE.fireEvent();
	}
	
	public static void clear() {
		_INSTANCE.subscriptions = new ArrayList<>();
		_INSTANCE.fireEvent();
	}
	
	private void fireEvent() {
		if(this.listener != null)
			this.listener.changed();
	}

	public static SubscriptionRepositoryListener getListener() {
		return _INSTANCE.listener;
	}

	public static SubscriptionRepository setListener(SubscriptionRepositoryListener listener) {
		_INSTANCE.listener = listener;
		return _INSTANCE;
	}
	
}
