package com.login.vo;

import java.io.Serializable;

public class Settings implements Serializable {
	public static double callCharges = 7;
	public static double smsCharges = 2;
	private String _id;
	private String _import_id;
	private String appTimeZone;
	private String userSuppliedTimeZone;
	private double currentCallCredits ;

	public String getAppTimeZone() {
		return appTimeZone;
	}

	public void setAppTimeZone(String appTimeZone) {
		this.appTimeZone = appTimeZone;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUserSuppliedTimeZone() {
		return userSuppliedTimeZone;
	}

	public void setUserSuppliedTimeZone(String userSuppliedTimeZone) {
		this.userSuppliedTimeZone = userSuppliedTimeZone;
	}

	public double getCurrentCallCredits() {
		return currentCallCredits;
	}

	public void setCurrentCallCredits(double currentCallCredits) {
		this.currentCallCredits = currentCallCredits;
	}

	public String get_import_id() {
		return _import_id;
	}

	public void set_import_id(String _import_id) {
		this._import_id = _import_id;
	}

}
