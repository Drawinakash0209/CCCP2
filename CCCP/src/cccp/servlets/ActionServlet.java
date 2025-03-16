package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import cccp.command.Command;
import cccp.factory.ControllerFactory;
import cccp.model.dao.BatchDAO;
import cccp.model.dao.BillDAO;
import cccp.model.dao.OnlineInventoryDAO;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.SaleDAO;
import cccp.model.dao.ShelfDAO;

/**
 * Servlet implementation class ActionServlet
 */
@WebServlet("/ActionServlet")
public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ControllerFactory controllerFactory;
	
	@Override
	public void init() throws ServletException {
		controllerFactory = new ControllerFactory(
				null,
				new BatchDAO(),
				new ShelfDAO(),
	            new ProductDAO(),
	            new SaleDAO(),
	            new BillDAO(),
	            new OnlineInventoryDAO()
				);
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String optionParam = request.getParameter("option");
		
		try {
			int option = Integer.parseInt(optionParam);
			
			if (option < 1 || option > 11) {
				response.sendRedirect("error.jsp");
				return;
			}
			
			Command command = controllerFactory.getCommand(option);
			if (command != null) {
				command.execute();
				response.sendRedirect("success.jsp");
			}else {
				response.sendRedirect("error.jsp");
			}
			
		}catch (NumberFormatException e) {
			response.sendRedirect("error.jsp");
		}
		// TODO Auto-generated method stub
//		doGet(request, response);
	}

}
