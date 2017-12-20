package shopit;

import shopit.entities.ShopList;
import shopit.entities.User;
import shopit.exceptions.NoItemFoundException;
import shopit.exceptions.UserIsNotAContributorException;

import javax.ejb.Local;

/**
 * Created by thomas on 30/05/2017.
 */
@Local
public interface CriterionBuilder {

    public void setUserInCharge(ShopList shopList, User contributor, String item)  throws NoItemFoundException, UserIsNotAContributorException;
}
