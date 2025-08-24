package errors;


public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException() {
        super("Invalid or expired refresh token");
    }

    public RefreshTokenException(String message) {
        super(message);
    }
}
