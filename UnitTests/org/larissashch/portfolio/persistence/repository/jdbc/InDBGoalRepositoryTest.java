package org.larissashch.portfolio.persistence.repository.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

import org.apache.commons.io.FileUtils;
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
				
				Step step;
				List<Step> steps = new ArrayList<>();
				for(int i=1; i<=5; i++){
					step = new Step();
					step.setName("test");
					repository.saveStep(step);
					steps.add(step);
				}
				goal.setSteps(steps);
				
				KeyWord keyWord;
				List<KeyWord> keyWords = new ArrayList<>();
				for(int i=1; i<=5; i++){
					keyWord = new KeyWord();
					keyWord.setValue("test"+i);
					repository.saveKeyWord(keyWord);
					keyWords.add(keyWord);
				}
				goal.setKeyWords(keyWords);
				repository.saveGoal(goal);
				System.out.println("!!!!!!!!!!!!!!!!!Goal with name:"+goal.getGoalName()+" added, id:"+goal.getId()+"!!!!!!!!!!!!!!!!!");
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
				
				KeyWord keyWord;
				List<KeyWord> keyWords = new ArrayList<>();
				for(int i=3; i<10; i++){
					keyWord = new KeyWord();
					keyWord.setValue("test"+i);
					repository.saveKeyWord(keyWord);
					keyWords.add(keyWord);
					System.out.println("!!!"+keyWord.getId()+"-"+keyWord.getValue());
				}
				step.setKeyWords(keyWords);
				
				repository.saveStep(step);
				System.out.println("!!!!!!!!!!!!!!!!!Step with name:"+step.getName()+" added, id:"+step.getId()+"!!!!!!!!!!!!!!!!!");
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
				System.out.println("!!!!!!!!!!!!!!!!!KeyWord with value:"+keyWord.getValue()+" added, id:"+keyWord.getId()+"!!!!!!!!!!!!!!!!!");
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
		System.out.println("Start");
		File file = new File("TestDerbyDBGoalCharger");
		if(file.exists()){
			try {
				FileUtils.deleteDirectory(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		repository = new InDBGoalRepository(true);
		
		
	}

	@After
	public void finish(){
		System.out.println("Finish");
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
			while (repository.getGoalCount()<50) {
				Thread.sleep(1000);
				System.out.println("GoalCount:"+repository.getGoalCount());
				
			}
			while (repository.getStepCount()<300) {
				Thread.sleep(100);
				System.out.println("StepCount:"+repository.getStepCount());
			}
			while (repository.getKeyWordCount()<10) {
				Thread.sleep(1000);
				System.out.println("KeyWordCount:"+repository.getKeyWordCount());
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(50, this.repository.getGoalCount());
		assertEquals(300, this.repository.getStepCount());
		assertEquals(60, this.repository.getKeyWordCount());

	}

	
}
