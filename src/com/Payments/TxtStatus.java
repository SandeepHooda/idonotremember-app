package com.Payments;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.paytm.pg.merchant.CheckSumServiceHelper;

import paytm_java.PaytmConstants;

/**
 * Servlet implementation class TxtStatus
 */
@WebServlet("/TxtStatus")
public class TxtStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate().setDeadline(300d);
	private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TxtStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ORDERID  = request.getParameter("orderID");
		if (null != ORDERID) {
			TreeMap<String,String> parameters = new TreeMap<String,String>();
	    	parameters.put("ORDERID",ORDERID);
	    	parameters.put("MID",PaytmConstants.MID);
	    	try {
				String checkSum =  CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSumGAE(PaytmConstants.MERCHANT_KEY, parameters);
				
				String responseStr = "";
				 try {
					String payload = URLEncoder.encode("{\"MID\":\""+PaytmConstants.MID+"\",\"ORDERID\":\""+ORDERID+"\",\"CHECKSUMHASH\":\""+checkSum+"\"}", "UTF-8");
				        URL url = new URL("https://securegw.paytm.in/merchant-status/getTxnStatus?JsonData="+payload);
			            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
			            HTTPResponse res = fetcher.fetch(req);
			            responseStr =(new String(res.getContent()));
			            
			        } catch (Exception e) {
			        	e.printStackTrace();
			        	
			        }
				
				 response.getWriter().append(responseStr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			response.getWriter().append("Please provide orderID ");
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
