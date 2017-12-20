package shopit.components;

import shopit.RecipeFinder;
import shopit.RecipeRegistration;
import shopit.entities.Recipe;
import shopit.entities.ShopListElement;
import shopit.exceptions.NoIngredientsSelectedException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by thomas on 31/05/2017.
 */
@Stateless
public class RecipeRegistryBean implements RecipeFinder, RecipeRegistration {

    @PersistenceContext
    private EntityManager manager;

    /**
     * Returns a map with the id/name of all the Recipes.
     * @return
     */
    @Override
    public Map<Integer, String> getAllRecipeName() {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Recipe> criteria = builder.createQuery(Recipe.class);
        Root<Recipe> root =  criteria.from(Recipe.class);
        criteria.select(root);
        TypedQuery<Recipe> query = manager.createQuery(criteria);

        try{
            List<Recipe> list = query.getResultList();
            Map<Integer, String> result = new HashMap<Integer, String>();
            for(Recipe r : list){
                result.put(r.getId(),r.getName());
            }
            return result;
        }
        catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Recipe getRecipeById(int id) {
        return manager.find(Recipe.class,id);
    }

    /**
     *
     * @return the entity or null if does not exist.
     */
    @Override
    public Recipe getRecipeByName(String name)
    {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Recipe> criteria = builder.createQuery(Recipe.class);
        Root<Recipe> root =  criteria.from(Recipe.class);
        criteria.select(root).where(builder.equal(root.get("name"), name));
        TypedQuery<Recipe> query = manager.createQuery(criteria);

        try{
            return query.getSingleResult();
        }
        catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Map<Integer, String> getRecipeByIngredients(List<String> ingredients) throws NoIngredientsSelectedException {

        if(ingredients==null || ingredients.size()==0){
            throw new NoIngredientsSelectedException("No ingredients selected for search");
        }

        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Integer> q = cb.createQuery(Integer.class);
        Root<Recipe> from = q.from(Recipe.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        Expression<Integer> id = from.get("id");

        Expression<Collection<String>> documentTags =
                from.get("ingredients").get("item");
        for (String tag : ingredients) {
            Expression<String> param = cb.literal(tag);
            Predicate predicate = cb.isMember(param, documentTags);
            predicates.add(predicate);
        }

        q.multiselect(id).where(
                cb.or(predicates.toArray(new Predicate[0])));
        q.distinct(true);
        q.groupBy(id);
        q.having(cb.equal(cb.count(id), ingredients.size()));

        TypedQuery<Integer> query = manager.createQuery(q);
        try{
            List<Integer> list = query.getResultList();
            Map<Integer, String> result = new HashMap<>();
            for(Integer i : list){
                result.put(i,manager.find(Recipe.class,i).getName());
            }
            return result;
        }
        catch(NoResultException e) {
            return null;
        }
    }

    private List<String> getListIngredients(Recipe recipe){
        List<String> ingredients = new ArrayList<String>();
        for(ShopListElement elt : recipe.getIngredients()){
            ingredients.add(elt.getItem());
        }
        return ingredients;
    }

    @Override
    public int addRecipe(String name, int nbPerson, List<ShopListElement> ingredients, String description)
    {
        if(getRecipeByName(name) != null)
            return -1;

        Recipe newRecipe = new Recipe(name, nbPerson, ingredients, description);
        manager.persist(newRecipe);
        return newRecipe.getId();
    }
}
