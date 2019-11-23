
let mapCenter = {};
let startLoc = {};
let endLoc = {};
var waypts = [] /*[{
    location: '30.697166, 76.849892',
    stopover: true
  }, {
    location: '30.714483, 76.849709',
    stopover: true
  }, {
    location: '30.715767, 76.850243',
    stopover: true
  }];*/

function getMapCordinates(){
	let xhr = null;
	if (window.XMLHttpRequest) {
	    // code for modern browsers
		xhr = new XMLHttpRequest();
	 } else {
	    // code for old IE browsers
		 xhr = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 ) {
			if (this.status == 200){
				
				let locations = JSON.parse(this.responseText.substring(5));
				flightPlanCoordinates = [];
				for (let i=locations.length-1;i>=0;i--){
					let aLoc = {};
					aLoc.lat = Number(locations[i].lat);
					aLoc.lng = Number(locations[i].lon);
					let aWayPoint = {"stopover": true};
					aWayPoint.location = ""+aLoc.lat+", "+aLoc.lng;
					if (i == 3){
						mapCenter = aLoc;
					}
					if ( i >=1 && i<=3){
						waypts.push(aWayPoint);
					}
					if (i==0){
						endLoc = aLoc;
					}else if (i==4){
						startLoc = aLoc;
					}
				}
				initMap();
			}
		     
		   }
		
	  
	  };
	xhr.open("GET", "/ws/location/recent5", true);
	//xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhr.send();
	
}
var map;
var infowindow = null;
var colors = ["#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF"/*, "#00FFFF"*/];
var polylines = [];
function initMap() {
	 infowindow = new google.maps.InfoWindow();
	 google.maps.event.addDomListener(window, "load", initMap);
  var directionsService = new google.maps.DirectionsService;
  var directionsDisplay = new google.maps.DirectionsRenderer({
    suppressPolylines: true,
    infoWindow: infowindow
  });
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 6,
    center: mapCenter
  });
  directionsDisplay.setMap(map);
  calculateAndDisplayRoute(directionsService, directionsDisplay);
}

function calculateAndDisplayRoute(directionsService, directionsDisplay) {
  
  directionsService.route({
    origin: startLoc,
    destination: endLoc,
    waypoints: waypts,
    optimizeWaypoints: true,
    travelMode: google.maps.TravelMode.DRIVING
  }, function(response, status) {
    if (status === google.maps.DirectionsStatus.OK) {
      directionsDisplay.setOptions({
        directions: response,
      })
      var route = response.routes[0];
      renderDirectionsPolylines(response, map);
    } else {
      window.alert('Directions request failed due to ' + status);
    }
  });

}



var polylineOptions = {
  strokeColor: '#C83939',
  strokeOpacity: 1,
  strokeWeight: 4
};


function renderDirectionsPolylines(response) {
  var bounds = new google.maps.LatLngBounds();
  for (var i = 0; i < polylines.length; i++) {
    polylines[i].setMap(null);
  }
  var legs = response.routes[0].legs;
  for (i = 0; i < legs.length; i++) {
    var steps = legs[i].steps;
    for (j = 0; j < steps.length; j++) {
      var nextSegment = steps[j].path;
      var stepPolyline = new google.maps.Polyline(polylineOptions);
      stepPolyline.setOptions({
        strokeColor: colors[i]
      })
      for (k = 0; k < nextSegment.length; k++) {
        stepPolyline.getPath().push(nextSegment[k]);
        bounds.extend(nextSegment[k]);
      }
      polylines.push(stepPolyline);
      stepPolyline.setMap(map);
      // route click listeners, different one on each step
      google.maps.event.addListener(stepPolyline, 'click', function(evt) {
        infowindow.setContent("you clicked on the route<br>" + evt.latLng.toUrlValue(6));
        infowindow.setPosition(evt.latLng);
        infowindow.open(map);
      })
    }
  }
  map.fitBounds(bounds);
}
   
   function refeshMap(){
	   setInterval(function(){ getMapCordinates(); }, 60000);
   }