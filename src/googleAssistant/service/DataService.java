package googleAssistant.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

import com.communication.email.EmailAddess;
import com.communication.email.EmailVO;
import com.communication.email.MailService;
import com.communication.phone.text.Key;
import com.esp8266.location.HealthPing;
import com.esp8266.location.HealthPing.HealthStatus;
import com.esp8266.location.facade.LocationFacade;
import com.esp8266.location.mapMyIndia.Device;
import com.esp8266.location.mapMyIndia.LiveLocations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LoginVO;
import com.login.vo.UserLocation;
import com.reminder.facade.ReminderFacade;
import com.reminder.vo.ReminderVO;
import com.reminder.vo.Thing;
import com.reminder.vo.ToDO;

import mangodb.MangoDB;


public class DataService {
	SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
	private ReminderFacade reminderFacade  = new ReminderFacade();
	private LocationFacade locationFacade = new LocationFacade();
	public boolean putMyThing(String email, String item, String location, String quertText) {
		if (item.startsWith("the ")) {
			item = item.substring(4);
		}
		Thing thing = new Thing( email,  item,  location,quertText);
		thing.setDateCreated(new Date().getTime());
		
		return reminderFacade.placeAThing(thing);
		
	}
	public String findMyThing(String email, String item) {
		item = item.toLowerCase().trim();
		if ("everything".equalsIgnoreCase(item.toLowerCase()) || "all my things".equalsIgnoreCase(item.toLowerCase())) {
			List<Thing> allThings = reminderFacade.findEveryThing( email);
			StringBuilder response = new StringBuilder();
			if (null != allThings && allThings.size() >0) {
				for (Thing aThing: allThings) {
					response.append("You have kept your , "+aThing.getItem() +" , "+aThing.getLocation()+" , on "+formatter.format(new Date(aThing.getDateCreated()))+". ");
				}
			}else {
				response.append("You haven't told me location of any of your things yet.");
			}
			return response.toString();
		}else {
			Thing thingFound = reminderFacade.findAThing( email,  item);
			if (null !=thingFound) {
				return "You have kept your , "+thingFound.getItem() +" , "+thingFound.getLocation()+" , on "+formatter.format(new Date(thingFound.getDateCreated()))+". ";
			}
			String itemLocation = "";
			List<Thing> allThings = reminderFacade.findEveryThing( email);
			String[] itemWords = item.split(" ");
			for (Thing aThing :allThings ) {
				for (String itemWord: itemWords) {
					if (null != aThing && null != aThing.getItem() && aThing.getItem().indexOf(itemWord) >=0 ) {
						itemLocation +="You have kept your , "+aThing.getItem() +" , "+aThing.getLocation()+" , on "+formatter.format(new Date(aThing.getDateCreated()))+". ";
						break;//so that same items don't get added twice 
					}
				}
				
			}
			if (!"".equals(itemLocation)) {
				return itemLocation;
			}else {
				return "Sorry, but  you never told me that where have you kept your "+item +". You can say things like put my toothpaste in red bag.";
			}
		}
		
		
		
		
	}
	public String forgetMyThing(String email, String item) {
		if ("everything".equalsIgnoreCase(item.toLowerCase()) || "all my things".equalsIgnoreCase(item.toLowerCase())) {
			List<Thing> allThings = reminderFacade.findEveryThing( email);
			StringBuilder response = new StringBuilder();
			if (null != allThings && allThings.size() >0) {
				response.append("I have removed the location of your , ");
				for (Thing aThing: allThings) {
					reminderFacade.forgetMything( email,  aThing.getItem());
					response.append(aThing.getItem() +" , ");
				}
				response.append(" , from my memory. ");
			}
			return response.toString();
		}else {
			reminderFacade.forgetMything( email,  item);
			
			return "Ok I have removed the location of your , "+item+" , from my memory.";
		}
		 
		
	}
	public List<String> getTodaysReminders(String email) {
		List<ToDO> toDoList = new ArrayList<ToDO>();
		List<ReminderVO> activeReminders = reminderFacade.getReminders(null,  email);
		Date today = new Date();
		if (null != activeReminders) {
			for (ReminderVO reminder : activeReminders) {
				if (reminder.getNextExecutionTime() - today.getTime() <= 3600000 * 24 *2) {//2 days
					ToDO todo = new ToDO();
					todo.setTaskDesc(reminder.getReminderSubject() +" "+reminder.getReminderText() );
					toDoList.add(todo);
				}
				
			}
		}
		List<String> pendingDotos = new ArrayList<String>();
		if ( toDoList.size() > 0) {
			for (ToDO aToDo: toDoList) {
				if (!aToDo.isComplete()) {
					pendingDotos.add(aToDo.getTaskDesc());
				}
			}
		}
		
		return pendingDotos;
	}
	public List<String> getRemindersByDate(String email, Date dateQuery) {
		
			List<ToDO> toDoList = new ArrayList<ToDO>();
			List<ReminderVO> activeReminders = reminderFacade.getReminders(null,  email);
			Date today = new Date();
			if (null != activeReminders) {
				for (ReminderVO reminder : activeReminders) { 
					long timeGap = reminder.getNextExecutionTime() - dateQuery.getTime();
					if (timeGap <0) {
						timeGap *= -1;
					}
					if (timeGap < 3600000 * 24 *2) {// 2 days
						ToDO todo = new ToDO();
						todo.setTaskDesc(reminder.getReminderSubject() +" "+reminder.getReminderText() );
						toDoList.add(todo);
					}
					
				}
			}
			List<String> pendingDotos = new ArrayList<String>();
			if ( toDoList.size() > 0) {
				for (ToDO aToDo: toDoList) {
					if (!aToDo.isComplete()) {
						pendingDotos.add(aToDo.getTaskDesc());
					}
				}
			}
			
			return pendingDotos;
		
	}
	public List<String> getAllRemindersString(String email){
		List<String> allReminders = new ArrayList<String>();
		List<ReminderVO> activeReminders = reminderFacade.getReminders(null,  email);
		
		if (null != activeReminders) {
			for (ReminderVO reminder : activeReminders) {
				allReminders.add((reminder.getReminderSubject() +" "+reminder.getReminderText()).trim().toLowerCase() );
			}
		}
		return allReminders;
	}
	public List<ReminderVO> getAllReminders(String email){
		
		return reminderFacade.getReminders(null,  email);
		
	}
	public List<String> getToDos(String email) {
		
		List<ToDO> toDoList = reminderFacade.getToDos(email);
		List<ReminderVO> snoozedReminder = reminderFacade.getSnoozedReminders( email);
		if (null == toDoList) {
			toDoList = new ArrayList<ToDO>();
		}
		if (null != snoozedReminder) {
			for (ReminderVO reminder : snoozedReminder) {
				ToDO todo = new ToDO();
				todo.setTaskDesc(reminder.getReminderSubject() +" "+reminder.getReminderText() );
				toDoList.add(todo);
			}
		}
		List<ReminderVO> activeReminders = reminderFacade.getReminders(null,  email);
		Date today = new Date();
		if (null != activeReminders) {
			for (ReminderVO reminder : activeReminders) {
				if (reminder.getNextExecutionTime() - today.getTime() <= 3600000 * 24 *5) {//5 days
					ToDO todo = new ToDO();
					todo.setTaskDesc(reminder.getReminderSubject() +" "+reminder.getReminderText() );
					toDoList.add(todo);
				}
				
			}
		}
		List<String> pendingDotos = new ArrayList<String>();
		if ( toDoList.size() > 0) {
			for (ToDO aToDo: toDoList) {
				if (!aToDo.isComplete()) {
					pendingDotos.add(aToDo.getTaskDesc());
				}
			}
		}
		
		return pendingDotos;
	}
	
	public String deleteReminder (String doDoStr, String email, boolean actualDelete) {
		List<ReminderVO> activeReminders = reminderFacade.getReminders(null,  email);
		Set<String> wordsInTodo = new HashSet<String>();
		StringTokenizer tokenizer = new StringTokenizer( doDoStr, " ");
		while (tokenizer.hasMoreTokens()) {
			wordsInTodo.add(tokenizer.nextToken().toLowerCase());
		}
		String response = "";
		for (ReminderVO aTodo: activeReminders) {
			for (String aToken: wordsInTodo) {
				String taskDesc = (aTodo.getReminderSubject()+ " "+aTodo.getReminderText()).toLowerCase();
				if (taskDesc.contains(aToken) ) {
					response += taskDesc +" ";
					if(actualDelete) {
						reminderFacade.deleteReminder( aTodo.get_id() );
					}
					
					break;
				}
			}
		}
		
		
		return response;
	}
	public String deleteToDo( String doDoStr, String email, boolean actualDelete) {
		
		List<ToDO> toDoList = reminderFacade.getToDos(email);
		Set<String> wordsInTodo = new HashSet<String>();
		StringTokenizer tokenizer = new StringTokenizer( doDoStr, " ");
		while (tokenizer.hasMoreTokens()) {
			wordsInTodo.add(tokenizer.nextToken().toLowerCase());
		}
		String response = "";
		for (ToDO aTodo: toDoList) {
			for (String aToken: wordsInTodo) {
				String taskDesc = aTodo.getTaskDesc().toLowerCase();
				if (taskDesc.contains(aToken) && !aTodo.isComplete()) {
					response += taskDesc +" ";
					if(actualDelete) {
						reminderFacade.markComplete( aTodo.get_id() , true);
					}
					
					break;
				}
			}
		}
		
		
		return response;
	}
	
	public String addToDo( String doDoStr, String email) {
		try{
			doDoStr = doDoStr.toLowerCase();
			doDoStr = doDoStr.replaceAll("add a new reminder to ", "");
			doDoStr = doDoStr.replaceAll("add a reminder to ", "");
			doDoStr = doDoStr.replaceAll("add a to do to ", "");
			doDoStr = doDoStr.replaceAll("add a new to do to ", "");
			doDoStr = doDoStr.replaceAll("add a new to do ", "");
			doDoStr = doDoStr.replaceAll("add new to do to ", "");
			doDoStr = doDoStr.replaceAll("add new to do ", "");
			doDoStr = doDoStr.replaceAll("add a to do ", "");
			doDoStr = doDoStr.replaceAll("add a task ", "");
			doDoStr = doDoStr.replaceAll("add a new task ", "");
			doDoStr = doDoStr.replaceAll("add a new item ", "");
			doDoStr = doDoStr.replaceAll("please add a to do ", "");
			doDoStr = doDoStr.replaceAll("please add a new to do ", "");
			doDoStr = doDoStr.replaceAll("please add a new task ", "");
			doDoStr = doDoStr.replaceAll("please add a new task to my to do list ", "");
			doDoStr = doDoStr.replaceAll("please add a task ", "");
			if (doDoStr.startsWith("get ")) {
				doDoStr = doDoStr.substring(4);
			}
			if (doDoStr.startsWith("to get ")) {
				doDoStr = doDoStr.substring(7);
			}
			
			ToDO todo = new ToDO();
			todo.setTaskDesc(doDoStr);
				todo.setDateCreated(new Date().getTime());
				todo.set_id(""+todo.getDateCreated()+"_"+email);
				todo.setEmail(email);
				reminderFacade.addToDo(todo);
				return doDoStr;
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return null;
		}
	}

public String getMMILocation() {
	Gson  json = new Gson();
	LiveLocations mmiLocation =  json.fromJson(MangoDB.getMMILiveLocations(),  new TypeToken<LiveLocations>() {}.getType());
	//System.out.println(MangoDB.getMMILiveLocations());
	String response = "Map my india is unable to get the locations of the car";
	if (mmiLocation.getStatus() == 200) {
		if (mmiLocation.getDevices() != null) {
			
			for (Device device : mmiLocation.getDevices()) {
				if (device.getDeviceId() == Key.mmiDeviceID) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy h, mm aa");
					TimeZone userTimeZone	=	TimeZone.getTimeZone("Asia/Calcutta");
					sdf.setTimeZone(userTimeZone);
					response = " As of "+sdf.format(new Date(device.getGprsTime()*1000L)) +". Your Car is located at " +device.getAddress() ;
					StringBuilder emailBody = new StringBuilder(response);
					//response += "https://maps.mapmyindia.com/@"+device.getLatitude()+","+device.getLongitude();
					emailBody.append(" <br/><br/>  <br/> \n https://maps.mapmyindia.com/@"+device.getLatitude()+","+device.getLongitude());
					sendEmail(emailBody.toString());
					//MailService.sendWhatAppMsg("919216411835", emailBody.toString());
					MailService.sendWhatAppMsg("917837394152", emailBody.toString());
					break;
				}
			}
		}
	}
	return response;
}

public Device mmiCarCordinates() {
	Gson  json = new Gson();
	LiveLocations mmiLocation =  json.fromJson(MangoDB.getMMILiveLocations(),  new TypeToken<LiveLocations>() {}.getType());
	
	if (mmiLocation.getStatus() == 200) {
		if (mmiLocation.getDevices() != null) {
			
			for (Device device : mmiLocation.getDevices()) {
				if (device.getDeviceId() == Key.mmiDeviceID) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy h, mm aa");
					TimeZone userTimeZone	=	TimeZone.getTimeZone("Asia/Calcutta");
					sdf.setTimeZone(userTimeZone);
					String response = " As of "+sdf.format(new Date(device.getGprsTime()*1000L)) +". Your Car is located at " +device.getAddress() ;
					StringBuilder emailBody = new StringBuilder(response);
					emailBody.append(" <br/><br/>  <br/> \n https://maps.mapmyindia.com/@"+device.getLatitude()+","+device.getLongitude());
					sendEmail(emailBody.toString());
					MailService.sendWhatAppMsg("917837394152", emailBody.toString());
					return device;
				
				}
			}
		}
	}
	return null;
}
 public String getCarLocation() {
	 String healthPingStr = MangoDB.getDocumentWithQuery("wemos-users", "health-ping", "HealthPing", null, true, MangoDB.mlabKeySonu, null) ;
		Gson  json = new Gson();
		HealthPing healthPing = null;
		if (healthPingStr != null && healthPingStr.trim().length() > 0) {
			healthPing = json.fromJson(healthPingStr,  new TypeToken<HealthPing>() {}.getType());
		}
		HealthStatus healthStat = healthPing.getHealthUpdate().get(healthPing.getHealthUpdate().size()-1);
		String healtStr = " Last health update "+healthStat.getTimeStr() +" WIFI details "+healthStat.getWifii()+" . ";
		StringBuilder serviceResponse = new StringBuilder(healtStr);
		StringBuilder emailBody = new StringBuilder(" <b>Track :</b> https://idonotremember-app.appspot.com/map.html <br/><br/>" + healtStr);
		List<UserLocation> top5 =  locationFacade.getRecentLocations();
		  
		SimpleDateFormat sdf = new SimpleDateFormat("h, mm aa");
		TimeZone userTimeZone	=	TimeZone.getTimeZone("Asia/Calcutta");
		sdf.setTimeZone(userTimeZone);
			
			
		for (UserLocation loc: top5) {
			String address = " Time. "+sdf.format(new Date(loc.get_id())) +" . . . " +loc.getLocation()+" . . . Accuracy. " +loc.getAccuracy() +" . . ." ;
			serviceResponse.append(address);
			emailBody.append(" <br/><br/> \n\n"+address+" <br/> \n https://maps.mapmyindia.com/@"+loc.getLat()+","+loc.getLon());
		}
		sendEmail(emailBody.toString());
		return serviceResponse.toString();
	}
 
 private void sendEmail(String emailBody) {
	 EmailVO emalVO = new EmailVO();
		emalVO.setUserName("personal.reminder.notification@gmail.com");
		emalVO.setPassword(Key.email);
		emalVO.setSubject("Car location ");
		emalVO.setHtmlContent(emailBody);
		EmailAddess from = new EmailAddess();
		from.setAddress(emalVO.getUserName());
		
		List<EmailAddess> receipients = new ArrayList<>();
		EmailAddess to = new EmailAddess();
		to.setAddress("sonu.hooda@gmail.com");
		emalVO.setFromAddress(from);
		receipients.add(to);
		emalVO.setToAddress(receipients);
		if (!MailService.sendSimpleMail(emalVO)) {
			System.out.println(" cound not send email");
		}
 }
	
}
