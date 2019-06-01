package heroku;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.communication.email.EmailVO;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class DateExtracter
 */
@WebServlet("/DateExtracter")
public class DateExtracter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate().setDeadline(300d);
	private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DateExtracter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		String httpsURL  = "https://date-parser.herokuapp.com/extractDateTime?text=";
		String text = request.getParameter("text");
		if ( text != null) {
			httpsURL += URLEncoder.encode(text, "UTF-8") ;
		}else {
			httpsURL += "Sunday%207%20am";
		}
		String responseStr = "";
		 try {
			
		        URL url = new URL(httpsURL);
		        
		        HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "text/html");
	            req.setHeader(header);
	           
	            header = new HTTPHeader("Accept", "text/html");
	            req.setHeader(header);
	           
	         
	            HTTPResponse res = fetcher.fetch(req);
	            responseStr =(new String(res.getContent()));
	            
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	
	        }
		response.getWriter().append(responseStr);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
