package shopit.exceptions;

import java.io.Serializable;

/**
 * Created by thomas on 30/05/2017.
 */
public class UserIsNotAContributorException extends Exception implements Serializable {

    public UserIsNotAContributorException(String message)
    {
        super(message);
    }

    @Override
    public String toString()
    {
        return "UserIsNotAContributorException : " + getMessage();
    }
}
