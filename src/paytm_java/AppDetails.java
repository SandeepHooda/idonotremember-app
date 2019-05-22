package paytm_java;

public class AppDetails {

	public static final String detaultAppName = "reminder";
	/* app.setAppName(detaultAppName);
	  app.setCallBackUrl("https://idonotremember-app.appspot.com/PaymentStatus");
	  app.setPaymentSuccessUrl("/ui/index.html#/menu/addCashSuccess");
	  app.setAppHome("/ui/index.html");*/
	  
	  private String appName;
	  private String CallBackUrl;
	  private String paymentSuccessUrl;
	  private String appHome;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getCallBackUrl() {
		return CallBackUrl;
	}
	public void setCallBackUrl(String callBackUrl) {
		CallBackUrl = callBackUrl;
	}
	public String getPaymentSuccessUrl() {
		return paymentSuccessUrl;
	}
	public void setPaymentSuccessUrl(String paymentSuccessUrl) {
		this.paymentSuccessUrl = paymentSuccessUrl;
	}
	public String getAppHome() {
		return appHome;
	}
	public void setAppHome(String appHome) {
		this.appHome = appHome;
	}
}
