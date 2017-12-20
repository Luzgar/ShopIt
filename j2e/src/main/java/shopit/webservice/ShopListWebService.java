package shopit.webservice;

import shopit.entities.ShopListElement;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Charly on 27/05/2017.
 */

@Path("/shoplist")
public interface ShopListWebService
{
    /**
     * Expected body request :
     {
        "id": 5,
        "name": "listName",
        "timestamp": 101500192
        "items":
        [
            {
                "item": "butter",
                "quantity": 1,
                "taken": false,
                "category": "fresh product",
                "recipe_container": "recette"
            },
            {
                "item": "water",
                "quantity": 3,
                "taken": false,
                "category": "beverage"
                "recipe_container": "recette"
            }
        ]
     }
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/plain")
    Response registerList(String json);

    /**
     * Returns this type of JSON Map : {"51":"Friday list","52":"Saturday list"}
     */
    @GET
    @Path("/allcurrenttitles/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllCurrentTitlesShopList(@PathParam("userid") int userId);

    /**
     * Returns this type of JSON Map : {"51":"Friday list","52":"Saturday list"}
     */
    @GET
    @Path("/allarchivetitles/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllArchiveTitlesShopList(@PathParam("userid") int userId);

    /**
     * Returns this type of JSON :
     {
        "id": 0,
        "name": "hoy",
        "owner":
        {
            "id": 0,
            "login": "bla",
            "pswd": "rebla",
            "pseudo": "rerebla"
        },
        "elements":
        [
            {
                "item": "butter",
                "number": 1,
                "taken": false,
                "category": "fresh product"
            },
            {
                "item": "water",
                "number": 3,
                "taken": false,
                "category": "beverage"
            }
         ],
        "contributors":
        [
            {
                "id": 0,
                "login": "bla",
                "pswd": "rebla",
                "pseudo": "rerebla"
            }
        ]
     }
     */
    @GET
    @Path("/listdetails/{listid}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getListWithId(@PathParam("listid") int listId);

    /**
     * Expected request body :
     {
        "id": 1,
        "item":
        {
            "name_item": "bla",
            "quantity": 2,
            "taken": false,
            "category": "bla"
            "recipe_container": "recette"
        }
     }
     */
    @POST
    @Path("/additem")
    @Consumes(MediaType.APPLICATION_JSON)
    Response addItem(String json);

    /**
     * Expected request body :
     {
        "id": 2,
        "item": "water"
     }
     */
    @POST
    @Path("/removeitem")
    @Consumes(MediaType.APPLICATION_JSON)
    Response removeItem(String json);

    /**
     * Expected body request :
     {
        "id": 51,
        "item": "water",
        "new_quantity": 3
     }
     */
    @POST
    @Path("/setquantity")
    @Consumes(MediaType.APPLICATION_JSON)
    Response setQuantity(String json);

    /**
     * Expected body request :
     {
        "id": 51,
        "item": "water",
        "istaken": true
     }
     */
    @POST
    @Path("/setTaken")
    @Consumes(MediaType.APPLICATION_JSON)
    Response setTaken(String json);

    /**
     * Expected body request :
     {
        "id": 51,
        "item": "water",
     }
     */
    @POST
    @Path("/setnotfound")
    @Consumes(MediaType.APPLICATION_JSON)
    Response setNotFound(String json);

    /**
     * Expected request body :
     {
        "id": 2
     }
     */
    @POST
    @Path("/removeshoplist")
    @Consumes(MediaType.APPLICATION_JSON)
    Response removeShopList(String json);

    /**
     * Expected request body :
     {
        "id": 2
     }
     */
    @POST
    @Path("/archiveshoplist")
    @Consumes(MediaType.APPLICATION_JSON)
    Response archiveShopList(String json);
}
