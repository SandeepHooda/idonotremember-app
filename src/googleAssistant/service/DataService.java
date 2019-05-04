package googleAssistant.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ws.rs.core.Response;

import com.login.vo.LoginVO;
import com.reminder.facade.ReminderFacade;
import com.reminder.vo.ReminderVO;
import com.reminder.vo.Thing;
import com.reminder.vo.ToDO;

public class DataService {
	SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
	private ReminderFacade reminderFacade  = new ReminderFacade();
	public boolean putMyThing(String email, String item, String location, String quertText) {
		Thing thing = new Thing( email,  item,  location,quertText);
		thing.setDateCreated(new Date().getTime());
		
		return reminderFacade.placeAThing(thing);
		
	}
	public String findMyThing(String email, String item) {
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
			Thing aThing = reminderFacade.findAThing( email,  item);
			if (null !=aThing) {
				return "You have kept your , "+aThing.getItem() +" , "+aThing.getLocation()+" , on "+formatter.format(new Date(aThing.getDateCreated()))+". ";
			}else {
				return "Sorry, but  you never told me that where have you kept your "+item +". ";
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
				String taskDesc = aTodo.getReminderSubject()+ " "+aTodo.getReminderText();
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
	
	public Response addToDo( String doDoStr, String email) {
		try{
			doDoStr = doDoStr.toLowerCase();
			doDoStr = doDoStr.replaceAll("add a new reminder to ", "");
			doDoStr = doDoStr.replaceAll("add a reminder to ", "");
			doDoStr = doDoStr.replaceAll("add a to do to ", "");
			doDoStr = doDoStr.replaceAll("add a new to do to ", "");
			doDoStr = doDoStr.replaceAll("add a new to do ", "");
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
				return Response.ok().entity(reminderFacade.addToDo(todo)).build();
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

}
