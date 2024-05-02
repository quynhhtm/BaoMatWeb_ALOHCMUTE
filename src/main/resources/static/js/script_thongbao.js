let clickCount = 0;

  const button = document.getElementById('bell');
  const popup = document.getElementById('popup');

  button.addEventListener('click', function() {
    clickCount++;
    if(clickCount===1)
	{
		popup.classList.add("open-popup")
		
	}		
    if (clickCount === 2) {
		popup.classList.remove("open-popup")
		clickCount=0;
    }
  });
function closePopup() {
	popup.classList.remove("open-popup")
};