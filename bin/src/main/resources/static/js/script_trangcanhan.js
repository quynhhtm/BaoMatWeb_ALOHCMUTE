$('.dropdown-toggle').on('click', function(e) {
	var $dropdownMenu = $(this).siblings('.dropdown-menu');

	$('.dropdown-menu').not($dropdownMenu).removeClass('show');

	$dropdownMenu.toggleClass('show');
});

$(document).on('click', function(e) {
	var $target = $(e.target);
	if (!$target.closest('.dropdown').length) {
		$('.dropdown-menu').removeClass('show');
	}
});

document.addEventListener('DOMContentLoaded', function() {
	// Lắng nghe sự kiện click vào nút có id="edit-btn"
	document.getElementById('edit-btn').addEventListener('click', function() {
		// Lấy phần tử chứa thông tin người dùng và biểu mẫu chỉnh sửa
		var viewInfo = document.getElementById('view-info');
		var editInfo = document.getElementById('edit-info');

		// Kiểm tra trạng thái hiển thị của các phần tử
		if (viewInfo.style.display === 'block' || viewInfo.style.display === '') {
			// Ẩn phần tử hiển thị thông tin
			viewInfo.style.display = 'none';
			// Hiển thị phần tử biểu mẫu chỉnh sửa
			editInfo.style.display = 'block';
		}

		// Lấy phần tử chứa nút "Edit"
		var editButton = document.getElementById('edit-btn');

		// Ẩn nút "Edit"
		editButton.style.display = 'none';
	});
});
// Function to handle cancel button click
function cancelEdit() {
	document.getElementById('view-info').style.display = 'block'; // Hiển thị phần thông tin
	document.getElementById('edit-info').style.display = 'none'; // Ẩn phần chỉnh sửa
	// Lấy phần tử chứa nút "Edit"
	var editButton = document.getElementById('edit-btn');

	// Ẩn nút "Edit"
	editButton.style.display = 'block';
}
// JavaScript để điều khiển hiển thị modal
function openModal() {
	document.getElementById("myModal").style.display = "block";
}

function closeModal() {
	document.getElementById("myModal").style.display = "none";
}
document.getElementById('input-file').addEventListener('change', function() {
	var uploadBtn = document.getElementById('uploadBtn');
	if (this.files.length > 0) {
		uploadBtn.style.display = 'block';
	} else {
		uploadBtn.style.display = 'none';
	}
});


function autoExpand(element) {
	element.style.height = 'auto';
	element.style.height = (element.scrollHeight) + 'px';
}

const textAreas = document.querySelectorAll('textarea.auto-expand');

textAreas.forEach(textArea => {
	textArea.addEventListener('input', function() {
		autoExpand(this);
	});
	autoExpand(textArea);
});


