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

	@Deprecated
	public static ProfileRepository instanceOf() {
		return _INSTANCE;
	}

	private Profile current;
	
	private ProfileRepositoryListener listener;

	private void saveJob(final Job job) {
		l.debug("Saving Job: ");
		l.debug(job.toString());
		current.addJob(job);
		fireEvent();
		ApplicationState.instanceOf().changesPending(true);
	}

	private void deleteJob(final Job job) {
		current.getJobs()
			.stream()
			.filter(j -> j.equals(job))
			.forEach(j -> j.setActive(Boolean.FALSE));
		fireEvent();
		ApplicationState.instanceOf().changesPending(true);
		
	}
	
	private List<Job> getJobsBy(Boolean all, Comparator<Job> comparator){
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
	
	private Profile _getProfile() {
		if (current == null)
			current = Profile.instanceOf();
		return current;
	}
	
	private void _load(final File file) {
		Optional<Profile> profile = Persistence.readProfile(file);
		this.current = profile.orElse(Profile.instanceOf());
		fireEvent();
	}
	
	private void _clear() {
		current = Profile.instanceOf();
		fireEvent();
	}
	
	private void fireEvent() {
		if(this.listener != null)
			this.listener.changed();
	}
	
	// Static methods
	
	public static void save(final Job job){
		_INSTANCE.saveJob(job);
	}
	
	public static void delete(final Job job){
		_INSTANCE.deleteJob(job);
	}

	public static Optional<Job> getJob(final ObjectId id) {
		return _INSTANCE.current.getJobs()
				.stream()
				.filter(j -> j.getId().equals(id))
				.findFirst();
	}

	public static List<Job> getActiveJobs() {
		return _INSTANCE.current.getJobs()
				.stream()
				.filter(j -> j.getActive())
				.sorted()
				.collect(Collectors.toList());
	}
	
	public static List<Job> getAllJobs() {
		return _INSTANCE.current.getJobs()
				.stream()
				.sorted()
				.collect(Collectors.toList());
	}
	
	public static List<Job> getJobsByDate(Boolean all) {
		return _INSTANCE.getJobsBy(all, new Order.CreatedComparator(Order.DESCENDING));
	}
	
	public static List<Job> getJobsByRating(Boolean all) {
		return _INSTANCE.getJobsBy(all, new Order.RatingComparator(Order.DESCENDING));
	}
	
	public static List<Job> getJobsByActivity(Boolean all) {
		return _INSTANCE.getJobsBy(all, new Order.ActivityComparator(Order.DESCENDING));
	}
	
	public static List<Job> getJobsByStatus(Boolean all) {
		return _INSTANCE.getJobsBy(all, new Order.StatusComparator(Order.DESCENDING));
	}
	
	public static Profile getProfile() {
		return _INSTANCE._getProfile();
	}

	public static void clear() {
		_INSTANCE._clear();
	}

	public static Set<String> getAutocompletePositions() {
		Set<String> positions = new HashSet<>();
		getProfile().getJobs().forEach(job -> positions.add(job.getPosition()));
		return positions;
	}

	public static Set<String> getAutocompleteCompanies() {
		Set<String> companies = new HashSet<>();
		getProfile().getJobs().forEach(j -> companies.add(j.getCompany().getName()));
		return companies;
	}

	public static void load(final File file) {
		_INSTANCE._load(file);
	}

	public static ProfileRepositoryListener getListener() {
		return _INSTANCE.listener;
	}

	public static void setListener(ProfileRepositoryListener listener) {
		_INSTANCE.listener = listener;
	}
	
	public static void setProfile(Profile profile) {
		_INSTANCE.current = profile;
	}

}
