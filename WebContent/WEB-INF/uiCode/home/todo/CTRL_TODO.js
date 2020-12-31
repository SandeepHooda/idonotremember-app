APP.CONTROLLERS.controller ('CTRL_TODO',['$window','$scope','$state','$rootScope','$ionicLoading','$http','$ionicPopup','appData','$interval',
    function($window,$scope,$state,$rootScope,$ionicLoading,$http,$ionicPopup, appData,$interval){
	
			
	var theCtrl = this;
	theCtrl.newTodo = "";
	$scope.MaxOrder = 0;
	$scope.bookMark = false;
	if (window.localStorage.getItem('book-mark-page') === 'todo'){
		$scope.bookMark = true;
	}
	window.localStorage.setItem('postlogin-moveto','menu.tab.todo');
	var regIDStorege = window.localStorage.getItem('regID');
	 var config = {
	            headers : {
	                'Content-Type': 'application/json;',
	                'Auth' : ''+regIDStorege
	            }
	        }
	var name = window.localStorage.getItem('name');
	if (name ){
		$scope.userName = "Welcome "+name;
	}else {
		$scope.userName ="Hello Guest";
	}
	$scope.toDoCache = JSON.parse(window.localStorage.getItem('to-do-cache'));
	
	
	$scope.editMode = false;
	$scope.todos = [];
	$scope.loginTry = 0;
	$scope.completedTodos = [];
	theCtrl.logOut = function(){
		$scope.$emit('logOut');
	}
	$scope.listenToVoice = function(){
	
		if ($window.location.host == ""){ //android
			$scope.listenToVoiceCordova();
		}else {
			$scope.listenToVoiceWeb();
		}
	}

	$scope.listenToVoiceWeb = function(){
		if (theCtrl.newTodo.length) {
			theCtrl.newTodo += ' ';
		  }
		$scope.recognition.start();
	}

	$scope.speachPermissionGranted = function(){
				var settings = {
					lang: "en-US",
					showPopup: true
			};

			window.plugins.speechRecognition.startListening(function(result){
				//console.log(result);
				theCtrl.newTodo = result[0];
				$scope.addNewTodo();
		}, function(err){
				console.log(err);
		}, settings);
	}

	$scope.listenToVoiceCordova = function(){

		window.plugins.speechRecognition.isRecognitionAvailable(function(available){
			if(available){
				window.plugins.speechRecognition.hasPermission(function (isGranted){
					if(isGranted){
						$scope.speachPermissionGranted();
					}else{
							window.plugins.speechRecognition.requestPermission(function (){
								$scope.speachPermissionGranted();
							}, function (err){
									// Opps, nope
							});
					}
				}, function(err){
						console.log(err);
				});


			}
	}, function(err){
			console.error(err);
	});

	}
	$scope.voiceInit = function(){
		try {
			  var SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
			  $scope.recognition = new SpeechRecognition();
			  $scope.recognition.continuous = false;
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
				  $scope.recognition.stop();
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
		 
		if ($window.location.host == ""){//android 
				TTS.speak(message).then(function () {
	           
	        }, function (reason) {
	           
	        });
		}else {
				var speech = new SpeechSynthesisUtterance();

				// Set the text and voice attributes.
				speech.text = message;
				speech.volume = 1;
				speech.rate = 1;
				speech.pitch = 1;
				window.speechSynthesis.speak(speech);
			}
			
	}
	$scope.voiceInit();
	
	$scope.checkEnter = function(){
		if(event.keyCode == 13){
			$scope.addNewTodo();
		}
	}
	$scope.login = function (){
		var regID = window.localStorage.getItem('regID');
		/*if (document.URL.indexOf('localhost')>=0){
			regID = "e18a8b7d-f6a8-433d-853b-97bd7b875bb1";
			 window.localStorage.setItem('regID', regID);
		}*/
		
		 $http.get(appData.getHost()+'/ws/login/validate/'+regID+'/timeZone/'+Intl.DateTimeFormat().resolvedOptions().timeZone.replace("/", "@"), config)
	  		.then(function(response){
	  			
	  		});
	}
	$scope.LoginAndRetry  = function(){
		$scope.login();
		$scope.addNewTodo();
	}
	
	$scope.findMaxOrder = function(){
		var maxID = 0;
	//completedTodos  todos
		if ($scope.todos){
			for (var i=0; i< $scope.todos.length;i++){
				if (maxID <$scope.todos[i].order){
					maxID = $scope.todos[i].order
				}
			}
		}
		if ($scope.completedTodos){
			for (var i=0; i< $scope.completedTodos.length;i++){
				if (maxID <$scope.completedTodos[i].order){
					maxID = $scope.completedTodos[i].order
				}
			}
		}
		return ++maxID;
			
	}
	$scope.addNewTodo  = function(){
		if (!theCtrl.newTodo) return;
		var toDo = {};
		if ($scope.MaxOrder == 0){
			$scope.MaxOrder = $scope.findMaxOrder();
			
		}else {
			$scope.MaxOrder ++;
		}
		toDo.order = $scope.MaxOrder;
		toDo.taskDesc = theCtrl.newTodo;
		$scope.showBusy();
		theCtrl.newTodo = "";
		$scope.hideBusy();
		$http.post(appData.getHost()+'/ws/todo/',toDo , config)
  		.then(function(response){
  			 
  			if (response.data){
  				$scope.loginTry = 0;
  				$scope.getToDos(false);
  			}else {
  				$scope.popUpRefresh('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				 if ($scope.loginTry == 0){
  					$scope.loginTry++;
  					$scope.LoginAndRetry();
  				 }else {
  					$scope.popUpRefresh('Failure', 'Please login back and then retry.',null );
  				 }
  				
  			 }else {
  				$scope.popUpRefresh('Failure', 'Please retry.',null );
  			 }
  			
  			
			
		});
	}
	$scope.addToFav  = function(cmd){
		if (cmd ==='yes'){
			window.localStorage.setItem('book-mark-page','todo');
			$scope.bookMark = true;
		}else {
			window.localStorage.removeItem('book-mark-page');
			$scope.bookMark = false;

		}
		
	}
	$scope.getToDos = function(showBusyPopup){
		
		if (showBusyPopup){
			$scope.showBusy();
		}
		 
		
		if ($scope.toDoCache){
			$scope.showToDos($scope.toDoCache);
		}
		
		 $http.get(appData.getHost()+'/ws/todo', config)
	  		.then(function(response){
	  			 $scope.hideBusy();
	  			//$scope.todos = response.data ;
	  			$scope.showToDos(response.data);
	  			window.localStorage.setItem('to-do-cache',JSON.stringify(response.data));
	  			//theCtrl.newTodo = "";
	  			if ($scope.MaxOrder == 0){
	  				$scope.MaxOrder = $scope.findMaxOrder();
	  				
	  			}
	  			
	  		},
			function(response){
	  			if (response.status == 401){
	  				$scope.hideBusy();
	 	  			$scope.popUp('Sorry ', 'Please close your browser and log back in. ',null  );
	  				//$state.transitionTo('menu.login');
	  			}else {
	  				 $scope.hideBusy();
	 	  			$scope.popUp('Sorry ', 'Could not fectch data. Please relaunch the app. ',null  );
	  			}
	  			
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
		
		/* $scope.showBusy();
		$interval(function() {
			$scope.hideBusy();
		},500);*/
		 $http.delete(appData.getHost()+'/ws/todo/id/'+todo._id, config)
	  		.then(function(response){
	  			 $scope.hideBusy();
	  			window.localStorage.setItem('to-do-cache',JSON.stringify(response.data));
	  			$scope.showToDos(response.data);
	  		},
			function(response){
	  			 $scope.hideBusy();
				
			});
		 
		 
			if ($scope.toDoCache){
				
				for (let i=0; i<$scope.toDoCache.length;i++){
					if ($scope.toDoCache[i]._id == todo._id){
						$scope.toDoCache[i].complete = !$scope.toDoCache[i].complete
						$scope.showToDos($scope.toDoCache);
						break;
					}
				}
				
			}
		
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
	$scope.edit = function(todo){
		//alert(JSON.stringify(todo))
		todo.editMode = true;
	}
	$scope.editOff = function(todo){
		todo.editMode = false;
		$scope.updateToDoOrderInDB(todo);
	}
	
	$scope.checkEnterToDoEdit = function(todo){
		if(event.keyCode == 13){
			$scope.editOff(todo);
		}
	}
	
	$scope.swapToDoOrderInDB = function(toDo1, toDo2){
		$scope.showBusy();
		var todos = []
		todos.push(toDo1)
		todos.push(toDo2)
		$http.post(appData.getHost()+'/ws/todo/swapToDoOrderInDB',todos , config)
  		.then(function(response){
 			 	$scope.hideBusy();
	  			$scope.showToDos(response.data);
	  			theCtrl.newTodo = "";
	  			
	  		},
			function(response){
	  			 $scope.hideBusy();
	  			$scope.popUp('Sorry ','Could not fectch data. Please relaunch the app. ',null );
			});
	}
	$scope.moveDown = function(index){
		
		if (index < ($scope.todos.length -1)){
			var todos = $scope.todos;
			var order = todos[index].order;
			todos[index].order = todos[index+1].order;
			todos[index+1].order = order;
			$scope.todos = [];
			//todos.sort($scope.compareToDosPending);
			$scope.todos = todos;
			//$scope.updateToDoOrderInDB(todos[index]);
			//$scope.updateToDoOrderInDB(todos[index+1]);
			$scope.swapToDoOrderInDB(todos[index],todos[index+1]);
		}
		
	}
	$scope.moveUp = function(index){
		if (index >0){
			var todos = $scope.todos;
			var order = todos[index].order;
			todos[index].order = todos[index-1].order;
			todos[index-1].order = order;
			$scope.todos = [];
			
			$scope.todos = todos;
			//$scope.updateToDoOrderInDB(todos[index]);
			//$scope.updateToDoOrderInDB(todos[index-1]);
			$scope.swapToDoOrderInDB(todos[index],todos[index-1]);
		}
	}
	$scope.markeComplete = function(index){
		let todo = $scope.todos[index];
		if (!todo.complete){
			var confirmPopup = $ionicPopup.confirm({
			     title: 'Do you want to delete ',
			     template: ''+todo.taskDesc
			   });
			 confirmPopup.then(function(res) {
				 if (res){
					 $scope.toggleComplete($scope.todos[index]);
				 }else {
					 return;
				 }
			  });
		}
		
	}
	$scope.markePending = function(index){
		$scope.toggleComplete($scope.completedTodos[index]);
	}
		
	$scope.popUpRefresh = function(subject, body, nextStep){
		var confirmPopup = $ionicPopup.confirm({
		     title: subject,
		     template: body
		   });
		 confirmPopup.then(function(res) {
			 location.reload();
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
		  
		  
		 
		  
	 
}])