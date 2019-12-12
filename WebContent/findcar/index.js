 
function getCarLocation() {
	    var xmlhttp = new XMLHttpRequest();

	    xmlhttp.onreadystatechange = function() {
	        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
	           if (xmlhttp.status == 200) {
	        	  
	        	   if(xmlhttp.responseText){
	        		   var resp =  JSON.parse(xmlhttp.responseText.substring(5));
		        	  let location = "http://maps.google.com/maps?&z=10&q="+resp.latitude+"+"+resp.longitude+"(Pool+Location)&mrt=yp"
	        		   window.open(location);
		        	
		         	  
	        	   }
	        	   else {
	        		   userSignStatus(false);
	        	   }

	               
	           }
	           else if (xmlhttp.status == 400) {
	              alert('There was an error 400');
	           }
	           else {
	               alert('something else other than 200 was returned');
	           }
	        }
	    };
	    xmlhttp.open("GET", "/ws/location/mmi/car/cordinates", true);
	 
	    xmlhttp.send(); 
	}


 
window.onload = function() {
	getCarLocation();
  }
  

  