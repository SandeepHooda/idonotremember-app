package googleAssistant;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.OauthGoogleActions;
import com.google.OauthGoogleActionsFindMyStuff;
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
	 private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Handler() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public String gettoDoList(String email) {
    	String response = "";
    	List<String> pendingDotos = dataService.getToDos(email);
		if (pendingDotos.size() ==0) {
			response ="You don't have any pending tasks. Do you want to add a new one? You can say things like, add a to do to get Milk";
			
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
        String access_token = null;
        GoogleRequest googlerequest = null;
        try {
        	 googlerequest = (GoogleRequest) gson.fromJson(sb.toString(), GoogleRequest.class);
        	intent = googlerequest.getQueryResult().getIntent().getDisplayName();
        	queryText = googlerequest.getQueryResult().getQueryText();
        	access_token = googlerequest.getOriginalDetectIntentRequest().getPayload().getUser().getAccessToken();
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        System.out.println(" complete request: "+sb.toString());
        String serviceResponse = "";
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String email = null;
		String name = null;
		if (null != access_token) {
			Map<String, String> userData = new OauthGoogleActions().getUserEmailFromMangoD(access_token);
			email = userData.get("emailID");
			name  = userData.get("name");
		}
		System.out.println(" got email and name from mango DB "+email+" "+name);
		if ("AddToDo".equalsIgnoreCase(intent) && null != queryText){
			dataService.addToDo(queryText, email) ;
			List<String> pendingDotos = dataService.getToDos(email);
			if (pendingDotos.size() ==0) {
				serviceResponse =name+", You don't have any pending tasks.";
			}else {
				for (String toDo: pendingDotos) {
					serviceResponse+=toDo+". ";
				}
			}
			serviceResponse =   name+", I have added it to your to do list. Here are your pending to do items. "+serviceResponse;
		}else if ("DeleteToDo".equalsIgnoreCase(intent) && null != queryText) {
			String todoDeleted = dataService.deleteToDo(queryText, email) ;
			if ("".equals(todoDeleted) ){
				serviceResponse =   name+", I couldn't recognize what task you want to delete. Please try again. "+serviceResponse;
			}else {
				serviceResponse =   name+", I have deleted "+todoDeleted+" from your to do items. "+serviceResponse;
			}
			
		}else if ("NewReminder".equalsIgnoreCase(intent) && null != queryText) {
			//String todoDeleted = dataService.deleteToDo(queryText, email) ;
			Calendar cal = null;
			   try {
				   String dateStr =(String) googlerequest.getQueryResult().getParameters().get("date");
				   String timeStr =(String) googlerequest.getQueryResult().getParameters().get("time");
				   String dateTimeStr = dateStr.substring(0, 10) +timeStr.substring(10, 19) ;
				
				cal = javax.xml.bind.DatatypeConverter.parseDateTime(dateTimeStr);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				serviceResponse =   name+", Reminder about  "+googlerequest.getQueryResult().getParameters().get("Reminde-Content")+" date & time "+new Date(cal.getTimeInMillis());
				 
				
			
			
		}
			else {
			serviceResponse = name+", "+gettoDoList(email);
			System.out.println(" serviceResponse "+serviceResponse);
		}
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
