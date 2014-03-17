<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page errorPage="error.jsp" %>

<% 
	String cookieName;
	String logged_in = "no";
	Cookie []cookies = request.getCookies();
	for(int i = 0; i < cookies.length; i++){
		cookieName = cookies[i].getName();
		if(cookieName.equals("logged_in")){
			logged_in = cookies[i].getValue();
			break;
		}
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="GoalCharger_src/style.css">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>GoalCharger</title>
</head>
<body>
		
	<div id="container">
		<div class="r_page" id="main_page">
			<div id="main_page_menu">


				<jsp:include page="menu.jsp">
				<jsp:param name="logged_in" value='<%=logged_in%>'/>
				</jsp:include>
				
			</div>
			<div class="content">
				<div id="goal_charger_logo">
					<a id="big_arrow_down" href="#login_page"></a>
				</div>
			</div>
		</div>
		<% if(logged_in.equals("no")){ %>
			<%@ include file="login_register_page.jsp" %>
		<%}%>
		<% if(logged_in.equals("yes")){ %>
			<%@ include file="logged_in_page.jsp" %>
		<%}%>

		
	</div>
</body>
		<script type="text/javascript">
			function resizableDiv(){
				h = document.documentElement.clientHeight;
				if(h<600){
					h = 600;
				}
				document.getElementById('main_page').style.height = h+'px';
				
				<% if(logged_in.equals("no")){ %>
					document.getElementById('login_page').style.height = h+'px';	
				
				<%}%>
				<% if(logged_in.equals("yes")){ %>
								
					document.getElementById('my_account').style.height = h+'px';
					document.getElementById('my_goals').style.height = h+'px';
					document.getElementById('goal').style.height = h+'px';
					document.getElementById('step').style.height = h+'px';
				<%}%>
			}
			setInterval("resizableDiv();", 500);
			
		</script>
</html>