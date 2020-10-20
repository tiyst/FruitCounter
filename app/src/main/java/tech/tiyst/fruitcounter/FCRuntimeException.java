package tech.tiyst.fruitcounter;

public class FCRuntimeException extends RuntimeException {

    public FCRuntimeException() {
        super("FCRuntimeException has occurred");
    }

    public FCRuntimeException(String message) {
        super(message);
    }
}
