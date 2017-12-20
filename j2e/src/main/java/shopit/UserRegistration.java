package shopit;

import shopit.exceptions.UserAlreadyExistingException;

import javax.ejb.Local;

/**
 * Created by thomas on 29/05/2017.
 */
@Local
public interface UserRegistration{

    int createUser(String pseudo, String login, String pswd) throws UserAlreadyExistingException;

}
