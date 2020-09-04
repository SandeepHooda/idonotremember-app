package googleAssistant;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.communication.email.EmailAddess;
import com.communication.email.MailService;
import com.google.OauthGoogleActions;
import com.google.OauthGoogleActionsFindMyStuff;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.facade.LoginFacade;
import com.login.vo.LoginVO;
import com.login.vo.PushNotifyUser;
import com.login.vo.Settings;
import com.reminder.facade.ReminderFacade;
import com.reminder.vo.ReminderVO;

import googleAssistant.service.DataService;
import mangodb.MangoDB;
import request.Argument;
import request.GoogleRequest;
import request.Input;
import request.OutputContexts;

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

    public String getTodaysReminders(String email) {
    	String response = "";
    	List<String> pendingDotos = dataService.getTodaysReminders(email);
		if (pendingDotos.size() ==0) {
			response ="Today is a relax day! You don't have any Reminders for today. ";
			
		}else {
			for (String toDo: pendingDotos) {
				response+=toDo+". ";
			}
			response =   " Your pending reminders for next 24 hours are.  "+response;
		}
		return response;
    }
    public String getRemindersByDate(String email, String date) {
    	if (date != null) {
    		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
    		SimpleDateFormat formatterDisplay  = new SimpleDateFormat("dd MMM yyyy");
        	date = date.replaceAll("-", "_");
        	try {
				Date dateQuery = formatter.parse(date);
				String response = "";
	        	List<String> pendingDotos = dataService.getRemindersByDate(email, dateQuery);
	    		if (pendingDotos.size() ==0) {
	    			response =formatterDisplay.format(dateQuery)+" is a relax day! You don't have any Reminders for that day. ";
	    			
	    		}else {
	    			for (String toDo: pendingDotos) {
	    				response+=toDo+". ";
	    			}
	    			response =   " Your pending reminders for "+formatterDisplay.format(dateQuery)+" are.  "+response;
	    		}
	    		return response;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
    		
    	}
    		
    	return  " I didn't get what date you mention.";
    	
    	
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		boolean needLocation = false;
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
		String timeZones[] = null;
		boolean continueConversation = true;
		if (null != access_token) {
			Map<String, String> userData = new OauthGoogleActions().getUserEmailFromMangoD(access_token);
			email = userData.get("emailID");
			name  = userData.get("name");
		}
        if ("DeleteToDo".equalsIgnoreCase(intent) && (null == queryText || queryText.toLowerCase().indexOf("delete") <0)) {
        	if (null == queryText ) {
        		intent = "GetToDO";
        	}else if (queryText.toLowerCase().indexOf("add a new to do") >=0 || 
        			queryText.toLowerCase().indexOf("add a to do") >=0 ||
        			queryText.toLowerCase().indexOf("add a new task") >=0 ||
        			queryText.toLowerCase().indexOf("add a  task") >=0){
        		intent = "AddToDo";
        	}else {
        		intent = "GetToDO";
        	}
        	
        }
		System.out.println(" got email and name from mango DB "+email+" "+name);
		if ("AddToDo".equalsIgnoreCase(intent) && null != queryText){
			String toDoAdded = dataService.addToDo(queryText, email) ;
			List<String> pendingDotos = dataService.getToDos(email);
			if (pendingDotos.size() ==0) {
				serviceResponse =name+", You don't have any pending tasks.";
			}else {
				for (String toDo: pendingDotos) {
					serviceResponse+=toDo+". ";
				}
			}
			serviceResponse =   name+", I have added "+toDoAdded+" to your to do list. Here are your pending to do items. "+serviceResponse;
		}else if ("DeleteToDo".equalsIgnoreCase(intent) && null != queryText) {
			String itemtoBeDelete = (String) googlerequest.getQueryResult().getParameters().get("any");
			String userQuery = (String) googlerequest.getQueryResult().getParameters().get("any");
			String todoDeleted = dataService.deleteToDo(itemtoBeDelete, email, false) ;
			if ("".equals(todoDeleted) ){
				todoDeleted = dataService.deleteReminder(itemtoBeDelete, email, false) ;
			}
			System.out.println(" Query text "+queryText +" : Slot value "+itemtoBeDelete);
			 
			if ("".equals(todoDeleted) ){
				serviceResponse =   name+", I couldn't Find, "+userQuery+" in your to do list or in reminders. Please try again. "+serviceResponse;
			}else {
				serviceResponse =   name+", Do you want to delete "+todoDeleted+serviceResponse;
				continueConversation = false;
			}
			
		}else if ("DeleteToDo - yes".equalsIgnoreCase(intent) && null != queryText) {
			String itemtoBeDelete = null;
			if (null != googlerequest.getQueryResult().getOutputContexts()) {
				for (OutputContexts context : googlerequest.getQueryResult().getOutputContexts()) {
					if (context.getName().endsWith("deletetodo-followup")) {
						itemtoBeDelete = (String) context.getParameters().get("any");
					}
				}
			}
			String todoDeleted = "";
			if (null != itemtoBeDelete) {
				todoDeleted  = dataService.deleteToDo(itemtoBeDelete, email, true) ;
				if ("".equals(todoDeleted) ){
					todoDeleted = dataService.deleteReminder(itemtoBeDelete, email, true) ;
				}
			}
			
			if ("".equals(todoDeleted) ){
				serviceResponse =   name+", I couldn't recognize what task you want to delete. Please try again. "+serviceResponse;
			}else {
				serviceResponse =   name+", I have deleted "+todoDeleted+serviceResponse;
			}
			
		}else if ("NewReminder".equalsIgnoreCase(intent) && null != queryText) {
			//String todoDeleted = dataService.deleteToDo(queryText, email) ;
			  try {
				   String dateStr =(String) googlerequest.getQueryResult().getParameters().get("date");
				   String timeStr =(String) googlerequest.getQueryResult().getParameters().get("time");
				   String dateTimeStr = dateStr.substring(0, 10) +timeStr.substring(10) ;
				    
				    Gson  json = new Gson();
					String settingsJson = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-settings-new", email, null,true, null, null);
					Settings settings = json.fromJson(settingsJson, new TypeToken<Settings>() {}.getType());
					if (null == settings) {
						settings = new Settings();
						settings.setAppTimeZone("Asia/Calcutta");
						needLocation = true;
					}
					
					ReminderVO reminder = new ReminderVO();
					
					
					reminder.setReminderSubject(""+googlerequest.getQueryResult().getParameters().get("any"));
					reminder.setReminderText(" ");
					
					List<String> phoneNumbers = new LoginFacade(). getPhoneViaStatus(email,  true);
					if (null != phoneNumbers && phoneNumbers.size() > 0) {
						reminder.setSelectedPhone(phoneNumbers.get(0));
						reminder.setMakeACall(true);
					}else {
						remindToAddNewPhone(email);
					}
					
					reminder.setDate(dateTimeStr.substring(0, 10).replaceAll("-", "_"));
					reminder.setTime(dateTimeStr.substring(11, 16).replaceAll(":", "_"));
					reminder.setEmail(email);
					
					String frequency= (String) googlerequest.getQueryResult().getParameters().get("frequency");
					String frequencyType = "Date";
					Calendar cal = new GregorianCalendar();
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
						Date date = sdf.parse(reminder.getDate());
						cal.setTime(date);
					}catch (Exception e) {
						e.printStackTrace();
					}
					if ("weekly".equalsIgnoreCase(frequency) ) {
						String[] days = {"","Sunday", "Monday", "Tuesday", "Wednesday", "Thrusday", "Friday", "Saturday","Sunday"};
						reminder.setDayRepeatFrequency("Every "+days[cal.get(Calendar.DAY_OF_WEEK)]);
						frequencyType = "Day";
						reminder.setDisplayTime(reminder.getDayRepeatFrequency()+" of every month @ "+(reminder.getTime().replaceAll("_", ":")));
						
						
					}else if ("Monthly".equalsIgnoreCase(frequency) ) {
						
						reminder.setDisplayTime("Every month "+cal.get(Calendar.DAY_OF_MONTH)+" @ "+(reminder.getTime().replaceAll("_", ":")));
						reminder.setFrequencyWithDate("Monthly");
					}else if ("Yearly".equalsIgnoreCase(frequency) ) {
						String[] months = {"January","February", "March", "April", "May", "June", "July", "August","September","October", "November", "December"};
						reminder.setDisplayTime("Every year "+cal.get(Calendar.DAY_OF_MONTH)+" "+months[cal.get(Calendar.MONTH)]+" @ "+(reminder.getTime().replaceAll("_", ":")));
						reminder.setFrequencyWithDate("Yearly");
					}
						
						else {
						reminder.setFrequencyWithDate("Once");
					}
					reminder.setFrequencyType(frequencyType);
					
					if (new ReminderFacade().addReminder(reminder,settings.getAppTimeZone() )) {
						reminder.setDisplayTime(reminder.formatDisplayTime(reminder.getNextExecutionTime(), settings.getAppTimeZone()));
						String displayDateStr = reminder.formatDisplayTimeWithDay(reminder.getNextExecutionTime(), settings.getAppTimeZone());
						serviceResponse =   name+", I have set the reminder for,  "+googlerequest.getQueryResult().getParameters().get("any")+", on  "+displayDateStr.replace("@", ", Time ") ;
						checkCallCredits(email,settings);
					}else {
						serviceResponse = " There was some error. Please try at some time later.";
					}
					
						 
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   
				
			
			
		}else if ("RemoveTaskHelp".equalsIgnoreCase(intent) ) {
			serviceResponse = "If you want to delete a task that you set to get bread, Say delete bread. If you want to delete a reminder then login to I do not remember hyphen app.appspot.com website and delete by left swipe on that reminder. Please check your email  "+email+" , for details about these steps.";
		
			new  MailService().sendSimpleMail(MailService.prepareEmailVO(new EmailAddess(email, ""), "Steps to delete a task/Reminder.",	"If you want to delete a task that you set to get bread, Say delete bread. If you want to delete a reminder then login to https://idonotremember-app.appspot.com website and delete by left swipe on that reminder. ", null, null));
		}else if ("GetRecentAppointments".equalsIgnoreCase(intent) ) {
			serviceResponse = name+", "+getTodaysReminders(email);
		}else if ("GetAppointmentByDate".equalsIgnoreCase(intent) ) {
			String dateStr =(String) googlerequest.getQueryResult().getParameters().get("date");
			if (dateStr.length() > 10) {
				dateStr = dateStr.substring(0,10);
			}
			
			serviceResponse = name+", "+getRemindersByDate(email, dateStr);
		}else if ("WhenIsMyAppointment".equalsIgnoreCase(intent) ) {
			String appointmentQuestion = (String)googlerequest.getQueryResult().getParameters().get("any");
			appointmentQuestion = appointmentQuestion.trim().toLowerCase();
			//List<String> allRemindersText = dataService.getAllRemindersString(email);
			//serviceResponse = MailService.questionMatchFromHerokuAI(appointmentQuestion,allRemindersText);
			 
			/*if ("".equals(serviceResponse)) {
				serviceResponse = " Sorry, I didn't find the reminder for "+appointmentQuestion;
			}else {*/
				List<ReminderVO>  allReminders = dataService.getAllReminders(email);
				Map<String, ReminderVO> reminderMap = new HashMap<String, ReminderVO>();
				for (ReminderVO reminder: allReminders) {
					String reminderText = (reminder.getReminderSubject() +" "+reminder.getReminderText()).trim().toLowerCase();
					if (null == reminderMap.get(reminderText)) {//So that monthly/quaterly reminders only the first one goes in
						reminderMap.put(reminderText, reminder);
					}
					
					
				}
				Gson  json = new Gson();
				String settingsJson = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-settings-new", email, null,true, null, null);
				Settings settings = json.fromJson(settingsJson, new TypeToken<Settings>() {}.getType());
				if (null == settings) {
					settings = new Settings();
					settings.setAppTimeZone("Asia/Calcutta");
				}
				String[] queryWords  = appointmentQuestion.split(" ");
				for (String aReminderText: reminderMap.keySet()) {
					for (String queryWord: queryWords) {
						if (aReminderText.indexOf(queryWord) >=0 ) {
							ReminderVO reminder = reminderMap.get(aReminderText);
							reminder.setDisplayTime(reminder.formatDisplayTime(reminder.getNextExecutionTime(), settings.getAppTimeZone()));
							serviceResponse += "You appointment with "+aReminderText+" is on "+reminder.getDisplayTime()+". ";
							break;//so that same items don't get added twice 
						}
					}
				}
				if ("".equals(serviceResponse)) {
					serviceResponse = "I couldn't find you appointment for "+appointmentQuestion;
				}
			//}
		} else  if ("finish_push_setup".equalsIgnoreCase(intent) ) {
			serviceResponse  = subscribeUser(email,name, googlerequest);
			
		} else if ("DeletePushNotification".equalsIgnoreCase(intent)  ){
			serviceResponse = name+", You won't receive notifications";
			
		}
			else {
			serviceResponse = name+", "+gettoDoList(email);
			
		}
		
		String continueStr  = "";
				if (continueConversation) {
					continueStr  = ". Anything else I can help you with?";
				}
		/*String responseStr = "{\r\n" + 
		"  \"fulfillmentText\": \"  "+serviceResponse+continueStr+"  \",\r\n" + 
		"  \"outputContexts\": []\r\n" + 
		"}";*/
		String responseStr =  getCompleteResponse( serviceResponse+continueStr);
		 if ("PushNotification".equalsIgnoreCase(intent)  ) {
			 responseStr =  pushNotificationPermision;
			 
		 }else if ("DeletePushNotification".equalsIgnoreCase(intent)  ){
			 List<PushNotifyUser> users =  getNotififationUser( email) ; 
			 if (null != users ) {
				 for (PushNotifyUser user : users) {
					 user.setSendUpdates(false);
					 Gson  json = new Gson();
			         String data = json.toJson(user, new TypeToken<PushNotifyUser>() {}.getType());
					 MangoDB.updateData("idonot-remember-g-push-notification", "users",data, user.get_id(), null);
				 }
				 
			 }
			
			 
			
		 }
		 System.out.println("intent "+intent+" queryText "+queryText+" serviceResponse "+responseStr);
		needLocation = false;
		if (needLocation) {
			out.print(location );
		}else {
			out.print(responseStr );
		}
       
       out.flush();   
	}
    private String subscribeUser(String email, String name, GoogleRequest googlerequest) {
    	//1. Get old users in the DB for that email
    	List<PushNotifyUser> userList =  getNotififationUser( email) ;
    	
    	//2. Check if we have new user information 
    	PushNotifyUser notifyUser =null;
    	for (Input input : googlerequest.getOriginalDetectIntentRequest().getPayload().getInputs()) {
			for (Argument argument: input.getArguments()) {
				System.out.println(argument.getName() +" = "+argument.getTextValue());
				if ("UPDATES_USER_ID".equalsIgnoreCase(argument.getName())) {
					notifyUser = new PushNotifyUser();
					notifyUser.setEmail(email);
					notifyUser.set_id(argument.getTextValue());
				}
			}
		}
    	
    	System.out.println(" Userr details in request "+notifyUser);
    	//Delete the previous records from DB
    	if (null != notifyUser && null != userList && userList.size() > 0) {
    		//deleteDocument(String dbName,String collection,  String dataKeyTobeDeleted, String key)
    		for (PushNotifyUser user: userList) {
    			MangoDB.deleteDocument("idonot-remember-g-push-notification", "users",  user.get_id(), null);
    		}
    	}
    	System.out.println(" User list in db  "+userList);
    	if (null != notifyUser) {
    		Gson  json = new Gson();
	         String data = json.toJson(notifyUser, new TypeToken<PushNotifyUser>() {}.getType());
	         MangoDB.createNewDocumentInCollection("idonot-remember-g-push-notification", "users", data, null);
    	}else if (null != userList && userList.size() > 0) {
    		Gson  json = new Gson();
    		for (PushNotifyUser user: userList) {
    			user.setSendUpdates(true);
    			System.out.println(" turn notification  on for "+user);
    			String data = json.toJson(user, new TypeToken<PushNotifyUser>() {}.getType());
    			MangoDB.updateData("idonot-remember-g-push-notification", "users",data, user.get_id(), null);
    		}
    	}
		
		return  name+" You have been registered for notifications now. ";
		
    }
    public List<PushNotifyUser> getNotififationUser(String email) {
    	Gson  json = new Gson();
		String jsonText = "["+ MangoDB.getDocumentWithQuery("idonot-remember-g-push-notification", "users", email, "email",false, null, null)+"]";
		List<PushNotifyUser> user = json.fromJson(jsonText, new TypeToken<List<PushNotifyUser>>() {}.getType());
		return user;
    }
    
    private String getCompleteResponse(String textToSpeak) {
    	return responsePre+textToSpeak+responsePost;
    	
    }
   
    private void checkCallCredits(String email, Settings settings) {
    	try {
    		if (settings.getCurrentCallCredits() <=10) {
    			new  MailService().sendSimpleMail(MailService.prepareEmailVO(new EmailAddess(email, ""), "Please add a phone number.",	"I can remind you about your appointments by call you on your phone. For that please add cash to your account via Paytm secure payment gateway. You may also use your credit/debit card as well. Please visit the link to complete the process : https://idonotremember-app.appspot.com/ui/index.html#/menu/addcash ", null, null));
    		}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    private void remindToAddNewPhone(String email) {
    	try {
    		new  MailService().sendSimpleMail(MailService.prepareEmailVO(new EmailAddess(email, ""), "Please add a phone number.",	"I can remind you about your appointments by calling you on your phone. For that please add your phone number and verify that with OPT message on your phone. Please visit the link to complete the process : https://idonotremember-app.appspot.com/ui/index.html#/menu/contacts ", null, null));
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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
	
	
	
	public static final String intentPush = "GetToDoPushNotification_9";
	private static  String pushNotificationPermision = "{\r\n" + 
			"  \"payload\": {\r\n" + 
			"    \"google\": {\r\n" + 
			"      \"expectUserResponse\": true,\r\n" + 
			"\"richResponse\": {\r\n" + 
			"        \"items\": [\r\n" + 
			"          {\r\n" + 
			"            \"simpleResponse\": {\r\n" + 
			"              \"textToSpeech\": \"Give permissions\"\r\n" + 
			"            }\r\n" + 
			"          }\r\n" + 
			"        ]\r\n" + 
			"      },\r\n" + 
			"\"userStorage\": \"{\\\"data\\\":{}}\",\r\n" + 
			"      \"systemIntent\": {\r\n" + 
			"        \"intent\": \"actions.intent.PERMISSION\",\r\n" + 
			"        \"data\": {\r\n" + 
			"          \"@type\": \"type.googleapis.com/google.actions.v2.PermissionValueSpec\",\r\n" + 
			"          \"optContext\": \"Please grant us the permisison to notify you when you have a reminder event. \",\r\n" + 
			"          \"permissions\": [\r\n" + 
			"            \"UPDATE\" \r\n" + 
			"          ], \"updatePermissionValueSpec\": {\r\n" + 
			"            \"intent\": \""+intentPush+"\"\r\n" + 
			"          }\r\n" + 
			"        }\r\n" + 
			"      }\r\n" + 
			"    }\r\n" + 
			"  }\r\n" + 
			"}";
	
	private static final String location = "{\r\n" + 
			"  \"payload\": {\r\n" + 
			"    \"google\": {\r\n" + 
			"      \"expectUserResponse\": true,\r\n" + 
			"      \"richResponse\": {\r\n" + 
			"        \"items\": [\r\n" + 
			"          {\r\n" + 
			"            \"simpleResponse\": {\r\n" + 
			"              \"textToSpeech\": \"PLACEHOLDER\"\r\n" + 
			"            }\r\n" + 
			"          }\r\n" + 
			"        ]\r\n" + 
			"      },\r\n" + 
			"      \"userStorage\": \"{\\\"data\\\":{}}\",\r\n" + 
			"      \"systemIntent\": {\r\n" + 
			"        \"intent\": \"actions.intent.PLACE\",\r\n" + 
			"        \"data\": {\r\n" + 
			"          \"@type\": \"type.googleapis.com/google.actions.v2.PermissionValueSpec\",\r\n" + 
			"          \"optContext\": \"To set reminder at correct time zone.\",\r\n" + 
			"          \"permissions\": [\r\n" + 
			"            \"NAME\",\r\n" + 
			"            \"DEVICE_PRECISE_LOCATION\"\r\n" + 
			"          ]\r\n" + 
			"        }\r\n" + 
			"      }\r\n" + 
			"    }\r\n" + 
			"  },\r\n" + 
			"  \"outputContexts\": [\r\n" + 
			"    {\r\n" + 
			"      \"name\": \"/contexts/_actions_on_google\",\r\n" + 
			"      \"lifespanCount\": 99,\r\n" + 
			"      \"parameters\": {\r\n" + 
			"        \"data\": \"{}\"\r\n" + 
			"      }\r\n" + 
			"    }\r\n" + 
			"  ]\r\n" + 
			"}";

}
