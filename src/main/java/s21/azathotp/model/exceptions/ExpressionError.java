package s21.azathotp.model.exceptions;

public class ExpressionError extends RuntimeException {
    public ExpressionError(String message) {
        super(message);
    }
}
