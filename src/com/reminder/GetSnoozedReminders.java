package com.reminder;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.communication.email.MailService;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reminder.vo.ReminderVO;

import mangodb.MangoDB;

/**
  * ScanReminders - See if any reminder is past its trigger time
 * GetSnoozedReminders - Send push over notification every 15 minutes for snoozed remoinders
 * DeleteOldTodosAndRemindSnoozed =  * Once in a day delete todo > 7 days * consolidated email for snoozed reminders
 */
@WebServlet("/GetSnoozedReminders")
public class GetSnoozedReminders extends HttpServlet {
	private static FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate().setDeadline(300d);
	private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSnoozedReminders() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String queryEmail = request.getParameter("queryEmail");
		if (null == queryEmail) {
			response.getWriter().append("Invalid request ");
			return;
		}
		
		String turnOnAllReminders = request.getParameter("turnOnAllReminders");
		/*if (turnOnAllReminders != null) {
			if (turnOnAllReminders != null) {
				reminder.setAnounceOnGoogleAssist(true);
			}	
		}*/
		
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "reminders-snooz", null,null, false, null,null)+"]";
		Gson  json = new Gson();
		String remindersForUser = null;
		List<ReminderVO> reminders  = json.fromJson(data, new TypeToken<List<ReminderVO>>() {}.getType());
		Map<String, String> soozedRemindersMap = new HashMap<String, String>();
		for(ReminderVO reminder:reminders ) {
			
			if (queryEmail.equalsIgnoreCase(reminder.getEmail()) && reminder.isAnounceOnGoogleAssist()) {
				String reminderText = soozedRemindersMap.get(reminder.getEmail());
				if (reminderText == null) {
					reminderText = "";
				}
				reminderText += ", " +reminder.getReminderSubject()+" "+reminder.getReminderText()+" , ";
				remindersForUser = reminderText;
				soozedRemindersMap.put(reminder.getEmail(), reminderText);
			}
			
		}
		
		if (null != remindersForUser) {
			remindersForUser = remindersForUser.replace("null", "");
			//String google = " Google response: "+notifyViaGoogleRelay(remindersForUser);
			//String alexa = " Alexa response: "+addAlexaNotification(remindersForUser);
			//remindersForUser += google + alexa;
			remindersForUser = remindersForUser.toLowerCase();
			remindersForUser = remindersForUser.replace("reminders", "");
			remindersForUser = remindersForUser.replace("reminder", "");
			MailService.sendWhatAppMsg("919216411835",remindersForUser,true,false);
			response.getWriter().append("repeat after me reboot "+remindersForUser);
		}else {
			response.getWriter().append("");
		}
		
		
	}
	
	private String notifyViaGoogleRelay(String data) {
		 try {
			 data = "{\"command\":\""+data+"\",\"user\":\"pi\",\"broadcast\":\"true\"}"; 
				
		        URL url = new URL("http://sanhoo.duckdns.org:3000/assistant");
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            
	            req.setHeader(header);
	           
	            req.setPayload(data.getBytes());
	            HTTPResponse res =fetcher.fetch(req);
	            System.out.println("respobnse code from duck DNS "+res.getResponseCode());
	            //if(res.getResponseCode() >=200 && res.getResponseCode()  <300) {
	            	return (res.getResponseCode() +" ="+new String(res.getContent()));
	            //}
	           
	 
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
		
         	return null;
        
	}
	
	private String addAlexaNotification(String data) {
		try {
			String host = "https://api.notifymyecho.com/v1/NotifyMe?notification=";
			URL url = new URL(host+URLEncoder.encode(data, "UTF-8")+Config.alexaNotifyMeKey);
            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
            HTTPResponse res = fetcher.fetch(req);
            return (res.getResponseCode() +" ="+new String(res.getContent()));
            
        } catch (Exception e) {
        	
        }
		return null;
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
