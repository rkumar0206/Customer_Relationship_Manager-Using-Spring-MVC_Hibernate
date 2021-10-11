package com.rohitThebest.springdemo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohitThebest.springdemo.dao.CustomerDAO;
import com.rohitThebest.springdemo.entity.Customer;

/**
 * @Service : This is added to the service implementation class and 
 * is scanned by Spring for declaring it as the service class
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	// need to inject CustomerDAO
	@Autowired
	private CustomerDAO customerDAO;
	
	/**
	 * @Transactional : Our service layer will handle the opening and closing
	 * of the transaction
	 */
	@Override
	@Transactional
	public List<Customer> getCustomers(int sortField) {
		
		return customerDAO.getCustomers(sortField);
	}

	@Override
	@Transactional
	public void saveCustomer(Customer customer) {
		
		customerDAO.saveCustomer(customer);
	}

	@Override
	@Transactional
	public Customer getCustomer(int id) {
		
		return customerDAO.getCustomer(id);
	}

	@Override
	@Transactional
	public void deleteCustomer(int id) {
		
		customerDAO.deleteCustomer(id);
	}

	@Override
	@Transactional
	public List<Customer> searchCustomers(String name) {

		return customerDAO.searchCustomers(name);
	}

}









