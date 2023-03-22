package ro.unibuc.hello.exception;

public class NullOrEmptyNumberException extends RuntimeException {

    private static final String nullOrEmptyTemplate = "Entity: the provided number is null or empty!";

    public NullOrEmptyNumberException() {
        super(nullOrEmptyTemplate);
    }
}
