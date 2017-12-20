package shopit;

import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.utils.RecurrencyStrategy;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Charly on 13/06/2017.
 */

@Local
public interface RecurrencyProcessing
{
    List<String> findRecurrentItems(List<ShopList> lists, RecurrencyStrategy strategy);
}
