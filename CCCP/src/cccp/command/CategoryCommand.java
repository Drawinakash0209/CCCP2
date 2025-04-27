package cccp.command;


import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class CategoryCommand implements Command {
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private String resultKey;

	
	public CategoryCommand(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.resultKey = UUID.randomUUID().toString();// Default result key
	}
	
	@Override
	public void execute() {
        try {
            request.getRequestDispatcher("CategoryServlet").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public String getResultKey() {
		// TODO Auto-generated method stub
		return resultKey;
	}

}
