package tech.tiyst.fruitcounter;

public class FruitCounterException extends RuntimeException {

    public FruitCounterException() {
        super("FruitCounterException has occurred");
    }

    public FruitCounterException(String message) {
        super(message);
    }
}
