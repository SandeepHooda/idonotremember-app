package monitor;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reminder.vo.ReminderVO;
import com.scheduler.SchedulerService;

import googleAssistant.PushNotificationUtils;

/**
 * ScanReminders - See if any reminder is past its trigger time
 */
@WebServlet("/ScanReminders")
public class ScanReminders extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScanReminders() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" Starting to check if any reminder is due");
		SchedulerService schedulerService = new SchedulerService();
		List<ReminderVO> currentReminders = schedulerService.getRemindersToBeExecuted();//Past reminders or executing in next 10 minutes
		
		schedulerService.executeReminderAndReschedule(currentReminders);//Send email/phone/sms and if it recurring find next execution time
		for (ReminderVO reminder: currentReminders) {
			reminder.set_id(null);
		}
		schedulerService.snoozReminders(currentReminders);//Keep reminding users till they stop it
		
		PushNotificationUtils.sendNotification(currentReminders);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
