package org.larissashch.portfolio.persistence.repository;

import org.larissashch.portfolio.goalcharger.model.entity.Administrator;
import org.larissashch.portfolio.goalcharger.model.entity.Customer;

public interface UserRepository {
	void saveCustomer(Customer customer);

	Customer readCustomer(int id);

	void deleteCustomer(int id);

	void saveAdministrator(Administrator administrator);

	Administrator readAdministrator(int id);

	void deleteAdministrator(int id);

	int getCustomerCount();

	int getAdministratorCount();

}
