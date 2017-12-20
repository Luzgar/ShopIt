package shopit;

import shopit.entities.Medal;
import shopit.entities.ShopList;
import shopit.entities.User;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by thomas on 13/06/2017.
 */
@Local
public interface MedalFinder {

    /**
     * Return the exposed Medal of the user.
     * @param user
     * @return
     */
    public Medal getUserMedal(User user);

    /**
     * Return all the medal exposable of the user.
     * @param user
     * @return
     */
    public List<String> getUserMedalsUnlocked(User user);

    /**
     * Return all the medal exposed by the owner and contributor of the shoplist.
     * @param shopList
     * @return
     */
    public List<String> getMedalOfContributors(ShopList shopList);
}
