package com.rohitThebest.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rohitThebest.springdemo.dao.CustomerDAO;
import com.rohitThebest.springdemo.entity.Customer;
import com.rohitThebest.springdemo.service.CustomerService;

@Controller
@RequestMapping("/customer")
public class CustomerController {

//	//need to inject the dao into the controller
//	@Autowired
//	private CustomerDAO customerDAO;

	// need to inject the customer service
	@Autowired
	private CustomerService customerService;
	
	/**
	 * @GetMapping : It is just used for handling the GET methods, any 
	 * other methods would be rejected by this method
	 */
	@GetMapping("/list")
	//@RequestMapping("/list")
	public String listCustomers(Model theModel) {
		
		// get customers from the service
		List<Customer> customers = customerService.getCustomers();
		
		// add the customers to the model
		theModel.addAttribute("customers", customers);
		
		return "list-customers";
	}
}
