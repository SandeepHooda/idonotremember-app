package com.esp8266.location.facade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.cxf.common.util.CollectionUtils;

import com.communication.phone.text.Key;
import com.esp8266.location.GoogleAddress;
import com.esp8266.location.LocationVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LatLang;
import com.login.vo.UserLocation;
import com.login.vo.UserLocationComparator;
import com.login.vo.UserLocations;

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
	            saveLocationInDB( latLang,  address);
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
	
	private void saveLocationInDB(LatLang latLang, GoogleAddress address) {
		Gson  json = new Gson();
		String allLocations = MangoDB.getDocumentWithQuery("wemos-users", "user-locations", UserLocations.id, null, true, MangoDB.mlabKeySonu, null) ;
		System.out.println(" allLocations ="+allLocations);
		UserLocations userLocations = new UserLocations();
		if (allLocations != null && allLocations.trim().length() > 0) {
			userLocations = json.fromJson(allLocations,  new TypeToken<UserLocations>() {}.getType());
		}
		
		Iterator<UserLocation> itr = userLocations.getLocations().iterator();
		while(itr.hasNext()) {
			UserLocation loc = itr.next();
			if (loc.getUuid() >= UserLocations.maxLocationCount) {
				itr.remove();
			}else {
				loc.setUuid(loc.getUuid()+1);
			}
		}
		 UserLocation userLocation = new UserLocation();
			userLocation.set_id(System.currentTimeMillis());
			userLocation.setUuid(1);;
			userLocation.setLat(latLang.getLatitude());
			userLocation.setLon(latLang.getLongitude());
			userLocation.setAccuracy(latLang.getAccuracy());
			userLocation.setLocation( address.getResults().get(0).getFormatted_address() );
			userLocations.getLocations().add(userLocation);
			allLocations = json.toJson(userLocations, new TypeToken<UserLocations>() {}.getType());
			System.out.println(" saving allLocations "+allLocations);
			 MangoDB.createNewDocumentInCollection("wemos-users", "user-locations",  allLocations, MangoDB.mlabKeySonu);
	}
	public List<UserLocation> getRecentLocations() {
		Gson  json = new Gson();
		String allLocations = MangoDB.getDocumentWithQuery("wemos-users", "user-locations", UserLocations.id, null, true, MangoDB.mlabKeySonu, null) ;
		System.out.println(" allLocations ="+allLocations);
		UserLocations userLocations = new UserLocations();
		if (allLocations != null && allLocations.trim().length() > 0) {
			userLocations = json.fromJson(allLocations,  new TypeToken<UserLocations>() {}.getType());
		}
		
		List<UserLocation> locations = userLocations.getLocations();
		Collections.sort(locations, new UserLocationComparator());
		List<UserLocation> top5 = new ArrayList<UserLocation>();
		String lastKnown = "";
		for (UserLocation loc :locations ) {
			if (!loc.getLocation().equalsIgnoreCase(lastKnown)) {
				lastKnown = loc.getLocation();
				top5.add(loc);
			}
		}
		return top5;
	}
}
