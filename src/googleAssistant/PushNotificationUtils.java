package googleAssistant;

import java.io.IOException;
import java.util.List;

import com.login.vo.PushNotifyUser;
import com.reminder.vo.ReminderVO;

public class PushNotificationUtils {
	public static void sendNotification(String title, String email) {
		try {
			if (null != email) {
				 List<PushNotifyUser> users =  new Handler(). getNotififationUser( email) ; 
				 if (null != users) {
					 for (PushNotifyUser user : users) {
						 if (user.isSendUpdates()) {
							 PushNotificationSender sender = new PushNotificationSender();
							 try {
								sender.sendNotification( title, user.get_id(), Handler.intentPush);
							} catch (IOException e) {
								e.printStackTrace();
							}
							 System.out.println(" push notification sent to "+user.getEmail());
						 }
						 
						
					 }
					
				 }
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void sendNotification(List<ReminderVO> currentReminders ) {
		try {
			if (currentReminders != null ) {
				for (ReminderVO reminder : currentReminders) {
					sendNotification( reminder.getReminderSubject() ,  reminder.getEmail());
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
