package com.communication.email;




import java.io.ByteArrayInputStream;
// [START multipart_includes]
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// [START simple_includes]

import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
// [END simple_includes]
import javax.mail.internet.MimeMultipart;
// [END multipart_includes]

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;




public class MailService {
	private static final Logger log = Logger.getLogger(MailService.class.getName());
	
	private static FetchOptions lFetchOptions = FetchOptions.Builder.doNotValidateCertificate().setDeadline(300d);
	private static URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
	
   public static String questionMatchFromHerokuAI(String userQuestion, List<String> allQuestions) {
		
		
		String httpsURL  = "https://ai-chat-bot-help.herokuapp.com/questionMatch";
		
		 try {
			
		        URL url = new URL(httpsURL);
		        
		        HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/x-www-form-urlencoded");
	            req.setHeader(header);
	           
	            header = new HTTPHeader("Accept", "*/*");
	            req.setHeader(header);
	           
	            StringBuilder sb = new StringBuilder("[");
	            for (String aQuestion : allQuestions) {
	            	sb.append("\"").append(aQuestion.trim()).append("\",");
	            }
	            String allQuestionsStr = sb.toString();
	            allQuestionsStr = allQuestionsStr.substring(0,allQuestionsStr.length()-1)+"]";//Remove last comma
	            String data = "a_question="+userQuestion.trim().toLowerCase()+"&all_question="+allQuestionsStr;
	            byte[] postData = data.getBytes( StandardCharsets.UTF_8 );
	            req.setPayload(postData);
	            com.google.appengine.api.urlfetch.HTTPResponse res = fetcher.fetch(req);
	            if (res.getResponseCode() == 200 ){
	            	return new String(res.getContent());
	            }else {
	            	System.out.println(" error code from heroku AI match "+res.getResponseCode());
	            	return "";
	            }
	            
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	log.warning("Error sending request for heroku AI processing : "+e.getLocalizedMessage());
	        	return "";
	        }
	  }
   public static void sendWhatAppMsg(String phone, String msg, boolean sandeepPushover ,boolean kusumPushover) {
	   String httpsURL  = "https://post-master.herokuapp.com/WhatsAppMessanger";
		
		 try {
			
			 URL url = new URL(httpsURL);
		        
		        HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            req.setHeader(header);
	           
	            header = new HTTPHeader("Accept", "application/json");
	            req.setHeader(header);
	           
	            WhatAppVO vo = new WhatAppVO();
	            vo.setMsg(msg);
	            vo.setPhone(phone);
	            vo.setKusumPushover(kusumPushover);
	            vo.setSandeepPushover(sandeepPushover);
	            Gson  json = new Gson();
	            String data = json.toJson(vo, new TypeToken<WhatAppVO>() {}.getType());
	            req.setPayload(data.getBytes());
	            com.google.appengine.api.urlfetch.HTTPResponse res = fetcher.fetch(req);
	     
	            System.out.println(" Response code "+res.getResponseCode() +" msg = "+res.getContent());
	            
	            
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	log.warning("Error in whats app   : "+e.getLocalizedMessage());
	        	
	        }
   }
	public static boolean sendSimpleMail(EmailVO emailVO ) {
		
		
		String httpsURL  = "https://post-master.herokuapp.com/SendEmail";
		
		 try {
			
		        URL url = new URL(httpsURL);
		        
		        HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST, lFetchOptions);
	            HTTPHeader header = new HTTPHeader("Content-type", "application/json");
	            req.setHeader(header);
	           
	            header = new HTTPHeader("Accept", "application/json");
	            req.setHeader(header);
	           
	         
	            Gson  json = new Gson();
	            String data = json.toJson(emailVO, new TypeToken<EmailVO>() {}.getType());
	            req.setPayload(data.getBytes());
	            com.google.appengine.api.urlfetch.HTTPResponse res = fetcher.fetch(req);
	            if (res.getResponseCode() == 200 && "Done".equals(new String(res.getContent()))){
	            	return true;
	            }else {
	            	log.warning("Email sending failed "+(new String(res.getContent())));
	            	return false;//Made it true if you get "Daily user sending quota exceeded" from heroku
	            }
	            
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	log.warning("Error in sms sending : "+e.getLocalizedMessage());
	        	return false;
	        }
	  }
	public boolean sendSimpleMail_Gmail(String toAddress,  String from, String subject, String body) {
	    
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);

	    try {
	      Message msg = new MimeMessage(session);
	      msg.setFrom(new InternetAddress("kusum.hooda@gmail.com", from));
	      msg.addRecipient(Message.RecipientType.TO,
	                       new InternetAddress(toAddress, ""));
	      msg.setSubject(subject);
	      msg.setText(body);
	      Transport.send(msg);
	      return true;
	    } catch (AddressException e) {
	      // ...
	    } catch (MessagingException e) {
	      // ...
	    } catch (UnsupportedEncodingException e) {
	      // ...
	    }
	    return false;
	    // [END simple_example]
	  }

	  public boolean sendMultipartMail_Gmail(String toAddress, String ccAddress,  String from, byte[] attachmentData, String subject, String body) {
		  log.info("toAddress " +toAddress +" ccAddress "+" from "+from +" subject "+subject  );
		  log.info(" body "+body  );
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);

	    String msgBody = "...";

	    try {
	      Message msg = new MimeMessage(session);
	      msg.setFrom(new InternetAddress("kusum.hooda@gmail.com", from));
	      msg.addRecipient(Message.RecipientType.TO,
	                       new InternetAddress(toAddress, ""));
	      if (null != ccAddress){
	    	  msg.addRecipient(Message.RecipientType.CC,
	                  new InternetAddress(ccAddress, ""));
	      }
	     
	      msg.setSubject(subject);
	      msg.setText(msgBody);

	      // [START multipart_example]
	      String htmlBody = body;          // ...
	     
	      Multipart mp = new MimeMultipart();

	      MimeBodyPart htmlPart = new MimeBodyPart();
	      htmlPart.setContent(htmlBody, "text/html");
	      mp.addBodyPart(htmlPart);

	      if (null != attachmentData){
	    	  MimeBodyPart attachment = new MimeBodyPart();
		      InputStream attachmentDataStream = new ByteArrayInputStream(attachmentData);
		      attachment.setFileName("sales.pdf");
		      attachment.setContent(attachmentDataStream, "application/pdf");
		      mp.addBodyPart(attachment);
	      }
	     

	      msg.setContent(mp);
	      // [END multipart_example]

	      Transport.send(msg);
	      return true;
	    } catch (AddressException e) {
	    	e.printStackTrace();
		     log.warning(" Error "+e.getLocalizedMessage());
	    } catch (MessagingException e) {
	    	e.printStackTrace();
		     log.warning(" Error "+e.getLocalizedMessage());
	    } catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
		     log.warning(" Error "+e.getLocalizedMessage());
	    }catch (Exception e) {
	    	e.printStackTrace();
		     log.warning(" Error "+e.getLocalizedMessage());
		    }
	    
	    return false;
	  }
	  
	  public static EmailVO prepareEmailVO( EmailAddess toAddress, String subject , String htmlBody, String base64attachment, String attachmentName ) {
			EmailVO emailVO = new EmailVO();
			
			emailVO.setUserName( "myshopemailnotification@gmail.com");
			emailVO.setPassword( "gizmtcibqjnqhqtz");
			EmailAddess fromAddress = new EmailAddess();
			fromAddress.setAddress(emailVO.getUserName());
			fromAddress.setLabel("Reminder App");
			emailVO.setFromAddress( fromAddress);
			
			
			List<EmailAddess> toAddressList = new ArrayList<EmailAddess>();
			
			toAddressList.add(toAddress);
			emailVO.setToAddress(toAddressList);
			emailVO.setSubject(subject);
			emailVO.setHtmlContent(htmlBody);
			emailVO.setBase64Attachment(base64attachment);
			emailVO.setAttachmentName(attachmentName);
			return emailVO;
		}
	

}
