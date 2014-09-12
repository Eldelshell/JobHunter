package jobhunter.persistence;

import java.util.ArrayList;
import java.util.List;

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
		return subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	public Boolean add(final Subscription subs){
		l.debug("Adding subscription");
		fireEvent();
		ApplicationState.instanceOf().changesPending(true);
		
		if(this.subscriptions == null)
			this.subscriptions = new ArrayList<>();
		
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
