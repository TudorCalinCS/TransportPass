package server;


public class SrvException extends Exception{
    public SrvException() {
    }
    public SrvException(String message) {
        super(message);
    }

    public SrvException(String message, Throwable cause) {
        super(message, cause);
    }
}
