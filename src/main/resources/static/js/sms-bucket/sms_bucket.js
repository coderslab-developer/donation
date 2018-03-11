$(document).ready(function(){	
	$('#generateSMSKey').on('click', function(e){
		e.preventDefault();
		//console.log($(this).attr('href'));
		$.get($(this).attr('href'), {}, function(response){
			if(response.status == "success"){
				console.log(response.smsKey);
				$('#smsKey').val(response.smsKey);
				$('#smsKey').attr('type', 'text');
				$('#generateSMSKey').css('display', 'none');
				$('#sellButton').removeClass('hidden');
			}
		});
	});
}); 