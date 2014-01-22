package org.larissashch.portfolio.goalcharger.bootstrap;

import org.larissashch.portfolio.goalcharger.model.entity.Goal;
import org.larissashch.portfolio.persistence.repository.jdbc.InDBGoalRepository;

public class Starter {

	public static void main(String[] args) {
		InDBGoalRepository repository = new InDBGoalRepository(true);
		Goal goal = new Goal();
		goal.setGoalName("test");
		repository.saveGoal(goal);
		
		
		
		System.out.println("test:"+repository.readGoal(1).getGoalName());
		

	}

}
