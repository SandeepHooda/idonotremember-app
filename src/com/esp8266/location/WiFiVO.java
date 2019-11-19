package com.esp8266.location;

public class WiFiVO {
	
	private String macAddress;
	private int signalStrength;
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public int getSignalStrength() {
		return signalStrength;
	}
	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}
	@Override
	public String toString() {
		return "WiFiVO [macAddress=" + macAddress + ", signalStrength=" + signalStrength + "]";
	}

}
