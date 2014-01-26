package org.larissashch.portfolio.goalcharger.bootstrap;


import java.util.ArrayList;
import java.util.List;

import org.larissashch.portfolio.goalcharger.model.entity.KeyWord;
import org.larissashch.portfolio.goalchatge.persistence.repository.jdbc.InDBGoalRepository;

public class Starter {

	public static void main(String[] args) {
		InDBGoalRepository repository = new InDBGoalRepository(true);
		KeyWord keyWord = new KeyWord();
		keyWord.setValue("test");
		repository.saveKeyWord(keyWord);
		
		
		
		System.out.println("KeyWord:"+repository.readKeyWord(1).getValue()+"-"+repository.getKeyWordCount());
		List<KeyWord> keyWords = new ArrayList<>();
		for (int i=1; i <= repository.getKeyWordCount(); i++){
			keyWords.add(repository.readKeyWord(i));
		}
		
		for(KeyWord keyWord2:keyWords){
			System.out.println(keyWord2.getId()+"-"+keyWord2.getValue());
		}
		

	}

}
