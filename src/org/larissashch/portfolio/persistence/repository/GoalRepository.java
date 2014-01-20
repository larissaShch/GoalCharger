package org.larissashch.portfolio.persistence.repository;


import org.larissashch.portfolio.goalcharger.model.entity.Goal;
import org.larissashch.portfolio.goalcharger.model.entity.Step;
import org.larissashch.portfolio.goalcharger.model.entity.KeyWord;

public interface GoalRepository {
	void saveGoal(Goal goal);
	Goal readGoal(int id);
	void deleteGoal(int id);
	
	void saveStep(Step step);
	Step readStep(int id);
	void deleteStep(int id);
	
	void saveKeyWord(KeyWord keyWord);
	KeyWord readKeyWord(int id);
	int readKeyWord(String keyWord);
	void deleteKeyWord(int id);
}
