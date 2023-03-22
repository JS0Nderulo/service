package ro.unibuc.hello.exception;

public class NullOrEmptyException extends RuntimeException {

    private static final String nullOrEmptyTemplate = "Entity: the provided number is null or empty!";

    public NullOrEmptyException() {
        super(nullOrEmptyTemplate);
    }
}
