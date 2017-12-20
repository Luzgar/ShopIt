package business;

import arquillian.AbstractTCFTest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import shopit.RecipeFinder;
import shopit.RecipeRegistration;
import shopit.entities.Recipe;
import shopit.entities.ShopListElement;
import shopit.exceptions.NoIngredientsSelectedException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by thomas on 31/05/2017.
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class RecipeRegistryTest extends AbstractTCFTest {

    @EJB RecipeFinder finder;
    @EJB RecipeRegistration registration;

    @PersistenceContext private EntityManager manager;
    @Inject private UserTransaction utx;

    private List<ShopListElement> ingredients;
    private List<ShopListElement> ingredients_burger;
    private List<ShopListElement> ingredients_vegan_burger;

    private String name1 = "Patate au four";
    private String name2 = "Raclette";
    private String name3 = "Mc Test";
    private String name4 = "Test Burger";
    private String name5 = "Mc Vegan";

    private int recipe1_id;
    private int recipe2_id;
    private int recipe3_id;
    private int recipe4_id;
    private int recipe5_id;

    @Before
    public void defineContext()
    {
        ingredients = new ArrayList<>();
        ingredients_burger = new ArrayList<>();
        ingredients_burger.add(new ShopListElement("Pain Burger",1,false,"boulangerie",0));
        ingredients_burger.add(new ShopListElement("Steak haché 200g",1,false,"boucherie",0));
        ingredients_burger.add(new ShopListElement("Tranche de cheddar",1,false,"fromagerie",0));
        ingredients_burger.add(new ShopListElement("Tomate",1,false,"fruit et légume",0));
        ingredients_burger.add(new ShopListElement("Salade",1,false,"fruit et légume",0));
        ingredients_burger.add(new ShopListElement("Sauce Test",1,false,"épicerie",0));

        ingredients_vegan_burger = new ArrayList<>();
        ingredients_vegan_burger.add(new ShopListElement("Pain Burger",1,false,"boulangerie",0));
        ingredients_vegan_burger.add(new ShopListElement("Steak de toffu 200g",1,false,"boucherie",0));
        ingredients_vegan_burger.add(new ShopListElement("Tomate",1,false,"fruit et légume",0));
        ingredients_vegan_burger.add(new ShopListElement("Salade",1,false,"fruit et légume",0));
        ingredients_vegan_burger.add(new ShopListElement("Sauce vegan",1,false,"épicerie",0));

        recipe1_id = registration.addRecipe(name1, 1, ingredients, "");
        recipe2_id = registration.addRecipe(name2, 4, ingredients, "");
        recipe3_id = registration.addRecipe(name3, 1, ingredients_burger, "");
        recipe4_id = registration.addRecipe(name4, 1, ingredients_burger, "");
        recipe5_id = registration.addRecipe(name5, 1, ingredients_vegan_burger, "");
    }

    @After
    public void cleanUp() throws Exception
    {
        utx.begin();

        manager.remove(manager.find(Recipe.class,recipe1_id));
        manager.remove(manager.find(Recipe.class,recipe2_id));
        manager.remove(manager.find(Recipe.class,recipe3_id));
        ingredients = null;
        ingredients_burger = null;

        utx.commit();
    }

    @Test
    public void testGetAllRecipeName()
    {
        Map<Integer, String> map = finder.getAllRecipeName();
        assertEquals(5,map.size());
        assertEquals(name1,map.get(recipe1_id));
        assertEquals(name2,map.get(recipe2_id));
        assertEquals(name3,map.get(recipe3_id));
    }

    @Test
    public void testGetRecipeById()
    {
        Recipe r1 = manager.find(Recipe.class,recipe1_id);
        Recipe r3 = manager.find(Recipe.class,recipe3_id);

        assertEquals(r1,finder.getRecipeById(recipe1_id));
        assertEquals(r3,finder.getRecipeById(recipe3_id));
    }

    @Test
    public void testAddRecipe()
    {
        assertNotNull(finder.getRecipeById(recipe3_id));
        assertEquals(-1, registration.addRecipe(name3, 1, ingredients_burger, ""));
    }

    @Test
    public void testGetRecipeByIngredients() throws Exception{
        //TODO: Corriger les tests
        List<String> ingredients_test = new ArrayList<>();
        ingredients_test.add("Tomate");
        ingredients_test.add("Salade");

        System.out.println("XXX");
        Map<Integer, String> map = finder.getRecipeByIngredients(ingredients_test);
        assertEquals(3,map.size());
        assertEquals(name3,map.get(recipe3_id));
        assertEquals(name4,map.get(recipe4_id));
        assertEquals(name5,map.get(recipe5_id));

        ingredients_test.add("Steak haché 200g");
        map = finder.getRecipeByIngredients(ingredients_test);
        assertEquals(2,map.size());
        assertEquals(name3,map.get(recipe3_id));
        assertEquals(name4,map.get(recipe4_id));

        ingredients_test.add("Sauce vegan");
        map = finder.getRecipeByIngredients(ingredients_test);
        assertEquals(0,map.size());
    }

    @Test (expected = NoIngredientsSelectedException.class)
    public void testCannotGetRecipeByIngredients() throws Exception{
        assertEquals(0,finder.getRecipeByIngredients(null).size());
    }

    @Test (expected = NoIngredientsSelectedException.class)
    public void testCannotGetRecipeByIngredients2() throws Exception{
        assertEquals(0,finder.getRecipeByIngredients(new ArrayList<String>()).size());
    }
}
