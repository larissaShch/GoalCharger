package org.larissashch.portfolio.goalcharger.service;


import org.larissashch.portfolio.goalcharger.model.entity.Administrator;
import org.larissashch.portfolio.goalcharger.model.entity.Customer;
import org.larissashch.portfolio.goalcharger.model.entity.Goal;
import org.larissashch.portfolio.goalcharger.model.entity.KeyWord;
import org.larissashch.portfolio.goalcharger.model.entity.Step;
import org.larissashch.portfolio.goalcharger.persistence.repository.GoalRepository;
import org.larissashch.portfolio.goalcharger.persistence.repository.UserRepository;

public interface GoalChargerService {

		public UserRepository getUserRepository();

		public void setUserRepository(UserRepository userRepository);

		public GoalRepository getGoalRepository();

		public void setGoalRepository(GoalRepository goalRepository);
		
		public void saveCustomer(Customer customer);
		public Customer readCustomer(int id);
		public void deleteCustomer(int id);
		
		public void saveAdministrator(Administrator administrator);
		public Administrator readAdministrator(int id);
		public void deleteAdministrator(int id);
		
		public void saveGoal(Goal goal);
		public Goal readGoal(int id);
		public void deleteGoal(int id);
		
		public void saveStep(Step step);
		public Step readStep(int id);
		public void deleteStep(int id);
		
		public void saveKeyWord(KeyWord keyWord);
		public void saveKeyWord(String value);
		public KeyWord readKeyWord(int id);
		public int readKeyWord(String value);
		public void deleteKeyWord(int id);
		
		

}
