$(document).ready(function(){
	$('.container').css({
		'min-height' : $(window).height()
	});
	var containerWidth = $('.container').width();
	var copyRightWidth = Math.ceil($('#copyRight').width());
	$('#copyRight').css({
		'left': containerWidth/2 - copyRightWidth/2,
		'bottom' : 0
	});

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

	//Customize bootstrap data-table api
	var dataTable;
	var calculateColumn = function(table, columnIndex, sumOfRows, columnId){
		$(table).find('tr').each(function(i, tr){
			$(this).find('td').each(function(j, td){
				if(j === columnIndex){
					sumOfRows += Number($(this).html());
				}
			});
		});
		if(columnIndex !== -1){
			$('.' + columnId + 'Result').html(sumOfRows);
		}
	};
	$('table#example').each(function (tindex, table) {
		var noSortColumns = [];
		var scrollY = "";
		var scrollCollapse = false;
		var paging = true;
		var searching = true;

		$(table).find('th').each(function (i, column) {
			if ($(this).attr('data-nosort')) {
				var value = $(this).attr('data-nosort');
				if(value == 'Y'){
					noSortColumns.push(i);
				}
			}
		});

		//calculate
		var sumOfRows = 0;
		var columnIndex = $('.calculate').index();
		var columnId = $(this).attr('id');
		calculateColumn($('#example'), columnIndex, sumOfRows, columnId);

		//add attribute text-align="center" or text-align="left" or text-align="right"
		$(table).find('thead, tfoot, tbody').each(function (i, item) {
			if ($(item).attr('text-align')) {
				$(item).find('th, td').each(function (j, column) {
					$(column).css('text-align', $(item).attr('text-align'));
				});
			}
		});

		//add attribute scroll-y="200px"
		if($(table).attr('scroll-y')){
			scrollY = $(table).attr('scroll-y');
			scrollCollapse = true;
			paging = false;
		}

		//add attribute searching="false"
		if($(table).attr('searching')){
			if($(table).attr('searching') === 'false'){
				searching = false;
			}
		}

		dataTable = $(table).DataTable({
			"responsive" : true,
			"columnDefs": [{
				"targets": noSortColumns,
				"orderable": false
			}],
			"lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"scrollY" : scrollY,
			"scrollCollapse": scrollCollapse,
			"paging": paging,
			"searching": searching
		});
	});

	//datepicker - bootstrap
	//add class 'datepicker'
	$('.datepicker').each(function(){
		$(this).datepicker({
			"todayBtn" : true,
			"format" : 'dd-mm-yyyy',
			"defaultViewDate" : 'today'
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

	//report form
	$("#report-form").submit(function(e){
		e.preventDefault();
		dataTable.clear().draw();	//clear the datatable
		$('.result').html('');		//clear the result field
		if($('#start-date').val() == '' || $('#end-date').val() == ''){
			alert("Please select date correctly");
			return;
		}
		var formData = {
			'donarId' : $('#donar').val(),
			'startDate' : $('#start-date').val(),
			'endDate' : $('#end-date').val()
		}
		$.post($("#report-form").attr("action"), formData)
		.done(function(data){
			if(data.length > 0){
				$.each(data, function(i, item) {
					dataTable.row.add([
						++i,
						item.donarName,
						item.payDate,
						item.payableAmount,
						item.paid,
						item.due
					]).draw(false);
				});
				//$('.result').html(result);
				$('.calculate').each(function(){
					var sumOfRows = 0;
					var columnIndex = $(this).index();
					var columnId = $(this).attr('id');
					calculateColumn($('#example'), columnIndex, sumOfRows, columnId);
				});
			}
		}); 
	});

});