package shopit;

import shopit.entities.ShopList;
import shopit.entities.User;

import javax.ejb.Local;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thomas on 29/05/2017.
 */
@Local
public interface ShopListFinder {

    public List<ShopList> getAllShopList(User user, boolean archive);

    /**
     * Return all the ShopList that the user own.
     * @param user
     * @return
     */
    public HashMap<Integer, String> getAllTitleShopList(User user, boolean archive);

    public ShopList getShopListById(int id);

    public int getItemPosition(ShopList shopList, String name);
}
