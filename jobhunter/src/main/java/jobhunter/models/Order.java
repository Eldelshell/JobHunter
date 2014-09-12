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

import java.util.Comparator;

public enum Order {
	ASCENDING, DESCENDING;
	
	
	public static abstract class AbstractComparator {
		protected final Order order;

		protected AbstractComparator(Order order) {
			super();
			this.order = order;
		}
		
	}
	
	public static class RatingComparator extends AbstractComparator implements Comparator<Job>{
		
		public RatingComparator(Order order) {
			super(order);
		}

		@Override
		public int compare(Job o1, Job o2) {
			return o1.getRating().compareTo(o2.getRating()) * (order.equals(ASCENDING) ? 1 : -1);
		}
		
	}
	
	public static class CreatedComparator extends AbstractComparator implements Comparator<Job>{
		
		public CreatedComparator(Order order) {
			super(order);
		}

		@Override
		public int compare(Job o1, Job o2) {
			return o1.getCreated().compareTo(o2.getCreated()) * (order.equals(ASCENDING) ? 1 : -1);
		}
		
	}
	
	public static class SubscriptionItemCreatedComparator extends AbstractComparator implements Comparator<SubscriptionItem>{
		
		public SubscriptionItemCreatedComparator(Order order) {
			super(order);
		}

		@Override
		public int compare(SubscriptionItem o1, SubscriptionItem o2) {
			return o1.getCreated().compareTo(o2.getCreated()) * (order.equals(ASCENDING) ? 1 : -1);
		}
		
	}
	
	public static class PositionComparator extends AbstractComparator implements Comparator<Job>{
		
		public PositionComparator(Order order) {
			super(order);
		}

		@Override
		public int compare(Job o1, Job o2) {
			return o1.getPosition().compareTo(o2.getPosition()) * (order.equals(ASCENDING) ? 1 : -1);
		}
		
	}
	
	public static class ActivityComparator extends AbstractComparator implements Comparator<Job>{
		
		public ActivityComparator(Order order) {
			super(order);
		}

		@Override
		public int compare(Job o1, Job o2) {
			return Integer.compare(o1.getLogs().size(), o2.getLogs().size()) * (order.equals(ASCENDING) ? 1 : -1);
		}
		
	}
	
	public static class StatusComparator extends AbstractComparator implements Comparator<Job>{
		
		public StatusComparator(Order order) {
			super(order);
		}

		@Override
		public int compare(Job o1, Job o2) {
			return Integer.compare(o1.getStatus().ordinal(), o2.getStatus().ordinal()) * (order.equals(ASCENDING) ? 1 : -1);
		}
		
	}
}
