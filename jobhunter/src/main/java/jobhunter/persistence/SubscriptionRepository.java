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
import jobhunter.utils.ApplicationState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SubscriptionRepository {

	_INSTANCE;

	private static final Logger l = LoggerFactory.getLogger(SubscriptionRepository.class);
	
	private SubscriptionRepositoryListener listener;

	public static SubscriptionRepository instanceOf() {
		return _INSTANCE;
	}
	
	private List<Subscription> subscriptions;

	public List<Subscription> getSubscriptions() {
		if(this.subscriptions == null)
			this.subscriptions = new ArrayList<>();
		return subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	public void add(final Subscription subs){
		l.debug("Adding subscription");
		this.getSubscriptions().add(subs);
		fireEvent();
		ApplicationState.instanceOf().changesPending(true);
	}
	
	public void delete(final Subscription subs){
		l.debug("Removing subscription");
		this.getSubscriptions().remove(subs);
		fireEvent();
		ApplicationState.instanceOf().changesPending(true);
	}
	
	public Optional<Subscription> findByTitle(final String title) {
		return getSubscriptions()
				.stream()
				.filter(sub -> sub.getTitle().equals(title))
				.findFirst();
	}
	
	public void load(final File file) {
		Optional<List<Subscription>> profile = Persistence.readSubscriptions(file);
		this.subscriptions = profile.orElse(new ArrayList<>());
		fireEvent();
	}
	
	private void fireEvent() {
		if(this.listener != null)
			this.listener.changed();
	}

	public SubscriptionRepositoryListener getListener() {
		return listener;
	}

	public SubscriptionRepository setListener(SubscriptionRepositoryListener listener) {
		this.listener = listener;
		return this;
	}
	
}
