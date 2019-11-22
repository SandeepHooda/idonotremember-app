package com.esp8266.location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.login.vo.UserLocation;

public class HealthPing {
	private String _id ="HealthPing";
	private List<HealthStatus> healthUpdate = new ArrayList<HealthStatus>();
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public List<HealthStatus> getHealthUpdate() {
		return healthUpdate;
	}
	public void setHealthUpdate(List<HealthStatus> healthUpdate) {
		this.healthUpdate = healthUpdate;
	}
	
	public class HealthStatus{
		private long time;
		private String wifii;
		private String timeStr;
		
		SimpleDateFormat sdf = new SimpleDateFormat("h, mm aa");
		TimeZone userTimeZone	=	TimeZone.getTimeZone("Asia/Calcutta");
		
		
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
			sdf.setTimeZone(userTimeZone);
			timeStr = sdf.format(new Date(time)) ;
		}
		public String getWifii() {
			return wifii;
		}
		public void setWifii(String wifii) {
			this.wifii = wifii;
		}
		public String getTimeStr() {
			return timeStr;
		}
		public void setTimeStr(String timeStr) {
			this.timeStr = timeStr;
		}
	}
}
