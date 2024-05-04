	$(document).ready(function () {
		$('input[name="username"').on('input', function () {
			if ($(this).val().indexOf(' ') !== -1){
				toastr.error('Không được nhập dấu cách.');
	            var inputValue = $(this).val().trim();
	            inputValue = inputValue.replace(/\s{2,}/g, ' ');
	            $(this).val(inputValue);
			}
        });
	});
