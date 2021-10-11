# Customer_Relationship_Manager-Using-Spring-MVC_Hibernate
A project using spring MVC and hibernate to make a simple web app for showing customer relationship manager (CRM)

### STEP 1 : Making a new dynamic web project
### STEP 2 : Adding required jars in _lib_ folder of _WEB-INF_
### STEP 3 : Adding servlet.xml file for configuration
* So make an xml file called __spring-mvc-crud-demo-servlet.xml__ and add the following code
  
        <?xml version="1.0" encoding="UTF-8"?>
        <beans xmlns="http://www.springframework.org/schema/beans"
        	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
        	xmlns:context="http://www.springframework.org/schema/context"
            xmlns:tx="http://www.springframework.org/schema/tx"
        	xmlns:mvc="http://www.springframework.org/schema/mvc"
        	xsi:schemaLocation="
        		http://www.springframework.org/schema/beans
        		http://www.springframework.org/schema/beans/spring-beans.xsd
        		http://www.springframework.org/schema/context
        		http://www.springframework.org/schema/context/spring-context.xsd
        		http://www.springframework.org/schema/mvc
        		http://www.springframework.org/schema/mvc/spring-mvc.xsd
        		http://www.springframework.org/schema/tx 
        		http://www.springframework.org/schema/tx/spring-tx.xsd">
        
        	<!-- Add support for component scanning -->
        	<context:component-scan base-package="com.rohitThebest.springdemo" />
        
        	<!-- Add support for conversion, formatting and validation support -->
        	<mvc:annotation-driven/>
        
        	<!-- Define Spring MVC view resolver -->
        	<bean
        		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        		<property name="prefix" value="/WEB-INF/view/" />
        		<property name="suffix" value=".jsp" />
        	</bean>
        
            <!-- Step 1: Define Database DataSource / connection pool -->
        	<bean id="myDataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
                  destroy-method="close">
                <property name="driverClass" value="com.mysql.cj.jdbc.Driver" />
                <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/web_customer_tracker?useSSL=false&amp;allowPublicKeyRetrieval=true&amp;serverTimezone=UTC" />
                <property name="user" value="springstudent" />
                <property name="password" value="springstudent" /> 
        
                <!-- these are connection pool properties for C3P0 -->
                <property name="minPoolSize" value="5" />
                <property name="maxPoolSize" value="20" />
                <property name="maxIdleTime" value="30000" />
        	</bean>  
        	
            <!-- Step 2: Setup Hibernate session factory -->
        	<bean id="sessionFactory"
        		class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
        		<property name="dataSource" ref="myDataSource" />
        		<property name="packagesToScan" value="com.rohitThebest.springdemo.entity" />
        		<property name="hibernateProperties">
        		   <props>
        		      <prop key="hibernate.dialect">org.hibernate.dialect.MySQLDialect</prop>
        		      <prop key="hibernate.show_sql">true</prop>
        		   </props>
        		</property>
           </bean>	  
        
            <!-- Step 3: Setup Hibernate transaction manager -->
        	<bean id="myTransactionManager"
                    class="org.springframework.orm.hibernate5.HibernateTransactionManager">
                <property name="sessionFactory" ref="sessionFactory"/>
            </bean>
            
            <!-- Step 4: Enable configuration of transactional behavior based on annotations -->
        	<tx:annotation-driven transaction-manager="myTransactionManager" />
        
        	<!-- Add support for reading web resources : css, images, js, etc -->
        	<!-- ** is used for recurse sub-directory -->
        	<mvc:resources location="/resources/" mapping="/resources/**"></mvc:resources>
        	
        
        </beans>
        
### STEP 4 : make __web.xml__ file
        <?xml version="1.0" encoding="UTF-8"?>
        <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd" id="WebApp_ID" version="3.1">
          <display-name>spring-mvc-crud-demo</display-name>
        
          <absolute-ordering />
        
          <welcome-file-list>
            <welcome-file>index.jsp</welcome-file>
            <welcome-file>index.html</welcome-file>
          </welcome-file-list>
        
          <servlet>
            <servlet-name>dispatcher</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <init-param>
              <param-name>contextConfigLocation</param-name>
              <param-value>/WEB-INF/spring-mvc-crud-demo-servlet.xml</param-value>
            </init-param>
            <load-on-startup>1</load-on-startup>
          </servlet>
          
          <servlet-mapping>
            <servlet-name>dispatcher</servlet-name>
            <url-pattern>/</url-pattern>
          </servlet-mapping>
        </web-app>
        
 ### STEP 5 :  Create DAO 
 * DAO (Data access object) : This is a simple interface used for declaring all the database functions.
        
 ##### CustomerDAO.java
        package com.rohitThebest.springdemo.dao;
        
        import java.util.List;
        
        import com.rohitThebest.springdemo.entity.Customer;
        
        public interface CustomerDAO {
        
        	public List<Customer> getCustomers();
        }

 ### STEP 6 : Create the implmentation class for the dao created above.
 * This implementation class will be used for defining all the functions declared in the __CustomerDAO__ class.

##### CustomerDAOImpl.java

      package com.rohitThebest.springdemo.dao;
      
      import java.util.List;
      
      import org.hibernate.Session;
      import org.hibernate.SessionFactory;
      import org.hibernate.query.Query;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.stereotype.Repository;
      import org.springframework.transaction.annotation.Transactional;
      
      import com.rohitThebest.springdemo.entity.Customer;
      
      /**
       * @Repository :  this will help spring to scan for the implementation of the DAO.
       * This annotation is only used on the DAO implementation classes
       */
      @Repository
      public class CustomerDAOImpl implements CustomerDAO {
      
      	// need to inject the session factory
      	@Autowired
      	private SessionFactory sessionFactory;
      	
      	
      	@Override
      	/**
      	 * @Transactional : Using this annotation we don't need to explicitly 
      	 * get the transaction object and also we don't need to call transaction.commit()
      	 * Spring framework will do this stuff for us.
      	 */
      	@Transactional  
      	public List<Customer> getCustomers() {
      		
      		// get the current hibernate session
      		Session currentSession = sessionFactory.getCurrentSession();
      		
      		// create a query
      		Query<Customer> query = 
      				currentSession.createQuery("from Customer", Customer.class);
      
      		
      		// execute query and the result list
      		List<Customer> customers = query.getResultList();
      		
      		// return the results
      		return customers;
      	}
      
      }

 * Observe the annotations used above :
 * __@Repository__ : This annotation makes the spring know that this class is the implemtation class for the dao and it is always declared on the implemtation class.
 * __@Autowired__ : This annotation inject the SessionFactory from which is defined in the xml file. Here we have used the field injection and spring will inject the session factory.
 * __@Transactional__ : Using this annotation we don't need to explicitly get the transaction object and also we don't need to call transaction.commit(). Spring framework will do this stuff for us automatically.
 
### STEP 7 : Create Customer controller class
* Now let's create a controller class for the customer.
* This class will help us do mapping and send the data to the jsp page so that the data can be displayed to the user.

##### CustomerController.java

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

* __@Controller__ : This class helps Spring to identify it as the controller class.
* __@RequestMapping()__ : Used for mapping the web pages and render it on the browser.
* __@AutoWired__ : Used for injecting the _CustomerDao_ object and it's implementation.
* __return "list-customers"__ : the return String here is actually the jsp file name. We load the data in the model and pass it to the jsp page.

### STEP 8 : Create the jsp file for showing the data on the web-page
* Creating the _list-customers.jsp_ file which will take the data passed by the controller and will diaplay it on the web-page.

##### list-customers.jsp

    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1"%>
    <!DOCTYPE html>
    <html>
    <head>
    	<meta charset="ISO-8859-1">
    	
    	<title>List Customers</title>
    	
    	<link 
    		type="text/css" 
    		rel="stylesheet" 
    		href="${pageContext.request.contextPath}/resources/css/style.css"/>
    	
    	
    </head>
    
    <body>
    
    	<div id="wrapper">
    		
    		<div id="header">
    			<h2>CRM - Customer Relationship Manager</h2>
    		</div>
    	</div>
    	
    	<div id="container">
    		
    		<div id="content">
    			
    			<!-- add out html table here -->
    			
    			<table>
    				
    				<tr>
    					<th>First Name </th>
    					<th>Last Name </th>
    					<th>Email</th>
    				</tr>
    				
    				<!-- loop over and print our customers -->
    				
    				<c:forEach var="tempCustomer" items="${customers}">
    					
    					<tr>
    						
    						<td>${tempCustomer.firstName}</td>
    						<td>${tempCustomer.lastName}</td>
    						<td>${tempCustomer.email}</td>
    						
    					</tr>
    					
    				</c:forEach>
    				
    			</table>
    			
    		</div>
    		
    	</div>
    
    </body>
    </html>

### STEP 9 : Create the welcome page
* In _web.xml_ file we have declared the welcome page index.jsp which we have not created yet. Let's create it!!

##### web.xml
     ...
     ...
     <welcome-file-list>
       <welcome-file>index.jsp</welcome-file>
       <welcome-file>index.html</welcome-file>
     </welcome-file-list>
     ...
     ...
     
##### index.jsp (in WEB-INF folder)
    <% response.sendRedirect("customer/list"); %>
 * this code will just redirect us to the paggine _customer/list/_. 

---

### Now let's refactor our code a bit

#### 1. Replacing RequestMapping("/list") to GetMapping("/list")
* First of all let's replace the __@RequestMapping("/list")__ on _listCustomers(Model theModel)_ in __CustomerController__ to __@GetMapping("/list")__.
* __@GetMapping()__ : This annotation is used on the methods which handle only the _GET_ request. This annotation will restrict any other request methods like POST, etc.

* After replacing our code will be like this - 

##### CustomerController.java

      /**
	    * @GetMapping : It is just used for handling the GET methods, any 
      * other methods would be rejected by this method
      */
      @GetMapping("/list")
      //@RequestMapping("/list")
      public String listCustomers(Model theModel) {
        
        // get customers from the dao
        List<Customer> customers = customerDAO.getCustomers();
        
        // add the customers to the model
        theModel.addAttribute("customers", customers);
        
        return "list-customers";
      }
	
#### 2. Adding a service layer 

* Here we add a service layer which will be between the CustomerController and CustomerDao.
* We add this layer so that we can integrate data from multiple sources (DAOs and repositories). 
* Let's say there are three __DAOs__; _CustomerDao_, _SalesDao_ and _LicenceDao_. No all this dao functions can be integrated into the single service and our controller will use them using the Service Implemetation class.
	* __STEP 1 :__ Make a package called _com.rohitThebest.springdemo.service_
	* __STEP 2 :__ Make a _CustomerService.java_ interface and add the method public List<Customer> getCustomers();
		
	##### CustomerService.java
	
		package com.rohitThebest.springdemo.service;
		
		import java.util.List;
		
		import com.rohitThebest.springdemo.entity.Customer;
		
		public interface CustomerService {
		
			public List<Customer> getCustomers();
		
		}
	
	* __STEP 3 : Make a service implmentation class called _CustomerServiceImpl.java_
	
	##### CustomerServiceImpl.java
	
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
			public List<Customer> getCustomers() {
				
				return customerDAO.getCustomers();
			}
		
		}
	
	* __STEP 4 :__ Remove the _@Transactional_ annotation from the __CustomerDaoImpl.java__
	
	##### CustomerDAOImpl.java
	
		package com.rohitThebest.springdemo.dao;
		
		import java.util.List;
		
		import org.hibernate.Session;
		import org.hibernate.SessionFactory;
		import org.hibernate.query.Query;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.stereotype.Repository;
		import org.springframework.transaction.annotation.Transactional;
		
		import com.rohitThebest.springdemo.entity.Customer;
		
		/**
		 * @Repository :  this will help spring to scan for the implementation of the DAO.
		 * This annotation is only used on the DAO implementation classes
		 */
		@Repository
		public class CustomerDAOImpl implements CustomerDAO {
		
			// need to inject the session factory
			@Autowired
			private SessionFactory sessionFactory;
			
			
			@Override 
			public List<Customer> getCustomers() {
				
				// get the current hibernate session
				Session currentSession = sessionFactory.getCurrentSession();
				
				// create a query
				Query<Customer> query = 
						currentSession.createQuery("from Customer", Customer.class);
		
				
				// execute query and the result list
				List<Customer> customers = query.getResultList();
				
				// return the results
				return customers;
			}
		
		}
	
	* __STEP 5 :__ Update the __CustomerController.java__ class by replacing the CustomerDAO object with CustomerService object
	
	##### CustomerController.java

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

---

### Adding Customer
	
* Now let's try to add a functionality for adding the customers to the database

* First of all we need a form for adding the customer details.

#### STEP 1 : Update the __list-customers.jsp__ 
	
* We need a button which will redirect the user to the page for customer-form.
	
##### Updated list-customers.jsp
	
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	    pageEncoding="ISO-8859-1"%>
	<!DOCTYPE html>
	<html>
	<head>
		<meta charset="ISO-8859-1">
		
		<title>List Customers</title>
		
		<link 
			type="text/css" 
			rel="stylesheet" 
			href="${pageContext.request.contextPath}/resources/css/style.css"/>
		
		
	</head>
			
	<body>
			
		<div id="wrapper">
			
			<div id="header">
				<h2>CRM - Customer Relationship Manager</h2>
			</div>
		</div>
		
		<div id="container">
			
			<div id="content">
				
				<!-- put new button: Add Customer -->
				
				<input type="button" value="Add Customer"
						onClick="window.location.href='showFormForAdd'; return false;"
						class="add-button"
						/>
				
				<!-- add out html table here -->
				
				<table>
					
					<tr>
						<th>First Name </th>
						<th>Last Name </th>
						<th>Email</th>
					</tr>
					
					<!-- loop over and print our customers -->
					
					<c:forEach var="tempCustomer" items="${customers}">
						
						<tr>
							
							<td>${tempCustomer.firstName}</td>
							<td>${tempCustomer.lastName}</td>
							<td>${tempCustomer.email}</td>
							
						</tr>
						
					</c:forEach>
					
				</table>
				
			</div>
			
		</div>
			
	</body>
	</html>

			
#### STEP 2 : Make customer-form.jsp file for showing the form for adding customer details

##### customer-form.jsp
			
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	    pageEncoding="ISO-8859-1"%>
	<!DOCTYPE html>
	<html>
	<head>
	<meta charset="ISO-8859-1">
	<title>Save Customer</title>
	
	<link type="text/css"
		rel="stylesheet"
		href="${pageContext.request.contextPath}/resources/css/style.css">
	
	<link type="text/css"
		rel="stylesheet"
		href="${pageContext.request.contextPath}/resources/css/add-customer-style.css">
	
	
	</head>
	<body>
	
		<div id="wrapper">
			
			<div id="header">
			
				<h2>CRM - Customer Relationship Manager</h2>
			</div>
		</div>
	
		<div id="container">
			
			<h3>Save Customer</h3>
			
			<form:form action="saveCustomer" modelAttribute="customer" method="POST">
				
				<table>
				
					<tbody>
						
						<tr>
							
							<td><label>First name:</label></td>
							<td><form:input path="firstName" /></td>
						</tr>
						
						<tr>
							
							<td><label>Last name:</label></td>
							<td><form:input path="lastName" /></td>
						</tr>
						
						<tr>
							
							<td><label>Email:</label></td>
							<td><form:input path="email" /></td>
						</tr>
						
						<tr>
							
							<td><label></label></td>
							<td><input type="submit" value="Save" class="save" /></td>
						</tr>
						
					</tbody>
				</table>
				
			</form:form>
			
		<div style="clear; both;"></div>
			
			<p>
				
				<a href="${pageContext.request.contextPath}/customer/list">Back to List</a>
			</p>
			
		</div>
		
	</body>
	</html>

#### STEP 3 : add method for saving customer in __CustomerService__ , __CustomerServiceImpl__, __CustomerDAO__, __CustomerDAOImpl__

##### CustomerService.java

	public void saveCustomer(Customer customer);


##### CustomerServiceImpl.java

	@Override
	@Transactional
	public void saveCustomer(Customer customer) {
		
		customerDAO.saveCustomer(customer);
	}
		
##### CustomerDAO.java
	
	public void saveCustomer(Customer customer);
		
##### CustomerDAOImpl.java

	@Override
	public void saveCustomer(Customer customer) {
		
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		// save the customer
		currentSession.save(customer);
	}

#### STEP 4 : Update the CustomerController class
	
##### CustomerController.java
	
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
		
		return "redirect:/customer/list"; // this will redirect the user to the customer/list page
	}
	
---
		
### Updating the Customer details

#### __STEP 1 :__ Update the __list-customer.jsp__ for adding a new column _Action_ 

##### list-customer.jsp
		
	<!-- add out html table here -->
	<table>
		
		<tr>
			<th>First Name </th>
			<th>Last Name </th>
			<th>Email</th>
			<th>Action</th>
		</tr>
		
		<!-- loop over and print our customers -->
		
		<c:forEach var="tempCustomer" items="${customers}">
			
			<!-- construct an "update" link with customer id -->
			
			<c:url var="updateLink" value="/customer/showFormForUpdate">
			
				<c:param name="customerId" value="${tempCustomer.id}" />
			</c:url>
			
			<tr>
				
				<td>${tempCustomer.firstName}</td>
				<td>${tempCustomer.lastName}</td>
				<td>${tempCustomer.email}</td>
				<td>
					<!-- display the update link -->
					<a href="${updateLink}">Update</a>
				</td>
			</tr>
			
		</c:forEach>
		
	</table>

* here we are making a link _updateLink_ withh parameter of __customer id__, which will navigate the user to the showFormForUpdate

#### __STEP 2 :__ Updating our __CustomerController.java__ class

##### CustomerController.java	
		
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("customerId") int id, Model model) {
		
		// get the customer from the database
		
		Customer customer = customerService.getCustomer(id);
		
		// set customer as a model attribute to pre-populate the form
		model.addAttribute("customer", customer);
		
		// send over to our form
		return "customer-form";
	}

* In this method we get the _customer_ using the _id_ and will send the data to the _customer-form.jsp_ so that the data can be pre populated.

#### __STEP 3 :__ Updating __customer-form.jsp__ for pre-populating the data

##### customer-form.jsp		
		
	<form:form action="saveCustomer" modelAttribute="customer" method="POST">
		
		<!-- need to associate this data with customer id -->
		<form:hidden path="id" />
		
		<table>
			<tbody>
				<tr>
					<td><label>First name:</label></td>
					<td><form:input path="firstName" /></td>
				</tr>
				<tr>
					
					<td><label>Last name:</label></td>
					<td><form:input path="lastName" /></td>
				</tr>
				<tr>
					
					<td><label>Email:</label></td>
					<td><form:input path="email" /></td>
				</tr>
				<tr>
					
					<td><label></label></td>
					<td><input type="submit" value="Save" class="save" /></td>
				</tr>	
			</tbody>
		</table>
		
	</form:form>
		
* __<form:hidden path="id" />__ : This will be the hidden form and will call the setId() method for setting the id so as we can update the data.
		
#### __STEP 4 :__ Adding method for getting the customer by id in _CustomerService_, _CustomerServiceImpl_, _CustomerDAO_, CustomerDAOImpl
	
##### CustomerService.java  & CustomerDAO.java
		
	public Customer getCustomer(int id);

##### CustomerServiceImpl.java

	@Override
	@Transactional
	public Customer getCustomer(int id) {
		
		return customerDAO.getCustomer(id);
	}

##### CustomerDAOImpl.java
		
	@Override
	public void saveCustomer(Customer customer) {
		
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		/**
		 * saveOrUpdate : this method checks if the primaryKey/id is empty
		 * then do an insert or else do the update to an existing data
		 */
		// save/update the customer
		currentSession.saveOrUpdate(customer);
	}


	@Override
	public Customer getCustomer(int id) {

		Session currentSession = sessionFactory.getCurrentSession();
		
		return currentSession.get(Customer.class, id);
	}

