package org.larissashch.portfolio.persistence.repository.jdbc;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.larissashch.portfolio.persistence.repository.GoalRepository;
import org.larissashch.portfolio.goalcharger.model.entity.CategoryType;
import org.larissashch.portfolio.goalcharger.model.entity.Goal;
import org.larissashch.portfolio.goalcharger.model.entity.KeyWord;
import org.larissashch.portfolio.goalcharger.model.entity.StatusType;
import org.larissashch.portfolio.goalcharger.model.entity.Step;

public class InDBGoalRepositoryTest {
	
	//test
	private GoalRepository repository;

	public static class TestTask implements Runnable {
		private String command;
		private GoalRepository repository;
		private int index;


		public TestTask(String command, GoalRepository repository, int index) {
			this.command = command;
			this.repository = repository;
			this.index = index;


		}

		@Override
		public void run() {
			if (command == "saveGoal") {
				Goal goal = new Goal();
				goal.setGoalName("name" + index);
				goal.setDescription("description"+index);
				goal.setCategory(CategoryType.OTHER);
				goal.setStatus(StatusType.NEW);
				goal.setStartDate(new Date());
				repository.saveGoal(goal);
				System.out.println("Goal with name:"+goal.getGoalName()+" added, id:"+goal.getId());
			}
			if (command == "deleteGoal") {
				repository.deleteGoal(index);

			}
			if (command == "readGoal") {
				Goal goal;
				goal = repository.readGoal(index);
				System.out.println("Goal with name:"+goal.getGoalName()+" readed, id:"+goal.getId());
				assertEquals(goal.getId(), index);
			}
			
			if (command == "saveStep") {
				Step step = new Step();
				step.setName("name" + index);
				step.setDescription("description"+index);

				step.setStatus(StatusType.INPROGRESS);
				step.setStartDate(new Date());
				repository.saveStep(step);
				System.out.println("Step with name:"+step.getName()+" added, id:"+step.getId());
			}
			if (command == "deleteStep") {
				repository.deleteStep(index);

			}
			if (command == "readStep") {
				Step step;
				step = repository.readStep(index);
				System.out.println("Step with name:"+step.getName()+" readed, id:"+step.getId());
				assertEquals(step.getId(), index);
			}
			
			if (command == "saveKeyWords") {
				KeyWord keyWord = new KeyWord();
				keyWord.setValue("word" + index);
				
				repository.saveKeyWord(keyWord);
				System.out.println("KeyWord with value:"+keyWord.getValue()+" added, id:"+keyWord.getId());
			}
			if (command == "deleteKeyWord") {
				repository.deleteKeyWord(index);

			}
			if (command == "readKeyWord") {
				KeyWord keyWord;
				keyWord = repository.readKeyWord(index);
				System.out.println("KeyWord with value:"+keyWord.getValue()+" readed, id:"+keyWord.getId());
				assertEquals(keyWord.getId(), index);
			}

		}
	}

	@Before
	public void setUp(){
		File file = new File("TestDerbyDBGoalCharger");
		if(file.exists()){
			file.delete();
		}
		repository = new InDBGoalRepository(true);
	}

	@After
	public void finish(){
		File file = new File("TestDerbyDBGoalCharger");
		
		if(file.exists()){
			file.delete();
		}
		
	}
	@Test
	public void test1Save() {
		try {
			ExecutorService executor = Executors.newFixedThreadPool(10);
			for (int i = 1; i <= 50; i++) {
				executor.submit(new TestTask("saveGoal", repository, i));
				executor.submit(new TestTask("saveStep", repository, i+50));
				executor.submit(new TestTask("saveKeyWord", repository, i+100));

			}
			while (50 == repository.getGoalCount()) {
				Thread.sleep(1000);
			}
			while (50 == repository.getStepCount()) {
				Thread.sleep(100);
			}
			while (50 == repository.getKeyWordCount()) {
				Thread.sleep(10);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(50, this.repository.getGoalCount());
		assertEquals(50, this.repository.getStepCount());
		assertEquals(50, this.repository.getKeyWordCount());

	}

	
}
