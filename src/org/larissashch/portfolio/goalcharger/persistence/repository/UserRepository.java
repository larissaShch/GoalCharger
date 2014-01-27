package org.larissashch.portfolio.goalcharger.persistence.repository;

import org.larissashch.portfolio.goalcharger.model.entity.Administrator;
import org.larissashch.portfolio.goalcharger.model.entity.Customer;
import org.larissashch.portfolio.goalcharger.model.entity.User;

public interface UserRepository {
	<T extends User> void saveUser(T user);
	
	void saveCustomer(Customer customer);

	Customer readCustomer(int id);

	void deleteCustomer(int id);

	void saveAdministrator(Administrator administrator);

	Administrator readAdministrator(int id);

	void deleteAdministrator(int id);

	int getCustomerCount();

	int getAdministratorCount();

}
