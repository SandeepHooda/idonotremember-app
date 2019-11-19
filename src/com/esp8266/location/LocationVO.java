package com.esp8266.location;

import java.util.List;

public class LocationVO {
	
	private int homeMobileCountryCode;
	private int homeMobileNetworkCode;
	private String radioType;
	private String carrier;
	private List<WiFiVO> wifiAccessPoints;
	public int getHomeMobileCountryCode() {
		return homeMobileCountryCode;
	}
	public void setHomeMobileCountryCode(int homeMobileCountryCode) {
		this.homeMobileCountryCode = homeMobileCountryCode;
	}
	public int getHomeMobileNetworkCode() {
		return homeMobileNetworkCode;
	}
	public void setHomeMobileNetworkCode(int homeMobileNetworkCode) {
		this.homeMobileNetworkCode = homeMobileNetworkCode;
	}
	public String getRadioType() {
		return radioType;
	}
	public void setRadioType(String radioType) {
		this.radioType = radioType;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public List<WiFiVO> getWifiAccessPoints() {
		return wifiAccessPoints;
	}
	public void setWifiAccessPoints(List<WiFiVO> wifiAccessPoints) {
		this.wifiAccessPoints = wifiAccessPoints;
	}
	@Override
	public String toString() {
		return "LocationVO [homeMobileCountryCode=" + homeMobileCountryCode + ", homeMobileNetworkCode="
				+ homeMobileNetworkCode + ", radioType=" + radioType + ", carrier=" + carrier + ", wifiAccessPoints="
				+ wifiAccessPoints + "]";
	}

}
