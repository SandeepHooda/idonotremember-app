package com.esp8266.location.EndPoint;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.esp8266.location.LocationVO;
import com.login.vo.LatLang;


@Path("")
public interface LocationEndpoint {
	
	
	@POST
	@Path("/location/WifiNetworks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response wifiNetworks( LocationVO locationVO,  @Context HttpServletRequest request);
	
	@POST
	@Path("/location/getAddress")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAddress( LatLang latLang,  @Context HttpServletRequest request);
	
	@GET
	@Path("/location/healthPing/wifii/{wifii}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response healthPing(@PathParam("wifii") String wifii,  @Context HttpServletRequest request);
	
	@GET
	@Path("/location/recent5")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response recent5Locations(@Context HttpServletRequest request);
	
	@GET
	@Path("/location/mmi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response mmiLocation(@Context HttpServletRequest request);
	
	@GET
	@Path("/location/mmi/car/cordinates")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response mmiCarCordinates(@Context HttpServletRequest request);
	
	@GET
	@Path("/location/safemate/kusum")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSafeMateLocation(@Context HttpServletRequest request);
	
	@GET
	@Path("/location/geofencingDistance/kusum")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response kusumGeoFencingDistance(@Context HttpServletRequest request);
	
	@GET
	@Path("/utility/bill/all")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUtilityBills(@Context HttpServletRequest request);
	
	@GET
	@Path("/weather")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getWeather(@Context HttpServletRequest request);
	
	@GET
	@Path("/weather/snoozAlerts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response snoozAlerts(@Context HttpServletRequest request);
	

}
