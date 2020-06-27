package com.esp8266.weather;

public class WeatherAlertSnooz {
	private String _id;
	private long time;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

}
