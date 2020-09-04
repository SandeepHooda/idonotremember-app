package com.esp8266.location.facade;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.communication.email.EmailAddess;
import com.communication.email.MailService;
import com.communication.phone.call.MakeACall;
import com.communication.phone.text.Key;
import com.esp8266.bill.UtilityBillResponse;
import com.esp8266.location.GoogleAddress;
import com.esp8266.location.HealthPing;
import com.esp8266.location.HealthPing.HealthStatus;
import com.esp8266.location.LocationVO;
import com.esp8266.location.Utils;
import com.esp8266.location.mapMyIndia.Device;
import com.esp8266.location.mapMyIndia.safemate.Address;
import com.esp8266.location.mapMyIndia.safemate.Position;
import com.esp8266.location.mapMyIndia.safemate.Pt;
import com.esp8266.location.mapMyIndia.safemate.SafeMateDevice;
import com.esp8266.weather.Current;
import com.esp8266.weather.Hourly;
import com.esp8266.weather.WeatherAlertSnooz;
import com.esp8266.weather.WeatherResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.LatLang;
import com.login.vo.UserLocation;
import com.login.vo.UserLocationComparator;
import com.login.vo.UserLocations;
import com.reminder.vo.CallLogs;

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
		String allLocations = MangoDB.getDocumentWithQuery("remind-me-on", "user-locations", UserLocations.id, null, true, null, null) ;
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
			
			 MangoDB.createNewDocumentInCollection("remind-me-on", "user-locations",  allLocations, null);
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
	public UtilityBillResponse getUtilityBills() {
		
		return null;
	}
	public WeatherAlertSnooz snoozAlerts(){
		String weatherAlertSnooz = MangoDB.getDocumentWithQuery("remind-me-on", "weather-alert-snooz", "weather-alert-snooz", null, true, null, null) ;
		Gson  json = new Gson();
		 WeatherAlertSnooz snoozUntil = json.fromJson(weatherAlertSnooz,  new TypeToken<WeatherAlertSnooz>() {}.getType());
		 System.out.println(weatherAlertSnooz);
		 snoozUntil.setTime(System.currentTimeMillis()+ 1000*60*60*12);//12 hour
		 weatherAlertSnooz = json.toJson(snoozUntil,  new TypeToken<WeatherAlertSnooz>() {}.getType());
		 MangoDB. updateData("remind-me-on", "weather-alert-snooz",  weatherAlertSnooz, "weather-alert-snooz",  null);
		 return snoozUntil;
	}
	
	public WeatherResponse getWeather( ) {
		WeatherResponse weather = new WeatherResponse();
		try {
			String urlStr = "https://api.openweathermap.org/data/2.5/onecall?units=metric&lat=30.7&lon=76.86&appid="+Key.weatherKey;
			
			
			 URL url = new URL(urlStr);
			 long startTime = System.currentTimeMillis();
	            
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
	    		Gson  json = new Gson();
	    		
	    		weather = json.fromJson(responseBuf.toString(),  new TypeToken<WeatherResponse>() {}.getType());
	    	
	    		Current current = weather.getCurrent();
	    		current.setWindSpeed(current.getWindSpeed()*3.6);//M per sec to Km per hour
	    		EmailAddess toAddress = new EmailAddess();
				 toAddress.setAddress("sonu.hooda@gmail.com");
				 
				 String weatherAlertSnooz = MangoDB.getDocumentWithQuery("remind-me-on", "weather-alert-snooz", "weather-alert-snooz", null, true, null, null) ;
				
				 WeatherAlertSnooz snoozUntil = json.fromJson(weatherAlertSnooz,  new TypeToken<WeatherAlertSnooz>() {}.getType());
				 System.out.println(weatherAlertSnooz);
				 
				 boolean highWindSpeed = false;
				 int highWindthreshHold = 25;
				 if (current.getWindSpeed() > highWindthreshHold) {
					 highWindSpeed = true;
				 }
					 
				 List<Hourly> hourly = weather.getHourly();
					for (int i =0;i <=4;i++ ) {
						hourly.get(i).setWindSpeed(hourly.get(i).getWindSpeed()*3.6);
						if( hourly.get(i).getWindSpeed() > highWindthreshHold ) {
							 highWindSpeed = true;
						}
					}		
					 
				 
				
	    		if (highWindSpeed && snoozUntil.getTime() < System.currentTimeMillis()) {
	    			
	    			DataService.sendPushOverNotification("Wind speed : "+current.getWindSpeed(),Key.sandeepPhone, true );
	    			CallLogs callLog = new CallLogs();
	    			callLog.set_id(""+new Date().getTime());
	    			callLog.setFrom("sonu.hooda@gmail.com");
	    			callLog.setTo("919216411835");
	    			
	    			callLog.setMessage("Please park your car inside. High spped winds are blowing at speed of "+current.getWindSpeed());
	    			String logsJson = json.toJson(callLog, new TypeToken<CallLogs>() {}.getType());
	    			 MangoDB.createNewDocumentInCollection("remind-me-on", "call-logs", logsJson, null);
	    			 MakeACall.call(callLog.getTo(), callLog.get_id(),"+12222222222");
	    			 
	    			 new  MailService().sendSimpleMail(MailService.prepareEmailVO(toAddress, "Wind speed : "+current.getWindSpeed(),	""+current.getWindSpeed(), null, null));
		    			
	    		
	    		}
	    		
	           return weather; 
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public com.esp8266.location.LatLang userGeoFencingDistance(List<com.esp8266.location.LatLang> favLocations , String userName) {
		String dbCollection  = "safemate-"+userName;
		
		 SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
			TimeZone userTimeZone	=	TimeZone.getTimeZone("Asia/Calcutta");
			sdf.setTimeZone(userTimeZone);
			String time_str = sdf.format(new Date());
		
		//User location from DB
		String userLocationStr = MangoDB.getDocumentWithQuery("remind-me-on", dbCollection, dbCollection, null, true, null, null) ;
		Gson  json = new Gson();
		com.esp8266.location.LatLang userLocationDB = new com.esp8266.location.LatLang();
		if (userLocationStr != null && userLocationStr.trim().length() > 0) {
			userLocationDB = json.fromJson(userLocationStr,  new TypeToken<com.esp8266.location.LatLang>() {}.getType());
		}else {
			userLocationStr = json.toJson(userLocationDB,  new TypeToken<com.esp8266.location.LatLang>() {}.getType());
			MangoDB.createNewDocumentInCollection("remind-me-on" , dbCollection,  userLocationStr, null);//create for the first time
		}
		
		
				
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
		com.esp8266.location.LatLang currentLocation = new com.esp8266.location.LatLang(current_lat,  current_lan, nearestFavLoc.getLabel(), dbCollection, pos.getGpsStatusTime(), pos.getGprsStatusTime());
		currentLocation.setDistanceFromNearestKnow(distanceFromFav);
		currentLocation.setBattery_percent(pos.getBatteryPercent());
		currentLocation.setTime(time_str);
		Address add =pos.getAddress();
		currentLocation.setAddress(add.getHouseNo()+" "+add.getStreet()+" "+add.getCity()+" near "+add.getPOI() + "  http://maps.google.com/maps?&z=10&q="+pt.getY()+"+"+pt.getX()+"(Pool+Location)&mrt=yp)");
		int safeDistancethreahHold = 100;
		if (!pos.getGpsStatus()) {
			safeDistancethreahHold = 1000;
		}
		
		if (distanceFromFav <= safeDistancethreahHold) {
			currentLocation.setAtKnownLocation(true);
		}
		
		System.out.println("  distanceFromFav "+distanceFromFav+" "+nearestFavLoc.getLabel());
		
		
		boolean changeInDBState = false;
		//Entering or existing any klnown location
		if (userLocationDB.isAtKnownLocation()  && !currentLocation.isAtKnownLocation()) {//existing
			Address address = pos.getAddress();
			DataService.sendPushOverNotification(userName +" has started from "+userLocationDB.getLabel()+". At "+address.getHouseNo()+" "+address.getCity(),Key.sandeepPhone, true );
			changeInDBState = true;
		}else if (!userLocationDB.isAtKnownLocation()  && currentLocation.isAtKnownLocation()) {//entering
			DataService.sendPushOverNotification(userName +" has reached  "+currentLocation.getLabel(),Key.sandeepPhone, true );
			changeInDBState = true;
		}
		boolean  battryNotificationSentDB = userLocationDB.isBattryNotificationSent();
		
		if (currentLocation.getBattery_percent() >= 30 && currentLocation.getBattery_percent() <= 95) { // 30 - 95
			
			if (battryNotificationSentDB) {
				changeInDBState = true;
				currentLocation.setBattryNotificationSent(false);
			}
		}else {
			if (!battryNotificationSentDB) {
				
				if (currentLocation.getBattery_percent() > 95) {// after 95
					DataService.sendPushOverNotification(userName +" device is fully charged.  Battery level: "+currentLocation.getBattery_percent(),Key.sandeepPhone, false );
				}else {//below 30
					DataService.sendPushOverNotification(userName +" device need charging. Battery level:  "+currentLocation.getBattery_percent(),Key.sandeepPhone, false );
				}
				
				changeInDBState = true;
				
			}
			currentLocation.setBattryNotificationSent(true);
		}

		double distanceFromLastSaved = 1000* Utils.distance(userLocationDB.getLat(), userLocationDB.getLan(), current_lat, current_lan, "K");
		if ((distanceFromLastSaved > safeDistancethreahHold ) || changeInDBState) {
			//kusum is moving to update time
			userLocationStr = json.toJson(currentLocation,  new TypeToken<com.esp8266.location.LatLang>() {}.getType());
			MangoDB.createNewDocumentInCollection("remind-me-on" , dbCollection,  userLocationStr,null);
		}
		
		
		
		currentLocation.setComment("Stay at current location minutes : "+((currentLocation.getLocationEntryTime() - userLocationDB.getLocationEntryTime())/60000 ) +
				" last GPS update in minutes "+((System.currentTimeMillis() - currentLocation.getGpsTime()*1000 )/60000 )+ " conn: "+pos.getGpsStatus()+
				" last GPRS update in minutes "+((System.currentTimeMillis() - currentLocation.getGprsTime()*1000 )/60000 )+ " conn: "+pos.getGprsStatus());
		return currentLocation;
	}
	
	public List<UserLocation> getRecentLocations() {
		//sendToRaspberryPi("55 Sector 25");
		Gson  json = new Gson();
		String allLocations = MangoDB.getDocumentWithQuery("wemos-users", "user-locations", UserLocations.id, null, true, null, null) ;
	
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
		String healthPingStr = MangoDB.getDocumentWithQuery("wemos-users", "health-ping", "HealthPing", null, true, null, null) ;
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
		MangoDB.createNewDocumentInCollection("wemos-users", "health-ping",  healthPingStr, null);
	}
}
