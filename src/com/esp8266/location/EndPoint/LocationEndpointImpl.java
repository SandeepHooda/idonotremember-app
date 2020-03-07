package com.esp8266.location.EndPoint;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import com.Constants;
import com.esp8266.bill.UtilityBillResponse;
import com.esp8266.location.LocationVO;
import com.esp8266.location.WiFiVO;
import com.esp8266.location.facade.LocationFacade;
import com.esp8266.location.mapMyIndia.Device;
import com.esp8266.location.mapMyIndia.safemate.SafeMateDevice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.facade.LoginFacade;
import com.login.vo.ContactUS;
import com.login.vo.LatLang;
import com.login.vo.LoginVO;
import com.login.vo.Phone;
import com.login.vo.Settings;
import com.login.vo.UserLocation;
import com.reminder.facade.ReminderFacade;

import mangodb.MangoDB;

public class LocationEndpointImpl implements LocationEndpoint {
	private LocationFacade locationFacade;
	
	public LocationFacade getLocationFacade() {
		return locationFacade;
	}

	public void setLocationFacade(LocationFacade locationFacade) {
		this.locationFacade = locationFacade;
	}


	@Override
	public Response wifiNetworks(LocationVO locationVO, HttpServletRequest request) {
		
		try{
			/*loginVO = loginFacade.loginWithPassword(loginVO);
			if (null == loginVO) {
				return Response.status(Response.Status.UNAUTHORIZED).entity(false).build();
			}else {
				return Response.ok().entity(loginVO).build();
			}*/
			System.out.println(" request "+locationVO);
			return Response.ok().entity(locationFacade.getLocation(locationVO)).build();
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response getAddress(LatLang latLang, HttpServletRequest request) {
		try{
			/*loginVO = loginFacade.loginWithPassword(loginVO);
			if (null == loginVO) {
				return Response.status(Response.Status.UNAUTHORIZED).entity(false).build();
			}else {
				return Response.ok().entity(loginVO).build();
			}*/
			System.out.println(" request "+latLang);
			
			return Response.ok().entity(locationFacade.getAddress( latLang)).build();
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response healthPing(String  wifii, HttpServletRequest request) {
		locationFacade.healthPing(wifii );
		return Response.ok().entity(wifii).build();
	}

	@Override
	public Response recent5Locations(HttpServletRequest request) {
		List<UserLocation> top5 =  locationFacade.getRecentLocations();
		return Response.ok().entity(top5).build();
	}
	
	@Override
	public Response mmiLocation(HttpServletRequest request) {
		return Response.ok().entity(locationFacade.mmiLocation()).build();
	}
	
	@Override
	public Response mmiCarCordinates(HttpServletRequest request) {
		Device device = locationFacade.mmiCarCordinates();
		if (null == device) {
			return Response.serverError().entity(device).build();
		}
		return Response.ok().entity(device).build();
	}

	@Override
	public Response getSafeMateLocation(HttpServletRequest request) {
		SafeMateDevice device = null;
		try {
			 device = locationFacade.getSafeMateLocation();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if (null == device) {
			return Response.serverError().entity(device).build();
		}
		return Response.ok().entity(device).build();
	}


	@Override
	public Response kusumGeoFencingDistance(HttpServletRequest request) {
		com.esp8266.location.LatLang latLan = locationFacade.userGeoFencingDistance( com.esp8266.location.mapMyIndia.Constants.kusumGeoFencing, "kusum");
		
		return Response.ok().entity(latLan).build();
	}
	

	@Override
	public Response getUtilityBills(HttpServletRequest request) {
		UtilityBillResponse utilityResponse = null;
		try {
			utilityResponse = locationFacade.getUtilityBills();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if (null == utilityResponse) {
			return Response.serverError().entity(utilityResponse).build();
		}
		return Response.ok().entity(utilityResponse).build();
	}
	
	

}
