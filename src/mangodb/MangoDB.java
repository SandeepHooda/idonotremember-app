package mangodb;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import com.communication.phone.text.Key;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;


public class MangoDB {
	public static final String mlabKeyReminder = "oEEHExhtLS3QShn3Y2Kl4_a4nampQKj9";
	public static final String mlabKeySonu = "soblgT7uxiAE6RsBOGwI9ZuLmcCgcvh_";
	public static final String noCollection = "";
	private static final Logger log = Logger.getLogger(MangoDB.class.getName());
	private static FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate().setDeadline(300d);
	private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
	
	public static String makeExternalRequest(String httpsURL, String method, String data, Map<String, String> headers) {
		return makeExternalRequest(httpsURL,  method,  data,  headers, true, null);
	}
	public static String makeExternalRequest2(String httpsURL, String method, String data, Map<String, String> headers, boolean followRefirect, String needResponseCookie) throws IOException {
		URL url = new URL(httpsURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Enable output for the connection.
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	
		
		 
	
		   
		// Set HTTP request method.
		conn.setRequestMethod("POST");

		conn.setConnectTimeout(30000);

		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(data);
		writer.close();
		conn.setConnectTimeout(30000);
		int respCode = conn.getResponseCode(); // New items get NOT_FOUND on PUT
		if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
		  
		  StringBuilder response = new StringBuilder();
		  String line;

		  // Read input data stream.
		  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  while ((line = reader.readLine()) != null) {
		    response.append(line);
		  }
		  reader.close();
		  System.out.println("Response "+ response.toString());
		} else {
		 System.out.println("code "+ conn.getResponseCode() + " " + conn.getResponseMessage());
		}
		return "";
	}
	public static String makeExternalRequest(String httpsURL, String method, String data, Map<String, String> headers, boolean followRefirect, String needResponseCookie) {
		try {
			FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate().setDeadline(300d);
			if (!followRefirect) {
				lFetchOptions = lFetchOptions.doNotFollowRedirects();
				
			}
	        URL url = new URL(httpsURL);
            HTTPRequest req = null;
            if ("POST".equalsIgnoreCase(method)) {
            	req = new HTTPRequest(url, HTTPMethod.POST, lFetchOptions);
            }else {
            	req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
            	
            }
            
           //conn.setConnectTimeout(2000);
            //conn.setReadTimeout(2000);
            
            	Set<String>  keys=  headers.keySet();
                for (String key: keys) {
                	 HTTPHeader header = new HTTPHeader(key, headers.get(key));
                     req.setHeader(header);
                }
            
            
           
            /*String contentType = headers.get("Content-type");
			if (null == contentType) {
				contentType = "application/json";//"Content-type"
			}*/
			
           
            if(null != data) {
            	req.setPayload(data.getBytes());
            }
            
           

            
           
            HTTPResponse res =fetcher.fetch(req);
         
           if (needResponseCookie != null) {
    			for (HTTPHeader header : res.getHeaders()) {
         			if (needResponseCookie.equalsIgnoreCase(header.getName())) {
         				return header.getValue();
         			}
         		}
    			return null;
    		}else {
    			int status = res.getResponseCode();
    			if(status >=200 && status  <300) {
    				return (new String(res.getContent()));
       			}else {
       				System.out.println(" response code is not ok "+res.getResponseCode());
	                return null;
       			}
    	    }
            
 
        } catch (IOException e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
	public static String getMMILiveLocations() {
		String httpsURL  = "https://apis.mapmyindia.com/intouch/v1/"+Key.mmiKey+"/getLiveData";
		
	
		String responseStr = "";
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPResponse res = fetcher.fetch(req);
	            responseStr =(new String(res.getContent()));
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	log.warning("Error while gettiung data from map my india");
	        	return null;
	        }
		
		
			
		
		 return responseStr;
	}
	
	public static String getSafeMateLocations() {
		String mmiCookie = getLoginCookie();
		return exchangeExcessToken(mmiCookie);
		
	}
	
	public static String getElecticity(String userName, String password) throws UnsupportedEncodingException  {
		String loginUrl = "http://uhbvn.org.in/web/portal/auth?lastPage=/consumer-info";
	
		Map<String, String> headers = new HashMap<String, String>();
	  
		String consumerInfo = "";
		try {
			consumerInfo = makeExternalRequest(loginUrl, "GET", null, headers, false,null);
			System.out.println(consumerInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		headers = new HashMap<String, String>();
	    headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
	    headers.put("Cookie", consumerInfo);
	    headers.put("Referer", "http://uhbvn.org.in/web/portal/auth?lastPage=/consumer-info");
		return null; 
	}
	
	private static String getLoginCookie()  {
		String loginUrl = "https://auth.mireo.hr/oauth2/login";
		String query = "username="+Key.safeMateUser; 
		query += "&pwd="+Key.safeMatePwd;
		query += "&rm=0" ;
		Map<String, String> headers = new HashMap<String, String>();
	    headers.put("Content-type", "application/x-www-form-urlencoded");
	    headers.put("Connection", "close");
	    headers.put("Content-Length", ""+query.getBytes().length);
		String mmiCookie =  makeExternalRequest(loginUrl, "POST", query, headers, false,"Set-Cookie"); 
		return mmiCookie.substring(5,mmiCookie.indexOf(";")); 
	}
	
	private static String exchangeExcessToken(String mmiCookie)  {
		
		String url ="https://auth.mireo.hr/oauth2/oauth?client_id=851982.apps.mireo.hr&redirect_uri=https://fleet.mapmyindia.com/Fleet2009/Fleet2009.html&response_type=token&client_domain=fleet.mapmyindia.com&welcome_url=";

		Map<String, String> headers = new HashMap<String, String>();
	    headers.put("Content-type", "application/json");
	    headers.put("Cookie", "LUID="+mmiCookie);
	    headers.put("Referer", "https://auth.mireo.hr/oauth2/html/login_fleet.html");
	    headers.put("Connection", "close");
	    String redirectUrl = makeExternalRequest(url, "GET", null, headers, false,"Location"); 
		String accessToken = redirectUrl.substring(redirectUrl.indexOf("=")+1);
		return accessToken;
	}
	public static String getDocumentWithQuery(String dbName, String collection,  String documentKey, String keyName, boolean isKeyString, String mlabApiKey, String query){
		if (null == mlabApiKey) {
			mlabApiKey = mlabKeyReminder;
		}
		String httpsURL  = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+mlabApiKey;
		if (null != documentKey){
			if (isKeyString){
				httpsURL += "&q=%7B%22_id%22:%22"+documentKey+"%22%7D";
			}else {
				httpsURL += "&q=%7B%22"+keyName+"%22:%22"+documentKey+"%22%7D";
			}
			
		}
		
		if (null != query ){
			httpsURL += query;
			
		}
		//System.out.println("This is the url "+httpsURL);
		String responseStr = "";
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.GET, lFetchOptions);
	            HTTPResponse res = fetcher.fetch(req);
	            responseStr =(new String(res.getContent()));
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	log.warning("Error while gettiung data dbName: "+dbName+" collection :"+collection+" documentKey: "+documentKey+e.getLocalizedMessage());
	        	return null;
	        }
		
		
			responseStr = responseStr.replaceFirst("\\[", "").trim();
			 if (responseStr.indexOf("]") >= 0){
				
				 responseStr = responseStr.substring(0, responseStr.length()-1);
			 }
		
		 return responseStr;
	}
	public static String getADocument(String dbName, String collection, String documentKey,String keyName,  boolean isKeyString, String mlabApiKey){
		return getDocumentWithQuery(dbName,  collection,  documentKey,keyName, isKeyString, mlabApiKey, null);
	}
	public static String getADocument(String dbName, String collection,  String documentKey, String keyName, String mlabApiKey){
		
		return getDocumentWithQuery(dbName,  collection,  documentKey, keyName,true, mlabApiKey, null);
		
	}
	public static String getData(String db, String collection,  String apiKey ){
		if (null == apiKey) {
			apiKey = mlabKeyReminder;
		}
		db = db.toLowerCase();
		return getADocument(db,collection,null,null,apiKey);
	}
	
	
	//Create 
public static void createNewDocumentInCollection(String dbName,String collection,  String data, String key){
	if (null == key) {
		key = mlabKeyReminder;
	}
		String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+key;
		
		 try {
			
		        URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            
	            req.setHeader(header);
	           
	            req.setPayload(data.getBytes());
	            fetcher.fetch(req);
	            
	 
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	}
	
public static void updateData(String dbName,String collection, String data, String documentKey,  String apiKey){
	if (null == apiKey) {
		apiKey = mlabKeyReminder;
	}
	String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+apiKey;
	if (null != documentKey){
		httpsURL += "&q=%7B%22_id%22:%22"+documentKey+"%22%7D";
		
	}	
	
	 try {
		 	/*URL url = new URL(httpsURL);
            HTTPRequest req = new HTTPRequest(url, HTTPMethod.PUT, lFetchOptions);
            HTTPResponse res = fetcher.fetch(req);*/
           
		
	       URL url = new URL(httpsURL);
            HTTPRequest req = new HTTPRequest(url, HTTPMethod.PUT, lFetchOptions);
            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
            
            req.setHeader(header);
           
            req.setPayload(data.getBytes());
            fetcher.fetch(req);
            
           //log.info("Updated the DB  collection "+collection+data);
 
        } catch (IOException e) {
        	 log.info("Error while  upfdating DB  collection "+collection+" Message "+e.getMessage());
        	e.printStackTrace();
        	
        }
	
}
public static void deleteAllDocuments(String dbName,String collection,   String apiKey){
	if (null == apiKey) {
		apiKey = mlabKeyReminder;
	}
	String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+apiKey;
	
	
	 try {
		 	/*URL url = new URL(httpsURL);
            HTTPRequest req = new HTTPRequest(url, HTTPMethod.PUT, lFetchOptions);
            HTTPResponse res = fetcher.fetch(req);*/
           
		
	       URL url = new URL(httpsURL);
            HTTPRequest req = new HTTPRequest(url, HTTPMethod.PUT, lFetchOptions);
            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
            
            req.setHeader(header);
           
            req.setPayload("[]".getBytes());
            fetcher.fetch(req);
            
           //log.info("Updated the DB  collection "+collection+data);
 
        } catch (IOException e) {
        	 log.info("Error while  upfdating DB  collection "+collection+" Message "+e.getMessage());
        	e.printStackTrace();
        	
        }
}
	
public static void deleteDocument(String dbName,String collection,  String dataKeyTobeDeleted, String key){
	if (null == key) {
		key = mlabKeyReminder;
	}
		
		String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"/"+dataKeyTobeDeleted+"?apiKey="+key;
		 HttpURLConnection connection = null;
		 try {
			
			 URL url = new URL(httpsURL);
	            HTTPRequest req = new HTTPRequest(url, HTTPMethod.DELETE, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            
	            req.setHeader(header);
	            fetcher.fetch(req);
	            
	         
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } finally {
	            if (connection != null) {
	                connection.disconnect();
	              }
	            }
	}
	

}
