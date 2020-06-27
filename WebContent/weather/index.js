 
function getWeather() {
	    var xmlhttp = new XMLHttpRequest();

	    xmlhttp.onreadystatechange = function() {
	        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
	           if (xmlhttp.status == 200) {
	        	   document.getElementById("loader").style.display = "none";
	        	   if(xmlhttp.responseText){
	        		   var resp =  JSON.parse(xmlhttp.responseText.substring(5));
		        	  
		        	  document.getElementById("weather").innerHTML = "Wind Speed : " +resp.current.windSpeed +"<br/> Temprature: "+resp.current.temp
		        	  +"<br/> Humidity : "+resp.current.humidity+"<br/> Clouds : "+resp.current.clouds
		        	  
		        	  document.getElementById("img").src = 'http://openweathermap.org/img/wn/'+resp.current.weather[0].icon+'.png'
		        	  document.getElementById("desc").innerHTML = resp.current.weather[0].main + " ( "+resp.current.weather[0].description +")"
	        		  
	        	   }
	        	   

	               
	           }
	           else if (xmlhttp.status == 400) {
	              alert('There was an error 400');
	              document.getElementById("loader").style.display = "none";
	           }
	           else {
	               alert('something else other than 200 was returned');
	               document.getElementById("loader").style.display = "none";
	           }
	        }
	    };
	    xmlhttp.open("GET", "/ws/weather", true);
	    document.getElementById("loader").style.display = "block";
	   xmlhttp.send(); 
	}
function snoozAlerts(){
	document.getElementById("snooz").innerHTML  = "";
	var xmlhttp = new XMLHttpRequest();

    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
           if (xmlhttp.status == 200) {
        	   document.getElementById("loader").style.display = "none";
        	   document.getElementById("snooz").innerHTML = "Alerts are snoozed for 12 hours " 
        	  
           }
           else if (xmlhttp.status == 400) {
              alert('There was an error 400');
              document.getElementById("loader").style.display = "none";
           }
           else {
               alert('something else other than 200 was returned');
               document.getElementById("loader").style.display = "none";
           }
        }
    };
    xmlhttp.open("GET", "/ws/weather/snoozAlerts", true);
    document.getElementById("loader").style.display = "block";
   xmlhttp.send(); 
}
 
window.onload = function() {
	getWeather();
  }
  

  