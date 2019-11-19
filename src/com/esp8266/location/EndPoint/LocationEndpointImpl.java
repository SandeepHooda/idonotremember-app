package com.esp8266.location.EndPoint;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import com.Constants;
import com.esp8266.location.LocationVO;
import com.esp8266.location.WiFiVO;
import com.esp8266.location.facade.LocationFacade;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.facade.LoginFacade;
import com.login.vo.ContactUS;
import com.login.vo.LatLang;
import com.login.vo.LoginVO;
import com.login.vo.Phone;
import com.login.vo.Settings;
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




	

	
	

}
