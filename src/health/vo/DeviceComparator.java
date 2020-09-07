package health.vo;

import java.util.Comparator;

public class DeviceComparator implements Comparator<Device> {

	public int compare(Device d1, Device d2) {
		return d1.getDisplayOrder()-d2.getDisplayOrder();
	}

	
	
	

}
