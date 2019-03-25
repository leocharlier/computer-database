$(document).ready(function () {
  $("#computerName").on('change', function () {
	  if($("#computerName").val().trim() != ""){
		  $("#emptyNameError").css('display', 'none');
	  } else {
		  $("#emptyNameError").css('display', 'block');
	  }
  }
  );
	
  $('#introduced').on('change', function () {
    $('#discontinued').attr('min',$('#introduced').val());
  });
  
  $("#introduced").on('input propertychange',
	event => $('#discontinued').prop('disabled', event.currentTarget.value == "")
  );
});