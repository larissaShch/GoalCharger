<% 
	String login_href = "#login_page";
	String my_account_href = "#my_account";
	String my_goals_href = "#my_goals";
	String goal_href = "#goal";
	String step_href = "#step";
%>
				
				<div class="main_page_manu_item">
					<div id="m_p_m_i_left">
						<% if(request.getParameter("logged_in").equals("no")) {
							my_account_href = my_goals_href = goal_href = step_href = login_href;
						
						%>

							<a href="<%=login_href%>">Log in</a>
						
						<%} %>
						
						<% if(request.getParameter("logged_in").equals("yes")) {%>

							<a href="web-application?logout=1">Log out</a>
						
						<%} %>
						
						
					</div>
				</div>
				<div class="main_page_manu_item">
					<div id="m_p_m_i_left">
						<a href="<%=my_account_href%>">
							My Account
						</a>
					</div>
				</div>
				<div class="main_page_manu_item">
					<div id="m_p_m_i_left">
						<a href="<%=my_goals_href%>">
							My Goals
						</a>
					</div>
				</div>
				<div class="main_page_manu_item">
					<div id="m_p_m_i_left">
						<a href="<%=goal_href%>">
							Start a New Goal
						</a>
					</div>
				</div>