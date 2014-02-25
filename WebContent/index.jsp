<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.lang.String, org.larissashch.portfolio.goalcharger.jsp.test.JSPTest" %>

<%@ page errorPage="error.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="GoalCharger_src/style.css">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>GoalCharger</title>
</head>
<body>
		
	<div id="container">
		<div id="main_page">
			<div id="main_page_menu">
				<%@ include file="menu.jsp" %>
				
			</div>
			<div class="content">
				<div id="goal_charger_logo">
					<a id="big_arrow_down" href="#login_page"></a>
				</div>
			</div>
		</div>
		<div id="login_page" name="login_page">
			<div id="login_page_content">
				<div id="login_logo"></div>
				<div id="signup_logo"></div>
				<br>
				<div id="login_form">
					<form action="index.jsp" method="POST">
						<input class="login_form_input" type="text" value="E-mail" name="email"><br>
						<input class="login_form_input" type="password" value="Password" name="password"><br>
							
						<input type="submit" id="login_button" value="" name="login">
					</form>
				</div>
				
				<div id="join_now_form">
					test
				</div>
				
			</div>
			
		</div>

		<div id="my_account">
			<% 
				String email = "test";
				String password = "test";

				email = request.getParameter("email");
				password = request.getParameter("password");
				
			%>
			<%= email %><br>
			<%= password %>
		</div>
		<div id="my_goals">
		</div>
		<div id="goal">
		</div>
		<div id="step">
		</div>
	</div>
</body>
		<script type="text/javascript">
			function resizableDiv(){
				h = document.documentElement.clientHeight;
				if(h<400){
					h = 400;
				}
				document.getElementById('main_page').style.height = h+'px';
				document.getElementById('login_page').style.height = h+'px';
				document.getElementById('my_account').style.height = h+'px';
				document.getElementById('my_goals').style.height = h+'px';
				document.getElementById('goal').style.height = h+'px';
				document.getElementById('step').style.height = h+'px';
				

			}
			setInterval("resizableDiv();", 50);
			
		</script>
</html>