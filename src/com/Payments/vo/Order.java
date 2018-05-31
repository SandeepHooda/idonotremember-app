package com.Payments.vo;

import java.util.TreeMap;

public class Order {
	
	private String _id;
	private TreeMap<String, String> orderDetails;
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

}
