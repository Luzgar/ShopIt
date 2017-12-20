package shopit;

import shopit.entities.ShopListElement;
import shopit.entities.User;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by thomas on 29/05/2017.
 */
@Local
public interface ShopListRegistration {

    public int createShopList(User owner, String name, List<ShopListElement> elements, Date dateCreation);

    public void removeShopList(int id);
}
