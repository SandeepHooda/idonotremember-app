package com.esp8266.location;


import java.util.List;

public class GoogleAddress {
	private String status;
	private List<AGoogleAddress> results;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<AGoogleAddress> getResults() {
		return results;
	}
	public void setResults(List<AGoogleAddress> results) {
		this.results = results;
	}

}
