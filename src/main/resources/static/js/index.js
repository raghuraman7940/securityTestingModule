$(document).ready(function () {

  
});

$( "#launch_zap" ).click(function() {
	console.log( "Handler for .click() called." );
	    $.get("/api/launchzap");

	});

$( "#trigger_selenium" ).click(function() {
	console.log( "Handler for sel.click() called." );
	    $.get("/api/triggerSileniumDemo");

	});

