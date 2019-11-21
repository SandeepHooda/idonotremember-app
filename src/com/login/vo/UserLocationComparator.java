package com.login.vo;

import java.util.Comparator;



public class UserLocationComparator implements Comparator<UserLocation> {

	@Override
	public int compare(UserLocation o1, UserLocation o2) {
		return o1.getUuid() - o2.getUuid();
		//sorting in assending order
		
	}

}

