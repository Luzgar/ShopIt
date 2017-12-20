package shopit.components;

import shopit.Logging;
import shopit.UserFinder;
import shopit.entities.User;
import shopit.exceptions.UserNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by Charly on 06/06/2017.
 */

@Stateless
public class AuthentificationBean implements Logging
{
    @EJB private UserFinder userFinder;

    @Override
    public int logIn(String login, String password) throws UserNotFoundException
    {
        User user = userFinder.findUserByLogin(login);
        if(user == null)
            throw new UserNotFoundException("No user found with this login");

        if(!user.getPswd().equals(password))
            throw new UserNotFoundException("User found with this login but password incorrect");

        return user.getId();
    }
}
