<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.lang.String, org.larissashch.portfolio.goalcharger.jsp.test.JSPTest" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GoalCharger</title>
</head>
<body>
	<p>
		Test page
		
		Привет! 
	</p>
	<%! private int summ; %>
	<% 
	//Scriplet
	int  a = 2;
	int b = 6;
	int res = a + b;
	%>
	<%= res %>
	<% 
		JSPTest jspTest = new JSPTest("JSP Test page!");
	%>
	<br>
	<%=jspTest.getTitle() %>
	
</body>
</html>