package org.larissashch.portfolio.persistence.repository.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.larissashch.portfolio.goalcharger.model.entity.Customer;
import org.larissashch.portfolio.goalcharger.model.entity.User;
import org.larissashch.portfolio.goalchatge.persistence.repository.UserRepository;
import org.larissashch.portfolio.goalchatge.persistence.repository.xml.InXMLUserRepository;

public class InXMLUserRepositoryTest {
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
				customer.setFirstName("name" + index);

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


	@Test
	public void test1Save() {
		repository = new InXMLUserRepository(true);
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
		File file = new File("TestCustomer.xml");
		file.delete();
		file = new File("TestAdministrator.xml");
		file.delete();
		repository = new InXMLUserRepository(true);
		Customer customer;

		int quantityBefore = repository.getCustomerCount();
		int quantityAfter = quantityBefore;
		
		try {
			for(int i = 1; i < 50; i++){
				customer = new Customer();
				customer.setId(i);
				customer.setFirstName("name" + i);
				repository.saveCustomer(customer);
				quantityAfter++;
			}
			
			for (int i = 1; i < 20; i++) {

				Thread thread;
				thread = new Thread(new TestTask("deleteCustomer", repository, i, null));
				thread.start();

				Thread.sleep(10);
				quantityAfter--;

			}
			
			while (quantityAfter < repository.getCustomerCount()) {
				Thread.sleep(100);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(quantityAfter, this.repository.getCustomerCount());

	}
	@Test
	public void test3Read() {
		
	}

}
