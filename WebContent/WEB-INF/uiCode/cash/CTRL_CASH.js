APP.CONTROLLERS.controller ('CTRL_CASH',['$scope','$ionicLoading','$http','$ionicPopup','$state',
    function($scope,$ionicLoading,$http,$ionicPopup, $state){
	var theCtrl = this;
	$scope.recharge = {}
	$scope.recharge.amount = 100;
	$scope.paymentGateway = "Payment gateway"
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
		$scope.popUp(' Work in progress ', 'Pening Integration',null);
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