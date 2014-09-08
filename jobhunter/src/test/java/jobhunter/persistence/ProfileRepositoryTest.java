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
		ProfileRepository.instanceOf().setProfile(prof);
		
		//Test that all are actives
		ProfileRepository.instanceOf().getActiveJobs().forEach(job -> {
			assertTrue(job.getActive());
		});
		
		// Test that the order is always the same
		List<Job> j1 = ProfileRepository.instanceOf().getActiveJobs();
		List<Job> j2 = ProfileRepository.instanceOf().getActiveJobs();
		
		assertEquals(j1.get(0), j2.get(0));
		assertEquals(j1.get(j1.size() - 1), j2.get(j2.size() - 1));
	}
	
	@Test
	public void getAllJobs() {
		Profile prof = Random.Profile();
		ProfileRepository.instanceOf().setProfile(prof);
		
		//Test that at least we have one inactive
		Long inactives = ProfileRepository.instanceOf().getAllJobs().stream()
				.filter(job -> !job.getActive()).count();

		assertTrue(inactives > 0);
		
		// Test that the order is always the same
		List<Job> j1 = ProfileRepository.instanceOf().getAllJobs();
		List<Job> j2 = ProfileRepository.instanceOf().getAllJobs();
		
		assertEquals(j1.get(0), j2.get(0));
		assertEquals(j1.get(j1.size() - 1), j2.get(j2.size() - 1));
	}
	
	@Test
	public void getAllJobsNewestFirst() {
		Profile prof = Random.Profile();
		ProfileRepository.instanceOf().setProfile(prof);
		
		List<Job> j1 = ProfileRepository.instanceOf().getJobsByDate(true);
		
		Job first = j1.get(0);
		Job last = j1.get(j1.size() - 1);
		
		assertTrue(first.getCreated().isAfter(last.getCreated()));
	}
	
	@Test
	public void getActiveJobsNewestFirst() {
		Profile prof = Random.Profile();
		ProfileRepository.instanceOf().setProfile(prof);
		
		List<Job> j1 = ProfileRepository.instanceOf().getJobsByDate(false);
		
		Job first = j1.get(0);
		Job last = j1.get(j1.size() - 1);
		
		assertTrue(first.getCreated().isAfter(last.getCreated()));
	}
	
	@Test
	public void getActiveHighestRatedFirst() {
		Profile prof = Random.Profile();
		ProfileRepository.instanceOf().setProfile(prof);
		
		List<Job> j1 = ProfileRepository.instanceOf().getJobsByRating(false);
		
		Job first = j1.get(0);
		Job last = j1.get(j1.size() - 1);
		
		assertTrue(first.getRating() > last.getRating());
	}
	
	@Test
	public void getAllHighestRatedFirst() {
		Profile prof = Random.Profile();
		ProfileRepository.instanceOf().setProfile(prof);
		
		List<Job> j1 = ProfileRepository.instanceOf().getJobsByRating(true);
		
		Job first = j1.get(0);
		Job last = j1.get(j1.size() - 1);
		
		assertTrue(first.getRating() > last.getRating());
	}
	
	@Test
	public void getMoreActiveFirst() {
		Profile prof = Random.Profile();
		ProfileRepository.instanceOf().setProfile(prof);
		
		List<Job> j1 = ProfileRepository.instanceOf().getJobsByActivity(true);
		
		Job first = j1.get(0);
		Job last = j1.get(j1.size() - 1);
		
		assertTrue(first.getLogs().size() >= last.getLogs().size());
	}

}
