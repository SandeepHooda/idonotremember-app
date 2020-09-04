package com.reminder.vo;

public class CallLogs {
	
	private String _import_id;
	private String _id;
	private String from;
	private String to;
	private String message;

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String get_import_id() {
		return _import_id;
	}
	public void set_import_id(String _import_id) {
		this._import_id = _import_id;
	}
	
}
