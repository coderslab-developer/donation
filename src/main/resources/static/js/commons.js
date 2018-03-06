$(document).ready(function(){
	//email validating
	var emailStatus = "invalid";
	var validateEmail = function(elementValue) {
		var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
		return emailPattern.test(elementValue);
	}
	var removeClass = function(element){
		var dangerDiv = $(element).next('div[class="text-danger"]');
		var successDiv = $(element).next('div[class="text-success"]');
		if(successDiv){
			successDiv.remove();
		}
		if(dangerDiv){
			dangerDiv.remove();
		}
	}
	//key press
	$('input[type="email"]').keyup(function() {
		var value = $(this).val();
		var valid = validateEmail(value);
		if (!valid) {
			removeClass($(this));
			$(this).after($("<div/>").addClass('text-danger').html("Invalid"));
			emailStatus = "invalid"
		}else {
			removeClass($(this));
			$(this).after($("<div/>").addClass('text-success').html("Valid"));
			emailStatus = "valid";
		}
	});
	//initial value
	if($('input[type="email"][required="required"]').length > 0){
		var value = $('input[type="email"][required="required"]').val();
		var valid = validateEmail(value);
		if (!valid) {
			removeClass($('input[type="email"][required="required"]'));
			$('input[type="email"][required="required"]').after($("<div/>").addClass('text-danger').html("Invalid"));
			emailStatus = "invalid"
		}else {
			removeClass($('input[type="email"][required="required"]'));
			$('input[type="email"][required="required"]').after($("<div/>").addClass('text-success').html("Valid"));
			emailStatus = "valid";
		}
	};

	var passwordStrength = 0;
	var usernameUniqueness = false;
	var mobileLength = 0;
	if($('input[name="mobile"][required="required"]').length > 0){
		mobileLength = $('input[name="mobile"][required="required"]').val().length;
	};

	//prevent submitting form by pressing enter key
	//declare class name 'no-enter'
	$('.no-enter').on('keyup keypress', function(e) {
		var keyCode = e.keyCode || e.which;
		if (keyCode === 13) { 
			e.preventDefault();
			return false;
		}
	});

	//access denied to cut, copy and past
	//declare class name 'no-copy'
	$('.no-copy').bind("cut copy paste", function(e) {
		e.preventDefault();
	});

	//non selectable element
	//declare class name 'no-select'
	$('.no-select').attr('unselectable', 'on')
	.css({
		'user-select': 'none',
		'MozUserSelect': 'none'
	})
	.on('selectstart', false)
	.on('mousedown', false);

	//numeric input 0-9 digits only
	//declare class name 'numeric-only'
	$('.numeric-only').on('keydown', function(e){-1!==$.inArray(e.keyCode,[46,8,9,27,13,110,190])||(/65|67|86|88/.test(e.keyCode)&&(e.ctrlKey===true||e.metaKey===true))&&(!0===e.ctrlKey||!0===e.metaKey)||35<=e.keyCode&&40>=e.keyCode||(e.shiftKey||48>e.keyCode||57<e.keyCode)&&(96>e.keyCode||105<e.keyCode)&&e.preventDefault()});

	//max-length checker
	//declare attribute 'maxlength=11'
	$(':input[maxlength]').each(function(){
		 var maxLength = $(this).attr('maxlength');
		 $(this).maxlength( {slider: true, maxCharacters: maxLength} );
	})

	//password strength checker
	//declare class name 'password'
	$('.password').password().on('password.score', function (e, score) {
		passwordStrength = score;
		console.log(score);
		if(!$('.password').attr('required') &&  $('.password').val() != '' && score < 75){
			$('button[type="submit"]').addClass('disabled');
			$('button[type="submit"]').attr('disabled','disabled');
		}else if(!$('.password').attr('required') &&  $('.password').val() === '' && (score === -1 || score > 75)){
			$('button[type="submit"]').removeClass('disabled');
			$('button[type="submit"]').removeAttr('disabled','disabled');
			checkAllRequiredFields();
		}
	});

	//prevent space character
	//declare nospace="true" attribute
	$('input[nospace="true"]').keypress(function( e ) {
		if(!/[0-9a-zA-Z-._@]/.test(String.fromCharCode(e.which)))
		return false;
	});

	//Print button
	//declare class name 'print'
	$('.print').on('click', function(){
		$('#inside_element').print();
	})

	//check username is already available in database table
	//$("#usersform").trigger("reset");
	$("#username").on('keyup change keypress blur', function(){
		$.post($(this).attr("action"), {'username' : $(this).val()}, function(data){
			if(data.status){
				$("#usernameError").html("Username already taken. Please try another username");
				usernameUniqueness = false;
				checkAllRequiredFields();
			}else{
				$("#usernameError").html("");
				usernameUniqueness = true;
				checkAllRequiredFields();
			}
		});
	});

	//success or error message alert
	$("#success-alert, #error-alert").delay(4000).slideUp(200, function() {
		$(this).alert('close');
	});

	//Required input field tooltip
	$('[data-toggle="tooltip"]').tooltip(); 
	$('.required-icon').tooltip({
		placement: 'left',
		title: 'Required field'
	});

	//Customize bootstrap data-table
	$('table#example').each(function (tindex, table) {
		var noSortColumns = [];
		$(table).find('th').each(function (thindex, column) {
			if ($(this).attr('data-nosort')) {
				var value = $(this).attr('data-nosort');
				if(value == 'Y'){
					noSortColumns.push(thindex);
				}
			}
		});
		$(table).DataTable({
			"responsive" : true,
			"columnDefs": [{
				"targets": noSortColumns,
				"orderable": false
			}]
		});
	});

	//mobile length checker
	$('input[name="mobile"][required="required"]').on('keyup change keypress blur', function(){
		mobileLength = $('input[name="mobile"][required="required"]').val().length;
	});

	//form disable for required fields 
	$(':input[required="required"]').each(function(){
		$(this).on('keyup change keypress blur', function(){
			checkAllRequiredFields();
		});
	});
	var checkAllRequiredFields = function(){
		$(':input[required="required"]').each(function(index, item){
			if($(this).attr('class') === 'select' && $(this).val() == ''){
				$('button[type="submit"]').addClass('disabled');
				$('button[type="submit"]').attr('disabled','disabled');
				return false;
			}
			if($(this).attr('type') === 'password' && passwordStrength < 75){
				$('button[type="submit"]').addClass('disabled');
				$('button[type="submit"]').attr('disabled','disabled');
				return false;
			}
			if($(this).attr('type') === 'email' && emailStatus === "invalid"){
				$('button[type="submit"]').addClass('disabled');
				$('button[type="submit"]').attr('disabled','disabled');
				return false;
			}
			if($(this).attr('name') === 'username' && usernameUniqueness === false){
				$('button[type="submit"]').addClass('disabled');
				$('button[type="submit"]').attr('disabled','disabled');
				return false;
			}
			if($(this).attr('name') === 'mobile' &&  mobileLength < 11){
				$('button[type="submit"]').addClass('disabled');
				$('button[type="submit"]').attr('disabled','disabled');
				return false;
			}
			if($(this).val() === ''){
				$('button[type="submit"]').addClass('disabled');
				$('button[type="submit"]').attr('disabled','disabled');
				return false;
			}else{
				$('button[type="submit"]').removeClass('disabled');
				$('button[type="submit"]').removeAttr('disabled');
			}
		});
	};
	checkAllRequiredFields();

	//only for create user page
	if($('#dealer-selector').length > 0){
		$(this).on('keyup change keypress blur', function(){
			checkAllRequiredFields();
		});
	}

});