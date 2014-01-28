package org.larissashch.portfolio.goalcharger.persistence.repository.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;  
import org.junit.Test;
import org.larissashch.portfolio.goalcharger.model.entity.ApplicationProperties;
import org.larissashch.portfolio.goalcharger.model.entity.Customer;
import org.larissashch.portfolio.goalcharger.model.entity.User;
import org.larissashch.portfolio.goalcharger.persistence.repository.UserRepository;
import org.larissashch.portfolio.goalcharger.persistence.repository.xml.InXMLUserRepository;

public class InXMLUserRepositoryTest{
	private InXMLUserRepository repository;

	public static class TestTask implements Runnable {
		private String command;
		private UserRepository repository;
		private int index;
		private List<User> list;  

		public TestTask(String command, UserRepository repository, int index,
				List<User> list) {
			this.command = command;
			this.repository = repository;
			this.index = index;
			this.list = list;

		}

		@Override
		public void run() {
			if (command == "saveCustomer") {
				Customer customer = new Customer();
				customer.setCreateDate(new Date());
				customer.setAutoDeleteFlag(true);
				customer.setTesterAccountFlag(true);
				customer.setBirthDate(new Date());
				customer.setEmail("customer"+index+"@gmail.com");
				customer.setFirstName("name" + index);
				customer.setLastName("surname"+index);
				customer.setPassword("123123123");
				
				repository.saveCustomer(customer);
				list.add(repository.readCustomer(index));

			}
			if (command == "deleteCustomer") {
				repository.deleteCustomer(index);

			}
			if (command == "readCustomer") {
				Customer customer;
				customer = repository.readCustomer(index);
				assertEquals(customer.getId(), index);
			}

		}
	}

	@Before
	public void setUp(){
		System.out.println("Start");
		File file = new File(ApplicationProperties.DB_PATH + "TestCustomer.xml");
		file.delete();
		file = new File(ApplicationProperties.DB_PATH + "TestAdministrator.xml");
		file.delete();
		repository = new InXMLUserRepository(true);
		
		
	}

	@After
	public void finish(){
		System.out.println("Finish");
	}

	@Test
	public void test1Save() {
		List<User> list = new ArrayList<>();


		int quantityBefore = repository.getCustomerCount();
		int quantityAfter = quantityBefore;

		try {
			ExecutorService executor = Executors.newFixedThreadPool(10);
			for (int i = 1; i <= 100; i++) {
				executor.submit(new TestTask("saveCustomer", repository, i,
						list));
				quantityAfter++;
			}
			while (quantityAfter > repository.getCustomerCount()) {
				Thread.sleep(1000);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(quantityAfter, this.repository.getCustomerCount());

	}

	@Test
	public void test2Delete() {

		Customer customer;
		
		try {
			for(int i = 1; i <= 100; i++){
				customer = new Customer();
				customer.setId(i);
				customer.setFirstName("name" + i);
				repository.saveCustomer(customer);
			}
			
			for (int i = 1; i <= 20; i++) {

				Thread thread;
				thread = new Thread(new TestTask("deleteCustomer", repository, i, null));
				thread.start();

				Thread.sleep(10);

			}
			
			while (repository.getCustomerCount()>80) {
				Thread.sleep(100);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(80, this.repository.getCustomerCount());

	}
	@Test
	public void test3Read() {
		Customer customer;
		
		try {
			for(int i = 1; i <= 50; i++){
				customer = new Customer();
				customer.setId(i);
				customer.setFirstName("name" + i);
				repository.saveCustomer(customer);
			}
			
			while (repository.getCustomerCount()>50) {
				Thread.sleep(100);
			}
			
			for (int i = 1; i <= 20; i++) {

				Thread thread;
				thread = new Thread(new TestTask("readCustomer", repository, i, null));
				thread.start();

				Thread.sleep(10);


			}
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}

}
