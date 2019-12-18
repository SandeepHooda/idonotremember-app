package com.esp8266.location;

public class LatLang {
	private  String _id = "safemate-kusum";
	public LatLang() {
		
	}
	public LatLang(double lat, double lan, String label) {
		this.lat = lat;
		this.lan = lan;
		this.label = label;
	}
	public LatLang(double lat, double lan, String label, String dbCollection , long gpsTime, long gprsTime) {
		this.lat = lat;
		this.lan = lan;
		this.label = label;
		this.locationEntryTime = System.currentTimeMillis();
		this._id = dbCollection;
		this.gpsTime = gpsTime;
		this.gprsTime = gprsTime;
	}
	private double lat;
	private double lan;
	private String label;
	private String address;
	private boolean atKnownLocation;
	private double distanceFromNearestKnow;
	private long locationEntryTime;
	private long gpsTime;
	private long gprsTime;
	private String comment;
	private String time;
	private long battery_percent;
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLan() {
		return lan;
	}
	public void setLan(double lan) {
		this.lan = lan;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isAtKnownLocation() {
		return atKnownLocation;
	}
	public void setAtKnownLocation(boolean atKnownLocation) {
		this.atKnownLocation = atKnownLocation;
	}
	public double getDistanceFromNearestKnow() {
		return distanceFromNearestKnow;
	}
	public void setDistanceFromNearestKnow(double distanceFromNearestKnow) {
		this.distanceFromNearestKnow = distanceFromNearestKnow;
	}
	public long getLocationEntryTime() {
		return locationEntryTime;
	}
	public void setLocationEntryTime(long locationEntryTime) {
		this.locationEntryTime = locationEntryTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String get_id() {
		return _id;
	}
	public long getGpsTime() {
		return gpsTime;
	}
	public void setGpsTime(long gpsTime) {
		this.gpsTime = gpsTime;
	}
	public long getGprsTime() {
		return gprsTime;
	}
	public void setGprsTime(long gprsTime) {
		this.gprsTime = gprsTime;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public long getBattery_percent() {
		return battery_percent;
	}
	public void setBattery_percent(long battery_percent) {
		this.battery_percent = battery_percent;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
