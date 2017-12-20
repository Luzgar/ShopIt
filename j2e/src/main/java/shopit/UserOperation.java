package shopit;

import shopit.exceptions.UserNotFoundException;

import javax.ejb.Local;
import javax.jws.soap.SOAPBinding;

/**
 * Created by Charly on 08/06/2017.
 */

@Local
public interface UserOperation
{
    void setUserLifespanList(int userId, int nb) throws UserNotFoundException;
    int getUserLifespanList(int userId) throws UserNotFoundException;

    void setUserDoReccurency(int userId, boolean doReccurency) throws UserNotFoundException;
}
