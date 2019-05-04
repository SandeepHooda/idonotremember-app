package com.communication.email;

public class EmailAddess {

	public EmailAddess() {
		
	}
	public EmailAddess(String address, String label) {
		this.address = address;
		this.label = label;
	}
	private String address;
	private String label;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
