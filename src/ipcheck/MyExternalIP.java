package ipcheck;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mangodb.MangoDB;

/**
 * Servlet implementation class MyExternalIP
 */
@WebServlet("/MyExternalIP")
public class MyExternalIP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyExternalIP() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ip= request.getParameter("ip");
		System.out.println("IP address in request "+ip);
		if (null != ip ) {
			String ipJson = "{\"_import_id\": \"ip\",\"myIP\": \""+ip+"\", \"time\": "+(new Date().getTime())+"}";
			MangoDB.updateData("remind-me-on", "external-ip", ipJson, "5f56230dd279373c0002f1a9",null);
			response.getWriter().append("Your IP is : ").append(ip);
		}else {
			String ipJson =  MangoDB.getDocumentWithQuery("remind-me-on", "external-ip", null, null,true, null, null);
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(ipJson);
			
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
