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
    
    private String gettoDoList() {
    	String response = "";
    	List<String> pendingDotos = dataService.getToDos(null);
		if (pendingDotos.size() ==0) {
			response ="You don't have any pending to tasks. Do you want to add a new one? You can say things like, add a to do to get ";
			
		}else {
			for (String toDo: pendingDotos) {
				response+=toDo+". ";
			}
			response =   " Your pending task are.  "+response;
		}
		return response;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
     
        String intent = "";
        String queryText = null;
        try {
        	GoogleRequest googlerequest = (GoogleRequest) gson.fromJson(sb.toString(), GoogleRequest.class);
        	intent = googlerequest.getQueryResult().getIntent().getDisplayName();
        	queryText = googlerequest.getQueryResult().getQueryText();
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        System.out.println(" complete request: "+sb.toString());
        String serviceResponse = "";
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if ("AddToDo".equalsIgnoreCase(intent) && null != queryText){
			dataService.addToDo(queryText) ;
			List<String> pendingDotos = dataService.getToDos(null);
			if (pendingDotos.size() ==0) {
				serviceResponse ="You don't have any pending to tasks.";
			}else {
				for (String toDo: pendingDotos) {
					serviceResponse+=toDo+". ";
				}
			}
			serviceResponse =   " I have added it to your do do list. Here are your pending to dos. "+serviceResponse;
		}else {
			serviceResponse = gettoDoList();
			System.out.println(" serviceResponse "+serviceResponse);
		}
		String responseStr = "{\r\n" + 
		"  \"fulfillmentText\": \"  "+serviceResponse+". \",\r\n" + 
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
