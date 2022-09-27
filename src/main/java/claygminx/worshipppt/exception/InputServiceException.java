package claygminx.worshipppt.exception;

/**
 * 输入服务异常
 */
public class InputServiceException extends Exception {

    public InputServiceException(String message) {
        super(message);
    }

    public InputServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
