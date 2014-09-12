package jobhunter.persistence;

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
	
	public void save() {
		
	}
	
	public void clear() {
		
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
