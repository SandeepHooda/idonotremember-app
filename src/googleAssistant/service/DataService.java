package googleAssistant.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import com.login.vo.LoginVO;
import com.reminder.facade.ReminderFacade;
import com.reminder.vo.ToDO;

public class DataService {
	private ReminderFacade reminderFacade  = new ReminderFacade();
	public List<String> getToDos(String email) {
		
		List<ToDO> toDoList = reminderFacade.getToDos("sonu.hooda@gmail.com");
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
	
	public Response addToDo( String doDoStr) {
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
			doDoStr = doDoStr.replaceAll("please add a to do ", "");
			doDoStr = doDoStr.replaceAll("please add a new to do ", "");
			doDoStr = doDoStr.replaceAll("please add a new task ", "");
			doDoStr = doDoStr.replaceAll("please add a new task to my to do list ", "");
			doDoStr = doDoStr.replaceAll("please add a task ", "");
			if (doDoStr.startsWith("get ")) {
				doDoStr = doDoStr.substring(4);
			}
			String email = "sonu.hooda@gmail.com";
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
