package com.esp8266.location.facade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.communication.phone.text.Key;
import com.esp8266.location.GoogleAddress;
import com.esp8266.location.LocationVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LatLang;


import mangodb.MangoDB;

public class LocationFacade {
	 private String httpsURL ="https://www.googleapis.com/geolocation/v1/geolocate?key="+Key.googleLocationAPI;

	public  String getLocation(LocationVO locationVO) {
		Gson  json = new Gson();
		 String data = json.toJson(locationVO, new TypeToken<LocationVO>() {}.getType());
		       System.out.println(" request data to google  ="+data);
		       Map<String, String> headers = new HashMap<String, String>();
		       headers.put("Content-type", "application/json");
		       headers.put("Connection", "close");
		       headers.put("Content-Length", ""+data.getBytes().length);
		      
		       String parsedResponse = MangoDB.makeExternalRequest(httpsURL,"POST",data,headers);
		       System.out.println(" parsedResponse "+parsedResponse);
		      return parsedResponse;
		      
	}
	
	public  String getAddress(LatLang latLang) {
		String responseStr = "";
		HttpURLConnection connection = null;
		 try {
			
			 URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?key="+Key.googleLocationToAddress+"&latlng="+latLang.getLatitude()+","+latLang.getLongitude());
			 
	            
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    	    conn.setRequestMethod("GET");
	    	    BufferedReader in = new BufferedReader(    new InputStreamReader(conn.getInputStream()));
	    		String inputLine;
	    		StringBuffer responseBuf = new StringBuffer();
	    		while ((inputLine = in.readLine()) != null) {
	    			responseBuf.append(inputLine);
	    		}
	    		in.close();
	    		
	    	
	            responseStr =responseBuf.toString();
	            Gson  json = new Gson();
	            GoogleAddress address = json.fromJson(responseStr, new TypeToken<GoogleAddress>() {}.getType());
	            System.out.println(" Address "+ address.getResults().get(0).getFormatted_address());
	            return address.getResults().get(0).getFormatted_address();
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	
	        	return "unknown";
	        }finally {
	            if (connection != null) {
	                connection.disconnect();
	              }
	            }
	}
}
