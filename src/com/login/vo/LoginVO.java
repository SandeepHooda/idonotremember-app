package com.login.vo;

import java.util.UUID;

public class LoginVO {
	
	private String accessToken;
	private String refreshToken;
	private String _id ;
	private String regID ;
	private String emailID;
	private String name;
    private String appTimeZone;
	private String userSuppliedTimeZone;
	private String errorMessage;
	private long loginTime;
	private Settings userSettings;
	private String ipAddress;
	private String type;//signin , signup
	//private IPtoLocation ipAddressLocation;
	//private String userAgent;
	//private String completeHeaders;
	//private UserAgent userAgentObj;
	//private Map<String, String> requestHeaders;
	
	private String password = null;//"Welcome";
	private String passwordConfirm = null;//"Welcome";
	private String userName = null;//"pooja.arora@paytm.com";
	private boolean emailValidated;
	 
	
	public LoginVO() {
		this.regID = UUID.randomUUID().toString();
		this._id = this.regID;
	}
	public String getRegID() {
		return regID;
	}
	public void setRegID(String regID) {
		this.regID = regID;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getAppTimeZone() {
		return appTimeZone;
	}
	public void setAppTimeZone(String appTimeZone) {
		this.appTimeZone = appTimeZone;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public long getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	public Settings getUserSettings() {
		return userSettings;
	}
	public void setUserSettings(Settings userSettings) {
		this.userSettings = userSettings;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	public boolean isEmailValidated() {
		return emailValidated;
	}
	public void setEmailValidated(boolean emailValidated) {
		this.emailValidated = emailValidated;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
