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
 * @Repository : this will help spring to scan for the implementation of the
 *             DAO. This annotation is only used on the DAO implementation
 *             classes
 */
@Repository
public class CustomerDAOImpl implements CustomerDAO {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	//
	// Removing the @Transactional as our service layer will now handle the
	// transaction
//	/**
//	 * @Transactional : Using this annotation we don't need to explicitly 
//	 * get the transaction object and also we don't need to call transaction.commit()
//	 * Spring framework will do this stuff for us.
//	 */
//	@Transactional  
	public List<Customer> getCustomers() {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		// create a query ... sort by lastName
		Query<Customer> query = currentSession.createQuery("from Customer order by lastName", Customer.class);

		// execute query and the result list
		List<Customer> customers = query.getResultList();

		// return the results
		return customers;
	}

	@Override
	public void saveCustomer(Customer customer) {

		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		/**
		 * saveOrUpdate : this method checks if the primaryKey/id is empty then do an
		 * insert or else do the update to an existing data
		 */
		// save/update the customer
		currentSession.saveOrUpdate(customer);
	}

	@Override
	public Customer getCustomer(int id) {

		Session currentSession = sessionFactory.getCurrentSession();

		return currentSession.get(Customer.class, id);
	}

	@Override
	public void deleteCustomer(int id) {

		Session currentSession = sessionFactory.getCurrentSession();

		// delete object with primary key
		Query query = 
				currentSession.createQuery("delete from Customer where id=:theCustomerId");

		query.setParameter("theCustomerId", id);

		query.executeUpdate();
	}

	@Override
	public List<Customer> searchCustomers(String name) {

		Session currentSession = sessionFactory.getCurrentSession();
		
		Query<Customer> query = null;
		
		if (name != null && !name.trim().isEmpty()) {
			
			query = currentSession
					.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
			
			query.setParameter("theName", name);
			
		}else {
			
			query = currentSession.createQuery("from Customer", Customer.class);
		}

		return query.getResultList();
	}

}
