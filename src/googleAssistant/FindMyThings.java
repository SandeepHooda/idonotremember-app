package googleAssistant;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Constants;
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
	 private final Pattern itemPattern1 = Pattern.compile("(put|placed|parked|park) my (.*?) (on|at|under|in) (.*?)");
	 private final Pattern itemPattern2 = Pattern.compile("(put|placed|parked|park) (.*?) (on|at|under|in) (.*?)");
	 private final Pattern locationPattern1 = Pattern.compile(" (on|at|under|in) (.*?)$");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindMyThings() {
        super();
        // TODO Auto-generated constructor stub
    }
    private class ItemLocation{
    	private String item;
    	private String location;
		public String getItem() {
			return item;
		}
		public void setItem(String item) {
			this.item = item;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
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
	        String item = null;
	        String location = null;
	        String query_lower = null;
	        Double number = 0.0;
	        try {
	        	GoogleRequest googlerequest = (GoogleRequest) gson.fromJson(sb.toString(), GoogleRequest.class);
	        	intent = googlerequest.getQueryResult().getIntent().getDisplayName();
	        	queryText = googlerequest.getQueryResult().getQueryText();
	        	query_lower = queryText.toLowerCase();
	        	access_token = googlerequest.getOriginalDetectIntentRequest().getPayload().getUser().getAccessToken();
	        	item = (String)googlerequest.getQueryResult().getParameters().get("Item-Name");
	        	location = (String)googlerequest.getQueryResult().getParameters().get("Item-Location");
	        	try {
	        		number =(Double) googlerequest.getQueryResult().getParameters().get("number");
	        	}catch(Exception e) {
	        		
	        	}
	        	
	        	System.out.println(" location "+location);
	        	if ("Slot number".equalsIgnoreCase(location)) {
	        		System.out.println(" getting slot from query  "+query_lower);
	        		location += queryText.substring(query_lower.indexOf("slot number")+11);
	        	}else if ("Slot".equalsIgnoreCase(location)) {
	        		System.out.println(" getting slot from query  "+query_lower);
	        		location += queryText.substring(query_lower.indexOf("slot")+4);
	        	}
	        	
	        	if (null != number && number> 0 && location.indexOf(""+number.intValue()) <0) {
	        		location += " "+number.intValue();
	        	}
	        	
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
			
			if ("Find".equalsIgnoreCase(intent)  && (null == item || "".equals(item.trim()))) {
				query_lower = query_lower.replaceAll("find my ", "").replaceAll("where is my ", "").replaceAll("where are my ", "");
				item = query_lower;
			}
		
			System.out.println(" intent "+intent+" location "+location +" item "+item);
			String serviceResponse = "Can you please repeat what you just said?";
			if ("Put".equalsIgnoreCase(intent) ){
				//if( null == location || "".equals(location.trim()) || null == item || "".equals(item.trim())) {
					ItemLocation itemLocation = findItemLocation(query_lower);
					if (null != itemLocation) {
						if (itemLocation.getLocation() != null ) {
							location = itemLocation.getLocation();
						}
						if (null != itemLocation.getItem() ) {
							item = itemLocation.getItem();
						}
					}
					
				//}
				
				if (null != location && null != item && !"".equals(item.trim()) && !"".equals(location.trim())) {
					if (dataService.putMyThing(email, item, location,queryText)) {
						 serviceResponse = name+", I will remember that you have placed your "+item+",  "+location+". ";
					 }else {
						 serviceResponse = " Sorry couldn't help this time.";
					 }
				}
				 
			}else if ("Find".equalsIgnoreCase(intent)  && null != item && !"".equals(item.trim())) {
				ItemLocation itemLocation = findItemLocation(query_lower);
				if (null != itemLocation) {
					if (itemLocation.getLocation() != null ) {
						location = itemLocation.getLocation();
					}
					if (null != itemLocation.getItem() ) {
						item = itemLocation.getItem();
					}
				}
				serviceResponse =  name+", "+ dataService.findMyThing(email, item);
				System.out.println(" serviceResponse "+serviceResponse);
			}else if ("Remove".equalsIgnoreCase(intent)  && null != item && !"".equals(item.trim()) ) {
				serviceResponse =  dataService.forgetMyThing(email, item);
			}else {
				Constants.sendEmail("sonu.hooda@gmail.com","Find my things ", "queryText: "+queryText+" <br/> intent "+intent+"<br/> location "+location +"<br/> item "+item);
				
			}
		
		String responseStr = "{\r\n" + 
				"  \"fulfillmentText\": \"  "+serviceResponse+" Anything else I can help you with? \",\r\n" + 
				"  \"outputContexts\": []\r\n" + 
				"}";
		       out.print(responseStr );
		       out.flush(); 
	}

	private ItemLocation findItemLocation(String query_lower) {
		Matcher itemMatcher1 = itemPattern1.matcher(query_lower);  
		Matcher itemMatcher2 = itemPattern2.matcher(query_lower);  
		Matcher locationMatcher1 = locationPattern1.matcher(query_lower);
		ItemLocation itemLocation = new ItemLocation();
		try {
			
			 if (itemMatcher1.find()) {
				 itemLocation.setItem(itemMatcher1.group(2));
			 }else if (itemMatcher2.find()) {
				 itemLocation.setItem(itemMatcher2.group(2));
			 }
			 
			 if (locationMatcher1.find()) {
				 itemLocation.setLocation(locationMatcher1.group(1)+" "+locationMatcher1.group(2));
			 }
			 return itemLocation;
		}catch(Exception e) {
			
		}
		return null;
	}
	private String findLocation(String query_lower) {
		if (query_lower.indexOf(" on ") > 0 ) {
			return query_lower.substring(query_lower.indexOf(" on ") +4);
		}else if (query_lower.indexOf(" at ") > 0 ) {
			return query_lower.substring(query_lower.indexOf(" at ") +4);
		}else if (query_lower.indexOf(" under ") > 0 ) {
			return query_lower.substring(query_lower.indexOf(" under ") +6);
		}else if (query_lower.indexOf(" in ") > 0 ) {
			return query_lower.substring(query_lower.indexOf(" in ") +4);
		}else if (query_lower.indexOf(" below ") > 0 ) {
			return query_lower.substring(query_lower.indexOf(" below ") +6);
		}
		return null;
	}
	
	private String findItem(String query_lower) {
		
		return null;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
