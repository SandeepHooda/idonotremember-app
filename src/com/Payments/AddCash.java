package com.Payments;

import java.io.IOException;
import java.util.Date;
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
 * Servlet implementation class AddCash
 */
@WebServlet("/AddCash")
public class AddCash extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCash() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user =(String) request.getSession().getAttribute("email");
		String amount = request.getParameter("amount");
		if (null != amount && amount.indexOf(".")>0) {
			amount = amount.substring(0,amount.indexOf("."));
		}
		if (null != user && null !=amount) {
			TreeMap<String,String> parameters = new TreeMap<String,String>();
			parameters.put("ORDER_ID",user+"_"+(new Date().getTime()));
			parameters.put("CUST_ID",user);
	
			parameters.put("TXN_AMOUNT",amount);
			
			
			parameters.put("MID",PaytmConstants.MID);
			parameters.put("CHANNEL_ID",PaytmConstants.CHANNEL_ID);
			parameters.put("INDUSTRY_TYPE_ID",PaytmConstants.INDUSTRY_TYPE_ID);
			parameters.put("WEBSITE",PaytmConstants.WEBSITE);
			//parameters.put("MOBILE_NO","7777777777");
			parameters.put("EMAIL",user);
			parameters.put("CALLBACK_URL", "http://www.idonotremember.com/PaymentStatus");
			
			try {
				//1. Get check sum
				String checkSum =  CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSumGAE(PaytmConstants.MERCHANT_KEY, parameters);
				
				StringBuilder outputHtml = new StringBuilder();
				outputHtml.append("<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>");
				outputHtml.append("<html>");
				outputHtml.append("<head>");
				outputHtml.append("<title>Merchant Check Out Page</title>");
				outputHtml.append("</head>");
				outputHtml.append("<body>");
				outputHtml.append("<center><h1>Please do not refresh this page...</h1></center>");
				outputHtml.append("<form method='post' action='"+ PaytmConstants.PAYTM_URL +"' name='f1'>");
				outputHtml.append("<table border='1'>");
				outputHtml.append("<tbody>");

				for(Map.Entry<String,String> entry : parameters.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					outputHtml.append("<input type='hidden' name='"+key+"' value='" +value+"'>");	
				}	  
					  


				outputHtml.append("<input type='hidden' name='CHECKSUMHASH' value='"+checkSum+"'>");
				outputHtml.append("</tbody>");
				outputHtml.append("</table>");
				outputHtml.append("<script type='text/javascript'>");
				outputHtml.append("document.f1.submit();");
				outputHtml.append("</script>");
				outputHtml.append("</form>");
				outputHtml.append("</body>");
				outputHtml.append("</html>");
				
				//Add to DB
				Order order = new Order();
				order.set_id(parameters.get("ORDER_ID"));
				order.setOrderDetails(parameters);
				Gson  json = new Gson();
				String orderJson = json.toJson(order, new TypeToken<Order>() {}.getType());
				
				MangoDB.createNewDocumentInCollection("remind-me-on", "add-cash-orders",  orderJson,null);
				
				EmailAddess toAddress = new EmailAddess();
				 toAddress.setAddress("sonu.hooda@gmail.com");
				new  MailService().sendSimpleMail(MailService.prepareEmailVO(toAddress, "Cash txn initiated  ",	parameters.get("EMAIL") +" Amount "+amount, null, null));
				
				
				response.getWriter().println(outputHtml);
			} catch (Exception e) {
				
				e.printStackTrace();
				 EmailAddess toAddress = new EmailAddess();
				 toAddress.setAddress("sonu.hooda@gmail.com");
				new  MailService().sendSimpleMail(MailService.prepareEmailVO(toAddress, "Payment failure!!  ",	 "Could not initiate to Paytm "+e.getLocalizedMessage(), null, null));
				
				response.sendRedirect("/ui/index.html");
			}
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
