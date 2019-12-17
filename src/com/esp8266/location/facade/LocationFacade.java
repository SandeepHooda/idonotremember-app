package com.esp8266.location.facade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.communication.phone.text.Key;
import com.esp8266.location.GoogleAddress;
import com.esp8266.location.HealthPing;
import com.esp8266.location.HealthPing.HealthStatus;
import com.esp8266.location.LocationVO;
import com.esp8266.location.Utils;
import com.esp8266.location.mapMyIndia.Device;
import com.esp8266.location.mapMyIndia.safemate.Position;
import com.esp8266.location.mapMyIndia.safemate.Pt;
import com.esp8266.location.mapMyIndia.safemate.SafeMateDevice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LatLang;
import com.login.vo.UserLocation;
import com.login.vo.UserLocationComparator;
import com.login.vo.UserLocations;

import googleAssistant.service.DataService;
import mangodb.MangoDB;

public class LocationFacade {
	Pattern societyAddres = Pattern.compile("GH[S]{0,1}[-]{0,1}[\\s]{0,1}(.*)");
	Pattern scoPattern = Pattern.compile("SCO[-]{0,1}[\\s]{0,1}(.*)");
	Pattern houseNoPattern = Pattern.compile("H.no[\\.]{0,1}[\\s]{0,1}(.*)");
	 
	
	
	 private String httpsURL ="https://www.googleapis.com/geolocation/v1/geolocate?key="+Key.googleLocationAPI;

	public  String getLocation(LocationVO locationVO) {
		Gson  json = new Gson();
		 String data = json.toJson(locationVO, new TypeToken<LocationVO>() {}.getType());
		       
		       Map<String, String> headers = new HashMap<String, String>();
		       headers.put("Content-type", "application/json");
		       headers.put("Connection", "close");
		       headers.put("Content-Length", ""+data.getBytes().length);
		      
		       String parsedResponse = MangoDB.makeExternalRequest(httpsURL,"POST",data,headers);
		  
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
	           String addressFormatted = address.getResults().get(0).getFormatted_address();
	            System.out.println(" Address "+ addressFormatted);
	            
	            saveLocationInDB( latLang,  address);
	            sendToRaspberryPi(addressFormatted);
	            return addressFormatted;
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	
	        	return "unknown";
	        }finally {
	            if (connection != null) {
	                connection.disconnect();
	              }
	            }
	}
	
	private void sendToRaspberryPi(String addressFormatted ) {
		try {
			String urlStr = "http://sanhoo.duckdns.org:5000/raspberry/text2speach/"+URLEncoder.encode("Sandeep is at. "+addressFormatted, "UTF-8");
					//"https://idonotremember-app.appspot.com/ws/location/recent5";
			//http://sanhoo.duckdns.org:5000/altoLocationUpdate?location="+URLEncoder.encode(addressFormatted, "UTF-8")
			
			 URL url = new URL(urlStr);
			 //long startTime = System.currentTimeMillis();
	            
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setConnectTimeout(2000);
	            conn.setReadTimeout(2000);
	    	    conn.setRequestMethod("GET");
	    	    BufferedReader in = new BufferedReader(    new InputStreamReader(conn.getInputStream()));
	    		String inputLine;
	    		StringBuffer responseBuf = new StringBuffer();
	    		while ((inputLine = in.readLine()) != null) {
	    			responseBuf.append(inputLine);
	    		}
	    		in.close();
	    		//System.out.println(" raspberry response in "+(System.currentTimeMillis() - startTime)+ " "+responseBuf.toString());
	    	
	            
		}catch(Exception e) {
			//e.printStackTrace();
		}
	}
	private void saveLocationInDB(LatLang latLang, GoogleAddress address) {
		Gson  json = new Gson();
		String allLocations = MangoDB.getDocumentWithQuery("wemos-users", "user-locations", UserLocations.id, null, true, MangoDB.mlabKeySonu, null) ;
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
			userLocation.setNwCount(latLang.getNwCount());
			userLocation.setWifii(latLang.getWifii());
			try {
				userLocation.setAccuracy(latLang.getAccuracy().substring(0,latLang.getAccuracy().indexOf(".") ));
			}catch(Exception e) {
				userLocation.setAccuracy(latLang.getAccuracy());
			}
			
			userLocation.setLocation( address.getResults().get(0).getFormatted_address() );
			userLocations.getLocations().add(userLocation);
			allLocations = json.toJson(userLocations, new TypeToken<UserLocations>() {}.getType());
			
			 MangoDB.createNewDocumentInCollection("wemos-users", "user-locations",  allLocations, MangoDB.mlabKeySonu);
	}
	public String mmiLocation() {
		return new DataService().getMMILocation();
	}
	public Device mmiCarCordinates() {
		return new DataService().mmiCarCordinates();
	}
	public SafeMateDevice getSafeMateLocation() {
		return new DataService().getSafeMateLocation();
	}
	public com.esp8266.location.LatLang userGeoFencingDistance(List<com.esp8266.location.LatLang> favLocations , String userName) {
		String dbCollection  = "safemate-"+userName;
		//Get current position from safemate servers
		double distanceFromFav = 1000000;
		com.esp8266.location.LatLang nearestFavLoc =null;
		SafeMateDevice safeMateDevice = new DataService().getSafeMateLocation();
		Position pos = safeMateDevice.getPositions().get(0);
		Pt pt= pos.getAddress().getPt();
		double current_lat = pt.getY(); 
		double current_lan = pt.getX();
		for (com.esp8266.location.LatLang aFavLoc: favLocations) {
			double distanceMeters = 1000* Utils.distance(aFavLoc.getLat(), aFavLoc.getLan(), current_lat, current_lan, "K");
			if (distanceFromFav > distanceMeters ) {
				distanceFromFav = distanceMeters ;
				nearestFavLoc = aFavLoc;
			}
		}
		com.esp8266.location.LatLang currentLocation = new com.esp8266.location.LatLang(current_lat,  current_lan, nearestFavLoc.getLabel(), userName, pos.getGpsStatusTime(), pos.getGpsStatusTime());
		currentLocation.setDistanceFromNearestKnow(distanceFromFav);
		if (distanceFromFav < 80) {
			currentLocation.setAtKnownLocation(true);
		}
		
		//User location from DB
		String userLocationStr = MangoDB.getDocumentWithQuery("wemos-users", dbCollection, dbCollection, null, true, MangoDB.mlabKeySonu, null) ;
		Gson  json = new Gson();
		com.esp8266.location.LatLang userLocationDB = currentLocation;
		if (userLocationStr != null && userLocationStr.trim().length() > 0) {
			userLocationDB = json.fromJson(userLocationStr,  new TypeToken<com.esp8266.location.LatLang>() {}.getType());
		}else {
			userLocationStr = json.toJson(userLocationDB,  new TypeToken<com.esp8266.location.LatLang>() {}.getType());
			MangoDB.createNewDocumentInCollection("wemos-users", dbCollection,  userLocationStr, MangoDB.mlabKeySonu);//create for the first time
		}
		
		
		//Entering or existing any klnown location
		if (userLocationDB.isAtKnownLocation()  && !currentLocation.isAtKnownLocation()) {//existing
			DataService.sendPushOverNotification(userName +" has started from "+userLocationDB.getLabel(),Key.sandeepPhone, true );
		}else if (!userLocationDB.isAtKnownLocation()  && currentLocation.isAtKnownLocation()) {//entering
			DataService.sendPushOverNotification(userName +" has reached  "+currentLocation.getLabel(),Key.sandeepPhone, true );
		}
		

		double distanceFromLastSaved = 1000* Utils.distance(userLocationDB.getLat(), userLocationDB.getLan(), current_lat, current_lan, "K");
		if (distanceFromLastSaved > 40) {
			//kusum is moving to update time
			userLocationStr = json.toJson(currentLocation,  new TypeToken<com.esp8266.location.LatLang>() {}.getType());
			MangoDB.createNewDocumentInCollection("wemos-users", dbCollection,  userLocationStr, MangoDB.mlabKeySonu);
		}
		
		
		
		currentLocation.setComment("Stay at current location minutes : "+((currentLocation.getLocationEntryTime() - userLocationDB.getLocationEntryTime())/60000 ) +
				" GPS time "+((System.currentTimeMillis() - currentLocation.getGprsTime()*1000 )/60000 )+
				" GPRS time "+((System.currentTimeMillis() - currentLocation.getGprsTime()*1000 )/60000 ));
		return currentLocation;
	}
	
	public List<UserLocation> getRecentLocations() {
		//sendToRaspberryPi("55 Sector 25");
		Gson  json = new Gson();
		String allLocations = MangoDB.getDocumentWithQuery("wemos-users", "user-locations", UserLocations.id, null, true, MangoDB.mlabKeySonu, null) ;
	
		UserLocations userLocations = new UserLocations();
		if (allLocations != null && allLocations.trim().length() > 0) {
			userLocations = json.fromJson(allLocations,  new TypeToken<UserLocations>() {}.getType());
		}
		
		List<UserLocation> locations = userLocations.getLocations();
		Collections.sort(locations, new UserLocationComparator());
		List<UserLocation> top5 = new ArrayList<UserLocation>();
		List<String> lastKnown = new ArrayList<String>();
		int i=0;
		for (UserLocation loc :locations ) {
			String currentLoc = loc.getLocation();
			Matcher societyMatcher = societyAddres.matcher(currentLoc);
			 if (societyMatcher.find()) {
				 currentLoc = "Group Housing Society "+ societyMatcher.group(1);
			 }
			 
			 Matcher scoMatcher = scoPattern.matcher(currentLoc);
			 if (scoMatcher.find()) {
				 currentLoc = "S.C.O. "+ scoMatcher.group(1);
			 }
			 
			 Matcher houseNoMatcher = houseNoPattern.matcher(currentLoc);
			 if (houseNoMatcher.find()) {
				 currentLoc = "House Number "+ houseNoMatcher.group(1);
			 }
			 
			if (lastKnown.indexOf(currentLoc) <0) {
				loc.setLocation(currentLoc);
				lastKnown.add(currentLoc);
				top5.add(loc);
				i++;
				
			}
			if (i>=5) {
				break;
			}
		}
		return top5;
	}
	public void healthPing(String wifii) {
		String healthPingStr = MangoDB.getDocumentWithQuery("wemos-users", "health-ping", "HealthPing", null, true, MangoDB.mlabKeySonu, null) ;
		Gson  json = new Gson();
		HealthPing healthPing = null;
		if (healthPingStr != null && healthPingStr.trim().length() > 0) {
			healthPing = json.fromJson(healthPingStr,  new TypeToken<HealthPing>() {}.getType());
		}else {
			 healthPing = new HealthPing();
		}
		HealthStatus status = healthPing.new HealthStatus();
		status.setTime(System.currentTimeMillis());
		status.setWifii(wifii);
		healthPing.getHealthUpdate().add(status);
		
		healthPingStr = json.toJson(healthPing, new TypeToken<HealthPing>() {}.getType());
		MangoDB.createNewDocumentInCollection("wemos-users", "health-ping",  healthPingStr, MangoDB.mlabKeySonu);
	}
}
