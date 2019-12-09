package googleAssistant;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.login.vo.UserLocation;

import googleAssistant.service.DataService;
import request.GoogleRequest;

/**
 * Servlet implementation class CarLocation
 */
@WebServlet("/CarLocation")
public class CarLocation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private Gson gson = new Gson(); 
	 private DataService dataService = new DataService();
	 private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      
   /**
    * @see HttpServlet#HttpServlet()
    */
   public CarLocation() {
       super();
       // TODO Auto-generated constructor stub
   }
   
  
 
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		 StringBuilder sb = new StringBuilder();
       String s;
       while ((s = request.getReader().readLine()) != null) {
           sb.append(s);
       }
    
       String intent = "";
       String queryText = null;

       GoogleRequest googlerequest = null;
       try {
       	 googlerequest = (GoogleRequest) gson.fromJson(sb.toString(), GoogleRequest.class);
       	intent = googlerequest.getQueryResult().getIntent().getDisplayName();
       	queryText = googlerequest.getQueryResult().getQueryText();
       	
       }catch(Exception e) {
       	e.printStackTrace();
       }
       System.out.println(" intent = "+intent + " queryText "+queryText);
       System.out.println(" complete request: "+sb.toString());
      
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		boolean continueConversation = true;
		

		//if ("AddToDo".equalsIgnoreCase(intent) && null != queryText){
		 String serviceResponse =  dataService.getMMILocation() ;
		
			
			
		//}
		
		String continueStr  = "";
				if (continueConversation) {
					continueStr  = ". Do you want to hear it again?";
				}
		
		String responseStr =  getCompleteResponse( serviceResponse+continueStr);
		
		 System.out.println("intent "+intent+" queryText "+queryText+" serviceResponse "+responseStr);
		
		
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
	 private String getCompleteResponse(String textToSpeak) {
	    	return responsePre+textToSpeak+responsePost;
	    	
	    }
	private static final String responsePre = "{\r\n" + 
			"  \"payload\": {\r\n" + 
			"    \"google\": {\r\n" + 
			"      \"expectUserResponse\": true,\r\n" + 
			"      \"richResponse\": {\r\n" + 
			"        \"items\": [\r\n" + 
			"          {\r\n" + 
			"            \"simpleResponse\": {\r\n" + 
			"              \"textToSpeech\": \"";
	private static final String responsePost ="\"\r\n" + 
			"            }\r\n" + 
			"          }\r\n" + 
			"        ]\r\n" + 
			"      }\r\n" + 
			"    }\r\n" + 
			"  }\r\n" + 
			"}";
	
	
	
	

}