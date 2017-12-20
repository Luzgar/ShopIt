package shopit.exceptions;

import shopit.entities.ShopList;
import shopit.entities.User;

import java.io.Serializable;

/**
 * Created by thomas on 30/05/2017.
 */
public class ContributorAlreadyExistingException extends Exception {

    public ContributorAlreadyExistingException(String message) {
        super(message);
    }

    @Override
    public String toString()
    {
        return "ContributorAlreadyExistingException : " + getMessage();
    }
}
