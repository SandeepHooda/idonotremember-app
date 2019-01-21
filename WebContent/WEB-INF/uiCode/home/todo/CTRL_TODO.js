APP.CONTROLLERS.controller ('CTRL_TODO',['$scope','$state','$rootScope','$ionicLoading','$http','$ionicPopup','appData',
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
	$scope.loginTry = 0;
	$scope.completedTodos = [];
	theCtrl.logOut = function(){
		$scope.$emit('logOut');
	}
	$scope.listenToVoice = function(){
		if (theCtrl.newTodo.length) {
			theCtrl.newTodo += ' ';
		  }
		$scope.recognition.start();
	}
	$scope.voiceInit = function(){
		try {
			  var SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
			  $scope.recognition = new SpeechRecognition();
			  $scope.recognition.continuous = true;
			  $scope.recognition.onstart = function() { 
				  //alert('Voice recognition activated. Try speaking into the microphone.');
				}

			  $scope.recognition.onspeechend = function() {
				  //alert('You were quiet for a while so voice recognition turned itself off.');
				}

			  $scope.recognition.onerror = function(event) {
				  /*if(event.error == 'no-speech') {
				    alert('No speech was detected. Try again.');  
				  };*/
				}
			  $scope.recognition.onresult = function(event) {

				  // event is a SpeechRecognitionEvent object.
				  // It holds all the lines we have captured so far. 
				  // We only need the current one.
				  var current = event.resultIndex;

				  // Get a transcript of what was said.
				  var transcript = event.results[current][0].transcript;

				  // Add the current transcript to the contents of our Note.
				  // There is a weird bug on mobile, where everything is repeated twice.
				  // There is no official solution so far so we have to handle an edge case.
				  var mobileRepeatBug = (current == 1 && transcript == event.results[0][0].transcript);

				  if(!mobileRepeatBug) {
				    //noteContent += transcript;
					// Add the current transcript to the contents of our Note.
					  theCtrl.newTodo += transcript;
					  //$scope.readOutLoud(theCtrl.newTodo);
					  $scope.addNewTodo();
				  }

				  
				  
				}
			}
			catch(e) {
			  
			}
	}
	 $scope.readToDo = function(){
		 var message = "";
		 for (var i=0;i< $scope.todos.length;i++){
			 message += $scope.todos[i].taskDesc+". ";
		 }
		 $scope.readOutLoud(message);
	 }
	
	 $scope.readOutLoud = function(message) {
		  var speech = new SpeechSynthesisUtterance();

		  // Set the text and voice attributes.
		  speech.text = message;
		  speech.volume = 1;
		  speech.rate = 1;
		  speech.pitch = 1;

		  window.speechSynthesis.speak(speech);
		}
	$scope.voiceInit();
	
	$scope.checkEnter = function(){
		if(event.keyCode == 13){
			$scope.addNewTodo();
		}
	}
	$scope.login = function (){
		var regID = window.localStorage.getItem('regID');
		if (document.URL.indexOf('localhost')>=0){
			regID = "69905a13-79b9-4314-95fa-17a87a6121b0";
			 window.localStorage.setItem('regID', regID);
		}
		
		 $http.get(appData.getHost()+'/ws/login/validate/'+regID+'/timeZone/'+Intl.DateTimeFormat().resolvedOptions().timeZone.replace("/", "@"))
	  		.then(function(response){
	  			
	  		});
	}
	$scope.LoginAndRetry  = function(){
		$scope.login();
		$scope.addNewTodo();
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
  				$scope.loginTry = 0;
  				$scope.getToDos();
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				 if ($scope.loginTry == 0){
  					$scope.loginTry++;
  					$scope.LoginAndRetry();
  				 }else {
  					$scope.popUp('Failure', 'Please login back and then retry.',null );
  				 }
  				
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
	$scope.noDataFound = false;
	$scope.showToDos = function(data){
		$scope.noDataFound = false;
		$scope.todos = []
		$scope.completedTodos = [];
		var todos = [];
		var completedTodos = [];
		if (data.length == 0){
			$scope.noDataFound = true;
		}
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
		  
		  
		 
		  
	 
}])