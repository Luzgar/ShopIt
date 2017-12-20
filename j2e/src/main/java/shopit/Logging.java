package shopit;

import shopit.exceptions.UserNotFoundException;

import javax.ejb.Local;

/**
 * Created by Charly on 06/06/2017.
 */

@Local
public interface Logging
{
    int logIn(String login, String password) throws UserNotFoundException;
}
