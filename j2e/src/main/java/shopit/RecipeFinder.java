package shopit;

import shopit.entities.Recipe;
import shopit.exceptions.NoIngredientsSelectedException;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 31/05/2017.
 */
@Local
public interface RecipeFinder {

    Map<Integer, String> getAllRecipeName();

    Recipe getRecipeById(int id);

    Recipe getRecipeByName(String name);

    Map<Integer, String> getRecipeByIngredients(List<String> ingredients) throws NoIngredientsSelectedException;
}
