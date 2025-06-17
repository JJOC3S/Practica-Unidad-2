package tercero.com.base.controller.exceptions;

public class CriterioNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public CriterioNotFoundException(String message) {
        super(message);
    }

    public CriterioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CriterioNotFoundException(Throwable cause) {
        super(cause);
    }
}
