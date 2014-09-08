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

package jobhunter.persistence;

import java.io.File;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jobhunter.models.Job;
import jobhunter.models.Order;
import jobhunter.models.Profile;
import jobhunter.utils.ApplicationState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ProfileRepository {

	_INSTANCE;

	private static final Logger l = LoggerFactory.getLogger(ProfileRepository.class);

	public static ProfileRepository instanceOf() {
		return _INSTANCE;
	}

	private Profile current;
	
	private ProfileRepositoryListener listener;

	public void saveJob(final Job job) {
		l.debug("Saving Job: ");
		l.debug(job.toString());
		current.addJob(job);
		fireEvent();
		ApplicationState.instanceOf().changesPending(true);
	}

	public void deleteJob(final Job job) {
		current.getJobs()
			.stream()
			.filter(j -> j.equals(job))
			.forEach(j -> j.setActive(Boolean.FALSE));
		fireEvent();
		ApplicationState.instanceOf().changesPending(true);
		
	}

	public Optional<Job> getJob(final ObjectId id) {
		return current.getJobs()
				.stream()
				.filter(j -> j.getId().equals(id))
				.findFirst();
	}

	public List<Job> getActiveJobs() {
		return current.getJobs()
				.stream()
				.filter(j -> j.getActive())
				.sorted()
				.collect(Collectors.toList());
	}
	
	public List<Job> getAllJobs() {
		return current.getJobs()
				.stream()
				.sorted()
				.collect(Collectors.toList());
	}
	
	public List<Job> getJobsByDate(Boolean all) {
		return getJobsBy(all, new Order.CreatedComparator(Order.DESCENDING));
	}
	
	public List<Job> getJobsByRating(Boolean all) {
		return getJobsBy(all, new Order.RatingComparator(Order.DESCENDING));
	}
	
	public List<Job> getJobsByActivity(Boolean all) {
		return getJobsBy(all, new Order.ActivityComparator(Order.DESCENDING));
	}
	
	public List<Job> getJobsBy(Boolean all, Comparator<Job> comparator){
		if(all){
			return current.getJobs()
				.stream()
				.sorted(comparator)
				.collect(Collectors.toList());
		}else{
			return current.getJobs()
				.stream()
				.filter(j -> j.getActive())
				.sorted(comparator)
				.collect(Collectors.toList());
		}
	}

	public Profile getProfile() {
		if (current == null)
			current = Profile.instanceOf();
		return current;
	}

	public void clear() {
		current = Profile.instanceOf();
		fireEvent();
	}

	public Set<String> getAutocompletePositions() {
		Set<String> positions = new HashSet<>();
		getProfile().getJobs().forEach(job -> positions.add(job.getPosition()));
		return positions;
	}

	public Set<String> getAutocompleteCompanies() {
		Set<String> companies = new HashSet<>();
		getProfile().getJobs().forEach(j -> companies.add(j.getCompany().getName()));
		return companies;
	}

	public void load(final File file) {
		Optional<Profile> profile = Persistence.read(file);

		if (profile.isPresent()){
			this.current = profile.get();
		}else{
			current = Profile.instanceOf();
		}
		fireEvent();
	}

	public void save(final File file) {
		Persistence.save(current, file);
		ApplicationState.instanceOf().changesPending(false);
	}
	
	private void fireEvent() {
		if(this.listener != null)
			this.listener.changed();
	}

	public ProfileRepositoryListener getListener() {
		return listener;
	}

	public void setListener(ProfileRepositoryListener listener) {
		this.listener = listener;
	}
	
	public void setProfile(Profile profile) {
		this.current = profile;
	}

}
