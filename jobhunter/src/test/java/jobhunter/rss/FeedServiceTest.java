package jobhunter.rss;

import static org.junit.Assert.assertNotNull;

import java.util.concurrent.ExecutionException;

import javafx.concurrent.Worker.State;
import jobhunter.gui.JavaFXThreadingRule;
import jobhunter.models.Subscription;
import jobhunter.rss.FeedService.FeedTask;

import org.junit.Rule;
import org.junit.Test;

public class FeedServiceTest {

	@Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
	
	@Test
	public void test() throws Exception {
		
		Subscription s = Subscription.create()
			.setPortal("Test")
			.setTitle("Test")
			.setURI("http://www.tecnoempleo.com/alertas-empleo-rss.php?te=Madrid&cp=,29,&co=,1,&du=,1,6,&free=,0,");
		
		FeedTask task = new FeedTask(s);
		
//		task.setOnSucceeded(e -> {
//			System.out.println("Done!");
//			try {
//				Subscription s1 = task.get();
//				assertNotNull(s1);
//				System.out.println(s1.getItems().size());
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		});
		
		task.run();
		
		Subscription s1 = task.get();
		assertNotNull(s1);
		System.out.println(s1.getItems().size());
		
		
		System.out.println("Sleeping");
		Thread.sleep(5000);
		System.out.println("Starting again");
		
		task = new FeedTask(s1);
		
		task.run();
		
		Subscription s2 = task.get();
		assertNotNull(s2);
		System.out.println(s2.getItems().size());
		
//		while(task.isRunning()){
//			
//		}
		
//		FeedService fs = FeedService.create("");
//		
//		fs.setOnSucceeded(e -> {
//			Subscription s = fs.getValue();
//			System.out.println(s.getURI());
//			assertNotNull(s);
//			System.exit(0);
//		});
//		
//		fs.setOnFailed(e -> {
//			System.out.println("FAiled");
//		});
//		
//		
//		
//		fs.start();
//		
//		while(fs.getState() != State.RUNNING){
////			System.out.println(fs.getProgress());
//		}
//		
//		fs.cancel();
	}

}
