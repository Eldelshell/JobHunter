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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import jobhunter.persistence.ObjectId;

import com.sun.xml.internal.ws.util.StringUtils;

/**
 * @author eldelshell
 */
public class Job implements Comparable<Job>{
	
	public enum Status {
		/**
		 * A Position we haven't applied to yet.
		 */
		OPEN,
		
		/**
		 * We applied to the position.
		 */
		APPLIED,
		
		/**
		 * The poster rejected our application.
		 */
		REJECTED,
		
		/**
		 * We've been called for an interview.
		 */
		INTERVIEWING,
		
		/**
		 * We're negotiating the deal.
		 */
		NEGOTIATING,
		
		/**
		 * We don't care about this any more. (delete?)
		 */
		UNINTERESTED,
		
		/**
		 * The poster has closed the position.
		 */
		CLOSED;
		
		public static List<String> asList() {
			return Arrays.asList(Status.values())
				.stream()
				.map(Status::capitalize)
				.collect(Collectors.toList());
		}
		
		public String capitalize() {
			return StringUtils.capitalize(this.name().toLowerCase());
		}
	}
    
	private ObjectId 			id;
	private LocalDateTime		created;
	private Boolean 			active = Boolean.TRUE;
	private String				extId;
	
	private Status				status;
    private String				position;
    private String				address;
    private String				link;
    private String				description;
    private String				portal;
    private Integer				rating;
    private String				salary;
    
    private Company				company;
    private Set<Contact>		contacts;
    private Set<String>			options;
    private Set<ActivityLog>	logs;
    
    public static Job of() {
    	Job j = new Job();
    	j.id = new ObjectId();
    	j.created = LocalDateTime.now();
    	j.status = Status.OPEN;
    	j.rating = 0;
    	j.addLog(ActivityLog.of()
			.setType(ActivityLog.Type.OPEN)
			.setCreated(LocalDate.now())
		);
    	j.setCompany(Company.instanceOf());
    	
    	return j;
    }
    
	public ObjectId getId() {
		return id;
	}

	public Job setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public Job setCreated(LocalDateTime created) {
		this.created = created;
		return this;
	}

	public Boolean getActive() {
		return active;
	}

	public Job setActive(Boolean active) {
		this.active = active;
		return this;
	}
	
	public String getExtId() {
		return extId;
	}

	public Job setExtId(String extId) {
		this.extId = extId;
		return this;
	}

	public Status getStatus() {
		return status;
	}

	public Job setStatus(Status status) {
		this.status = status;
		return this;
	}

	public String getPosition() {
		return position;
	}

	public Job setPosition(String position) {
		this.position = position;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public Job setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getLink() {
		return link;
	}

	public Job setLink(String link) {
		this.link = link;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Job setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getPortal() {
		return portal;
	}

	public Job setPortal(String portal) {
		this.portal = portal;
		return this;
	}

	public Integer getRating() {
		return rating;
	}

	public Job setRating(Integer rating) {
		this.rating = rating;
		return this;
	}

	public String getSalary() {
		return salary;
	}

	public Job setSalary(String salary) {
		this.salary = salary;
		return this;
	}

	public Company getCompany() {
		return company;
	}

	public Job setCompany(Company company) {
		this.company = company;
		return this;
	}

	public Set<String> getOptions() {
		return options;
	}

	public Job setOptions(Set<String> options) {
		this.options = options;
		return this;
	}
	
	public Set<Contact> getContacts() {
		if(this.contacts == null)
			this.contacts = new TreeSet<Contact>();
		return contacts;
	}

	public Job setContacts(Set<Contact> contacts) {
		this.contacts = contacts;
		return this;
	}
	
	public Job addContact(Contact contact) {
		this.getContacts().add(contact);
		return this;
	}
	
	public Set<ActivityLog> getLogs() {
		if(this.logs == null)
			this.logs = new TreeSet<ActivityLog>();
		return logs;
	}

	public Job setLogs(Set<ActivityLog> logs) {
		this.logs = logs;
		return this;
	}
	
	public Job addLog(ActivityLog log) {
		this.getLogs().add(log);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Job [id=").append(id).append(", created=")
				.append(created).append(", active=").append(active)
				.append(", status=").append(status).append(", position=")
				.append(position).append(", address=").append(address)
				.append(", link=").append(link).append(", portal=")
				.append(portal).append(", rating=").append(rating)
				.append(", salary=").append(salary).append(", company=")
				.append(company).append(", options=").append(options)
				.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Job o) {
		return this.id.compareTo(o.id);
	}
	
	public static class DateComparator implements Comparator<Job> {

		@Override
		public int compare(Job o1, Job o2) {
			return o1.getCreated().compareTo(o2.getCreated());
		}
		
	}
	
}
