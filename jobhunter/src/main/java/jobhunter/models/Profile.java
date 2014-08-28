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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import jobhunter.persistence.ObjectId;

public class Profile {

	private ObjectId id;
	private String positionsHistory;
	private Set<Job> jobs;
	
	public static Profile instanceOf(){
		Profile p = new Profile();
		p.id = new ObjectId();
		p.jobs = new TreeSet<Job>();
		return p;
	}
	
	public Optional<ObjectId> getId() {
		return Optional.of(id);
	}
	
	public Profile setId(ObjectId id) {
		this.id = id;
		return this;
	}
	
	public Set<Job> getJobs() {
		return jobs;
	}
	
	public Profile setJobs(Set<Job> jobs) {
		this.jobs = jobs;
		return this;
	}
	
	public Profile addJob(Job job){
		if(this.jobs == null)
			this.jobs = new HashSet<>();
		
		this.jobs.add(job);
		return this;
	}


	public Optional<String> getPositionsHistory() {
		return Optional.ofNullable(positionsHistory);
	}

	public Profile setPositionsHistory(String positionsHistory) {
		this.positionsHistory = positionsHistory;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Profile [id=").append(id).append(", positionsHistory=")
				.append(positionsHistory).append(", jobs=").append(jobs)
				.append("]");
		return builder.toString();
	}

}
