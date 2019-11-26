(function() {
	
	// Get a regular interval for drawing to the screen
	window.requestAnimFrame = (function (callback) {
		return window.requestAnimationFrame || 
					window.webkitRequestAnimationFrame ||
					window.mozRequestAnimationFrame ||
					window.oRequestAnimationFrame ||
					window.msRequestAnimaitonFrame ||
					function (callback) {
					 	window.setTimeout(callback, 1000/60);
					};
	})();

	// Set up the canvas
	
	var submitBtn = document.getElementById("sig-submitBtn");
	
	submitBtn.addEventListener("click", function (e) {
		sendReview(document.getElementById("myReview").value)
		
	}, false);
	
	var clearBtn = document.getElementById("sig-clearBtn");
	
	clearBtn.addEventListener("click", function (e) {
		document.getElementById("myReview").value = "";
		 document.body.style.backgroundColor = "#FFFFFF";
		
	}, false);
	
	var micBtn = document.getElementById("floatingBtnMic");
	
	micBtn.addEventListener("click", function (e) {
		listenToVoice()
		
	}, false);

	function readOutLoud (message) {
		  var speech = new SpeechSynthesisUtterance();

		  // Set the text and voice attributes.
		  if (message >0){
			speech.text = "I am glad you liked it!";
			document.body.style.backgroundColor = "#98FB98";
		  }else {
			  speech.text = "Sorry to hear that you didn't like it.";
			 document.body.style.backgroundColor = "#FF6347";
		  }
		  
		  speech.volume = 1;
		  speech.rate = 1;
		  speech.pitch = 1;

		  window.speechSynthesis.speak(speech);
	}
	
	
	function sendReview(text){
		
		var xhttp =  new XMLHttpRequest();;
		if (window.XMLHttpRequest) {
		    // code for modern browsers
			xhttp = new XMLHttpRequest();
		 } else {
		    // code for old IE browsers
			 xhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		  xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
			 //document.getElementById("response").innerHTML = this.responseText;
			
			 readOutLoud(this.responseText)
			}
		  };
		xhttp.open("GET", "http://sanhoo.duckdns.org:5000/raspberry/text2speach/"+encodeURI(text), true);
		xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xhttp.send();  
	}
	
	var recognition = null;
	function listenToVoice() {
		
		recognition.start();
	}
	function voiceInit() {
		try {
			  var SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
			  recognition = new SpeechRecognition();
			  recognition.continuous = true;
			  recognition.onstart = function() { 
				  //alert('Voice recognition activated. Try speaking into the microphone.');
				}

			  recognition.onspeechend = function() {
				  //alert('You were quiet for a while so voice recognition turned itself off.');
				}

			  recognition.onerror = function(event) {
				  /*if(event.error == 'no-speech') {
				    alert('No speech was detected. Try again.');  
				  };*/
				}
			  recognition.onresult = function(event) {

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
					if (document.getElementById("myReview").value.length) {
						document.getElementById("myReview").value += ' ';
					  }
					  document.getElementById("myReview").value += transcript;
					  sendReview(document.getElementById("myReview").value)
					  
				  }

				  
				  
				}
			}
			catch(e) {
			  
			}
	}
voiceInit()
})();