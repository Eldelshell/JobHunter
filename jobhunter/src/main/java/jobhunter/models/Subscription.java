package jobhunter.models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.TreeSet;

import jobhunter.persistence.ObjectId;
import jobhunter.rss.Item;

public class Subscription implements Comparable<Subscription> {
	
	private ObjectId id;
	
	private String title;

	private String portal;
	
	private String uri;
	
	private LocalDateTime lastUpdate;
	
	private Long ttl = 1L; //hours
	
	private Integer history = 50;
	
	private Set<SubscriptionItem> items;
	
	public static Subscription create() {
		Subscription s = new Subscription();
		s.setId(new ObjectId()).setItems(new TreeSet<>());
		return s;
	}
	
	public Boolean isUpdatable(){
		Long diff = ChronoUnit.HOURS.between(lastUpdate, LocalDateTime.now());
		return diff >= ttl;
	}
	
	public ObjectId getId() {
		return id;
	}

	public Subscription setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public String getPortal() {
		return portal;
	}

	public Subscription setPortal(String portal) {
		this.portal = portal;
		return this;
	}

	public String getURI() {
		return uri;
	}

	public Subscription setURI(String link) {
		this.uri = link;
		return this;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public Subscription setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
		return this;
	}

	public Long getTtl() {
		return ttl;
	}

	public Subscription setTtl(Long ttl) {
		this.ttl = ttl;
		return this;
	}
	
	public Integer getHistory() {
		return history;
	}

	public Subscription setHistory(Integer history) {
		this.history = history;
		return this;
	}
	
	public Set<SubscriptionItem> getItems() {
		return items;
	}

	public Subscription setItems(Set<SubscriptionItem> items) {
		this.items = items;
		return this;
	}
	
	public Subscription addItem(SubscriptionItem item) {
		if(this.items == null)
			this.items = new TreeSet<>();
		
		if(this.items.size() >= history){
			// remove the last element
		}
		
		this.items.add(item);
		return this;
	}
	
	public Subscription addItem(Item item) {
		addItem(
			SubscriptionItem
				.create()
				.setCreated(item.getPubDate())
				.setDescription(item.getDescription())
				.setLink(item.getLink())
				.setPortal("")
				.setExtId(item.getGuid())
		);
		
		return this;
	}
	
	public String getTitle() {
		return title;
	}

	public Subscription setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getUri() {
		return uri;
	}

	public Subscription setUri(String uri) {
		this.uri = uri;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result + ((portal == null) ? 0 : portal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscription other = (Subscription) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (portal == null) {
			if (other.portal != null)
				return false;
		} else if (!portal.equals(other.portal))
			return false;
		return true;
	}

	@Override
	public int compareTo(Subscription o) {
		if(o == null) throw new IllegalArgumentException("Can't be null");
		return this.id.compareTo(o.id);
	}
	
}
