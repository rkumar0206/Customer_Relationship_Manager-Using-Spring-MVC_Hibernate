# Customer_Relationship_Manager-Using-Spring-MVC_Hibernate

#### Added AOP - Aspect Oriented Programming support
#### Used maven for creating the project

### Adding AOP
- add dependency for aspectj
- update the *spring-mvc-crud-demo-servlet.xml* file
  - add aop namespace
  - add aop schema
  - add aspectj autoproxy element for enabling aspectj scanning
  
  ##### spring-mvc-crud-demo-servlet.xml
  
        <?xml version="1.0" encoding="UTF-8"?>
        <beans xmlns="http://www.springframework.org/schema/beans"
        	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
        	xmlns:context="http://www.springframework.org/schema/context"
            xmlns:tx="http://www.springframework.org/schema/tx"
        	xmlns:mvc="http://www.springframework.org/schema/mvc"
        	xmlns:aop="http://www.springframework.org/schema/aop"
        	xsi:schemaLocation="
        		http://www.springframework.org/schema/beans
        		http://www.springframework.org/schema/beans/spring-beans.xsd
        		http://www.springframework.org/schema/context
        		http://www.springframework.org/schema/context/spring-context.xsd
        		http://www.springframework.org/schema/mvc
        		http://www.springframework.org/schema/mvc/spring-mvc.xsd
        		http://www.springframework.org/schema/tx 
        		http://www.springframework.org/schema/tx/spring-tx.xsd
        		http://www.springframework.org/schema/aop
        		http://www.springframework.org/schema/aop/spring-aop.xsd">
        
        	<!-- Add aspectj autoproxy support for AOP -->
        	<aop:aspectj-autoproxy/>
        
        	<!-- Add support for component scanning -->
        	<context:component-scan base-package="com.rohitThebest.springdemo"/>
        
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


- make a aspect class and add the advices

  ##### CRMLoggingAspect.java
  
        package com.rohitThebest.springdemo.aspect;
        
        import org.springframework.stereotype.Component;
        
        import java.util.logging.Logger;
        
        import org.aspectj.lang.JoinPoint;
        import org.aspectj.lang.annotation.AfterReturning;
        import org.aspectj.lang.annotation.Aspect;
        import org.aspectj.lang.annotation.Before;
        import org.aspectj.lang.annotation.Pointcut;
        
        @Aspect
        @Component
        public class CRMLoggingAspect {
        
        	// setup logger
        	private Logger logger = Logger.getLogger(getClass().getName());
        	
        	// setup pointcut expression
        	@Pointcut("execution (* com.rohitThebest.springdemo.controller.*.*(..))")
        	private void forControllerPackage() {}
        	
        	@Pointcut("execution (* com.rohitThebest.springdemo.service.*.*(..))")
        	private void forServicePackage() {}
        	
        	@Pointcut("execution (* com.rohitThebest.springdemo.dao.*.*(..))")
        	private void forDAOPackage() {}
        	
        	@Pointcut("forControllerPackage() || forServicePackage() || forDAOPackage()")
        	private void forAppFlow() {}
        	
        	
        	// add @Before annotation
        	@Before("forAppFlow()")
        	public void before(JoinPoint joinPoint) {
        		
        		// display method we are calling
        		String method = joinPoint.getSignature().toShortString();
        		logger.info("=====> in @Before: calling method - " + method);
        		
        		// display the arguments to the method
        		
        		Object[] args = joinPoint.getArgs();
        		
        		for(Object arg : args) {
        			
        			logger.info("======> argument : " + arg);
        		}
        	}
        	
        	// add @AfterReturning advice
        	
        	@AfterReturning(
        			pointcut = "forAppFlow()",
        			returning = "result"
        			)
        	public void afterReturning(JoinPoint joinPoint, Object result) {
        		
        		// display method we are returning from
        		String method = joinPoint.getSignature().toShortString();
        		logger.info("=====> in @AfterReturning: from method - " + method);
        		
        		// display data returned
        		logger.info("====> result : " + result);
        	}	
        }





