package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.larissashch.portfolio.goalcharger.model.entity.Customer;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		out.write("<h1>Succes!!!</h1>");
		out.flush();
		out.close();*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer id;
		String button = request.getParameter("button");
		String forwarTo = "/index.jsp";
		String logged_in = "no";
		if(button.equals("login")){
			id = logIn(request.getParameter("email"), request.getParameter("password"));
			if(id!=null){
				//set session
				//set cookie
				logged_in = "yes";
				//forward
				
			}else{
				//forward
				//forwarTo="/error.jsp";
			}
			Cookie cookie = new Cookie("logged_in", logged_in);
			cookie.setMaxAge(1*24*60*60);
			response.addCookie(cookie);
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwarTo);
			dispatcher.forward(request, response);
		}
		System.out.println(button);
	}
	
	private Integer logIn(String login, String password){
		return null;
	}
	
	private void logOut(){
		
	}
	private Integer signIn(Customer customer){
		return null;
	}

}
