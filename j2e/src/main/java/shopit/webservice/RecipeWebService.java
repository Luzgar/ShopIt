package shopit.webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by thomas on 31/05/2017.
 */
@Path("/recipe")
public interface RecipeWebService {

    @GET
    @Path("/allrecipe")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllRecipeName();

    @GET
    @Path("/getrecipebyid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getRecipeById(@PathParam("id") int id);

    /**
     * Expected body request :
     {
        "name": "poulet au curry",
        "number_person": 2,
        "description": "Mah c'est soupère",
        "author_id": 1,
        "ingredients": [
            {
                "name_item": "poulet 1kg",
                "quantity": 1,
                "taken": false,
                "category": "viande"
            },
            {
                "name_item": "curry",
                "quantity": 2,
                "taken": false,
                "category": "epice"
            }
        ]
     }
     */
    @POST
    @Path("/addrecipe")
    @Consumes(MediaType.APPLICATION_JSON)
    Response addRecipe(String json);

    /*
        Expected link request :
        /recipe/recipebyingredients/courgette,creme fraiche
        /recipe/recipebyingredients/pain burger,salade,tomate
     */
    @GET
    @Path("/recipebyingredients/{parameters}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getRecipeByIngredients(@PathParam("parameters") String parameters);
}
