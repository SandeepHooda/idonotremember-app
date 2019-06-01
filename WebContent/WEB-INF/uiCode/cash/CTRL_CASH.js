APP.CONTROLLERS.controller ('CTRL_CASH',['$window','$scope','$ionicLoading','$http','$ionicPopup','$state','appData',
    function($window,$scope,$ionicLoading,$http,$ionicPopup, $state,appData ){
	var theCtrl = this;
	$scope.recharge = {}
	$scope.recharge.amount = "";
	$scope.paymentGateway = "Payment gateway"
		$scope.androidApp= false;	
		
	
		var regID = window.localStorage.getItem('regID');
		if (!regID){
			regID = $location.search()['regID']
			window.localStorage.setItem('regID',regID );
		}
		if ($window.location.host == "") { //android app
			//$scope.popUp(' Patment in progress ', 'Please complete your payment via the secure payment gateway and come back to the app',null);
			
			$scope.androidApp= true;
			window.open("https://idonotremember-app.appspot.com/ui/index.html#/menu/addcash?regID="+regID, "_system");
			 //$state.transitionTo('menu.tab.home');
			
		}
	$scope.goToHome = function(){
		var moveTo = "";
		if (window.localStorage.getItem('postlogin-moveto')){
			moveTo = window.localStorage.getItem('postlogin-moveto');
			
		}else {
			moveTo = 'menu.tab.home';
		}
		
		$scope.popUp(' Cancel ', 'Are you sure you want to cancel?',moveTo);
	}
	$scope.goToPaytm = function(){
		if (isNaN($scope.recharge.amount) || $scope.recharge.amount < 1){
			
		}else {
			if ($scope.recharge.amount.indexOf(".") >=0){
				$scope.popUp(' Error ', 'Please don not enter paisa.',null);
			}else {
				window.open("/AddCash?amount="+$scope.recharge.amount, "_self");
			}
			
		}
		
		
	}
	$scope.recharge.callValue = 0;
	$scope.recharge.smsValue =0;
	$scope.validateAmount = function(){
		if (isNaN($scope.recharge.amount)){
			$scope.recharge.amount = "";
		}else {
			$scope.recharge.callValue = Math.floor($scope.recharge.amount / 7);
			$scope.recharge.smsValue = Math.floor($scope.recharge.amount / 2);
			window.localStorage.setItem('rechargeAmount', $scope.recharge.amount);
		}
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
}])