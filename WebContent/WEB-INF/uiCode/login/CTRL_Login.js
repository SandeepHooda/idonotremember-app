APP.CONTROLLERS.controller ('CTRL_Login',['$scope','$state','$http','$ionicLoading','appData','$ionicPopup',
    function($scope,$state,$http,$ionicLoading,appData,$ionicPopup){
	var theCtrl = this;
	$scope.login = {};
	$scope.login.type="signin";
	$scope.host = appData.getHost();
	$scope.conditionAgree = false;
	 var config = {
	            headers : {
	                'Content-Type': 'application/json;'
	            }
	        }
	$scope.detectMobilebrowser = detectMobilebrowser;
	$scope.iAgree = function(){
		$scope.conditionAgree = true;
	}
	$scope.iAgreeAndGoToTop = function() {
		$scope.iAgree();
	    document.body.scrollTop = 0;
	    document.documentElement.scrollTop = 0;
	}
	// This method is executed when the user press the "Sign in with Google" button
	  $scope.googleSignIn = function() {
	    $ionicLoading.show({
	      template: 'Logging in...'
	    });
	    window.plugins.googleplus.login(
	      {},
	      function (user_data) {
	        // For the purpose of this example I will store user data on local storage
	       /* UserService.setUser({
	          userID: user_data.userId,
	          name: user_data.displayName,
	          email: user_data.email,
	          picture: user_data.imageUrl,
	          accessToken: user_data.accessToken,
	          idToken: user_data.idToken
	        });*/
	    	  alert( user_data.email)
	        $ionicLoading.hide();
	    	  $state.transitionTo('menu.tab.home');
	      },
	      function (msg) {
	    	  alert( msg)
	        $ionicLoading.hide();
	      }
	    );
	  };
	
	
	function getCookie(cname) {
	    var name = cname + "=";
	    var decodedCookie = decodeURIComponent(document.cookie);
	    var ca = decodedCookie.split(';');
	    for(var i = 0; i <ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0) == ' ') {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length, c.length);
	        }
	    }
	    return "";
	}
	var regID = getCookie('regID');
	var regIDStorege = window.localStorage.getItem('regID');
	 if (regID && regIDStorege != 'invalid'){
		 window.localStorage.setItem('regID', regID);
		 window.localStorage.setItem('name', getCookie('name'));
	 }

	 var name = window.localStorage.getItem('name');
		if (name ){
			$scope.userName = "Welcome "+name;
		}else {
			$scope.userName ="Hello Guest";
		}
	
	if (regIDStorege && regIDStorege != 'invalid' ){
		if (window.localStorage.getItem('postlogin-moveto')){
			$state.transitionTo(window.localStorage.getItem('postlogin-moveto'));
			
		}else {
			$state.transitionTo('menu.tab.home');
		}
		
	}else if (regIDStorege == 'invalid'){
		localStorage.removeItem('regID');
	}
	$scope.showMenu = function () {
	    $ionicSideMenuDelegate.toggleLeft();
	};
	
	
	 theCtrl.signIN = function(){
		 if(window.localStorage.getItem('regID')){
			 localStorage.removeItem('regID');
			 localStorage.removeItem('name');
		 }
		 
		 window.open("/Oauth", "_self");
	 }
	 theCtrl.signInPassword = function(){
		 if(window.localStorage.getItem('regID')){
			 localStorage.removeItem('regID');
			 localStorage.removeItem('name');
		 }
		 if ($scope.validateUserNamePwd()){
			 $scope.showBusy();
			 $http.post(appData.getHost()+'/ws/login/loginWithPassword/',$scope.login , config)
		  		.then(function(response){
		  			 $scope.hideBusy();
		  			 let loginVO = response.data
		  			 if (loginVO){
		  				window.localStorage.setItem('regID', loginVO.regID);
		  				 if ($scope.login.type === 'signup'){
		  					$scope.popUp('Verify your identity', 'We have sent you and verification email on you email ID '+$scope.login.userName+
		  							'. Please check your email and confirm that you are the owner of the email ID.',null ) 
		  				 }else {
		  					$state.transitionTo("menu.tab.home");
		  				 }
		  				
			  			 
		  			 }else {
		  				$scope.popUp('Server Error', 'We encontered some error while processing your request. Please retry after some time. ',null ) 
		  			 }
		  			 
		  		},
				function(response){
		  			 $scope.hideBusy();
		  			 if (response.status == 401){
		  				$scope.popUp('Invalid user name/password', 'The user name/password that you supplied doesn\'t match our records. ',null )
		  			 }else {
		  				$scope.popUp('Server Error', 'We encontered some error while processing your request. Please retry after some time. ',null )
		  			 }
		  			
		  			});
		 }
		
	 }
	 theCtrl.showPassword  = function(){
		 let x = document.getElementById("password");
		  if (x.type === "password") {
		    x.type = "text";
		  } else {
		    x.type = "password";
		  }
	 }
	 $scope.validateUserNamePwd = function(){
		 if ($scope.login.userName){
			 $scope.login.userName = $scope.login.userName.toLocaleLowerCase();
			 if ($scope.login.userName.endsWith("@gmail.com")){
				 if($scope.login.password && $scope.login.password.length >=8 ){
					 if ( $scope.login.type=="signin" || ( $scope.login.passwordConfirm === $scope.login.password)){
						 return true;
					 }else {
						 $scope.popUp('Password mismatch', 'Your password and confirm password don\'t match',null ) 
					 }
						
				 }else {
					 $scope.popUp('Invalid password', 'Please use your a password with length equal or greater than 8',null ) 
				 }
				
			 }else {
				 $scope.popUp('Invalid user name', 'Please use your Gmail ID',null )
			 }
		 }
		
		 return false;
	 }
	 
	 $scope.popUp = function(subject, body, nextStep){
			var confirmPopup = $ionicPopup.confirm({
			     title: subject,
			     template: body
			   });
			 confirmPopup.then(function(res) {
				 if (nextStep){
					 $state.transitionTo(nextStep);
				 }
			  });
		}
		
		//Busy icon
		  $scope.showBusy = function() {
			    $ionicLoading.show({
			      template: 'Please Wait...',
			      duration: 10000
			    }).then(function(){
			       console.log("The loading indicator is now displayed");
			    });
			  };
			  $scope.hideBusy = function(){
			    $ionicLoading.hide().then(function(){
			       console.log("The loading indicator is now hidden");
			    });
			  };
	 
	  
}
])