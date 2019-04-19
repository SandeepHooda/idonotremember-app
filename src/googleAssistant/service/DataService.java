package googleAssistant.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import com.login.vo.LoginVO;
import com.reminder.facade.ReminderFacade;
import com.reminder.vo.ToDO;

public class DataService {
	private ReminderFacade reminderFacade  = new ReminderFacade();
	public List<String> getToDos(String email) {
		
		List<ToDO> toDoList = reminderFacade.getToDos(email);
		List<String> pendingDotos = new ArrayList<String>();
		if (null != toDoList && toDoList.size() > 0) {
			for (ToDO aToDo: toDoList) {
				if (!aToDo.isComplete()) {
					pendingDotos.add(aToDo.getTaskDesc());
				}
			}
		}
		
		return pendingDotos;
	}
	
	public String deleteToDo( String doDoStr, String email) {
		
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
					reminderFacade.markComplete( aTodo.get_id() , true);
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
