$(document).ready(function(){
	//make textbox non cut copy and past
	$('#smsKey').bind("cut copy paste", function(e) {
		e.preventDefault();
	});

	$('.numeric-input').on('keydown', function(e){-1!==$.inArray(e.keyCode,[46,8,9,27,13,110,190])||(/65|67|86|88/.test(e.keyCode)&&(e.ctrlKey===true||e.metaKey===true))&&(!0===e.ctrlKey||!0===e.metaKey)||35<=e.keyCode&&40>=e.keyCode||(e.shiftKey||48>e.keyCode||57<e.keyCode)&&(96>e.keyCode||105<e.keyCode)&&e.preventDefault()});

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