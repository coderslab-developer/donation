$(document).ready(function(){
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

	//password strength checker
	//declare class name 'password'
	$(document).ready(function() {
		$('.password').password().on('password.score', function (e, score) {
			if (score > 75) {console.log(score)}
		});
	});

	//max-length checker
	//declare attribute 'maxlength=11'
	$(':input[maxlength]').each(function(){
		 var maxLength = $(this).attr('maxlength');
		 $(this).maxlength( {slider: true, maxCharacters: maxLength} );
	})

	//email validating
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
	$('input[type="email"]').keyup(function() {
		var value = $(this).val();
		var valid = validateEmail(value);
		if (!valid) {
			removeClass($(this));
			$(this).after($("<div/>").addClass('text-danger').html("Invalid"));
		}else {
			removeClass($(this));
			$(this).after($("<div/>").addClass('text-success').html("Valid"));
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
	$(document).ready(function(){
		$('.print').on('click', function(){
			console.log("hi");
			$('#inside_element').print();
		})
	});

	//check username and email is already available in database table
	$("#usersform").trigger("reset");
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
});