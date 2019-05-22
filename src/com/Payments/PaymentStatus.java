package com.Payments;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Payments.vo.Order;
import com.communication.email.EmailAddess;
import com.communication.email.MailService;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.Settings;
import com.paytm.pg.merchant.CheckSumServiceHelper;

import mangodb.MangoDB;
import paytm_java.AppDetails;
import paytm_java.PaytmConstants;

/**
 * Servlet implementation class PaymentStatus
 */
@WebServlet("/PaymentStatus")
public class PaymentStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate().setDeadline(300d);
	private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

    private String printTxnStatus(String ORDERID, String txnID ) {
    	System.out.println(" Looks like payment is successful. Just to be double sure, Will get the status of order id updated "+ORDERID);
    	TreeMap<String,String> parameters = new TreeMap<String,String>();
    	parameters.put("ORDERID",ORDERID);
    	parameters.put("MID",PaytmConstants.MID);
    	try {
			String checkSum =  CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSumGAE(PaytmConstants.MERCHANT_KEY, parameters);
			
			String responseStr = "";
			 try {
				String payload = URLEncoder.encode("{\"MID\":\""+PaytmConstants.MID+"\",\"ORDERID\":\""+ORDERID+"\",\"CHECKSUMHASH\":\""+checkSum+"\"}", "UTF-8");
			        //URL url = new URL("https://securegw-stage.paytm.in/merchant-status/getTxnStatus?JsonData="+payload);
				URL url = new URL("https://securegw.paytm.in/merchant-status/getTxnStatus?JsonData="+payload);
				
		            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
		            HTTPResponse res = fetcher.fetch(req);
		            responseStr =(new String(res.getContent()));
		            
		        } catch (Exception e) {
		        	e.printStackTrace();
		        	
		        }
			 	System.out.println(" txt status for order "+ORDERID+" "+responseStr);
			 	if( (responseStr.indexOf("\"RESPCODE\":\"01\"") > 0) && responseStr.indexOf(txnID) >0 ){
			 		return txnID;
			 	}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AppDetails appDetails = null;
		//Parse teh response into map -parameters
		Enumeration<String> paramNames = request.getParameterNames();

		Map<String, String[]> mapData = request.getParameterMap();
		TreeMap<String,String> parameters = new TreeMap<String,String>();
		String paytmChecksum =  "";
		while(paramNames.hasMoreElements()) {
			String paramName = (String)paramNames.nextElement();
			if(paramName.equals("CHECKSUMHASH")){
				paytmChecksum = mapData.get(paramName)[0];
			}else{
				parameters.put(paramName,mapData.get(paramName)[0]);
			}
		}
		boolean isValideChecksum = false;
		
		try{
			isValideChecksum = CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSumGAE(PaytmConstants.MERCHANT_KEY,parameters,paytmChecksum);
			if(isValideChecksum && parameters.containsKey("RESPCODE")){
				if(parameters.get("RESPCODE").equals("01")){//2. On sucess
					//3. get order details from DB
					String ORDERID = mapData.get("ORDERID")[0];
					String TXNID = mapData.get("TXNID")[0];
					if (TXNID.equalsIgnoreCase(printTxnStatus(ORDERID,TXNID))) {//Second level check
						String orderJson = MangoDB.getDocumentWithQuery("paytm-orders", "add-cash-orders", ORDERID, null,true, null, null);
						Gson  json = new Gson();
						 Order order = json.fromJson(orderJson, new TypeToken<Order>() {}.getType());
						 order.getOrderDetails().put("Paytm_TXNID", TXNID);
						 orderJson = json.toJson(order, new TypeToken<Order>() {}.getType());
						 MangoDB.createNewDocumentInCollection("paytm-orders", "add-cash-orders", orderJson, null);//Put the paytm txn id back in db
						
						 appDetails = PaytmConstants.appMap.get(order.getInitAppName());
						 if (null == appDetails) {
							 appDetails =  PaytmConstants.appMap.get(AppDetails.detaultAppName);
						 }
						 
						 //4. get user call settings from DB
						 String settingsJson = MangoDB.getDocumentWithQuery("remind-me-on", "registered-users-settings", order.getOrderDetails().get("EMAIL"), null,true, null, null);
						 Settings settings = json.fromJson(settingsJson, new TypeToken<Settings>() {}.getType());
						 double txAmount = Double.parseDouble(mapData.get("TXNAMOUNT")[0]);
						 settings.setCurrentCallCredits(settings.getCurrentCallCredits() + txAmount);
						 settingsJson = json.toJson(settings, new TypeToken<Settings>() {}.getType());
						 //5. Add txn amount to user credits
						 MangoDB.createNewDocumentInCollection("remind-me-on", "registered-users-settings", settingsJson, null);
						 
						 EmailAddess toAddress = new EmailAddess();
						 toAddress.setAddress("sonu.hooda@gmail.com");
						new  MailService().sendSimpleMail(MailService.prepareEmailVO(toAddress, "Cash txn Sucess!!  for app  : "+appDetails.getAppName(),	order.getOrderDetails().get("EMAIL") +" Amount "+ mapData.get("TXNAMOUNT")[0], null, null));
						
					}else {
						sendFailureEmail(mapData, appDetails.getAppName());
					}
					
					 response.sendRedirect(appDetails.getPaymentSuccessUrl());
				}else{
					showFailurePage(mapData, response);
				}
			}else{
				sendFailureEmail(mapData, appDetails.getAppName());
				showFailurePage(mapData, response);
			}
		}catch(Exception e){
			e.printStackTrace();
			sendFailureEmail(mapData, appDetails.getAppName());
			showFailurePage(mapData, response);
		}
		
	}
	
	private void showFailurePage(Map<String, String[]> mapData , HttpServletResponse response) throws IOException {
		String orderID = "";
		 if ( mapData.get("ORDERID") != null) {
			 orderID =  mapData.get("ORDERID")[0];
		 }
		 String TXNAMOUNT = "";
		 if ( mapData.get("TXNAMOUNT") != null) {
			 TXNAMOUNT =  mapData.get("TXNAMOUNT")[0];
		 }
		 String TXNID = "";
		 if ( mapData.get("TXNID") != null) {
			 TXNID =  mapData.get("TXNID")[0];
		 }

		response.getWriter().println("<b>Payment Failed. orderID "+orderID+ " TXNID "+TXNID+"  Amount:"+TXNAMOUNT+"</b>");
	}
	private void sendFailureEmail(Map<String, String[]> mapData, String appName) {
		 EmailAddess toAddress = new EmailAddess();
		 toAddress.setAddress("sonu.hooda@gmail.com");
		 String orderID = "";
		 if ( mapData.get("ORDERID") != null) {
			 orderID =  mapData.get("ORDERID")[0];
		 }
		 String TXNAMOUNT = "";
		 if ( mapData.get("TXNAMOUNT") != null) {
			 TXNAMOUNT =  mapData.get("TXNAMOUNT")[0];
		 }
		 String TXNID = "";
		 if ( mapData.get("TXNID") != null) {
			 TXNID =  mapData.get("TXNID")[0];
		 }
		new  MailService().sendSimpleMail(MailService.prepareEmailVO(toAddress, "Payment failure!!  for app  : "+appName,	" order id : "+orderID+" txn amt "+ TXNAMOUNT +" txt id "+TXNID, null, null));
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
