package com.rohitThebest.springdemo.dao;

import java.util.List;

import com.rohitThebest.springdemo.entity.Customer;

public interface CustomerDAO {

	public List<Customer> getCustomers();
	
	public void saveCustomer(Customer customer);

	public Customer getCustomer(int id);

}
