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
import javax.servlet.http.HttpSession;

import org.larissashch.portfolio.goalcharger.model.entity.Customer;
import org.larissashch.portfolio.goalcharger.persistence.repository.GoalRepository;
import org.larissashch.portfolio.goalcharger.persistence.repository.UserRepository;
import org.larissashch.portfolio.goalcharger.persistence.repository.jdbc.InDBGoalRepository;
import org.larissashch.portfolio.goalcharger.persistence.repository.xml.InXMLUserRepository;
import org.larissashch.portfolio.goalcharger.service.GoalChargerService;
import org.larissashch.portfolio.goalcharger.service.GoalChargerServiceImpl;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GoalChargerService service;
	private GoalRepository goalRepository;
	private UserRepository userRepository;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
        
        service = new GoalChargerServiceImpl();
        goalRepository = new InDBGoalRepository(false);
        userRepository = new InXMLUserRepository(false);
        
        service.setGoalRepository(goalRepository);
        service.setUserRepository(userRepository);
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
		logIn(request, response);
	}
	
	private void logIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Customer customer;
		Integer id;
		String button = request.getParameter("button");
		String forwarTo = "/index.jsp";
		String logged_in = "no";
		if(button.equals("login")){
			customer = service.getCustomer(request.getParameter("email"), request.getParameter("password"));
			id = customer.getId();
			if(id!=null){
				//set session
				HttpSession session = request.getSession();
				session.setAttribute("customer_id", id);
				logged_in = "yes";
				forwarTo = "/index.jsp";
				
			}else{
				logged_in = "no";
				forwarTo="/error.jsp";
			}
			Cookie cookie = new Cookie("logged_in", logged_in);
			cookie.setMaxAge(1*24*60*60);
			response.addCookie(cookie);
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwarTo);
			dispatcher.forward(request, response);
		}
		System.out.println(button);
	}
	
	private void logOut(){
		
	}
	private Integer signIn(Customer customer){
		return null;
	}

}
