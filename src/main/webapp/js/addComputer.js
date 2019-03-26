//$(document).ready(function () {
//  $("#computerName").on('change', function () {
//	  if($("#computerName").val().trim() != ""){
//		  $("#emptyNameError").css('display', 'none');
//	  } else {
//		  $("#emptyNameError").css('display', 'block');
//	  }
//  });
//	
//  $('#introduced').on('change', function () {
//    $('#discontinued').attr('min',$('#introduced').val());
//  });
//  
//  $("#introduced").on('input propertychange',
//	  event => $('#discontinued').prop('disabled', event.currentTarget.value == "")
//  );
//});

$(document).ready(function () {
  if($("#introduced").val() == "") {
	  $('#discontinued').prop('disabled', true);
  }
  
  $("#introduced").on('change', function () {
	  if(event.currentTarget.value == "") {
		  $('#discontinued').val("");
	  } else if(new Date(event.currentTarget.value).getTime() > new Date($('#discontinued').val())) {
		  $('#discontinued').val(event.currentTarget.value);
	  }
	  
	  $('#discontinued').prop('disabled', event.currentTarget.value == "");
	  $('#discontinued').attr('min', event.currentTarget.value);
  });
  
  $("#computerName").on('change', function () {
	  if($("#computerName").val().trim() != ""){
		  $("#emptyNameError").css('display', 'none');
	  } else {
		  $("#emptyNameError").css('display', 'block');
	  }
  });
});