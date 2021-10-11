package com.rohitThebest.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		
		// create model attribute to bind form data
		
		Customer customer = new Customer();
		
		theModel.addAttribute("customer", customer);
		
		return "customer-form";
	}
	
	@PostMapping("/saveCustomer")
	public String saveCustomer(@ModelAttribute("customer") Customer theCustomer) {
		
		customerService.saveCustomer(theCustomer);
		
		return "redirect:/customer/list";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("customerId") int id, Model model) {
		
		// get the customer from the database
		
		Customer customer = customerService.getCustomer(id);
		
		// set customer as a model attribute to pre-populate the form
		model.addAttribute("customer", customer);
		
		// send over to our form
		return "customer-form";
	}
	
	@GetMapping("/delete")
	public String deleteCustomer(@RequestParam("customerId") int id) {
		
		// delete the customer
		customerService.deleteCustomer(id);
		
		return "redirect:/customer/list";
	}
}

















