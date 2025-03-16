package cccp.command;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class CategoryCommand implements Command {
	private final HttpServletRequest request;
	private final HttpServletResponse response;

	
	public CategoryCommand(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	@Override
	
	public void execute() {
        try {
            request.getRequestDispatcher("CategoryServlet").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
