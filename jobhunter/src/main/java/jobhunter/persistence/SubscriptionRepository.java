package jobhunter.persistence;

import java.util.Set;

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
	
	private Set<Subscription> subscriptions;

	public Set<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Set<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	public Boolean add(final Subscription subs){
		l.debug("Saving subscription: ");
		fireEvent();
		ApplicationState.instanceOf().changesPending(true);
		return this.subscriptions.add(subs);
	}
	
	public Boolean delete(final Subscription subs){
		fireEvent();
		ApplicationState.instanceOf().changesPending(true);
		return this.subscriptions.remove(subs);
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
