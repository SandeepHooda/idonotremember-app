APP.SERVICES.service ('appData',['$window','dataRestore','$ionicPopup',
    function( $window,dataRestore, $ionicPopup){
	this.cartItems = [];
	this.offerItems = [];
	this.getShopID = function () {
		return "1519981368108";
	}
	this.getHost = function () {
		//var host = "http://www.idonotremember.com";
		//var host = "https://idonotremember-app.appspot.com";
		var host = "";
		if ($window.location.host == ""){ //android app
			host = "https://idonotremember-app.appspot.com";
			//host = "https://deliveratmydoor.appspot.com";
		}else if ($window.location.host.indexOf("localhost") >=0 ){
			host = "http://localhost:9080";
		}else {
			host = "";
		}
		
		return host;
	}
	
	this.showErrorMessage = function(httpCode){
		if ( httpCode == 403){
			var confirmPopup = $ionicPopup.confirm({
			     title: 'Password mimatch',
			     template: 'Your password donot match our records.'
			   });
			 confirmPopup.then(function(res) {
			  });
		}else {
			var confirmPopup = $ionicPopup.confirm({
			     title: 'Internal Server Error',
			     template: 'Something unusual happened at server.'
			   });
			 confirmPopup.then(function(res) {
			  });
				
		}
	}
	
	this.getOfferItems = function(){
		return this.offerItems;
	}
	this.setOfferItems = function(offerItems){
		this.offerItems = offerItems;
	}
	this.getCartItems = function(){
		return this.cartItems;
	}
	
	
}

]);