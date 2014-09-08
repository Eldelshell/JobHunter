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
}
