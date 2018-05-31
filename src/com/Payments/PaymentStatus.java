package com.Payments;

import java.io.IOException;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.login.vo.Settings;
import com.paytm.pg.merchant.CheckSumServiceHelper;

import mangodb.MangoDB;
import paytm_java.PaytmConstants;

/**
 * Servlet implementation class PaymentStatus
 */
@WebServlet("/PaymentStatus")
public class PaymentStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
					String orderJson = MangoDB.getDocumentWithQuery("remind-me-on", "add-cash-orders", ORDERID, null,true, null, null);
					Gson  json = new Gson();
					 Order order = json.fromJson(orderJson, new TypeToken<Order>() {}.getType());
					
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
					new  MailService().sendSimpleMail(MailService.prepareEmailVO(toAddress, "Cash txn Sucess!!  ",	order.getOrderDetails().get("EMAIL") +" Amount "+ mapData.get("TXNAMOUNT")[0], null, null));
					
					 response.sendRedirect("/ui/index.html");
				}else{
					 EmailAddess toAddress = new EmailAddess();
					 toAddress.setAddress("sonu.hooda@gmail.com");
					new  MailService().sendSimpleMail(MailService.prepareEmailVO(toAddress, "Payment failure!!  ",	 mapData.get("ORDERID")[0]+" "+ mapData.get("TXNAMOUNT")[0], null, null));
					
					response.getWriter().println("<b>Payment Failed.</b>");
				}
			}else{
				 EmailAddess toAddress = new EmailAddess();
				 toAddress.setAddress("sonu.hooda@gmail.com");
				new  MailService().sendSimpleMail(MailService.prepareEmailVO(toAddress, "Payment failure!!  ",	 mapData.get("ORDERID")[0]+" "+ mapData.get("TXNAMOUNT")[0], null, null));
				
				response.getWriter().println("<b>Checksum mismatched.</b>");
			}
		}catch(Exception e){
			e.printStackTrace();
			 EmailAddess toAddress = new EmailAddess();
			 toAddress.setAddress("sonu.hooda@gmail.com");
			new  MailService().sendSimpleMail(MailService.prepareEmailVO(toAddress, "Payment failure!!  ",	 mapData.get("ORDERID")[0]+" "+ mapData.get("TXNAMOUNT")[0]+" Error message "+e.getLocalizedMessage(), null, null));
			
			response.getWriter().println("<b>Payment Failed.</b>");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
