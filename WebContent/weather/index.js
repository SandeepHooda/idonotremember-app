 
function getWeather() {
	    var xmlhttp = new XMLHttpRequest();

	    xmlhttp.onreadystatechange = function() {
	        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
	           if (xmlhttp.status == 200) {
	        	   document.getElementById("loader").style.display = "none";
	        	   if(xmlhttp.responseText){
	        		   let resp =  JSON.parse(xmlhttp.responseText.substring(5));
	        		   document.getElementById("img").src = 'http://openweathermap.org/img/wn/'+resp.current.weather[0].icon+'.png'
			           document.getElementById("desc").innerHTML = resp.current.weather[0].main + " ( "+resp.current.weather[0].description +")"
	        		   
		        	  /*let windSpeed = "("+resp.current.windSpeed.toFixed(2) + " ) "+ resp.hourly[0].windSpeed.toFixed(2) + ", "+ resp.hourly[1].windSpeed.toFixed(2) + ", "+ resp.hourly[2].windSpeed.toFixed(2)  + ", "+ resp.hourly[3].windSpeed.toFixed(2) + ", "+ resp.hourly[4].windSpeed.toFixed(2) 
		        	  document.getElementById("weather").innerHTML = "Wind Speed : " +windSpeed +" km/h <br/> Temprature: "+resp.current.temp
		        	  +" C <br/> Humidity : "+resp.current.humidity+" % <br/> Clouds : "+resp.current.clouds +" % "*/
		        	  
		        	  document.getElementById("wind_0").innerHTML = resp.hourly[0].windSpeed.toFixed(2);
	        		  document.getElementById("wind_1").innerHTML = resp.hourly[1].windSpeed.toFixed(2);
	        		  document.getElementById("wind_2").innerHTML = resp.hourly[2].windSpeed.toFixed(2);
	        		  document.getElementById("wind_3").innerHTML = resp.hourly[3].windSpeed.toFixed(2);
	        		  document.getElementById("wind_4").innerHTML = resp.hourly[4].windSpeed.toFixed(2);
	        		  
	        		  document.getElementById("t_0").innerHTML = resp.hourly[0].temp.toFixed(2);
	        		  document.getElementById("t_1").innerHTML = resp.hourly[1].temp.toFixed(2);
	        		  document.getElementById("t_2").innerHTML = resp.hourly[2].temp.toFixed(2);
	        		  document.getElementById("t_3").innerHTML = resp.hourly[3].temp.toFixed(2);
	        		  document.getElementById("t_4").innerHTML = resp.hourly[4].temp.toFixed(2);
	        		  
	        		  document.getElementById("h_0").innerHTML = resp.hourly[0].humidity.toFixed(2);
	        		  document.getElementById("h_1").innerHTML = resp.hourly[1].humidity.toFixed(2);
	        		  document.getElementById("h_2").innerHTML = resp.hourly[2].humidity.toFixed(2);
	        		  document.getElementById("h_3").innerHTML = resp.hourly[3].humidity.toFixed(2);
	        		  document.getElementById("h_4").innerHTML = resp.hourly[4].humidity.toFixed(2);
	        		  
	        		  document.getElementById("c_0").innerHTML = resp.hourly[0].clouds.toFixed(2);
	        		  document.getElementById("c_1").innerHTML = resp.hourly[1].clouds.toFixed(2);
	        		  document.getElementById("c_2").innerHTML = resp.hourly[2].clouds.toFixed(2);
	        		  document.getElementById("c_3").innerHTML = resp.hourly[3].clouds.toFixed(2);
	        		  document.getElementById("c_4").innerHTML = resp.hourly[4].clouds.toFixed(2);
	        		  
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
  

  