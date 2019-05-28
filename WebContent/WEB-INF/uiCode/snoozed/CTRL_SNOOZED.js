APP.CONTROLLERS.controller ('CTRL_SNOOZED',['$window', '$location','$scope','$ionicSideMenuDelegate','$state','$http','$rootScope','$ionicPopup','$ionicLoading','appData',
    function($window,$location,$scope, $ionicSideMenuDelegate,$state,$http,$rootScope,$ionicPopup,$ionicLoading,appData){
	var theCtrl = this;
	var regID = window.localStorage.getItem('regID');
	$scope.snoozedReminders = [];
	$scope.currentCallCredits = 0;
	
	
	/*if (document.URL.indexOf('localhost')>=0){
		regID = "69905a13-79b9-4314-95fa-17a87a6121b0";
		 window.localStorage.setItem('regID', regID);
	}*/
	var regIDStorege = window.localStorage.getItem('regID');
	 var config = {
	            headers : {
	                'Content-Type': 'application/json;',
	                'Auth' : ''+regIDStorege
	            }
	        }
	if (!regID){
		regID = $location.search()['regID']
		window.localStorage.setItem('regID',regID );
	}
	
		 $http.get(appData.getHost()+'/ws/login/validate/'+regID+'/timeZone/'+Intl.DateTimeFormat().resolvedOptions().timeZone.replace("/", "@"), config)
	  		.then(function(response){
					if (!window.localStorage.getItem('name') ){
						let userName = response.data.userName;
						if (userName.indexOf("@") >0){
							userName = userName.substring(0,userName.indexOf("@"));
						}
						window.localStorage.setItem('name', userName);
					}
				
	  			$scope.getCallCredits();
	  			$scope.recordLoginSucess();
	  		},
			function(response){
	  			window.localStorage.removeItem('regID');
  				localStorage.removeItem('name');
  				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
				$state.transitionTo('menu.login');
				
			});
	
	
	$scope.getSnoozedReminders = function(){
		$http.get(appData.getHost()+'/ws/snoozed/reminder',config)
  		.then(function(response){
  			$scope.snoozedReminders = response.data;
  		
  		},
		function(response){
  			window.localStorage.removeItem('regID');
				localStorage.removeItem('name');
				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
			$state.transitionTo('menu.login');
			
		});
	}
	$scope.deleteReminder = function(deleteIndex){
		$scope.deleteIndex  = deleteIndex;
		 var confirmPopup = $ionicPopup.confirm({
		     title: ''+$scope.snoozedReminders[$scope.deleteIndex].reminderSubject+" "+$scope.snoozedReminders[$scope.deleteIndex].reminderText,
		     template: 'Do you want to delete this reminder.'
		   });

		   confirmPopup.then(function(res) {
			   if (res){
				   
				   $scope.showBusy();
					
					 $http.delete(appData.getHost()+'/ws/snoozed/reminder/'+$scope.snoozedReminders[$scope.deleteIndex]._id, config)
				  		.then(function(response){
				  			 $scope.hideBusy();
				  			$scope.snoozedReminders = response.data;
				  		},
						function(response){
				  			 $scope.hideBusy();
							
						});
				   
				  
			   }
		     
		   });
	}
	
	$scope.getCallCredits = function(){
		$http.get(appData.getHost()+'/ws/callcredits/regid/'+regID,config)
  		.then(function(response){
  			$scope.currentCallCredits = response.data;
  			$scope.$emit('getMyRemindersList');
  		},
		function(response){
  			window.localStorage.removeItem('regID');
				localStorage.removeItem('name');
				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
			$state.transitionTo('menu.login');
			
		});
	}
	
	$scope.recordLoginSucess = function(){
		$http.get(appData.getHost()+'/ws/recordLoginSucess', config)
  		.then(function(response){
  			if(response.data.userAgent ){
  				var userAgent = response.data.userAgent;
  				userAgent = userAgent.toLowerCase();
  				if (userAgent.indexOf("iphone") >=0 || userAgent.indexOf("apple") >=0 ){
  					$rootScope.isIos = true;
  				}
  			}
  		},
		function(response){
  			
			
		});
	}
	$scope.logOut = function(event){
		var confirmPopup = $ionicPopup.confirm({
		     title: 'Log out',
		     template: 'Are you sure you want to log off?'
		   });

		   confirmPopup.then(function(res) {
			   if (res){
				   
				   if ($window.location.host == "") { //android app
						window.localStorage.removeItem('regID');
						$state.transitionTo('menu.login');
					}else {
						 regIDStorege = window.localStorage.getItem('regID');
						 $http.get(appData.getHost()+'/ws/logout/'+regIDStorege,config)
					  		.then(function(response){
					  			if (!response.data){//Reg id don't exist in DB - after delete 
					  				window.localStorage.removeItem('regID');
					  				localStorage.removeItem('name');
					  				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
					  				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
					  				$state.transitionTo('menu.login');
					  			}
					  		},
							function(response){
					  			//Failed to log out
								
							});
					}
				   
				  
			   }
		     
		   });
		   
		
			
	}
	
	$rootScope.$on('logOut',$scope.logOut);
	
	$scope.addCash = function(){
		if ($window.location.host == "") { //android app
			//$scope.popUp(' Patment in progress ', 'Please complete your payment via the secure payment gateway and come back to the app',null);
			
			window.open("https://idonotremember-app.appspot.com/ui/index.html#/menu/addcash?regID="+regID, "_system");
			 //$state.transitionTo('menu.tab.home');
			
		}else {
			$state.transitionTo('menu.addcash');
		}
		
	}
	var name = window.localStorage.getItem('name');
	if (name ){
		$scope.userName = "Welcome "+name;
	}else {
		$scope.userName ="Hello Guest";
	}
	
	$scope.showMenu = function () {
		if(document.URL.indexOf('/menu/login') <0){//Disable hanburger in log in state
			 $ionicSideMenuDelegate.toggleLeft();
		}
	   
	  };
	  
	//Busy icon
	  $scope.showBusy = function() {
		  $scope.gettingUserReminderList = true;
		    $ionicLoading.show({
		      template: 'Please Wait...',
		      duration: 10000
		    }).then(function(){
		       console.log("The loading indicator is now displayed");
		    });
		  };
		  $scope.hideBusy = function(){
			  $scope.gettingUserReminderList = false;
		    $ionicLoading.hide().then(function(){
		       console.log("The loading indicator is now hidden");
		    });
		  };
	  
	  
}
])