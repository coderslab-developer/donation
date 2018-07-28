//$(document).ready(function(){
//	var formName = "smsAccountform";
//	$('#' + formName).on('submit', function(e){
//		e.preventDefault();
//		var formData = $(this).serializeArray();
//		var submitUrl = $('#' + formName).attr('action');
//		console.log(formData);
//		console.log(submitUrl);
//		
//		$.post(submitUrl, formData, function(data, status){
//			console.log(data);
//		});
//	})
//});