package com.esp8266.location.EndPoint;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.esp8266.location.LocationVO;
import com.esp8266.location.WiFiVO;
import com.login.vo.ContactUS;
import com.login.vo.LatLang;
import com.login.vo.LoginVO;
import com.login.vo.Phone;


@Path("")
public interface LocationEndpoint {
	
	
	@POST
	@Path("/location/WifiNetworks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response wifiNetworks( LocationVO locationVO,  @Context HttpServletRequest request);
	
	

}
