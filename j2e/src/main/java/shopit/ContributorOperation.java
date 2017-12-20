package shopit;

import shopit.entities.ShopList;
import shopit.entities.User;
import shopit.exceptions.ContributorAlreadyExistingException;

import javax.ejb.Local;

/**
 * Created by thomas on 29/05/2017.
 */
@Local
public interface ContributorOperation {

    public void addContributorToShopList(ShopList shopList, User contributor) throws ContributorAlreadyExistingException;

    public void removeContributorFromShopList(ShopList shopList, User contributor);
}
