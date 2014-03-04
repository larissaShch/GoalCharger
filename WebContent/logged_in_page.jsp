		<div class="r_page" id="my_account">
			<% 
				String email = "test";
				String password = "test";

				email = request.getParameter("email");
				password = request.getParameter("password");
				
			%>
			<%= email %><br>
			<%= password %>
		</div>
		<div class="r_page" id="my_goals">
		</div>
		<div class="r_page" id="goal">
		</div>
		<div class="r_page" id="step">
		</div>