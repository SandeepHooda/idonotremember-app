package paytm_java;

import java.util.HashMap;
import java.util.Map;

public class PaytmConstants {
	public static final String detaultAppName = "reminder";
 public static Map<String, AppDetails> appMap = null;
  public final static String MID="MaaDur53067059433032";//"MaaDur53067059433032";//"Remind29538375717256";
  public final static String MERCHANT_KEY="H9ImWI6KsyUSAg8f";//"H9ImWI6KsyUSAg8f";//"htDAp1KejfuJq00L";
  public final static String INDUSTRY_TYPE_ID="Retail109";//"Retail109";//"Retail";
  public final static String CHANNEL_ID="WEB";
  public final static String WEBSITE="WEBPROD";//"WEBPROD";//"WEBSTAGING";
  public final static String PAYTM_URL="https://securegw.paytm.in/theia/processTransaction";//"https://securegw.paytm.in/theia/processTransaction";//"https://securegw-stage.paytm.in/theia/processTransaction"; 
  
  static {
	  appMap = new HashMap<String, AppDetails>();
	  AppDetails app = new AppDetails();
	  app.setAppName(detaultAppName);
	  app.setCallBackUrl("https://idonotremember-app.appspot.com/PaymentStatus");
	  app.setPaymentSuccessUrl("/ui/index.html#/menu/addCashSuccess");
	  app.setAppHome("/ui/index.html");
	  
	  appMap.put(app.getAppName(), app);
	  
	  
	  app = new AppDetails();
	  app.setAppName("sosServices");
	  app.setCallBackUrl("https://idonotremember-app.appspot.com/PaymentStatus");
	  app.setPaymentSuccessUrl("https://sosservices.appspot.com/ui/#/addCashSuccess");
	  app.setAppHome("https://sosservices.appspot.com/");
	  
	  appMap.put(app.getAppName(), app);
	  
	  
  }
}

