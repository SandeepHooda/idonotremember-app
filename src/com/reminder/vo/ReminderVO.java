package com.reminder.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ReminderVO {
	private String _import_id;
	private String _id;
	private String regID;
	private String email;
	private String date;
	private String time;
	private String reminderSubject;
	private String reminderText;
	private String repeatFrequency;
	private boolean makeACall;
	private boolean sendText;
	private String frequencyWithDate; //Once , Monthly, yearly
	private String dayRepeatFrequency;//First Monday
	private String frequencyType = "Day";//Day or Date
	private String displayTime ;
	private long nextExecutionTime;
	private String selectedPhone;
	private boolean anounceOnGoogleAssist = true;
	private static Map<String, String> monthMap = new HashMap<String, String>();
	static {
		monthMap.put("01", " Jan ");
		monthMap.put("02", " Feb ");
		monthMap.put("03", " Mar ");
		monthMap.put("04", " Apr ");
		monthMap.put("05", " May ");
		monthMap.put("06", " Jun ");
		monthMap.put("07", " Jul ");
		monthMap.put("08", " Aug ");
		monthMap.put("09", " Sep ");
		monthMap.put("10", " Oct ");
		monthMap.put("11", " Nov ");
		monthMap.put("12", " Dec ");
		
		
	}
	public String formatDisplayTime(long dateTimeStr, String timeZone ) {
		SimpleDateFormat sdfDiaplay = new SimpleDateFormat("dd MMM yyyy hh:mm a");
		
		TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);
		sdfDiaplay.setTimeZone(userTimeZone);
		
		
		return sdfDiaplay.format(dateTimeStr);
	}
	
	public String formatDisplayTimeWithDay(long dateTimeStr, String timeZone ) {
		SimpleDateFormat sdfDiaplay = new SimpleDateFormat("EEEE , dd MMM yyyy hh:mm a");
		
		TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);
		sdfDiaplay.setTimeZone(userTimeZone);
		
		
		return sdfDiaplay.format(dateTimeStr);
	}

	public String getRegID() {
		return regID;
	}
	public void setRegID(String regID) {
		this.regID = regID;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getReminderSubject() {
		return reminderSubject;
	}
	public void setReminderSubject(String reminderSubject) {
		this.reminderSubject = reminderSubject;
	}
	public String getReminderText() {
		return reminderText;
	}
	public void setReminderText(String reminderText) {
		this.reminderText = reminderText;
	}
	public String getRepeatFrequency() {
		return repeatFrequency;
	}
	public void setRepeatFrequency(String repeatFrequency) {
		this.repeatFrequency = repeatFrequency;
	}
	public boolean isMakeACall() {
		return makeACall;
	}
	public void setMakeACall(boolean makeACall) {
		this.makeACall = makeACall;
	}
	public boolean isSendText() {
		return sendText;
	}
	public void setSendText(boolean sendText) {
		this.sendText = sendText;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getFrequencyWithDate() {
		return frequencyWithDate;
	}
	public void setFrequencyWithDate(String frequencyWithDate) {
		this.frequencyWithDate = frequencyWithDate;
	}
	public String getDayRepeatFrequency() {
		return dayRepeatFrequency;
	}
	public void setDayRepeatFrequency(String dayRepeatFrequency) {
		this.dayRepeatFrequency = dayRepeatFrequency;
	}
	public String getFrequencyType() {
		return frequencyType;
	}
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDisplayTime() {
		return displayTime;
	}
	public void setDisplayTime(String displayTime) {
		this.displayTime = displayTime;
	}
	public long getNextExecutionTime() {
		return nextExecutionTime;
	}
	public void setNextExecutionTime(long nextExecutionTime) {
		this.nextExecutionTime = nextExecutionTime;
	}
	public String getSelectedPhone() {
		return selectedPhone;
	}
	public void setSelectedPhone(String selectedPhone) {
		this.selectedPhone = selectedPhone;
	}

	public boolean isAnounceOnGoogleAssist() {
		return anounceOnGoogleAssist;
	}

	public void setAnounceOnGoogleAssist(boolean anounceOnGoogleAssist) {
		this.anounceOnGoogleAssist = anounceOnGoogleAssist;
	}

	public String get_import_id() {
		return _import_id;
	}

	public void set_import_id(String _import_id) {
		this._import_id = _import_id;
	}

}
