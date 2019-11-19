package com.esp8266.location.facade;

import java.util.HashMap;
import java.util.Map;

import com.communication.phone.text.Key;
import com.esp8266.location.LocationVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import mangodb.MangoDB;

public class LocationFacade {
	 private String httpsURL ="https://www.googleapis.com/geolocation/v1/geolocate?key="+Key.googleLocationAPI;

	public  String getLocation(LocationVO locationVO) {
		Gson  json = new Gson();
		 String data = json.toJson(locationVO, new TypeToken<LocationVO>() {}.getType());
		       
		       Map<String, String> headers = new HashMap<String, String>();
		       headers.put("Content-type", "application/json");
		      
		       String parsedResponse = MangoDB.makeExternalRequest(httpsURL,"POST",data,headers);
		       System.out.println(" parsedResponse "+parsedResponse);
		      return parsedResponse;
		      
	}
}
