APP.CONTROLLERS.controller ('CTRL_AddCashSuccess',['$scope','$http','$state','appData',
    function($scope,$http, $state, appData){
	var theCtrl = this;
	$scope.recharge = {}
	$scope.recharge.amount = "";
	$scope.recharge.orderID = "";
	$scope.paymentGateway = "Payment gateway"
	$scope.goToHome = function(){
	
		$state.transitionTo('menu.tab.home');
	}
	$scope.recharge.amount = window.localStorage.getItem('rechargeAmount');
	
	$http.get(appData.getHost()+'/RecentOrders')
		.then(function(response){
			if (response.data != "null"){
				$scope.recharge.orderID = response.data ;
			}
			
		},
	function(response){
			$scope.recharge.orderID = "";
			
	});
	
}])