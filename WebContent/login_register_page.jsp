		<div class="r_page" id="login_page">
			<div id="login_page_content">
				<div id="login_logo"></div>
				<div id="signup_logo"></div>
				<br>
				<div id="login_form">
					<form action="web-application" method="POST">
						E-mail <input class="login_form_input" type="text" value="" name="email"><br>
						Password <input class="login_form_input" type="password" value="" name="password"><br>
						<input class="login_form_input" type="hidden" value="login" name="button">
							
						<input type="submit" id="login_button" value="" name="login">
					</form>
				</div>
				
				<div id="join_now_form">
					<form action="web-application" method="POST">
						<jsp:useBean id="customer" class="org.larissashch.portfolio.goalcharger.model.entity.Customer" scope="request"/>
						<jsp:setProperty name="customer" property="*"/>
						
						First Name <input class="login_form_input" type="text" value="" name="firstName"><br>
						Last Name <input class="login_form_input" type="text" value="" name="lastName"><br>
						E-mail <input class="login_form_input" type="text" value="" name="email"><br>
						Password <input class="login_form_input" type="password" value="" name="password"><br>
						Confirm Password <input class="login_form_input" type="password" value="" name="confirm_password"><br>
						Birth Date <input class="login_form_input" type="date" value="" name="birthDate">
						<input class="login_form_input" type="hidden" value="signup" name="button">
						<input type="submit" id="signup_button" value="" name="signup">
					</form>
				</div>
				
			</div>
			
		</div>