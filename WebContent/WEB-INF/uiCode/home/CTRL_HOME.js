APP.CONTROLLERS.controller ('CTRL_HOME',['$window','$scope','$state','$rootScope','$ionicLoading','$http','$ionicPopup','appData','$timeout',
    function( $window,$scope,$state,$rootScope,$ionicLoading,$http,$ionicPopup, appData,$timeout){
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
	
	

	
	var regIDStorege = window.localStorage.getItem('regID');
	 var config = {
	            headers : {
	                'Content-Type': 'application/json;',
	                'Auth' : ''+regIDStorege
	            }
	        }
	 
	 dataLayer.push({'pageTitle': 'Home'});    // Better
	 $scope.retryCount = 0;
	var theCtrl = this;
	theCtrl.searchInput = "";
	$scope.reminders =[];
	$scope.remindersInDB =[];
	
	theCtrl.voiceReminder = {};
	 theCtrl.voiceReminder.currentQuestion = 1;//1 - reminder text, 2- Date , 3 time, 4 Frequency
	 theCtrl.voiceReminder.questions = ["Can you please say that again?","What do you want to be reminded of ?", "On what date do you need this reminder ?", "On what time do you need this reminder ?","How frequent do you need this. You can say things like , once , weekly, monthly or yearly. "]
	 theCtrl.voiceReminder.questionsTextOnly = [];
	 theCtrl.voiceReminder.answers = [""];
	 for (let i=0;i<theCtrl.voiceReminder.questions.length;i++){
		 theCtrl.voiceReminder.questionsTextOnly.push(theCtrl.voiceReminder.questions[i].replace(/[^0-9a-zA-Z ]/g, ''));
		}
	var days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
 var	month = ["January","February","March", "April", "May", "June","July","August", "September",
 "October","November", "December"];
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
	
	$scope.herokuInit = function(){
		//Start heroku 
		$http.get(appData.getHost()+'/DateExtracter' , config)
  		.then(function(response){
  		},
		function(response){
				});
	}

	$scope.getVerifiedPhones = function(){
		$http.get(appData.getHost()+'/ws/phone/verified/true', config)
  		.then(function(response){
  			 if (response.data){
  				$scope.verifiedPhones = response.data;
  				if($scope.verifiedPhones.length > 0){
  					theCtrl.selectedPhone = $scope.verifiedPhones[0];
					}
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){

		});
	}



	$scope.micOn = function(){
		$scope.recognition.start();
		$scope.speakNowPopUp();
	}

	$scope.speachPermissionGranted = function(){
		var settings = {
			lang: "en-US",
			showPopup: true
	};

	window.plugins.speechRecognition.startListening(function(result){
		$scope.recordAnswer(result[0]);  
		
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
	
	$scope.readOutLoudQuestion = function() {
		if (theCtrl.voiceReminder.currentQuestion > 4){
			theCtrl.voiceReminder.answers = [""];
			theCtrl.voiceReminder.currentQuestion = 1;
		}
		let message = theCtrl.voiceReminder.questions[theCtrl.voiceReminder.currentQuestion];
		if ($window.location.host == ""){//android 
			let timeout = 2200;
			if (theCtrl.voiceReminder.currentQuestion == 4){
				timeout = 5000;
			}
			$timeout(function(){
			
				$scope.listenToVoiceCordova();
			}, timeout);
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
				speech.onend = function (event) {
					$scope.micOn();
				};
				window.speechSynthesis.speak(speech);
			}
			
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
		
		 $http.get(appData.getHost()+'/ws/reminder',config )
	  		.then(function(response){
	  			 $scope.hideBusy();
	  			$scope.formatReminderDisplay(response.data) ;
	  			
	  			
	  		},
			function(response){
	  			if (response.status == 401){
	  				$scope.popUp('Please Log in ', 'Seams like you have been logged out. Please login back.',null  );
	  				$state.transitionTo('menu.login');
	  				
	  			}else {
	  				 $scope.hideBusy();
	 	  			$scope.retryCount ++;
	 	  			if ($scope.retryCount <=10){
	 	  				$timeout($scope.getReminders(), 500);
	 	  				
	 	  			}else {
	 	  				$scope.popUp('Sorry '+$scope.retryCount, 'Could not fectch data. Please relaunch the app. ',null  );
	 	  			}
	  			}
	  			
	  			
			});
	}
	
	if (!$rootScope.refresh){
		$rootScope.refresh = $rootScope.$on('getMyRemindersList',function(event){
			$scope.getReminders();
			
			});
	}
	$scope.noDataFound = false;
	$scope.formatReminderDisplay = function(dbResponse){
		$scope.noDataFound = false;
		var formattedReminders = [];
		if (dbResponse){
			if (dbResponse.length == 0){
				$scope.noDataFound = true;
			}
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
		let reminderText = $scope.reminders[$scope.deleteIndex].reminderText;
		if (!reminderText || reminderText == "null"){
			reminderText = ""
		}
		 var confirmPopup = $ionicPopup.confirm({
		     title: ''+$scope.reminders[$scope.deleteIndex].reminderSubject+" "+reminderText,
		     template: 'Do you want to delete this reminder. Please note that delte a reminder means that all future ocurances will also be cancled.'
		   });

		   confirmPopup.then(function(res) {
			   if (res){
				   
				   $scope.showBusy();
					
					 $http.delete(appData.getHost()+'/ws/reminder/reminderID/'+$scope.reminders[$scope.deleteIndex]._id, config)
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
	
	$scope.voiceInit = function(){
		try {
			  var SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
			 // var grammar = '#JSGF V1.0; grammar colors; public <frequency> = once | weekly | monthly | yearly ;'
			 // var speechRecognitionList = new SpeechGrammarList();
			  //speechRecognitionList.addFromString(grammar, 1);

			  $scope.recognition = new SpeechRecognition();
			  //$scope.recognition.grammars  = speechRecognitionList;
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
					  $scope.recordAnswer( transcript);
					  //$scope.readOutLoud(theCtrl.newTodo);
					  
				  }

				  
				  
				}
			}
			catch(e) {
			  
			}
	}
	$scope.recordAnswer = function(answer){
		console.log(answer);
		answer = answer.replace(/[^0-9a-zA-Z :]/g, '');
		if (!answer || !answer.trim()){
			return;
		}
		answer = answer.trim()
		let question = theCtrl.voiceReminder.questionsTextOnly[theCtrl.voiceReminder.currentQuestion];
		let answerWords = answer.split(" ");
		let countOfWords = 0;
		for (let i=0;i<answerWords.length;i++){
			if (question.indexOf(answerWords[i]) >=0){
				countOfWords++;
			}
			if (countOfWords >= 5){
				return;
			}
		}
		if ($window.location.host != ""){
			$scope.recognition.stop();
		}
		let requestWaitingFromHeroku = false;
		
		if (answer){
			if (theCtrl.voiceReminder.currentQuestion == 2){
				let dateTime = $scope.getDateAndTime(answer);
				if (dateTime.date){
					theCtrl.voiceReminder.answers.push(dateTime.date);
					theCtrl.voiceReminder.currentQuestion++;
					if (dateTime.time){
						theCtrl.voiceReminder.answers.push(dateTime.time);
						theCtrl.voiceReminder.currentQuestion++;
					}
				}else {
					//get date from heroku 
					requestWaitingFromHeroku = true;
					$scope.getDateAndTimeHeroku(answer)
				}
				
			}else if(theCtrl.voiceReminder.currentQuestion == 3) {
				let dateTime = $scope.getDateAndTime("today "+answer);
				if (dateTime.time){
					theCtrl.voiceReminder.answers.push(dateTime.time);
					theCtrl.voiceReminder.currentQuestion++;
				}
			}else if(theCtrl.voiceReminder.currentQuestion == 4) {
				if ("early" ==answer ){
					answer = "yearly";
				}else if ("bitly" ==answer ){
					answer = "weekly";
				}
				if ( answer != 'weekly' && answer != 'monthly' && answer != 'yearly'){
					answer = 'once';
				}
				theCtrl.voiceReminder.answers.push(answer);
				theCtrl.voiceReminder.currentQuestion++;
				$scope.addReminder();
			}else {
				theCtrl.voiceReminder.answers.push(answer);
				theCtrl.voiceReminder.currentQuestion++;
			}
			
		}
		if (theCtrl.voiceReminder.currentQuestion <=4 && !requestWaitingFromHeroku){
			$scope.readOutLoudQuestion();
		}
		
		
	}
	$scope.voiceInit();
	$scope.getDateAndTimeHeroku = function(answer){

		$http.get(appData.getHost()+'/DateExtracter?text='+answer , config)
  		.then(function(response){
				dateTimeHeroku = response.data
				if (dateTimeHeroku && dateTimeHeroku.length >= 16){
					let dateTime = {};
					dateTime.date = dateTimeHeroku.substring(0,10);
					dateTime.date = dateTime.date.replace(/-/g,"_");
					if (dateTimeHeroku.substring(11,13) != "00"){
						dateTime.time = dateTimeHeroku.substring(11,16);
						dateTime.time = dateTime.time.replace(/:/g,"_");
					}
					
					if (dateTime.date){
						theCtrl.voiceReminder.answers.push(dateTime.date);
						theCtrl.voiceReminder.currentQuestion++;
						if (dateTime.time){
							theCtrl.voiceReminder.answers.push(dateTime.time);
							theCtrl.voiceReminder.currentQuestion++;
						}
					}
				}
				if (theCtrl.voiceReminder.currentQuestion <=4){
					$scope.readOutLoudQuestion();
				}
  		},
		function(response){
			if (theCtrl.voiceReminder.currentQuestion <=4){
				$scope.readOutLoudQuestion();
			}
				});
		
	}
		
	$scope.getDateAndTime = function(answer){
		console.log(" date time answer "+answer);
		let dateTime = {};
		
		if (answer){
			answer = answer.trim().toLowerCase().replace(/[^0-9a-zA-Z :]/g, '');
		}else {
			answer = ""
		}
		
		dateTime.time = answer.match(/\d{1,2}:{0,1}\d{0,2} (am|pm)/g);
		if (dateTime.time && dateTime.time[0]){
			console.log("time extracted "+dateTime.time);
			let time = dateTime.time[0];
			
			answer = answer.substring(0,answer.indexOf(time)).trim();
			
			let timeDigit = time.match(/\d{1,2}/g);
			if (timeDigit && timeDigit[0]){
				let timeHourDigit = timeDigit[0];
				let timeMinuteDigit = timeDigit[1];
				timeHourDigit = parseInt(timeHourDigit) 
				if (time.indexOf("pm") >=0 ){
					timeHourDigit += 12;
				}
				dateTime.time = $scope.formatNum(timeHourDigit);
				if (!timeMinuteDigit){
					timeMinuteDigit = 0;
				}
				dateTime.time += "_"+$scope.formatNum(timeMinuteDigit)
			}
			
		}
		 
		
		let d = new Date();
		needPython = false;
		if (answer == "today") {
			
		}else if (answer == "tomorrow") {
			d.setDate(d.getDate() +1);
		}else if (answer == "day after tomorrow") {
			d.setDate(d.getDate() +2);
		}else if (answer == "monday") {
			d.setDate(d.getDate() + (1 + 7 - d.getDay()) % 7);
		}else if (answer == "tuesday") {
			d.setDate(d.getDate() + (2 + 7 - d.getDay()) % 7);
		}else if (answer == "wednesday") {
			d.setDate(d.getDate() + (3 + 7 - d.getDay()) % 7);
		}else if (answer == "thrusday") {
			d.setDate(d.getDate() + (4 + 7 - d.getDay()) % 7);
		}else if (answer == "friday") {
			d.setDate(d.getDate() + (5 + 7 - d.getDay()) % 7);
		}else if (answer == "saturday") {
			d.setDate(d.getDate() + (6 + 7 - d.getDay()) % 7);
		}else if (answer == "sunday") {
			d.setDate(d.getDate() + (0 + 7 - d.getDay()) % 7);
		}else {
			needPython = true;
		}
		if (!needPython){
			dateTime.date = $scope.formatDate(d);
			return dateTime;
		}
		return "";
	}
	
	$scope.formatNum = function(num){
		if (num <=9){
		return "0"+num;
		}
		return ""+num;
		
	}
		$scope.formatDate = function(date) {
			let month = $scope.formatNum(date .getMonth() + 1);
		    let day = $scope.formatNum(date .getDate());
		    let year = $scope.formatNum(date .getFullYear());
		    return(  year+"_"+month+"_"+day);
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
	$scope.addReminder = function(){
		let reminderObj = {};
		reminderObj.regID = window.localStorage.getItem('regID');
		reminderObj.reminderSubject = theCtrl.voiceReminder.answers[1] ;
		reminderObj.date =theCtrl.voiceReminder.answers[2];
		reminderObj.time = theCtrl.voiceReminder.answers[3];
		reminderObj.frequencyType =  "Date";
		reminderObj.frequencyWithDate = theCtrl.voiceReminder.answers[4];
		reminderObj.selectedPhone = theCtrl.selectedPhone;
		if (reminderObj.selectedPhone){
			reminderObj.makeACall = true;
		}

		let date = reminderObj.date;
		date = date.replace(/_/g,"-");
		var dt = new Date(date);
		
		if ("weekly" == reminderObj.frequencyWithDate){
			reminderObj.frequencyType =  "Day";
			reminderObj.dayRepeatFrequency ="Every "+days[dt.getDay()];
			reminderObj.displayTime = reminderObj.dayRepeatFrequency +" of every month @ "+(reminderObj.time.replace(/_/g,":"))
		}else if ("monthly" == reminderObj.frequencyWithDate) {
			reminderObj.displayTime = "Every month  "+dt.getDate()+" @ "+(reminderObj.time.replace(/_/g,":"))
		}else if ("yearly" == reminderObj.frequencyWithDate) {
			reminderObj.displayTime = "Every year  "+month[dt.getMonth()] +" "+dt.getDate()+" @ "+(reminderObj.time.replace(/_/g,":"))
		}

		
		reminderObj._id = new Date().getTime() +Math.random();
		$scope.httpAdd(reminderObj);
	}

	$scope.httpAdd = function(reminderObj){
		$scope.showBusy();
		$http.post(appData.getHost()+'/ws/reminder/',reminderObj , config)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
					$scope.getReminders();
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
	 $scope.speakNowPopUp = function() {
		  $scope.gettingUserReminderList = true;
		    $ionicLoading.show({
		      template: 'Please speak Now...',
		      duration: 2000
		    }).then(function(){
		       console.log("Please speak Now");
		    });
		  };
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