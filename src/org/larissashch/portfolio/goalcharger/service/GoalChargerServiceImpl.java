package org.larissashch.portfolio.goalcharger.service;

import java.util.List;

import org.larissashch.portfolio.goalcharger.model.entity.Administrator;
import org.larissashch.portfolio.goalcharger.model.entity.Customer;
import org.larissashch.portfolio.goalcharger.model.entity.Goal;
import org.larissashch.portfolio.goalcharger.model.entity.KeyWord;
import org.larissashch.portfolio.goalcharger.model.entity.Step;
import org.larissashch.portfolio.goalcharger.persistence.repository.GoalRepository;
import org.larissashch.portfolio.goalcharger.persistence.repository.UserRepository;

public class GoalChargerServiceImpl implements GoalChargerService {
	
	private UserRepository userRepository;
	private GoalRepository goalRepository;


	@Override
	public UserRepository getUserRepository() {
		return userRepository;
	}

	@Override
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
		
	}

	@Override
	public GoalRepository getGoalRepository() {
		return goalRepository;
	}

	@Override
	public void setGoalRepository(GoalRepository goalRepository) {
		this.goalRepository = goalRepository;
		
	}

	@Override
	public void saveCustomer(Customer customer) {
		this.userRepository.saveCustomer(customer);
		
	}

	@Override
	public Customer readCustomer(int id) {
		return this.userRepository.readCustomer(id);
	}

	@Override
	public void deleteCustomer(int id) {
		this.userRepository.deleteCustomer(id);
		
	}

	@Override
	public void saveAdministrator(Administrator administrator) {
		this.userRepository.saveAdministrator(administrator);
		
	}

	@Override
	public Administrator readAdministrator(int id) {
		return this.userRepository.readAdministrator(id);
	}

	@Override
	public void deleteAdministrator(int id) {
		this.userRepository.deleteAdministrator(id);
		
	}

	@Override
	public void saveGoal(Goal goal) {
		this.userRepository.saveUser(goal.getCreatedBy());
		this.goalRepository.saveGoal(goal);
		
	}

	@Override
	public Goal readGoal(int id) {
		Goal goal = this.goalRepository.readGoal(id);
		if(goal.getCreatedBy().getUserType().equalsIgnoreCase("Customer")){
			goal.setCreatedBy(this.userRepository.readCustomer(goal.getCreatedBy().getId()));
		}
		if(goal.getCreatedBy().getUserType().equalsIgnoreCase("Administrator")){
			goal.setCreatedBy(this.userRepository.readAdministrator(goal.getCreatedBy().getId()));
		}
		
		List<Step> steps;
		steps = this.goalRepository.readGoal(id).getSteps();
		for(Step step: steps){
			step = this.goalRepository.readStep(step.getId());
		}
		goal.setSteps(steps);
		
		List<KeyWord> keyWords;
		keyWords = this.goalRepository.readGoal(id).getKeyWords();
		for(KeyWord keyWord: keyWords){
			keyWord = this.goalRepository.readKeyWord(keyWord.getId());
		}
		goal.setKeyWords(keyWords);
		
		return goal;
	}

	@Override
	public void deleteGoal(int id) {
		List<Step> steps;
		steps = this.goalRepository.readGoal(id).getSteps();
		for(Step step: steps){
			this.goalRepository.deleteStep(step.getId());
		}
		this.goalRepository.deleteGoal(id);
	}

	@Override
	public void saveStep(Step step) {
		this.userRepository.saveUser(step.getCreatedBy());
		this.goalRepository.saveStep(step);
		
	}

	@Override
	public Step readStep(int id) {
		Step step = this.goalRepository.readStep(id);
		if(step.getCreatedBy().getUserType().equalsIgnoreCase("Customer")){
			step.setCreatedBy(this.userRepository.readCustomer(step.getCreatedBy().getId()));
			
		}
		if(step.getCreatedBy().getUserType().equalsIgnoreCase("Administrator")){
			step.setCreatedBy(this.userRepository.readAdministrator(step.getCreatedBy().getId()));
			
		}
		return step;
	}

	@Override
	public void deleteStep(int id) {
		this.goalRepository.deleteStep(id);
		
	}

	@Override
	public void saveKeyWord(KeyWord keyWord) {
		this.goalRepository.saveKeyWord(keyWord);
		
	}
	
	@Override
	public void saveKeyWord(String value) {
		KeyWord keyWord = new KeyWord();
		this.goalRepository.saveKeyWord(keyWord);
		
		
	}
	

	@Override
	public KeyWord readKeyWord(int id) {
		return this.goalRepository.readKeyWord(id);
	}

	@Override
	public int readKeyWord(String value) {
		return this.goalRepository.readKeyWord(value);
	}

	@Override
	public void deleteKeyWord(int id) {
		this.goalRepository.deleteKeyWord(id);
		
	}

}
