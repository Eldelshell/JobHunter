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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jobhunter.persistence.ObjectId;

import com.sun.xml.internal.ws.util.StringUtils;

/**
 * Model to describe an event that happens to a job application
 */
public class ActivityLog implements Comparable<ActivityLog> {
	
	public enum Type {
		OPEN,
		APPLIED,
		REJECTED,
		INTERVIEWING,
		NEGOTIATING,
		UNINTERESTED,
		CLOSED,
		CALL,
		INTERVIEW,
		OFFER;
		
		public static List<String> asList() {
			return Arrays.asList(Type.values())
				.stream()
				.map(Type::capitalize)
				.collect(Collectors.toList());
		}
		
		public String capitalize() {
			return StringUtils.capitalize(this.name().toLowerCase());
		}
	}
	
	private ObjectId 		id;
	private LocalDate		created;
	private Type			type;
	private String			description;
	
	public static ActivityLog of() {
		return new ActivityLog().setId(new ObjectId());
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
		ActivityLog other = (ActivityLog) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActivityLog [id=").append(id).append(", created=")
				.append(created).append(", Type=").append(type)
				.append(", description=").append(description).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(ActivityLog o) {
		return this.id.compareTo(o.id);
	}

	public ObjectId getId() {
		return id;
	}

	public ActivityLog setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public LocalDate getCreated() {
		return created;
	}
	
	public ActivityLog setCreated(LocalDate created) {
		this.created = created;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public ActivityLog setDescription(String description) {
		this.description = description;
		return this;
	}

	public Type getType() {
		return type;
	}

	public ActivityLog setType(Type type) {
		this.type = type;
		return this;
	}
	
	//Used by ActivityLogController
	
	public String getEvent() {
		return type.capitalize();
	}
	
	public String getDate() {
		return this.created.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

}
