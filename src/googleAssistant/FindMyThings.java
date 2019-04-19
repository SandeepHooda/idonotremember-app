package googleAssistant;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.OauthGoogleActions;
import com.google.gson.Gson;

import googleAssistant.service.DataService;
import request.GoogleRequest;

/**
 * Servlet implementation class FindMyThings
 */
@WebServlet("/FindMyThings")
public class FindMyThings extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private Gson gson = new Gson(); 
	 private DataService dataService = new DataService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindMyThings() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		 StringBuilder sb = new StringBuilder();
	        String s;
	        while ((s = request.getReader().readLine()) != null) {
	            sb.append(s);
	        }
	        System.out.println(" complete request: "+sb.toString());
	        String intent = "";
	        String queryText = null;
	        String access_token = null;
	        try {
	        	GoogleRequest googlerequest = (GoogleRequest) gson.fromJson(sb.toString(), GoogleRequest.class);
	        	intent = googlerequest.getQueryResult().getIntent().getDisplayName();
	        	queryText = googlerequest.getQueryResult().getQueryText();
	        	access_token = googlerequest.getOriginalDetectIntentRequest().getPayload().getUser().getAccessToken();
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	        
	        
	        
	        String email = null;
			String name = null;
			if (null != access_token) {
				Map<String, String> userData = new OauthGoogleActions().getUserEmailFromMangoD(access_token);
				email = userData.get("emailID");
				name  = userData.get("name");
			}
			System.out.println(" got email and name from mango DB "+email+" "+name);
		
			String serviceResponse = name+" Your intent "+intent+" and query "+queryText;
		
		String responseStr = "{\r\n" + 
				"  \"fulfillmentText\": \"  "+serviceResponse+" Anything else I can help you with? \",\r\n" + 
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
