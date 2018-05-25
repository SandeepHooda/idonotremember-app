// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
var APP = {};
APP.DIRECTIVE = angular.module('allDirective',[]);
APP.CONTROLLERS = angular.module('allControllers',[]);
APP.SERVICES = angular.module('allServices',[]);
APP.FACTORY = angular.module('allFact',[]);
APP.DEPENDENCIES = ['allControllers',
                    'allServices',
                    'allDirective',
                    'allFact'
                    ];
APP.OTHERDEPENDENCIES = ['ionic','ngCordova','ionic-numberpicker'];
angular.module('starter', APP.DEPENDENCIES.concat(APP.OTHERDEPENDENCIES))
.config(['$urlRouterProvider','$stateProvider','$ionicConfigProvider',
         function($urlRouterProvider,$stateProvider,$ionicConfigProvider){
	$ionicConfigProvider.tabs.position('bottom');
	 // setup an abstract state for the tabs directive
				$stateProvider.state('menu',{
					url:'/menu',
					abstract: true,
					templateUrl:'menu.html'	
					 
					
				}).state('menu.contacts',{
					url:'/contacts',
					templateUrl: 'contacts/contacts.html',
					controller: 'CTRL_CONTACTS'
				}).state('menu.help',{
					url:'/help',
					templateUrl: 'help/help.html',
					controller: 'CTRL_help'
				}).state('menu.tab',{
					url:'/tab',
					abstract: true,
					templateUrl:'tabs.html'	
					 
					
				}).state('menu.tab.home',{
					url:'/home',
					views: {
						 'tab-home': {
						 templateUrl: 'home/home.html',
						 controller: 'CTRL_HOME'
						 }
					}	
					
				}).state('menu.snoozed',{
					url:'/snoozed',
					templateUrl: 'snoozed/snoozed.html',
					controller: 'CTRL_SNOOZED'
				}).state('menu.feedback',{
					url:'/feedback',
					templateUrl: 'help/feedback/feedback.html',
					controller: 'CTRL_feedback'
				}).state('menu.anexture',{
					url:'/anexture',
					templateUrl: 'help/annexure/annexure.html',
					controller: 'CTRL_help'
				}).state('menu.tab.todo',{
					url:'/todo',
					views: {
						 'tab-todo': {
						 templateUrl: 'home/todo/todo.html',
						 controller: 'CTRL_TODO'
						 }
					}	
					
				}).state('menu.newReminder',{
					url:'/newReminder',
					templateUrl: 'newReminder/newReminder.html',
					controller: 'CTRL_NewReminder'
				}).state('menu.login',{
					url:'/login',
					templateUrl: 'login/login.html',
					controller: 'CTRL_Login'
				}).state('menu.addcontacts',{
					url:'/addcontact',
					templateUrl: 'contacts/add/addcontacts.html',
					controller: 'CTRL_ADDCONTACTS'
				}).state('menu.addcash',{
					url:'/addcash',
					templateUrl: 'cash/cash.html',
					controller: 'CTRL_CASH'
				})
				$urlRouterProvider.otherwise('/menu/tab/home');
			}
         ])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if(window.cordova && window.cordova.plugins.Keyboard) {
      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
      // for form inputs)
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);

      // Don't remove this line unless you know what you are doing. It stops the viewport
      // from snapping when text inputs are focused. Ionic handles this internally for
      // a much nicer keyboard experience.
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }
  });
})
;APP.CONTROLLERS.controller ('CTRL_SNOOZED',['$scope','$ionicSideMenuDelegate','$state','$http','$rootScope','$ionicPopup','$ionicLoading','appData',
    function($scope, $ionicSideMenuDelegate,$state,$http,$rootScope,$ionicPopup,$ionicLoading,appData){
	var theCtrl = this;
	var regID = window.localStorage.getItem('regID');
	$scope.snoozedReminders = [];
	$scope.currentCallCredits = 0;
	if (document.URL.indexOf('localhost')>=0){
		regID = "070dd753-08e7-4a58-81f9-29ea3ad6c6c0";
		 window.localStorage.setItem('regID', regID);
	}
	
	if (!regID){
		$state.transitionTo('menu.login');
	}else {
		 $http.get(appData.getHost()+'/ws/login/validate/'+regID+'/timeZone/'+Intl.DateTimeFormat().resolvedOptions().timeZone.replace("/", "@"))
	  		.then(function(response){
	  			$scope.getCallCredits();
	  			$scope.recordLoginSucess();
	  		},
			function(response){
	  			window.localStorage.setItem('regID', 'invalid');
  				localStorage.removeItem('name');
  				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
				$state.transitionTo('menu.login');
				
			});
	}
	
	$scope.getSnoozedReminders = function(){
		$http.get(appData.getHost()+'/ws/snoozed/reminder')
  		.then(function(response){
  			$scope.snoozedReminders = response.data;
  		
  		},
		function(response){
  			window.localStorage.setItem('regID', 'invalid');
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
					
					 $http.delete(appData.getHost()+'/ws/snoozed/reminder/'+$scope.snoozedReminders[$scope.deleteIndex]._id)
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
		$http.get(appData.getHost()+'/ws/callcredits/regid/'+regID)
  		.then(function(response){
  			$scope.currentCallCredits = response.data;
  			$scope.$emit('getMyRemindersList');
  		},
		function(response){
  			window.localStorage.setItem('regID', 'invalid');
				localStorage.removeItem('name');
				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
			$state.transitionTo('menu.login');
			
		});
	}
	
	$scope.recordLoginSucess = function(){
		$http.get(appData.getHost()+'/ws/recordLoginSucess')
  		.then(function(response){
  			
  		},
		function(response){
  			
			
		});
	}
	
	$rootScope.$on('logOut',function(event){
		 regIDStorege = window.localStorage.getItem('regID');
		 $http.get(appData.getHost()+'/ws/logout/'+regIDStorege)
	  		.then(function(response){
	  			if (!response.data){//Reg id don't exist in DB - after delete 
	  				window.localStorage.setItem('regID', 'invalid');
	  				localStorage.removeItem('name');
	  				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	  				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	  				$state.transitionTo('menu.login');
	  			}
	  		},
			function(response){
	  			//Failed to log out
				
			});
		});
	
	$scope.addCash = function(){
		$state.transitionTo('menu.addcash');
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
]);APP.CONTROLLERS.controller ('CTRL_Login',['$scope','$state','$http','$ionicLoading','appData',
    function($scope,$state,$http,$ionicLoading,appData){
	var theCtrl = this;
	$scope.host = appData.getHost();
	$scope.conditionAgree = false;
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
	 
	 
	
	 
	  
}
]);APP.CONTROLLERS.controller ('CTRL_NewReminder',['$scope','$http','$rootScope','$ionicPopup','$state','$ionicLoading','$window','appData',
    function($scope, $http, $rootScope ,$ionicPopup,$state,$ionicLoading,$window,appData){

	var theCtrl = this;
	theCtrl.selectedPhone = "";
	$scope.reminder = {}
	 var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
var monthNames =[
		
		"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
	]
	$scope.frequencyDays = ["Monday", "Tuesday", "Wednesday", "Thrusday","Friday", "Saturday", "Sunday"];
	$scope.frequencyDaysRepeat = ["Every", "First", "Second", "Third", "Fourth"];
	$scope.reminder.selectedDayRepeat = "Every";
	$scope.reminder.selectedDay = "Monday";
	$scope.moveToMonth =function(){
		if ( ($scope.reminder.year > 999 && $scope.reminder.year < 2018) || isNaN($scope.reminder.year) ){
			$scope.reminder.year = "";
		}
		if ($scope.reminder.year.length == 4){
			var month = $window.document.getElementById('month');
			month.focus();
		}
		
	}
	$scope.moveToDay =function(){
		if ($scope.reminder.month > 12 || isNaN($scope.reminder.month) ){
			$scope.reminder.month = "";
		}
		if ($scope.reminder.month.length == 2){
			var day = $window.document.getElementById('day');
			day.focus();
		}
		
	}
	$scope.moveToHour =function(){
		if ($scope.reminder.day > 31 || isNaN($scope.reminder.day) ){
			$scope.reminder.day = "";
		}
		if ($scope.reminder.day.length == 2){
			var hour = $window.document.getElementById('hour');
			hour.focus();
		}
		
	}
	$scope.moveToMinute =function(){
		if ($scope.reminder.hour > 23 || isNaN($scope.reminder.hour) ){
			$scope.reminder.hour = "";
		}
		if ($scope.reminder.hour.length ==2){
			var minute = $window.document.getElementById('minute');
			minute.focus();
		}
		
	}
	$scope.moveToMinute_Day  =function(){
		if ($scope.reminder.hour > 23 || isNaN($scope.reminder.hour) ){
			$scope.reminder.hour = "";
		}
		if ($scope.reminder.hour.length ==2){
			var minute_Day = $window.document.getElementById('minute_Day');
			minute_Day.focus();
		}
		
	}
	$scope.moveToSubject = function(){
		if ($scope.reminder.minute > 59 || isNaN($scope.reminder.minute) ){
			$scope.reminder.minute = "";
		}
		if ($scope.reminder.minute.length ==2){
			var subject = $window.document.getElementById('subject');
			subject.focus();
		}
		
	}
	$scope.checkEnter = function(){
		if(event.keyCode == 13){
			//$scope.addReminder();
		}
	}
	
	$scope.addPhoneNoToProfile = function(){
		$state.transitionTo('menu.addcontacts');
	}
	$scope.toggleCall = function (){
		if($scope.verifiedPhones.length <= 0){
				$scope.reminder.makeACall = false;
				$scope.reminder.sendText = false;
		}else {
			$scope.reminder.makeACall = !$scope.reminder.makeACall;
		}
		
		window.localStorage.setItem('makeACall', ""+$scope.reminder.makeACall);
		window.localStorage.setItem('sendText', ""+$scope.reminder.sendText);
	}
	$scope.toggleSMS = function (){
		if($scope.verifiedPhones.length <= 0){
			$scope.reminder.makeACall = false;
			$scope.reminder.sendText = false;
		}else {
			$scope.reminder.sendText = !$scope.reminder.sendText;
		}
		window.localStorage.setItem('makeACall', ""+$scope.reminder.makeACall);
		window.localStorage.setItem('sendText', ""+$scope.reminder.sendText);
	}
	$scope.frequencyWithDate = "Once";
	$scope.changeFrequency = function(){
		if ($scope.frequencyWithDate == "Once"){
			$scope.frequencyWithDate = "Monthly";
		}else if ($scope.frequencyWithDate == "Monthly"){
			$scope.frequencyWithDate = "Yearly";
		}else {
			$scope.frequencyWithDate = "Once";
		}
	}
	$scope.frequencyType = "Date";
	$scope.changeFrequencyType = function(){
		if ($scope.frequencyType == "Date"){
			$scope.frequencyType = "Day";
		}else {
			$scope.frequencyType = "Date";
		}
	}
	$scope.validateRequiredfields = function(){
		var reminderObj = {};
		if (!$scope.reminder.reminderSubject || !$scope.reminder.reminderText){
			  return null;
		  }
		var allRequiredFields = true;
		if($scope.frequencyType =='Date'){
			 if ($scope.frequencyWithDate== "Once" ){
				 	if (!$scope.reminder.year  || !$scope.reminder.month || !$scope.reminder.day || !$scope.reminder.hour || !$scope.reminder.minute  ) {
				 		allRequiredFields = false;
				 	}else {
				 		reminderObj.displayTime = $scope.reminder.day + " " +monthNames[$scope.reminder.month-1] +" "+$scope.reminder.year +" @ "+ $scope.reminder.hour +" : "+$scope.reminder.minute 
				 	}
			  }else  if ($scope.frequencyWithDate== "Monthly"){
				  if (!$scope.reminder.day || !$scope.reminder.hour || !$scope.reminder.minute) {
					  allRequiredFields = false;
				  }else {
					  reminderObj.displayTime = "Every month "+$scope.reminder.day +" @ "+ $scope.reminder.hour +" : "+$scope.reminder.minute;
				  }
			  }else {
				  if (!$scope.reminder.month || !$scope.reminder.day || !$scope.reminder.hour || !$scope.reminder.minute) {
					  allRequiredFields = false;
				  }else {
					  reminderObj.displayTime = "Every Year  "+$scope.reminder.day + " " +monthNames[$scope.reminder.month-1] +" @ "+ $scope.reminder.hour +" : "+$scope.reminder.minute;
				  }
			  }
			  
			  
			 
		}else {
			if (!$scope.reminder.hour || !$scope.reminder.minute) {
				  allRequiredFields = false;
			  }else {
				  reminderObj.displayTime = $scope.reminder.selectedDayRepeat +" "+$scope.reminder.selectedDay +" of every month @ "+ $scope.reminder.hour +" : "+$scope.reminder.minute;
			  }
			
		}
		if (allRequiredFields){
			return reminderObj;
		}else {
			return null;
		}
	}
	$scope.addReminder = function(){
		
		var reminderObj = $scope.validateRequiredfields();
		
		 
		  if(reminderObj == null){
				$scope.popUp('Invalid entry', 'Please fill all the mandatory fields',null );
				return;
			}
		  
		  
			reminderObj.regID = window.localStorage.getItem('regID');
			
			
			reminderObj.reminderSubject = $scope.reminder.reminderSubject ;
			reminderObj.reminderText = $scope.reminder.reminderText;
			
			reminderObj.makeACall = $scope.reminder.makeACall;
			reminderObj.sendText = $scope.reminder.sendText;
			
			reminderObj.frequencyType = $scope.frequencyType;
			reminderObj.selectedPhone = theCtrl.selectedPhone;
		if($scope.frequencyType =='Date'){
			reminderObj.frequencyWithDate = $scope.frequencyWithDate;//Once , Monthly, Yearly
			reminderObj.date =$scope.reminder.year+"_"+$scope.reminder.month+"_"+$scope.reminder.day;
			reminderObj.time = $scope.reminder.hour+"_"+$scope.reminder.minute;
			reminderObj._id = new Date($scope.reminder.year,$scope.reminder.month -1, $scope.reminder.day, $scope.reminder.hour, $scope.reminder.minute).getTime() +Math.random();
		
			
		}else {
			reminderObj.time = $scope.reminder.hour+"_"+$scope.reminder.minute;
			reminderObj._id = new Date().getTime()
			reminderObj.dayRepeatFrequency = $scope.reminder.selectedDayRepeat +" "+$scope.reminder.selectedDay ;
			
		}
		
			
		$scope.showBusy();
		$http.post(appData.getHost()+'/ws/reminder/',reminderObj , config)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.popUp('Success', 'Reminder added Successfully','menu.tab.home' );
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				$scope.popUp('Failure', 'Please login back and then retry.',null );
  			 }else {
  				$scope.popUp('Failure', 'Please retry.',null );
  			 }
  			
  			
			
		});
	}
	$scope.verifiedPhones  = [];
	$scope.setCallAndTextSettings = function(){
		$scope.reminder.makeACall = true;
		$scope.reminder.sendText = true;
		if (window.localStorage.getItem('makeACall') && window.localStorage.getItem('makeACall') == "false"){
			$scope.reminder.makeACall = false;
		}
		if (window.localStorage.getItem('sendText') && window.localStorage.getItem('sendText') == "false"){
			$scope.reminder.sendText = false;
		}
	}
	$scope.getVerifiedPhones = function(){
		$http.get(appData.getHost()+'/ws/phone/verified/true')
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.verifiedPhones = response.data;
  				if($scope.verifiedPhones.length > 0){
  					theCtrl.selectedPhone = $scope.verifiedPhones[0];
  					
  					$scope.setCallAndTextSettings();
  				}else {
  					$scope.reminder.makeACall = false;
  					$scope.reminder.sendText = false;
  					window.localStorage.setItem('makeACall', ""+$scope.reminder.makeACall);
  					window.localStorage.setItem('sendText', ""+$scope.reminder.sendText);
  					
  				}
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				$scope.popUp('Failure', 'Please login back and then retry.',null );
  			 }else {
  				$scope.popUp('Failure', 'Please retry.',null );
  			 }
  			
  			
			
		});
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
		  
		  $scope.getVerifiedPhones();
	 
}]);APP.CONTROLLERS.controller ('CTRL_CONTACTS',['$scope','$ionicLoading','$http','$ionicPopup','$state','appData',
    function($scope,$ionicLoading,$http,$ionicPopup, $state,appData){
	var theCtrl = this;
	$scope.data = {};
	$scope.data.otpValue = "";
	$scope.userPhonesVerified = [];
	$scope.userPhonesNotVerified = [];
	var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
	$scope.newContact = function (){
		$state.transitionTo('menu.addcontacts');
	}
	
	$scope.getContacts = function(){
		$scope.showBusy();
		
		$http.get(appData.getHost()+'/ws/phone/')
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.populatePhoneNos(response);
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				$scope.popUp('Failure', 'Please login back and then retry.',null );
  			 }else {
  				$scope.popUp('Failure', 'Please retry.',null );
  			 }
  			
  			
			
		});
	}
	
	$scope.populatePhoneNos = function(response){
		$scope.userPhonesVerified = [];
		$scope.userPhonesNotVerified = [];
		for (var i =0; i<response.data.length;i++){
				response.data[i].number = "( "+response.data[i].number.substring(0,3) +" ) "+response.data[i].number.substring(3,6) +" - "+response.data[i].number.substring(6);
				if (response.data[i].verified){
					$scope.userPhonesVerified.push(response.data[i]);
				}else {
					$scope.userPhonesNotVerified.push(response.data[i]);
				}
			}
			
	}
	$scope.deleteContacts = function(index){
		
		
		 var confirmPopup = $ionicPopup.confirm({
		     title: 'Confirmation',
		     template: 'Do you want to delete this Phone no?'
		   });

		   confirmPopup.then(function(res) {
			   if (res){
				   
				   $scope.showBusy();
					
					$http.delete(appData.getHost()+'/ws/phone/phoneID/'+$scope.userPhonesNotVerified[index]._id)
			  		.then(function(response){
			  			 $scope.hideBusy();
			  			if (response.data){
			  				$scope.populatePhoneNos(response);
			  			}else {
			  				$scope.popUp('Failure', 'Please retry',null )
			  			}
			  			
			  		},
					function(response){
			  			 $scope.hideBusy();
			  			
			  			 if (response.status == 401) {
			  				$scope.popUp('Failure', 'Please login back and then retry.',null );
			  			 }else {
			  				$scope.popUp('Failure', 'Please retry.',null );
			  			 }
			  			
			  			
						
					});
				   
			   }
		   });
		
	}
	
	$scope.confirmOtp = function(index){
		$scope.showBusy();
		
		$http.get(appData.getHost()+'/ws/phone/phoneID/'+$scope.userPhonesNotVerified[index]._id+'/confirmOtp/'+$scope.data.otpValue)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.verifyOptInpt = -1;
  				$scope.data.otpValue = "";
  				$scope.getContacts();
  				
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				$scope.popUp('Failure', 'Please login back and then retry.',null );
  			 }else {
  				$scope.popUp('Failure', 'Please retry.',null );
  			 }
  			
  			
			
		});
		
	}
	$scope.verifyOptInpt = -1;
	$scope.verify = function(index){
		$scope.showBusy();
		
		$http.get(appData.getHost()+'/ws/phone/verify/ID/'+$scope.userPhonesNotVerified[index]._id)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.verifyOptInpt = index;
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				$scope.popUp('Failure', 'Please login back and then retry.',null );
  			 }else {
  				$scope.popUp('Failure', 'Please retry.',null );
  			 }
  			
  			
			
		});
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
		  
		  $scope.getContacts();
}]);APP.CONTROLLERS.controller ('CTRL_ADDCONTACTS',['$scope','$ionicLoading','$http','$ionicPopup','$state','appData',
    function($scope,$ionicLoading,$http,$ionicPopup, $state,appData){
	var theCtrl = this;
	$scope.countryCodes = ["India", "USA"];
	$scope.phone = {};
	$scope.phone.countryCode =  "India";
	var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
	
	$scope.addContact = function(){
		$scope.showBusy();
		
		$http.post(appData.getHost()+'/ws/phone/',$scope.phone , config)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.popUp('Success', 'Phone No added sucessfully','menu.contacts' );
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				$scope.popUp('Failure', 'Please login back and then retry.',null );
  			 }else {
  				$scope.popUp('Failure', 'Please retry.',null );
  			 }
  			
  			
			
		});
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
}]);APP.CONTROLLERS.controller ('CTRL_CASH',['$scope','$ionicLoading','$http','$ionicPopup','$state',
    function($scope,$ionicLoading,$http,$ionicPopup, $state){
	var theCtrl = this;
	$scope.recharge = {}
	$scope.recharge.amount = 100;
	$scope.paymentGateway = "Payment gateway"
	$scope.goToHome = function(){
		if (window.localStorage.getItem('postlogin-moveto')){
			$state.transitionTo(window.localStorage.getItem('postlogin-moveto'));
			
		}else {
			$state.transitionTo('menu.tab.home');
		}
	}
	$scope.validateAmount = function(){
		if (isNaN($scope.recharge.amount)){
			$scope.recharge.amount = 100;
		}
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
}]);APP.CONTROLLERS.controller ('CTRL_HOME',['$scope','$state','$rootScope','$ionicLoading','$http','$ionicPopup','appData',
    function($scope,$state,$rootScope,$ionicLoading,$http,$ionicPopup, appData){
	//cordova plugin add cordova-plugin-googleplus --variable REVERSED_CLIENT_ID=myreversedclientid
	//cordova plugin add cordova-plugin-keyboard
	//https://github.com/apache/cordova-plugin-geolocation
	//cordova plugin add phonegap-nfc 
	//cordova plugin add cordova-plugin-vibration
	//cordova plugin add https://github.com/katzer/cordova-plugin-email-composer.git#0.8.2
	//cordova plugin add https://github.com/cowbell/cordova-plugin-geofence
	//cordova plugin add cordova-plugin-vibration
	//cordova plugin add cordova-plugin-device-motion
	//cordova plugin add cordova-plugin-whitelist
	//cordova plugin add cordova-plugin-shake
	//cordova plugin add cordova-plugin-sms
	//cordova plugin add cordova-plugin-android-permissions@0.6.0
	//cordova plugin add cordova-plugin-tts
	//cordova plugin add https://github.com/macdonst/SpeechRecognitionPlugin org.apache.cordova.speech.speechrecognition
	//cordova plugin add https://github.com/SandeepHooda/Speachrecognization org.apache.cordova.speech.speechrecognition
	//cordova plugin add https://github.com/katzer/cordova-plugin-background-mode.git
//	cordova plugin add cordova-plugin-http
	//cordova plugin add cordova-plugin-contacts-phonenumbers
	//cordova plugin add https://github.com/boltex/cordova-plugin-powermanagement
	//cordova plugin add https://github.com/katzer/cordova-plugin-local-notifications de.appplant.cordova.plugin.local-notification
	 var config = {
	            headers : {
	                'Content-Type': 'application/json;'
	            }
	        }
	 dataLayer.push({'pageTitle': 'Home'});    // Better
	var theCtrl = this;
	theCtrl.searchInput = "";
	$scope.reminders =[];
	$scope.remindersInDB =[];
	var days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
	window.localStorage.setItem('postlogin-moveto','menu.tab.home');
	var name = window.localStorage.getItem('name');
	if (name ){
		$scope.userName = "Welcome "+name;
	}else {
		$scope.userName ="Hello Guest";
	}
	theCtrl.addNewReminder  = function(){
		$state.transitionTo('menu.newReminder');
	}
	theCtrl.logOut = function(){
		$scope.$emit('logOut');
	}
	
	
	$scope.showPosition = function(position) {
	  
	   var latLang = {};
	   latLang.latitude = position.coords.latitude;
	   latLang.longitude = position.coords.longitude;
	   
	    
	    $http.post(appData.getHost()+'/ws/updatePreciseLocation/',latLang , config)
  		.then(function(response){
  		},
		function(response){
  			});
	}
	
	$scope.getLocation = function() {
		$scope.geoLocationPermissionGranted = true;
		window.localStorage.setItem('geoLocationPermissionGranted',''+$scope.geoLocationPermissionGranted);
	    if (navigator.geolocation) {
	        navigator.geolocation.getCurrentPosition($scope.showPosition);
	    } 
	}
	$scope.geoLocationPermissionGranted = false;
	if (window.localStorage.getItem('geoLocationPermissionGranted')){
		$scope.geoLocationPermissionGranted = true;
		if (!$rootScope.gotPreciseLocation){
			$scope.getLocation();
		}
		$rootScope.gotPreciseLocation = true;
		
		
	}
	$scope.updateReminder = function(reminder){
		$http.put(appData.getHost()+'/ws/reminder/', reminder, config)
  		.then(function(response){
  			
  		},
		function(response){
  			 
		});
	}
	$scope.toggleCall = function(index){
		$scope.reminders[index].makeACall = !$scope.reminders[index].makeACall;
		$scope.updateReminder($scope.reminders[index]);
		
	}
	
	$scope.toggleSMS = function(index){
		$scope.reminders[index].sendText = !$scope.reminders[index].sendText;
		$scope.updateReminder($scope.reminders[index]);
		
	}
	$scope.filterByText = function(){
		$scope.reminders =[];
		var filteredReminders = [];
		
		//Nothing to filter
		if (theCtrl.searchInput == ""){
			for (var i=0;i<$scope.remindersInDB.length;i++){
				filteredReminders.push($scope.remindersInDB[i]);
				
			}
			$scope.reminders = filteredReminders;
			  return;
		  }
		
		
		  var searchArray = theCtrl.searchInput.toLowerCase().split(" ");//split the input by space
		  
		  filteredReminders = 
				  	  _.filter($scope.remindersInDB, function(o) {//Search in all reminders in DB
				  		        var matchingProduct = true;
				  		      _.forEach(searchArray, function(search){ // look for all words in search string in any order and  case insensitive
				  		    	if ( (o.reminderSubject.toLowerCase().indexOf(search) < 0 ) && (o.reminderText.toLowerCase().indexOf(search) < 0 )){
				  		    		matchingProduct = false;
				  				}
				  		      });
						  		
				  		      if (matchingProduct) return o;
						  });
			 
		 
		  filteredReminders = _.without(filteredReminders, undefined);
		  filteredReminders = _.without(filteredReminders, null);
		  
		$scope.reminders = filteredReminders;
	}
	$scope.filter = function(frequencyType, frequencyWithDate){
		$scope.reminders =[];
		var filteredReminders = [];
		for (var i=0;i<$scope.remindersInDB.length;i++){
			if (frequencyType == null || ($scope.remindersInDB[i].frequencyType == frequencyType &&  $scope.remindersInDB[i].frequencyWithDate == frequencyWithDate)){
				filteredReminders.push($scope.remindersInDB[i]);
			}
		}
		$scope.reminders = filteredReminders;
	}
	
	
	$scope.getReminders = function(){
		
		if ($scope.gettingUserReminderList){
			return ;
		}
		$scope.gettingUserReminderList = false;
		
		$scope.showBusy();
		
		 $http.get(appData.getHost()+'/ws/reminder')
	  		.then(function(response){
	  			 $scope.hideBusy();
	  			$scope.formatReminderDisplay(response.data) ;
	  			
	  			
	  		},
			function(response){
	  			 $scope.hideBusy();
	  			$scope.popUp('Sorry ', 'Could not fectch data. Do you want to retry now?','menu.login' );
			});
	}
	
	if (!$rootScope.refresh){
		$rootScope.refresh = $rootScope.$on('getMyRemindersList',function(event){
			$scope.getReminders();
			
			});
	}
	
	$scope.formatReminderDisplay = function(dbResponse){
		var formattedReminders = [];
		if (dbResponse){
				for (var i=0;i<dbResponse.length;i++){
					var date =  new Date(dbResponse[i].nextExecutionTime);
					var today = new Date();
					var tomorrow = new Date();
					tomorrow.setDate(today.getDate()+1);
					var dayAfterTomorrow = new Date();
					dayAfterTomorrow.setDate(today.getDate()+2);
					dbResponse[i].nextExecutionDisplayTime = ": "+ days[ date.getDay() ] +" "+date.toString("dd-MMM-yyyy HH:mm");
					if (date.toString("dd-MMM-yyyy") == today.toString("dd-MMM-yyyy")) {
						dbResponse[i].nextExecutionDisplayTime = ": Today at "+date.toString("HH:mm");
					}else if (date.toString("dd-MMM-yyyy") == tomorrow.toString("dd-MMM-yyyy"))  {
						dbResponse[i].nextExecutionDisplayTime = ": Tomorrow at "+date.toString("HH:mm");
					}else if (date.toString("dd-MMM-yyyy") == dayAfterTomorrow.toString("dd-MMM-yyyy"))  {
						dbResponse[i].nextExecutionDisplayTime = ": Day after Tomorrow at "+date.toString("HH:mm");
					}
					
					/*if (dbResponse[i].frequencyWithDate == 'Once'){
						dbResponse[i].displayTime  = dbResponse[i].nextExecutionDisplayTime 
					}*/
					formattedReminders.push(dbResponse[i]);
					
				}
				$scope.reminders =[];
				$scope.remindersInDB = [];
		  			$scope.reminders = formattedReminders;
		  			$scope.remindersInDB = formattedReminders;
			 }
	}
	$scope.deleteReminder = function(deleteIndex){
		$scope.deleteIndex  = deleteIndex;
		 var confirmPopup = $ionicPopup.confirm({
		     title: ''+$scope.reminders[$scope.deleteIndex].reminderSubject+" "+$scope.reminders[$scope.deleteIndex].reminderText,
		     template: 'Do you want to delete this reminder. Please note that delte a reminder means that all future ocurances will also be cancled.'
		   });

		   confirmPopup.then(function(res) {
			   if (res){
				   
				   $scope.showBusy();
					
					 $http.delete(appData.getHost()+'/ws/reminder/reminderID/'+$scope.reminders[$scope.deleteIndex]._id)
				  		.then(function(response){
				  			 $scope.hideBusy();
				  			$scope.formatReminderDisplay(response.data) ;
				  		},
						function(response){
				  			 $scope.hideBusy();
							
						});
				   
				   ///////////////////
				   for (var i=0; i <$scope.reminders.length;i++){
					   if (i==$scope.deleteIndex){
						 //splice is safe here as at a time only one item removed in whole iteration
						   $scope.reminders.splice(i,1);
						   //dataRestore.saveInCache('savedAddress', $scope.myData.savedAddress);
					   }
				   }
				  ////////////////////
			   }
		     
		   });
	}
	
	$scope.popUp = function(subject, body, nextStep){
		var confirmPopup = $ionicPopup.confirm({
		     title: subject,
		     template: body
		   });
		 confirmPopup.then(function(res) {
			 if (res && nextStep){
				 $state.transitionTo(nextStep);
			 }
		  });
	}
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
		  
	 
}]);APP.CONTROLLERS.controller ('CTRL_TODO',['$scope','$state','$rootScope','$ionicLoading','$http','$ionicPopup','appData',
    function($scope,$state,$rootScope,$ionicLoading,$http,$ionicPopup, appData){
	
	
	var theCtrl = this;
	theCtrl.newTodo = "";
	window.localStorage.setItem('postlogin-moveto','menu.tab.todo');
	var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
	$scope.todos = [];
	$scope.completedTodos = [];
	theCtrl.logOut = function(){
		$scope.$emit('logOut');
	}
	
	$scope.checkEnter = function(){
		if(event.keyCode == 13){
			$scope.addNewTodo();
		}
	}
	$scope.addNewTodo  = function(){
		if (!theCtrl.newTodo) return;
		var toDo = {};
		toDo.order = $scope.todos.length +1;
		toDo.taskDesc = theCtrl.newTodo;
		$scope.showBusy();
		$http.post(appData.getHost()+'/ws/todo/',toDo , config)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				
  				$scope.getToDos();
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				$scope.popUp('Failure', 'Please login back and then retry.',null );
  			 }else {
  				$scope.popUp('Failure', 'Please retry.',null );
  			 }
  			
  			
			
		});
	}
	$scope.getToDos = function(){
		
		$scope.showBusy();
		
		 $http.get(appData.getHost()+'/ws/todo')
	  		.then(function(response){
	  			 $scope.hideBusy();
	  			//$scope.todos = response.data ;
	  			$scope.showToDos(response.data);
	  			theCtrl.newTodo = "";
	  			
	  		},
			function(response){
	  			 $scope.hideBusy();
	  			$scope.popUp('Sorry ', 'Could not fectch data. Do you want to retry now?','menu.login' );
			});
		}
	$scope.showToDos = function(data){
		$scope.todos = []
		$scope.completedTodos = [];
		var todos = [];
		var completedTodos = [];
		for (var i=0;i<data.length;i++){
			if (data[i].complete){
				completedTodos.push(data[i]);
			}else {
				todos.push(data[i]);
			}
		}
		$scope.todos = todos;
		completedTodos.sort($scope.compareToDos)
		$scope.completedTodos = completedTodos;
	}
	
	$scope.toggleComplete = function(todo){
		 $scope.showBusy();
		 $http.delete(appData.getHost()+'/ws/todo/id/'+todo._id)
	  		.then(function(response){
	  			 $scope.hideBusy();
	  			$scope.showToDos(response.data);
	  		},
			function(response){
	  			 $scope.hideBusy();
				
			});
	}
	
	$scope.compareToDos = function(a, b) {
		  return  b.dateCompleted - a.dateCompleted;
	}
	$scope.compareToDosPending = function(a, b) {
		  return   a.order - b.order;
	}
	$scope.updateToDoOrderInDB = function(toDo){
		$http.post(appData.getHost()+'/ws/todo/update',toDo , config)
  		.then(function(response){
  			
  		},
		function(response){
  			});
	}
	$scope.moveDown = function(index){
		
		if (index < ($scope.todos.length -1)){
			var todos = $scope.todos;
			var order = todos[index].order;
			todos[index].order = todos[index+1].order;
			todos[index+1].order = order;
			$scope.todos = [];
			todos.sort($scope.compareToDosPending);
			$scope.todos = todos;
			$scope.updateToDoOrderInDB(todos[index]);
			$scope.updateToDoOrderInDB(todos[index+1]);
		}
		
	}
	$scope.moveUp = function(index){
		if (index >0){
			var todos = $scope.todos;
			var order = todos[index].order;
			todos[index].order = todos[index-1].order;
			todos[index-1].order = order;
			$scope.todos = [];
			todos.sort($scope.compareToDosPending);
			$scope.todos = todos;
			$scope.updateToDoOrderInDB(todos[index]);
			$scope.updateToDoOrderInDB(todos[index-1]);
		}
	}
	$scope.markeComplete = function(index){
		$scope.toggleComplete($scope.todos[index]);
	}
	$scope.markePending = function(index){
		$scope.toggleComplete($scope.completedTodos[index]);
	}
		
		
		$scope.popUp = function(subject, body, nextStep){
			var confirmPopup = $ionicPopup.confirm({
			     title: subject,
			     template: body
			   });
			 confirmPopup.then(function(res) {
				 if (res && nextStep){
					 $state.transitionTo(nextStep);
				 }
			  });
		}
		
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
		  
		  
		 
		  
	 
}]);APP.CONTROLLERS.controller ('CTRL_help',['$scope','$ionicLoading','$http','$ionicPopup','$state',
    function($scope,$ionicLoading,$http,$ionicPopup, $state){
	var theCtrl = this;
	
}]);APP.CONTROLLERS.controller ('CTRL_feedback',['$scope','$ionicLoading','$http','$ionicPopup','$state','appData',
    function($scope,$ionicLoading,$http,$ionicPopup, $state,appData){
	var theCtrl = this;
	$scope.feedback = {};
	var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
	$scope.sendFeedback = function(){
		$scope.showBusy();
		$http.post(appData.getHost()+'/ws/feedback/',$scope.feedback.text , config)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response){
  				$scope.popUp('Thanks', 'We have received your feedback and will work on that soon.',null );
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				$scope.popUp('Failure', 'Please login back and then retry.',null );
  			 }else {
  				$scope.popUp('Failure', 'Please retry.',null );
  			 }
  			
  			
			
		});
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
}]);APP.SERVICES.service ('appData',['$window','dataRestore','$ionicPopup',
    function( $window,dataRestore, $ionicPopup){
	this.cartItems = [];
	this.offerItems = [];
	this.getShopID = function () {
		return "1519981368108";
	}
	this.getHost = function () {
		var host = "https://remind-me-on.appspot.com";
		if ($window.location.host == ""){
			host = "phone";
			//host = "https://deliveratmydoor.appspot.com";
		}else if ($window.location.host.indexOf("localhost:8080") >=0 ){
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

]);;APP.SERVICES.service('dataRestore', function($rootScope) {
	
	this.saveInCache = function (key, value) {
		window.localStorage.setItem(key, JSON.stringify(value))
	}
	this.getFromCache = function (key, type) {
		var value = "";
		
		if (type === 'boolean'){
			value = false;
			if (window.localStorage.getItem(key) === 'true'){
				value = true;
			}
		}
		
		if (type === 'number'){
			value = parseInt(window.localStorage.getItem(key))
			if (isNaN(value) ){
				value = 0; 
			}
		}
		
		if (type === 'str' || type == undefined || type == null){
			value = window.localStorage.getItem(key)
			if (value == null || value == 'null'){
				value = "";
			}
			
		}
		if (type === 'obj' ){
			value = window.localStorage.getItem(key)
			if (value){
				return JSON.parse(value)
			}else {
				return null;
			}
			
		}
		return value;
	}
	  
    
});;APP.DIRECTIVE.directive("phoneNumber", [function() {
    return {
        restrict: "A",
        link: function(scope, elem, attrs) {
        	 var reg = new RegExp('[0-9,#()+ ]$');
            angular.element(elem).on("keyup", function(e) {
                if (!reg.test(this.value)) {
                	this.value = this.value.replace(/[^0-9,#()+ ]/g, "");
                }
            });
        }
    }
}]);