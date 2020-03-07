package com.esp8266.bill;

import java.util.ArrayList;
import java.util.List;

public class UtilityBillResponse {
	private List<UtilityBill> bills = new ArrayList<UtilityBill>();

	public List<UtilityBill> getBills() {
		return bills;
	}

	public void setBills(List<UtilityBill> bills) {
		this.bills = bills;
	}

}
