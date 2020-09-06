package com.reminder.facade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LoginVO;
import com.reminder.vo.ReminderVO;
import com.reminder.vo.ReminderVOComparator;
import com.reminder.vo.Thing;
import com.reminder.vo.ToDO;
import com.reminder.vo.ToDoVOComparator;

import mangodb.MangoDB;

public class ReminderFacade {
	public static String timeFormat = "yyyy_M_d HH_mm";
	private static Map<String, Integer> dayToCalDay = new HashMap<String, Integer>();
	private static Map<String, DayOfWeek> dayToLocal = new HashMap<String, DayOfWeek>();
	private static Map<String, Integer> wordsToMath = new HashMap<String, Integer>();
	static {
		dayToCalDay.put("Monday", Calendar.MONDAY);
		dayToCalDay.put("Tuesday", Calendar.TUESDAY);
		dayToCalDay.put("Wednesday", Calendar.WEDNESDAY);
		dayToCalDay.put("Thrusday", Calendar.THURSDAY);
		dayToCalDay.put("Friday", Calendar.FRIDAY);
		dayToCalDay.put("Saturday", Calendar.SATURDAY);
		dayToCalDay.put("Sunday", Calendar.SUNDAY);
		
		dayToLocal.put("Monday", DayOfWeek.MONDAY);
		dayToLocal.put("Tuesday", DayOfWeek.TUESDAY);
		dayToLocal.put("Wednesday", DayOfWeek.WEDNESDAY);
		dayToLocal.put("Thrusday", DayOfWeek.THURSDAY);
		dayToLocal.put("Friday", DayOfWeek.FRIDAY);
		dayToLocal.put("Saturday", DayOfWeek.SATURDAY);
		dayToLocal.put("Sunday", DayOfWeek.SUNDAY);
		
		wordsToMath.put("First", 1);
		wordsToMath.put("Second", 2);
		wordsToMath.put("Third", 3);
		wordsToMath.put("Fourth", 4);
	}
	public boolean addReminder(ReminderVO reminderVO, String timeZone) throws ParseException {
		
		 Gson  json = new Gson();
         
         String email = reminderVO.getEmail();
         if (null == email) {
        	 email = getEmail(reminderVO.getRegID());
         }
         if (null != email) {
        	 reminderVO.setEmail(email);
        	 
     		Date reminderDate = nextReminder(reminderVO, timeZone);
     		reminderVO.setNextExecutionTime(reminderDate.getTime());
     		adjustTimeIfPast(reminderVO, timeZone);
     		reminderVO.set_id(null);
     		if (reminderVO.get_import_id() == null) {
     			reminderVO.set_import_id(""+reminderVO.getNextExecutionTime()+Math.random());
     		}
     		reminderVO.set_import_id(reminderVO.get_import_id()+"_"+email);
        	 String data = json.toJson(reminderVO, new TypeToken<ReminderVO>() {}.getType());
        	 MangoDB.createNewDocumentInCollection("remind-me-on", "reminders", data, null);
     		return true;
         }else {
        	 return false;
         }
         
	}
	
	public boolean updateReminder(ReminderVO reminderVO) throws ParseException {
		
		 Gson  json = new Gson();
         String data = json.toJson(reminderVO, new TypeToken<ReminderVO>() {}.getType());
       	 MangoDB.updateData("remind-me-on", "reminders", data,reminderVO.get_id(), null);
    	return true;
        
        
	}
	public boolean updateSnoozedReminder(ReminderVO reminderVO) throws ParseException {
		
		 Gson  json = new Gson();
        String data = json.toJson(reminderVO, new TypeToken<ReminderVO>() {}.getType());
      	 MangoDB.updateData("remind-me-on", "reminders-snooz", data,reminderVO.get_id(), null);
   	return true;
       
       
	}
	public boolean addToDo(ToDO toDO) throws ParseException {
		Gson  json = new Gson();
         String data = json.toJson(toDO, new TypeToken<ToDO>() {}.getType());
       	 MangoDB.createNewDocumentInCollection("remind-me-on", "to-dos", data, null);
    		return true;
    }
	
	public boolean placeAThing(Thing thing)  {
		Gson  json = new Gson();
         String data = json.toJson(thing, new TypeToken<Thing>() {}.getType());
       	 MangoDB.createNewDocumentInCollection("find-my-things", "things", data, null);
    		return true;
    }
	
	public Thing findAThing(String email, String item)  {
		
		Gson  json = new Gson();
		item = item.replaceAll(" ", "_").toLowerCase();
		System.out.println(" finding "+ email+"_"+item);
		 String data = MangoDB.getDocumentWithQuery("find-my-things", "things", email+"_"+item,null, true, null,null);
		
		 if (data == null || "".equals(data.trim())) {
			 return null;
		 }
		 System.out.println(" found "+data);
		 return json.fromJson(data, new TypeToken<Thing>() {}.getType());
		
       	
    }
	public List<Thing> findEveryThing(String email)  {
		Gson  json = new Gson();
		
		 String data = "["+MangoDB.getDocumentWithQuery("find-my-things", "things",  email,"email", false, null,null)+"]";
		
		 if (data == null || "".equals(data.trim())) {
			 return null;
		 }
		 return json.fromJson(data, new TypeToken<List<Thing>>() {}.getType());
		
       	
    }
	
	public boolean updateToDo(ToDO toDO) throws ParseException {
		Gson  json = new Gson();
         String data = json.toJson(toDO, new TypeToken<ToDO>() {}.getType());
       	 MangoDB.updateData("remind-me-on", "to-dos", data, toDO.get_id(), null);
    		return true;
    }
	
	private static void adjustTimeIfPast(ReminderVO reminderVO, String timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
	   	 TimeZone userTimeZone	=	TimeZone.getTimeZone(timeZone);
	   	
		sdf.setTimeZone(userTimeZone);
		
		Calendar cal = new GregorianCalendar();
		Calendar today = new GregorianCalendar();
		today.setTimeZone(userTimeZone);
		cal.setTimeZone(userTimeZone);
		if (reminderVO.getNextExecutionTime() < today.getTimeInMillis()) {
			reminderVO.setNextExecutionTime(reminderVO.getNextExecutionTime()+ (60000 *60*12)); //+ 12 hours
		}
		
	}
	public static Date nextReminder(ReminderVO reminderVO, String timeZone) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
	   	 TimeZone userTimeZone	=	TimeZone.getTimeZone(timeZone);
	   	 System.out.println(" User timezone "+timeZone);
		sdf.setTimeZone(userTimeZone);
		System.out.println(" Freq Type : "+reminderVO.getFrequencyType());
		if ("Day".equalsIgnoreCase(reminderVO.getFrequencyType())) {
			String[] split = reminderVO.getDayRepeatFrequency().split(" ");
			String[] timeSplit = reminderVO.getTime().split("_");
			
			Calendar cal = new GregorianCalendar();
			Calendar today = new GregorianCalendar();
			today.setTimeZone(userTimeZone);
			cal.setTimeZone(userTimeZone);
			
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
			System.out.println("split[0] : "+split[0] +" "+split[1]);
			if ("Every".equals(split[0])){//Every sunday/monday
				LocalDate ld =  LocalDate.now();
				ld = ld.with(TemporalAdjusters.nextOrSame(dayToLocal.get(split[1])));
				cal.set(Calendar.YEAR, ld.getYear());
				cal.set(Calendar.MONTH, ld.getMonthValue()-1);
				cal.set(Calendar.DATE, ld.getDayOfMonth());
				
				if (cal.before(today)){//Reminder date has past in this month
					ld = ld.with(TemporalAdjusters.next(dayToLocal.get(split[1])));
					cal.set(Calendar.YEAR, ld.getYear());
					cal.set(Calendar.MONTH, ld.getMonthValue()-1);
					cal.set(Calendar.DATE, ld.getDayOfMonth());
				}
				
			}else { //first, second sunday
				cal.set(Calendar.DAY_OF_WEEK, dayToCalDay.get(split[1]));
				cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, wordsToMath.get(split[0]));
				
				if (cal.before(today)){//Reminder date has past in this month
					cal.add(Calendar.MONTH, 1);
					cal.set(Calendar.DAY_OF_WEEK, dayToCalDay.get(split[1]));
					cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, wordsToMath.get(split[0]));
				}
				
			}
			
			
			
		
			
			
			return sdf.parse(""+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.DATE)+" "+reminderVO.getTime());
		}else if ("Monthly".equalsIgnoreCase(reminderVO.getFrequencyWithDate())) {
			String[] dateSplit = reminderVO.getDate().split("_");
			String[] timeSplit = reminderVO.getTime().split("_");
			Calendar cal = new GregorianCalendar();
			Calendar today = new GregorianCalendar();
			today.setTimeZone(userTimeZone);
			cal.setTimeZone(userTimeZone);
			cal.set(Calendar.DATE, Integer.parseInt(dateSplit[2]));
			{//If date is 31 then and this month don't have 31 then cals months needs to be adjusted
				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));// This actually sets correct next month 
			}
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
			System.out.println(cal.getTime()+" #= "+cal.get(Calendar.MONTH));
			cal.get(Calendar.MONTH);
			if (cal.before(today)){//Reminder date has past in this month
				cal.add(Calendar.MONTH, 1);
				System.out.println(" adding a month");
			}
			System.out.println(cal.getTime()+" #= "+cal.get(Calendar.MONTH));
			return sdf.parse(""+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.DATE)+" "+reminderVO.getTime());
		}else if ("Yearly".equalsIgnoreCase(reminderVO.getFrequencyWithDate())) {
			String[] dateSplit = reminderVO.getDate().split("_");
			String[] timeSplit = reminderVO.getTime().split("_");
			Calendar cal = new GregorianCalendar();
			Calendar today = new GregorianCalendar();
			today.setTimeZone(userTimeZone);
			cal.setTimeZone(userTimeZone);
			cal.set(Calendar.MONTH, (Integer.parseInt(dateSplit[1])) -1);
			cal.set(Calendar.DATE, Integer.parseInt(dateSplit[2]));
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
			if (cal.before(today)){//Reminder date has past in this month
				cal.add(Calendar.YEAR, 1);
			}
			return sdf.parse(""+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.DATE)+" "+reminderVO.getTime());
		}
		return sdf.parse(reminderVO.getDate()+" "+reminderVO.getTime());
	}
	public List<ReminderVO> getReminders(String regID) {
		return getReminders( regID,  null);
	}
	public List<ReminderVO> getReminders(String regID, String emailStr) {
		 String email = emailStr ;
		if (null == emailStr) {
			email = getEmail(regID);
		}
		
		 if (null != email) {
			 String sortByExecutionTimeAsc =  "&sort=nextExecutionTime";
			 String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "reminders", email,"email", false, null,sortByExecutionTimeAsc)+"]";
			 Gson  json = new Gson();
			 List<ReminderVO> result  = json.fromJson(data, new TypeToken<List<ReminderVO>>() {}.getType());
			 //Collections.sort(result, new ReminderVOComparator());
			 return result;
		 }else {
			 return new ArrayList<ReminderVO>();
		 }
		
        
	}
	
	public List<ToDO> getToDos(String email) {
		 
		 if (null != email) {
			 String order =  "&s=%7B%22order%22%3A%201%7D";
			 String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "to-dos", email,"email", false, null,order)+"]";
			 Gson  json = new Gson();
			 List<ToDO> result  = json.fromJson(data, new TypeToken<List<ToDO>>() {}.getType());
			 Collections.sort(result, new ToDoVOComparator());
			 return result;
		 }else {
			 return new ArrayList<ToDO>();
		 }
		
       
	}
	public List<ReminderVO> getSnoozedReminders(String email) {
		
			 String sortByExecutionTimeAsc =  "&s=%7B%22nextExecutionTime%22%3A%201%7D";
			 String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "reminders-snooz", email,"email", false, null,sortByExecutionTimeAsc)+"]";
			 Gson  json = new Gson();
			 List<ReminderVO> result  = json.fromJson(data, new TypeToken<List<ReminderVO>>() {}.getType());
			 //Collections.sort(result, new ReminderVOComparator());
			 return result;
		 
       
	}
	public void deleteReminder(String reminderID ) {
		MangoDB.deleteDocument("remind-me-on", "reminders", reminderID, null);
    }
	
	public void forgetMything(String email , String thing ) {
		String thingID = email+"_"+thing.toLowerCase().replaceAll(" ", "_");
		MangoDB.deleteDocument("find-my-things", "things", thingID, null);
    }
	
	public void deleteSnoozedReminder(String reminderID ) {
		MangoDB.deleteDocument("remind-me-on", "reminders-snooz", reminderID, null);
    }
	
	public void markComplete(String todoID ) {
		markComplete( todoID, false );
	}
	public void markComplete(String todoID ,  boolean force) {
		String data = MangoDB.getDocumentWithQuery("remind-me-on", "to-dos", todoID, "_id",false, null, null);
		 Gson  json = new Gson();
		 ToDO toDo  = json.fromJson(data, new TypeToken<ToDO>() {}.getType());
		 toDo.setComplete(!toDo.isComplete());
		 if(toDo.isComplete()) {
			 toDo.setDateCompleted(new Date().getTime());
		 }else {
			 toDo.setDateCompleted(0);
		 }
		 data = json.toJson(toDo, new TypeToken<ToDO>() {}.getType());
		 MangoDB.updateData("remind-me-on", "to-dos", data, toDo.get_id(),null);
    }
	
	
	
	public String getEmail(String regID) {
		String email = null;
		String data = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users", regID, null,true, null, null);
		if (null == data || data.trim().length() ==0) {
			data = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-android-phone", regID, null,true, null, null);
		}
		if (null != data) {
			Gson  json = new Gson();
			LoginVO loginVO = json.fromJson(data, new TypeToken<LoginVO>() {}.getType());
			if (null != loginVO) {
				email = loginVO.getEmailID();
			}
			
		}
		
		return email;
	}

}
