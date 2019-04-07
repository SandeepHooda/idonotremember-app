package googleAssistant;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import googleAssistant.service.DataService;
import request.GoogleRequest;

/**
 * Servlet implementation class Handler
 */
@WebServlet("/Handler")
public class Handler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private Gson gson = new Gson(); 
	 private DataService dataService = new DataService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Handler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }

        GoogleRequest googlerequest = (GoogleRequest) gson.fromJson(sb.toString(), GoogleRequest.class);
        
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String serviceResponse = "";
		if ("GetToDO".equalsIgnoreCase(googlerequest.getQueryResult().getIntent().getDisplayName())) {
			List<String> pendingDotos = dataService.getToDos(null);
			if (pendingDotos.size() ==0) {
				serviceResponse ="You don't have any pending to tasks.";
			}else {
				for (String toDo: pendingDotos) {
					serviceResponse+=toDo+". ";
				}
			}
		}else if ("AddToDo".equalsIgnoreCase(googlerequest.getQueryResult().getIntent().getDisplayName())){
			dataService.addToDo(googlerequest.getQueryResult().getQueryText()) ;
			List<String> pendingDotos = dataService.getToDos(null);
			if (pendingDotos.size() ==0) {
				serviceResponse ="You don't have any pending to tasks.";
			}else {
				for (String toDo: pendingDotos) {
					serviceResponse+=toDo+". ";
				}
			}
		}
		String responseStr = "{\r\n" + 
		"  \"fulfillmentText\": \"You asked me to,  "+googlerequest.getQueryResult().getQueryText()+". Intent name is  , " +serviceResponse+". \",\r\n" + 
		"  \"outputContexts\": []\r\n" + 
		"}";
       out.print(responseStr );
       out.flush();   
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
