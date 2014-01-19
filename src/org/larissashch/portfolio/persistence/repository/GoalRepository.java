package org.larissashch.portfolio.persistence.repository;

import org.apache.taglibs.standard.extra.spath.Step;
import org.larissashch.portfolio.goalcharger.model.entity.Goal;

public interface GoalRepository {
	void saveGoal(Goal goal);
	Goal readGoal(int id);
	void deleteGoal(int id);
	
	void saveStep(Step step);
	Goal readStep(int id);
	void deleteStep(int id);
}
