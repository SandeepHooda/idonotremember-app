APP.CONTROLLERS.controller ('CTRL_AddCashSuccess',['$scope','$http','$state','appData',
    function($scope,$http, $state, appData){
	var theCtrl = this;
	$scope.recharge = {}
	$scope.recharge.amount = "";
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
	$scope.recharge.amount = window.localStorage.getItem('rechargeAmount');
}])