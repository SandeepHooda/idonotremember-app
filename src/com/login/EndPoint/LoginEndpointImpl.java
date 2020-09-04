package com.login.EndPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import com.Constants;
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

public class LoginEndpointImpl implements LoginEndpoint {
	private LoginFacade loginFacade;
	public LoginFacade getLoginFacade() {
		return loginFacade;
	}

	public void setLoginFacade(LoginFacade loginFacade) {
		this.loginFacade = loginFacade;
	}

	public Response recordLoginSucess( HttpServletRequest request) {
		try{
			
			return Response.ok().entity(loginFacade.recordLoginSucess(request)).build();
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	public Response updatePreciseLocation( LatLang latLang, HttpServletRequest request) {
		try{
			
			return Response.ok().entity(loginFacade.updatePreciseLocation(latLang, request)).build();
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	@Override
	public Response validateRegID(String regID, HttpServletRequest request, String appTimeZone) {
		try{
			HttpSession session = request.getSession();
			session.setAttribute("regID", regID);
           if (null != appTimeZone) {
        	   appTimeZone = appTimeZone.replace("@", "/");
           }
			session.setAttribute("timeZoneSettings", appTimeZone);
			
			LoginVO loginVO = loginFacade.validateRegID(regID,  appTimeZone);
			if (null != loginVO) {
				if (loginVO.getUserSuppliedTimeZone() != null) {
					session.setAttribute("timeZoneSettings", loginVO.getUserSuppliedTimeZone());
					
				}
				System.out.println(" user logged in = "+loginVO.getEmailID());
				if (!"sonu.hooda@gmail.com".equalsIgnoreCase(loginVO.getEmailID())) {
					System.out.println(" user not authorized "+loginVO.getEmailID()+" regID "+regID);
					LoginVO vo = new LoginVO();
					vo.setErrorMessage("Please log in to authenticate ");
					return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
				}
				
				session.setAttribute("email", loginVO.getEmailID());
				session.setAttribute("userName", loginVO.getName());
				session.setAttribute("settings", loginVO.getUserSettings());
				return Response.ok().entity(loginVO).build();
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response addPhoneNo(Phone phone, HttpServletRequest request) {
		try{
			
			String regID = request.getHeader("Auth");
			
			if (null == regID) {
				HttpSession session = request.getSession();
				 regID = (String)session.getAttribute("regID");
			}
			
			
			HttpSession session = request.getSession();
			Settings usrSettings = (Settings) session.getAttribute("settings");
			if (usrSettings.getCurrentCallCredits() > 10) {
				usrSettings.setCurrentCallCredits(usrSettings.getCurrentCallCredits() -Settings.smsCharges);
				Gson  json = new Gson();
				 String settingsJson = json.toJson(usrSettings, new TypeToken<Settings>() {}.getType());
				 String email = new ReminderFacade().getEmail(regID);
				 MangoDB.updateData("remind-me-on", "registered-users-settings-new", settingsJson, email, null);
				
				return Response.ok().entity(loginFacade.addPhoneNo(phone, regID)).build();
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Insufficient balance");
				return Response.status(Response.Status.PAYMENT_REQUIRED).entity(vo).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	@Override
	public Response logout(String regID, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			regID = (String)session.getAttribute("regID");
			return Response.ok().entity(loginFacade.logout(regID, session)).build();
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response getUserPhones(HttpServletRequest request) {
		try{
			String regID = request.getHeader("Auth");
			
			if (null == regID) {
				HttpSession session = request.getSession();
				 regID = (String)session.getAttribute("regID");
			}
			String email = new ReminderFacade().getEmail(regID);
			return Response.ok().entity(loginFacade.getUserPhones( email)).build(); 
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
		
	}

	@Override
	public Response deletePhone(String phoneID, HttpServletRequest request) {
		try{
			String regID = request.getHeader("Auth");
			
			if (null == regID) {
				HttpSession session = request.getSession();
				 regID = (String)session.getAttribute("regID");
			}
			String email = new ReminderFacade().getEmail(regID);
			if (phoneID.endsWith(email)) {
				loginFacade.deletePhone( phoneID);
				return Response.ok().entity(loginFacade.getUserPhones( email)).build(); 
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response sendOtp(String phoneID, HttpServletRequest request) {
		try{
			String regID = request.getHeader("Auth");
			
			if (null == regID) {
				HttpSession session = request.getSession();
				 regID = (String)session.getAttribute("regID");
			}
			String email = new ReminderFacade().getEmail(regID);
			if (phoneID.endsWith(email)) {
				String userName = (String)request.getSession().getAttribute("userName");
				if (null == userName) {
					userName = "";
				}
				loginFacade.sendOtp( phoneID, userName);
				return Response.ok().entity(new LoginVO()).build(); 
			}else {
				LoginVO vo = new LoginVO();
				vo.setErrorMessage("Please log in to authenticate ");
				return Response.status(Response.Status.UNAUTHORIZED).entity(vo).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	@Override
	public Response  confirmOTP( String phoneID,  String otp){
		try{
			return Response.ok().entity(loginFacade.confirmOPT( phoneID,otp)).build(); 
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response getPhoneViaStatus(boolean status, HttpServletRequest request) {
		try{
			String regID = request.getHeader("Auth");
			
			if (null == regID) {
				HttpSession session = request.getSession();
				 regID = (String)session.getAttribute("regID");
			}
			String email = new ReminderFacade().getEmail(regID);
			return Response.ok().entity(loginFacade.getPhoneViaStatus( email, status)).build(); 
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response getCallCredits(String regID, HttpServletRequest request) {
		try{
			HttpSession session = request.getSession();
			Settings usrSettings = (Settings) session.getAttribute("settings");
			return Response.ok().entity(usrSettings.getCurrentCallCredits()).build(); 
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	@Override
	public Response feedback( String feedback,   HttpServletRequest request) {
		try{
			String regID = request.getHeader("Auth");
			String email = null;
			if (null != regID) {
				 email = new ReminderFacade().getEmail(regID);
			}
			if (email == null) {
				HttpSession session = request.getSession();
				 email = (String)session.getAttribute("email");
			}
			String userName = (String)request.getSession().getAttribute("userName");
			if (null == userName) {
				userName = "";
			}
			if (email == null) {
				email = "Contact us page: ";
			}
			Constants.sendEmail("sonu.hooda@gmail.com", "Feebkack from Reminder app ", userName+" - "+email+" has provided feedback: <br/><br/> "+feedback);
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Feedback received ");
			return Response.ok().entity(vo).build(); 
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	@Override
	public Response contactUS( ContactUS contact) {
		try{
			
			Constants.sendEmail("sonu.hooda@gmail.com","Contact US from Reminder App", contact.getFname()+" "+ contact.getLname()+" "+contact.getCountry()+": <br/><br/> "+contact.getFeedback());
			 
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Feedback received ");
			return Response.ok().entity(vo).build(); 
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response loginWithPassword(LoginVO loginVO, HttpServletRequest request) {
		
		try{
			loginVO = loginFacade.loginWithPassword(loginVO);
			if (null == loginVO) {
				return Response.status(Response.Status.UNAUTHORIZED).entity(false).build();
			}else {
				return Response.ok().entity(loginVO).build();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}

	@Override
	public Response validateEmail(String email, String regID, HttpServletRequest request) {
		try{
			if (loginFacade.validateEmail(email, regID)) {
				return Response.ok().entity("Thanks for your confirmation. Your identity has now been verified. You may close this window. Please go to the app and sign in with the user name and password that you used.").build();
			}else {
				return Response.status(Response.Status.UNAUTHORIZED).entity(false).build();
			}
		
			
				
			
		}catch(Exception e){
			e.printStackTrace();
			LoginVO vo = new LoginVO();
			vo.setErrorMessage("Internal Server Error ");
			
			return Response.serverError().entity(vo).build();
		}
	}
	
	

}
