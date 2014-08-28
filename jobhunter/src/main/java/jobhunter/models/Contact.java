/*
 * Copyright (C) 2014 Alejandro Ayuso
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import jobhunter.persistence.ObjectId;

/**
 * @author eldelshell
 */
public class Contact implements Comparable<Contact>{
    
	private ObjectId	id;
    private String		name;
    private String		email;
    private String		phone;
    private String		position;
    
    public static Contact of() {
    	return new Contact()
    		.setId(new ObjectId());
    }
    
    public ObjectId getId() {
		return id;
	}

	public Contact setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public String getName() {
        return name;
    }

    public Contact setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Contact setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Contact setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getPosition() {
        return position;
    }

    public Contact setPosition(String position) {
        this.position = position;
        return this;
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
		Contact other = (Contact) obj;
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
		builder.append("Contact [id=").append(id).append(", name=")
				.append(name).append(", email=").append(email)
				.append(", phone=").append(phone).append(", position=")
				.append(position).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Contact o) {
		return this.id.compareTo(o.getId());
	}
	
	public static class NameComparator implements Comparator<Contact> {

		@Override
		public int compare(Contact o1, Contact o2) {
			return o1.name.compareTo(o2.name);
		}
		
	}
    
}
