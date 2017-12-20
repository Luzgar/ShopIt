package shopit;

import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.exceptions.NoItemFoundException;

import javax.ejb.Local;

/**
 * Created by thomas on 29/05/2017.
 */
@Local
public interface ShopListOperation {

    public void addItemToShopList(int id, ShopListElement itemToAdd);

    public void removeItemFromShopList(int id, String name) throws NoItemFoundException;

    public void setItemQuantityFromShopList(int id, String name, int newQuantity) throws NoItemFoundException;

    public void setItemTaken(int id, String name, boolean isTaken) throws NoItemFoundException;

    public void archiveShopList(int id);

    void setItemNotFound(int id, String name) throws NoItemFoundException;
}
