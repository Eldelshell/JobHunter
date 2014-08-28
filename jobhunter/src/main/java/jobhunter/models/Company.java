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

import jobhunter.persistence.ObjectId;

/**
 * @author eldelshell
 */
public class Company {
    
	private ObjectId id;
    private String name;
    private String address;
    private String url;
    
    public static Company instanceOf() {
    	Company c = new Company();
    	c.id = new ObjectId();
    	return c;
    }
    
	public ObjectId getId() {
		return id;
	}

	public Company setId(ObjectId id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return this.name;
	}
	
	public Company setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public Company setAddress(String address) {
		this.address = address;
		return this;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public Company setUrl(String url) {
		this.url = url;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Company [id=").append(id).append(", name=")
				.append(name).append(", address=").append(address)
				.append(", url=").append(url).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
    
}
