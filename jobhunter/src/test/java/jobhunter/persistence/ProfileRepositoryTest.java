package jobhunter.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import jobhunter.models.Job;
import jobhunter.models.Profile;
import jobhunter.utils.Random;

import org.junit.Test;

public class ProfileRepositoryTest {

	@Test
	public void getActiveJobs() {
		Profile prof = Random.Profile();
		ProfileRepository.setProfile(prof);
		
		//Test that all are actives
		ProfileRepository.getActiveJobs().forEach(job -> {
			assertTrue(job.getActive());
		});
		
		// Test that the order is always the same
		List<Job> j1 = ProfileRepository.getActiveJobs();
		List<Job> j2 = ProfileRepository.getActiveJobs();
		
		assertEquals(j1.get(0), j2.get(0));
		assertEquals(j1.get(j1.size() - 1), j2.get(j2.size() - 1));
	}
	
	@Test
	public void getAllJobs() {
		Profile prof = Random.Profile();
		ProfileRepository.setProfile(prof);
		
		//Test that at least we have one inactive
		Long inactives = ProfileRepository.getAllJobs().stream()
				.filter(job -> !job.getActive()).count();

		assertTrue(inactives > 0);
		
		// Test that the order is always the same
		List<Job> j1 = ProfileRepository.getAllJobs();
		List<Job> j2 = ProfileRepository.getAllJobs();
		
		assertEquals(j1.get(0), j2.get(0));
		assertEquals(j1.get(j1.size() - 1), j2.get(j2.size() - 1));
	}
	
	@Test
	public void getAllJobsNewestFirst() {
		Profile prof = Random.Profile();
		ProfileRepository.setProfile(prof);
		
		List<Job> j1 = ProfileRepository.getJobsByDate(true);
		
		Job first = j1.get(0);
		Job last = j1.get(j1.size() - 1);
		
		assertTrue(first.getCreated().isAfter(last.getCreated()));
	}
	
	@Test
	public void getActiveJobsNewestFirst() {
		Profile prof = Random.Profile();
		ProfileRepository.setProfile(prof);
		
		List<Job> j1 = ProfileRepository.getJobsByDate(false);
		
		Job first = j1.get(0);
		Job last = j1.get(j1.size() - 1);
		
		assertTrue(first.getCreated().isAfter(last.getCreated()));
	}
	
	@Test
	public void getActiveHighestRatedFirst() {
		Profile prof = Random.Profile();
		ProfileRepository.setProfile(prof);
		
		List<Job> j1 = ProfileRepository.getJobsByRating(false);
		
		Job first = j1.get(0);
		Job last = j1.get(j1.size() - 1);
		
		assertTrue(first.getRating() > last.getRating());
	}
	
	@Test
	public void getAllHighestRatedFirst() {
		Profile prof = Random.Profile();
		ProfileRepository.setProfile(prof);
		
		List<Job> j1 = ProfileRepository.getJobsByRating(true);
		
		Job first = j1.get(0);
		Job last = j1.get(j1.size() - 1);
		
		assertTrue(first.getRating() > last.getRating());
	}
	
	@Test
	public void getMoreActiveFirst() {
		Profile prof = Random.Profile();
		ProfileRepository.setProfile(prof);
		
		List<Job> j1 = ProfileRepository.getJobsByActivity(true);
		
		Job first = j1.get(0);
		Job last = j1.get(j1.size() - 1);
		
		assertTrue(first.getLogs().size() >= last.getLogs().size());
	}

}
