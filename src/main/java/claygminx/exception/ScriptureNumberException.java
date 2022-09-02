package claygminx.exception;

/**
 * 经文编号异常
 */
public class ScriptureNumberException extends Exception {

    public ScriptureNumberException(String message) {
        super(message);
    }

    public ScriptureNumberException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
