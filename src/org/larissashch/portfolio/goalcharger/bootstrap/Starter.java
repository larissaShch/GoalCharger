package org.larissashch.portfolio.goalcharger.bootstrap;


import org.larissashch.portfolio.goalcharger.model.entity.KeyWord;
import org.larissashch.portfolio.persistence.repository.jdbc.InDBGoalRepository;

public class Starter {

	public static void main(String[] args) {
		InDBGoalRepository repository = new InDBGoalRepository(true);
		KeyWord keyWord = new KeyWord();
		keyWord.setValue("test");
		repository.saveKeyWord(keyWord);
		
		
		
		System.out.println("KeyWord:"+repository.readKeyWord(1).getValue());
		

	}

}
