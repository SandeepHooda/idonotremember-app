package health;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import health.vo.Device;
import health.vo.DeviceComparator;
import mangodb.MangoDB;


public class Devices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Devices() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String deviceJson = "["+ MangoDB.getDocumentWithQuery("idonot-remember", "device-id", null, null,true, null, null)+"]";
		Gson  json = new Gson();
		List<Device> allDevices = json.fromJson(deviceJson, new TypeToken<List<Device>>() {}.getType());
		Collections.sort(allDevices, new DeviceComparator());
		deviceJson = json.toJson(allDevices, new TypeToken<List<Device>>() {}.getType());
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(deviceJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 StringBuilder sb = new StringBuilder();
	        String s;
	        while ((s = request.getReader().readLine()) != null) {
	            sb.append(s);
	        }
	     
	        Gson  json = new Gson();
			List<Device> allDevices = json.fromJson(sb.toString(), new TypeToken<List<Device>>() {}.getType());
			if (null != allDevices) {
				for (Device device: allDevices) {
					String deviceJson = json.toJson(device, new TypeToken<Device>() {}.getType());
					MangoDB.updateData("sanhoo-home-security", "device-id",deviceJson , null,device.get_id());
				}
				
			}

	        
	        doGet(request,  response);
	}

}
