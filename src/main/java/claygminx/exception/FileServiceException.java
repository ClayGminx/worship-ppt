package claygminx.exception;

/**
 * 文件服务异常
 */
public class FileServiceException extends Exception {

    public FileServiceException(String message) {
        super(message);
    }

    public FileServiceException(String message, Throwable e) {
        super(message, e);
    }
}
