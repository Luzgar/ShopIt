package shopit.exceptions;

/**
 * Created by thomas on 06/06/2017.
 */
public class NoIngredientsSelectedException extends Exception {

    public NoIngredientsSelectedException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "NoIngredientsSelectedException{}";
    }
}
