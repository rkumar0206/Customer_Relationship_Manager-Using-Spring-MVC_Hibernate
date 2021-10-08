package com.rohitThebest.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rohitThebest.springdemo.dao.CustomerDAO;
import com.rohitThebest.springdemo.entity.Customer;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	//need to inject the dao into the controller
	@Autowired
	private CustomerDAO customerDAO;

	@RequestMapping("/list")
	public String listCustomers(Model theModel) {
		
		// get customers from the dao
		List<Customer> customers = customerDAO.getCustomers();
		
		// add the customers to the model
		theModel.addAttribute("customers", customers);
		
		return "list-customers";
	}
}
