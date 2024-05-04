	$(document).ready(function () {
		console.log("DOne");
		
		var passwordInput = $("#password");
		var confirmPasswordInput = $("#confirmPassword");
		var passwordMatchError = $("#passwordMatchError");
		var registerBtn = $("#registerBtn");

		$('input[name="username"').on('input', function () {
			if ($(this).val().indexOf(' ') !== -1){
				toastr.error('Không được nhập dấu cách.');
	            var inputValue = $(this).val().trim();
	            inputValue = inputValue.replace(/\s{2,}/g, ' ');
	            $(this).val(inputValue);
			}
        });
        
        $('input[name="firstName"').on('input', function () {
			if ($(this).val().match(/\s{2,}/)){
				toastr.error('Không được nhập nhiều hơn 2 dấu cách.');
	            var inputValue = $(this).val().trim();
	            inputValue = inputValue.replace(/\s{2,}/g, '  ');	
	            $(this).val(inputValue);
			}
        });
        
        $('input[name="lastName"').on('input', function () {
			if ($(this).val().match(/\s{2,}/)){
				toastr.error('Không được nhập nhiều hơn 2 dấu cách.');
	            var inputValue = $(this).val().trim();
	            inputValue = inputValue.replace(/\s{2,}/g, '  ');	
	            $(this).val(inputValue);
			}
        });
		
		$('input[name="nickName"').on('input', function () {
			if ($(this).val().indexOf(' ') !== -1){
				toastr.error('Không được nhập dấu cách.');
	            var inputValue = $(this).val().trim();
	            inputValue = inputValue.replace(/\s{2,}/g, ' ');
	            $(this).val(inputValue);
			}
		});
		
		$('input[name="email"').on('input', function () {
			if ($(this).val().indexOf(' ') !== -1){
				toastr.error('Không được nhập dấu cách.');
	            var inputValue = $(this).val().trim();
	            inputValue = inputValue.replace(/\s{2,}/g, ' ');
	            $(this).val(inputValue);
			}
		});
		

		confirmPasswordInput.on("input", function () {
			var password = passwordInput.val();
			var confirmPassword = confirmPasswordInput.val();

			if (password !== confirmPassword) {
				passwordMatchError.show();
				registerBtn.prop("disabled", true);

			} else {
				passwordMatchError.hide();
				registerBtn.prop("disabled", false);
			}
		});

		passwordInput.on("input", function () {
			var password = passwordInput.val();
			var confirmPassword = confirmPasswordInput.val();

			if (password !== confirmPassword) {
				passwordMatchError.show();
				registerBtn.prop("disabled", true);

			} else {
				passwordMatchError.hide();
				registerBtn.prop("disabled", false);
			}
		});
	});
