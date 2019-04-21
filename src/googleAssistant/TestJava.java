package googleAssistant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import request.GoogleRequest;

public class TestJava {

	public static void main(String[] args) {
		 Pattern itemPattern1 = Pattern.compile("(put|placed|parked) my (.*?) (on|at|under|in) (.*?)");
		 Pattern itemPattern2 = Pattern.compile("(put|placed|parked|park) (.*?) (on|at|under|in) (.*?)");
		 Pattern locationPattern1 = Pattern.compile(" (on|at|under|in) (.*?)$");
	
		 String query = "I have tany garbaheo parked my note book under the garden beautiful";
		Matcher itemMatcher1 = itemPattern1.matcher(query);  
		Matcher itemMatcher2 = itemPattern2.matcher(query);  
		Matcher locationMatcher1 = locationPattern1.matcher(query); 
		 if (itemMatcher1.find()) {
			 System.out.println("1->"+itemMatcher1.group(2)+" # "+itemMatcher1.group(4));
		 }else if (itemMatcher2.find()) {
			 System.out.println("2->"+itemMatcher2.group(2));
		 }else{
			 System.out.println(" Not found");
		 }
		 
		 if (locationMatcher1.find()) {
			 System.out.println(locationMatcher1.group(2));
		 }else {
			 System.out.println(" Not found");
		 }
	}

}
