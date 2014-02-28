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