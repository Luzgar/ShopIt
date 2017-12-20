package shopit.webservice;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import shopit.MedalOperation;
import shopit.RecipeFinder;
import shopit.RecipeRegistration;
import shopit.UserFinder;
import shopit.entities.Recipe;
import shopit.entities.ShopListElement;
import shopit.entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 31/05/2017.
 */
@Stateless
public class RecipeWebServiceImpl implements RecipeWebService{

    @EJB private RecipeFinder recipeFinder;
    @EJB private RecipeRegistration recipeRegistration;
    @EJB private MedalOperation medalOperation;
    @EJB private UserFinder userFinder;

    @Override
    public Response getAllRecipeName() {

        Map<Integer, String> nameWithId = recipeFinder.getAllRecipeName();

        // Map to JSON Object
        Gson gson = new Gson();
        String json = gson.toJson(nameWithId);

        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();

    }

    @Override
    public Response getRecipeById(int id) {

        Recipe recipe = recipeFinder.getRecipeById(id);

        Gson gson = new Gson();
        String json = gson.toJson(recipe);

        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
    }

    @Override
    public Response addRecipe(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        String name = jsonObject.getString("name");
        int numberPerson = jsonObject.getInt("number_person");
        String description = jsonObject.getString("description");
        int author_id = jsonObject.getInt("author_id");
        JSONArray ingredientsJson = jsonObject.getJSONArray("ingredients");

        List<ShopListElement> ingredients = new ArrayList<>();
        for(int i = 0; i < ingredientsJson.length(); i++)
        {
            ShopListElement temp = new ShopListElement();
            JSONObject object = (JSONObject) ingredientsJson.get(i);
            temp.setItem(object.getString("name_item"));
            temp.setNumber(object.getInt("quantity"));
            temp.setTaken(object.getBoolean("taken"));
            temp.setCategory(object.getString("category"));

            ingredients.add(temp);
        }

        int result = recipeRegistration.addRecipe(name, numberPerson, ingredients, description);
        if(result == -1) {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("recipe name already exists").build();
        }
        User author = userFinder.findUserById(author_id);
        if(author!=null){
            medalOperation.progressionAddRecipeMedal(author);
        }

        return Response.ok().build();
    }

    @Override
    public Response getRecipeByIngredients(String parameters) {

        String[] ingredients = parameters.split(",");
        List<String> list = new ArrayList<>();
        for(String ingredient : ingredients){
            list.add(ingredient);
        }

        try {
            Map<Integer, String> nameWithId =
                    recipeFinder.getRecipeByIngredients(list);

            Gson gson = new Gson();
            String json = gson.toJson(nameWithId);

            return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
        }
        catch(Exception e){
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }
    }
}
