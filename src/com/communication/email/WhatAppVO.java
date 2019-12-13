package com.communication.email;

public class WhatAppVO {
	private String msg;
	private String phone;
	private boolean sandeepPushover = true;
	private boolean kusumPushover = false;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isSandeepPushover() {
		return sandeepPushover;
	}
	public void setSandeepPushover(boolean sandeepPushover) {
		this.sandeepPushover = sandeepPushover;
	}
	public boolean isKusumPushover() {
		return kusumPushover;
	}
	public void setKusumPushover(boolean kusumPushover) {
		this.kusumPushover = kusumPushover;
	}

}
