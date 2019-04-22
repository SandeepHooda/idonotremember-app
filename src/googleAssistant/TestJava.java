package googleAssistant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import request.GoogleRequest;

public class TestJava {

	public static void main(String[] args) {
		Pattern itemPattern1 = Pattern.compile("(put|placed|parked|park|keep|kept) [m]{0,1}[y]{0,1}[ ]{0,1}(.*?) (on|at|under|in) (.*?)");
		 Pattern itemPattern2 = Pattern.compile("[m]{0,1}[y]{0,1}[ ]{0,1}(.*?) [ia]{0,1}[sr]{0,1}[ e]{0,1}[ ]{0,1}(on|at|under|in) (.*?)");
		 Pattern locationPattern1 = Pattern.compile(" (on|at|under|in) (.*?)$");
		  Pattern itemPatternWhere1 = Pattern.compile("(where is|where are|find) [m]{0,1}[y]{0,1}[ ]{0,1}(.*?)$");
	
		 String[] queryArray = new String[] {"I have kept my toy under table","my toy under table","my toys are on table","toy is on table","keep toy on table","toy on table",
				"my pen is in my pocket", "find toy"};
		 for (String query:queryArray ) {
			 Matcher itemMatcher1 = itemPattern1.matcher(query);  
				Matcher itemMatcher2 = itemPattern2.matcher(query);  
				Matcher locationMatcher1 = locationPattern1.matcher(query); 
				Matcher locationMatcherWhere1 = itemPatternWhere1.matcher(query); 
				 if (itemMatcher1.find()) {
					 System.out.println("1->"+itemMatcher1.group(2));
				 }else if (itemMatcher2.find()) {
					 System.out.println("2->"+itemMatcher2.group(1));
				 }else{
					 System.out.println(" Not found");
				 }
				 
				 if (locationMatcher1.find()) {
					 System.out.println(locationMatcher1.group(1)+" "+locationMatcher1.group(2));
				 }else {
					 System.out.println(" Not found");
				 }
				 if (locationMatcherWhere1.find()) {
					 System.out.println(locationMatcherWhere1.group(2));
				 }
		 }
		
	}

}
