package shopit;

import shopit.entities.Medal;
import shopit.entities.ShopList;
import shopit.entities.User;
import shopit.exceptions.InvalidMedalException;

import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Schedules;

/**
 * Created by thomas on 12/06/2017.
 */
@Local
public interface MedalOperation {

    /**
     * Set the Medal exposed of a user.
     * @param user
     * @param medal
     * @throws InvalidMedalException
     */
    public void setUserMedal(User user, Medal medal) throws InvalidMedalException;

    /**
     * Check all the Medal of a user to clear the progression and add the won badge
     * @param user
     */
    public void checkMedalProgression(User user);

    /**
     * Apply the progression of the Recipe Medal to the user.
     * @param user
     */
    public void progressionAddRecipeMedal(User user);

    /**
     * Apply the progression of the Sharing Medal to the user.
     * @param user
     */
    public void progressionSharingShopListMedal(User user);

    /**
     * Apply the progression of the ShopRecipe Medal to the user.
     * @param user
     */
    public void progressionShopRecipeMedal(User user);

    /**
     *Apply the progression of the category medals for each contributor of the ShopList.
     * @param shopList
     */
    public void progressionCategoriesMedalsForShopList(ShopList shopList);

    /**
     * Calculate the rewards of all user of the system.
     */
    @Schedules({
            @Schedule(dayOfMonth="Last"),
            @Schedule(hour="22", minute = "00")
    })
    public void calculateAllUsersRewards();

    public void addMedalUser(User user, String name);
}
