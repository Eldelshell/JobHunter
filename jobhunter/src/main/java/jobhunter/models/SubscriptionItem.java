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

package jobhunter.models;

import java.time.LocalDateTime;
import java.util.Comparator;

import jobhunter.persistence.ObjectId;

public class SubscriptionItem implements Comparable<SubscriptionItem>{

	private ObjectId id;
	
	private String position;

	private LocalDateTime created;

	private Boolean active = Boolean.TRUE;
	
	private String extId;

	private String portal;

	private String link;

	private String description;
	
	public static SubscriptionItem create() {
		return new SubscriptionItem().setId(new ObjectId());
	}

	public ObjectId getId() {
		return id;
	}

	public SubscriptionItem setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public SubscriptionItem setCreated(LocalDateTime created) {
		this.created = created;
		return this;
	}

	public Boolean getActive() {
		return active;
	}

	public SubscriptionItem setActive(Boolean active) {
		this.active = active;
		return this;
	}

	public String getExtId() {
		return extId;
	}

	public SubscriptionItem setExtId(String extId) {
		this.extId = extId;
		return this;
	}

	public String getPortal() {
		return portal;
	}

	public SubscriptionItem setPortal(String portal) {
		this.portal = portal;
		return this;
	}

	public String getLink() {
		return link;
	}

	public SubscriptionItem setLink(String link) {
		this.link = link;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public SubscriptionItem setDescription(String description) {
		this.description = description;
		return this;
	}
	
	public String getPosition() {
		return position;
	}

	public SubscriptionItem setPosition(String position) {
		this.position = position;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((link == null) ? 0 : link.hashCode());
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
		SubscriptionItem other = (SubscriptionItem) obj;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		return true;
	}

	@Override
	public int compareTo(SubscriptionItem o) {
		// We use the link because it's the only element we can relate
		// between an Item and a SubscriptionItem
		return this.link.compareTo(o.link);
	}
	
	public static class DateTimeComparator implements Comparator<SubscriptionItem>{

		@Override
		public int compare(SubscriptionItem o1, SubscriptionItem o2) {
			return o1.getCreated().compareTo(o2.getCreated());
		}

	}
}
