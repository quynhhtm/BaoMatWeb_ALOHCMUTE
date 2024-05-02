var passwordInput = document.getElementById("password");
var confirmPasswordInput = document.getElementById("confirmPassword");
var passwordMatchError = document.getElementById("passwordMatchError");

confirmPasswordInput.addEventListener("blur", checkPasswordMatch);

function checkPasswordMatch() {
  var password = passwordInput.value;
  var confirmPassword = confirmPasswordInput.value;

  if (password !== confirmPassword) {
    passwordMatchError.style.display = "block";
  } else {
    passwordMatchError.style.display = "none";
  }
}
