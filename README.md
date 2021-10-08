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
 * DAO (Data access object) : This 
 
 
 