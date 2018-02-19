$( document ).ready(function() {
	console.log( "ready!" );
	$("#dealerform").trigger("reset");
	$("#dealerName").focus();
	$("#username").focusout(function(){
		$.post($(this).attr("action"), {'username' : $(this).val()}, function(data){
			if(data.status){
				$("#usernameError").html("Username already taken. Please try another username");
			}else{
				$("#usernameError").html("");
			}
		}).error(function(data){
			Window.alert("error");
		});
	});
	$("#email").focusout(function(){
		$.post($(this).attr("action"), {'email' : $(this).val()}, function(data){
			if(data.status){
				$("#emailError").html("Email already available. Please try another email");
			}else{
				$("#emailError").html("");
			}
		}).error(function(data){
			Window.alert("error");
		});
	});

});
	
