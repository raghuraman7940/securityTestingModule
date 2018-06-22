$(document).ready(function () {

  
});

$( "#launch_zap" ).click(function() {
	console.log( "Handler for .click() called." );
		$.get("/api/launchzap", function(data, status){
			alert("Data: " + data + "\nStatus: " + status);
		});

	});

$( "#trigger_selenium" ).click(function() {
	console.log( "Handler for sel.click() called." );
	    $.get("/api/triggerSileniumDemo");

	});

	$( "#GetZapConfig" ).click(function() {
		console.log( "Handler for sel.click() called." );
			$.getJSON("/api/zapconfig", function(data, status){
				alert("Data: " + data + "\nStatus: " + status);
			});
	
		});

		$( "#SaveZapConfig" ).click(function(hotname,port,apikey) {
			console.log( "Handler for sel.click() called." );
				$.post("/api/zapconfig",
				{
					zaphostname: "localhost",
					zapport: "8080",
					zapapikey:"fn4ism7pac59tdfac434mvmpao"
				},
				function(data, status){
					alert("1Data: " + data + "\nStatus: " + status);
				});
		
			});

