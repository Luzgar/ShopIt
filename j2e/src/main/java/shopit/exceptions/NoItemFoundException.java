package shopit.exceptions;

import java.io.Serializable;

/**
 * Created by thomas on 30/05/2017.
 */
public class NoItemFoundException extends Exception {

    public NoItemFoundException(String message) {
        super(message);
    }

    @Override
    public String toString()
    {
        return "NoItemFoundException : " + getMessage();
    }
}
