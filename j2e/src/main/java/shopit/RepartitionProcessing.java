package shopit;

import shopit.entities.ShopList;

import javax.ejb.Local;

/**
 * Created by thomas on 30/05/2017.
 */
@Local
public interface RepartitionProcessing {

    public void doCategoryRepartition(ShopList shopList);

    public void doPriceRepartition(ShopList shopList);
}
