<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="com.rohitThebest.springdemo.utility.SortUtils" %>
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
			
			<!-- add a search box here -->
			<form:form action="search" method="GET">
				
				Search Customer: <input type="text" name="theSearchName"/>
				
				<input type="submit" value="Search" class="add-button" />
			</form:form>
			
			<!-- add out html table here -->
			<table>
				
				<!-- construct a sort link for firstName -->
				<c:url var="sortLinkFirstName" value="/customer/list">
					
					<c:param name="sort" value="<%= Integer.toString(SortUtils.FIRST_NAME) %>"/>
				</c:url>
				
				<!-- construct a sort link for lastName -->
				<c:url var="sortLinklastName" value="/customer/list">
					
					<c:param name="sort" value="<%= Integer.toString(SortUtils.LAST_NAME) %>"/>
				</c:url>
				
				<!-- construct a sort link for email -->
				<c:url var="sortLinkEmail" value="/customer/list">
					
					<c:param name="sort" value="<%= Integer.toString(SortUtils.EMAIL) %>"/>
				</c:url>
				
				
				<tr>
					<th><a href="${sortLinkFirstName}">First Name</a></th>
					<th><a href="${sortLinklastName}">Last Name</a></th>
					<th><a href="${sortLinkEmail}">Email</a></th>
					<th>Action</th>
				</tr>
				
				<!-- loop over and print our customers -->
				
				<c:forEach var="tempCustomer" items="${customers}">
					
					<!-- construct an "update" link with customer id -->
					
					<c:url var="updateLink" value="/customer/showFormForUpdate">
					
						<c:param name="customerId" value="${tempCustomer.id}" />
					</c:url>
					
					<!-- construct an "update" link with customer id -->
					<c:url var="deleteLink" value="/customer/delete">
					
						<c:param name="customerId" value="${tempCustomer.id}" />
					</c:url>
					
					<tr>
						
						<td>${tempCustomer.firstName}</td>
						<td>${tempCustomer.lastName}</td>
						<td>${tempCustomer.email}</td>
						<td>
							<!-- display the update and delete link -->
							<a href="${updateLink}">Update</a>
							|
							<a href="${deleteLink}"
								onClick= "if (!(confirm('Are you sure you want to delete this customer?'))) return false"
							>Delete</a>
						</td>
					</tr>
					
				</c:forEach>
				
			</table>
			
		</div>
		
	</div>

</body>
</html>