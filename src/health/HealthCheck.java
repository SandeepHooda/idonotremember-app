package health;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.communication.email.EmailAddess;
import com.communication.email.EmailVO;
import com.communication.email.MailService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import health.vo.Device;
import mangodb.MangoDB;

/**
 * Servlet implementation class HealthCheck
 */

public class HealthCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HealthCheck() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String status = "";
		try {
			status = monitorHealth();
			//Thread.sleep(20000);
			//status = monitorHealth();
			//Thread.sleep(20000);
			//status = monitorHealth();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().append(status);
	}
	
	private String monitorHealth() {
		String deviceJson = "["+ MangoDB.getDocumentWithQuery("idonot-remember", "device-id", null, null,true, null, null)+"]";
		Gson  json = new Gson();
		List<Device> allDevices = json.fromJson(deviceJson, new TypeToken<List<Device>>() {}.getType());
		return notifyHealthStatus(allDevices);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private static String notifyHealthStatus(List<Device> allDevices)  {
		    boolean phoneCallRequired = false;
		    if (null !=allDevices && allDevices.size()>0 ) {
		    	Set<String> healthy = new HashSet<String>();
		    	Set<String> notWell = new HashSet<String>();
		    	Set<String> sleepMode = new HashSet<String>();
		    	for (Device aDevice: allDevices) {
		    		
		    		if ("0".equals(aDevice.get_import_id())) {
		    			if(!aDevice.isTurnOnHealthCheck()) {
		    				return "Please turn on \"TurnOnHealthCheck\" flag for device id \"0\" on \"sanhoo-home-security@device-id \"";//No Need to send health status email;
		    			}
		    		}else if(aDevice.isTurnOnHealthCheck()) {
		    			if ("doorSensor".equals(aDevice.getDeviceType())) {
		    				if ( (new Date().getTime() -aDevice.getHealthCheckTime()) > aDevice.getHeatBeatTimeout()) {//No status update since last 30 seconds. ESPP send updates every 6 seconds
			    				//but some updates might got miss so give some time
			    				notWell.add(aDevice.getName());//Device is Un plugged and device is being monitored
			    				if (!aDevice.isAlertType_emailOnly()) {//If a device is on "email only" alert then no need to make a cal for that device
			    					phoneCallRequired = true;
			    				}
			    			}else {
			    				healthy.add(aDevice.getName());
			    			}
		    			}
		    			
		    		}else {
		    			sleepMode.add(aDevice.getName());
		    		}
		    		
		    	}
		    	if (notWell.size() < 1) {
		    		return "All devices that are not snoozed are working good.";
		    	}
		    	String makeAcallError = null;
		    	if (phoneCallRequired) {
		    		makeAcallError = IamAlive.callSandeepPhoneNumbers("0");
		    	}
		    	EmailVO emalVO = new EmailVO();
				emalVO.setUserName("personal.reminder.notification@gmail.com");
				
				emalVO.setSubject("Health status of Monitoring devices");
				emalVO.setHtmlContent("<b>Device Not working : </b>"+notWell+"<br/><br/><b>Device In good health:</b> "+healthy+
						"<br/><br/><b>Sleep mode :</b> "+sleepMode+"<br/><br/>"+
						"If you don't want to receive these emails then please turn off \"TurnOnHealthCheck\" flag for device id \"0\" on \"sanhoo-home-security@device-id \"");
				if (null != makeAcallError) {
					emalVO.setHtmlContent(makeAcallError+" <br/><br/> "+ emalVO.getHtmlContent());
				}
				EmailAddess from = new EmailAddess();
				from.setAddress(emalVO.getUserName());
				
				List<EmailAddess> receipients = new ArrayList<EmailAddess>();
				EmailAddess to = new EmailAddess();
				to.setAddress("sonu.hooda@gmail.com");
				emalVO.setFromAddress(from);
				receipients.add(to);
				emalVO.setToAddress(receipients);
				
					if (!MailService.sendSimpleMail(emalVO)) {
						return " health check email not working";
					}
				
				
//				if (!) {
//					return " health check email not working";
//				}else {
//					return "Health email sent";
//				}
				return "Health email sent";
		    }else {
		    	return "No devices found to monitor";
		    }
	}

}
