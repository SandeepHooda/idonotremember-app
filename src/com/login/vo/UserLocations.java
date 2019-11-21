package com.login.vo;

import java.util.ArrayList;
import java.util.List;

public class UserLocations {
	public static final int maxLocationCount = 1000;
	public static final   String id = "allLocations";
	private   String _id = id;
	private List<UserLocation> locations = new ArrayList<UserLocation>();
	public List<UserLocation> getLocations() {
		return locations;
	}
	public void setLocations(List<UserLocation> locations) {
		this.locations = locations;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public static int getMaxlocationcount() {
		return maxLocationCount;
	}
	

}
