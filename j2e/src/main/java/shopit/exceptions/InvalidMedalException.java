package shopit.exceptions;

/**
 * Created by thomas on 13/06/2017.
 */
public class InvalidMedalException extends Exception {

    public InvalidMedalException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "InvalidMedalException{}";
    }
}
