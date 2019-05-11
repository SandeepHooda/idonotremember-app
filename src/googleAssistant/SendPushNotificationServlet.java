package googleAssistant;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.login.vo.PushNotifyUser;

/**
 * Servlet implementation class SendPushNotificationServlet
 */
@WebServlet("/SendPushNotificationServlet")
public class SendPushNotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendPushNotificationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = request.getParameter("email");
		if (null != email) {
			 PushNotifyUser user =  new Handler(). getNotififationUser( email) ; 
			 if (null != user) {
				 PushNotificationSender sender = new PushNotificationSender();
				 sender.sendNotification( "A test", user.get_id(), "GetToDoPushNotification_1");
				 System.out.println(" push notification sent to "+user.getEmail());
				 response.getWriter().println(" push notification sent to "+user.getEmail());
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
