package com.reminder.vo;

public class Thing {

	public Thing() {
		
	}
	public Thing (String email, String item, String location) {
		this.email = email;
		this.location = location;
		this.item = item;
		this._id = email +"_"+item;
	}
	private String _id;
	private long dateCreated;
	private String email;
	private String location;
	private String item;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public long getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
}
