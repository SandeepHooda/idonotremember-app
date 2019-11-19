package com.login.vo;

public class LatLang {
private String latitude;
private String longitude;
private String accuracy;
public String getLatitude() {
	return latitude;
}
public void setLatitude(String latitude) {
	this.latitude = latitude;
}
public String getLongitude() {
	return longitude;
}
public void setLongitude(String longitude) {
	this.longitude = longitude;
}

public String getAccuracy() {
	return accuracy;
}
public void setAccuracy(String accuracy) {
	this.accuracy = accuracy;
}
@Override
public String toString() {
	return "LatLang [latitude=" + latitude + ", longitude=" + longitude + ", accuracy=" + accuracy + "]";
}

}
