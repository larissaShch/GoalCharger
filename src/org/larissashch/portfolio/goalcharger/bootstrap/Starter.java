package org.larissashch.portfolio.goalcharger.bootstrap;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.larissashch.portfolio.goalcharger.model.entity.ApplicationProperties;
import org.larissashch.portfolio.goalcharger.model.entity.Customer;
import org.larissashch.portfolio.goalcharger.model.entity.KeyWord;
import org.larissashch.portfolio.goalcharger.persistence.repository.UserRepository;
import org.larissashch.portfolio.goalcharger.persistence.repository.jdbc.InDBGoalRepository;
import org.larissashch.portfolio.goalcharger.persistence.repository.xml.InXMLUserRepository;

public class Starter {

	public static void main(String[] args) {
		/*      
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
			//test
		}
		System.out.println(ApplicationProperties.DB_PATH);
		*/
		/*UserRepository userRepositoryTest = new InXMLUserRepository(true);
		
		Customer customer = userRepositoryTest.getCustomer("customer72@gmail.com", "Pw:12312312372");
		System.out.println(customer.getFirstName());
		*/
		/*customer = userRepository.getCustomer("Customer732@gmail.com", "1231231234");
		System.out.println(customer);*/
		
		
		UserRepository userRepository = new InXMLUserRepository(false);
		Customer customer = new Customer();
		customer.setAutoDeleteFlag(true);
		customer.setTesterAccountFlag(false);
		customer.setCreateDate(new Date());
		customer.setEmail("larisa@ya.ru");
		customer.setFirstName("Larisa");
		customer.setLastName("S");
		customer.setPassword("123456");
		
		userRepository.saveCustomer(customer);
		System.out.println(customer.getId());
		
	}

}
