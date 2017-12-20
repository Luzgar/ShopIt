package shopit;

import shopit.entities.User;

import javax.ejb.Local;

/**
 * Created by Charly on 31/05/2017.
 */
@Local
public interface TokenManaging
{
    void setUserDeviceToken(User user, String token);
}
