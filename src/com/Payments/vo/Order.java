package com.Payments.vo;

import java.util.TreeMap;

public class Order {
	
	private String _id;
	private TreeMap<String, String> orderDetails;
	private String initAppName;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public TreeMap<String, String> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(TreeMap<String, String> orderDetails) {
		this.orderDetails = orderDetails;
	}
	public String getInitAppName() {
		return initAppName;
	}
	public void setInitAppName(String initAppName) {
		this.initAppName = initAppName;
	}

}
