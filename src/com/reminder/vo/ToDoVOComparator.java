package com.reminder.vo;

import java.util.Comparator;

public class ToDoVOComparator implements Comparator<ToDO>{

	@Override
	public int compare(ToDO o1, ToDO o2) {
		if (o1.getOrder() - o2.getOrder() > 0) {
			return -1;
		}else {
			return 1;
		}
		
	}

}

