package com.rohitThebest.springdemo.service;

import java.util.List;

import com.rohitThebest.springdemo.entity.Customer;

public interface CustomerService {

	public List<Customer> getCustomers();

	public void saveCustomer(Customer customer);
}
