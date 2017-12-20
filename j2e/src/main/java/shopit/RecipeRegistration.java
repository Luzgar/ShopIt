package shopit;

import shopit.entities.ShopListElement;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Charly on 01/06/2017.
 */

@Local
public interface RecipeRegistration
{
    int addRecipe(String name, int nbPerson, List<ShopListElement> ingredients, String description);
}
