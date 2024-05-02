package hcmute.alohcmute.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 6857655397617918803L;

	public UserAlreadyExistsException(String message) {
		super(message);
	}
}
