package shopit.webservice;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import shopit.*;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.exceptions.NoItemFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Charly on 29/05/2017.
 */

@Stateless
public class ShopListWebServiceImpl implements ShopListWebService
{
    @EJB private ShopListRegistration shopListRegistration;
    @EJB private ShopListFinder shopListFinder;
    @EJB private ShopListOperation shopListOperation;
    @EJB private UserFinder userFinder;
    @EJB private DeviceCommunication deviceCommunication;

    @Override
    public Response registerList(String json)
    {
        JSONObject jsonObject = new JSONObject(json);

        int userId = jsonObject.getInt("id");
        String nameList = jsonObject.getString("name");
        int timeStamp = jsonObject.getInt("timestamp");

        Date dateCreation = new Date(new Timestamp(timeStamp).getTime());

        List<ShopListElement> items = new ArrayList<>();

        JSONArray jsonArray = jsonObject.getJSONArray("items");
        for(int i = 0; i < jsonArray.length(); i++)
        {
            ShopListElement temp = new ShopListElement();
            JSONObject object = (JSONObject) jsonArray.get(i);
            temp.setItem(object.getString("name_item"));
            temp.setNumber(object.getInt("quantity"));
            temp.setTaken(object.getBoolean("taken"));
            temp.setCategory(object.getString("category"));
            temp.setRecipeContainer(object.getString("recipe_container"));

            items.add(temp);
        }

        User user = userFinder.findUserById(userId);
        int idList = shopListRegistration.createShopList(user, nameList, items, dateCreation);

        return Response.ok().type(MediaType.TEXT_PLAIN).entity(idList).build();
    }

    @Override
    public Response getAllCurrentTitlesShopList(int userId)
    {
        User user = userFinder.findUserById(userId);
        Map<Integer, String> titlesWithId = shopListFinder.getAllTitleShopList(user, false);

        // Map to JSON Object
        Gson gson = new Gson();
        String json = gson.toJson(titlesWithId);

        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
    }

    @Override
    public Response getAllArchiveTitlesShopList(int userId)
    {
        User user = userFinder.findUserById(userId);
        Map<Integer, String> titlesWithId = shopListFinder.getAllTitleShopList(user, true);

        // Map to JSON Object
        Gson gson = new Gson();
        String json = gson.toJson(titlesWithId);

        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
    }

    @Override
    public Response getListWithId(int listId)
    {
        ShopList list = shopListFinder.getShopListById(listId);

        Gson gson = new Gson();
        String json = gson.toJson(list);

        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
    }

    @Override
    public Response addItem(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        int idList = jsonObject.getInt("id");

        //TODO: ok for the price ?
        JSONObject newItemJson = jsonObject.getJSONObject("item");
        ShopListElement newItem = new ShopListElement(
                newItemJson.getString("name_item"),
                newItemJson.getInt("quantity"),
                newItemJson.getBoolean("taken"),
                newItemJson.getString("category"),0);
        newItem.setRecipeContainer(newItemJson.getString("recipe_container"));

        shopListOperation.addItemToShopList(idList, newItem);

        return Response.ok().build();
    }

    @Override
    public Response removeItem(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        int idList = jsonObject.getInt("id");
        String nameItem = jsonObject.getString("item");

        try
        {
            shopListOperation.removeItemFromShopList(idList, nameItem);
        }
        catch(NoItemFoundException e)
        {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().build();
    }

    @Override
    public Response setQuantity(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        int idList = jsonObject.getInt("id");
        String nameItem = jsonObject.getString("item");
        int newQuantity = jsonObject.getInt("new_quantity");

        try {
            shopListOperation.setItemQuantityFromShopList(idList, nameItem, newQuantity);
        }
        catch(NoItemFoundException e)
        {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().build();
    }

    @Override
    public Response setNotFound(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        int idList = jsonObject.getInt("id");
        String nameItem = jsonObject.getString("item");

        try{
            shopListOperation.setItemNotFound(idList, nameItem);
        }
        catch(NoItemFoundException e)
        {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().build();
    }

    @Override
    public Response setTaken(String json) {
        JSONObject jsonObject = new JSONObject(json);
        int idList = jsonObject.getInt("id");
        String nameItem = jsonObject.getString("item");
        boolean isTaken = jsonObject.getBoolean("istaken");

        try {
            shopListOperation.setItemTaken(idList, nameItem, isTaken);
        }
        catch(NoItemFoundException e)
        {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().build();
    }

    @Override
    public Response removeShopList(String json) {

        JSONObject jsonObject = new JSONObject(json);
        int idList = jsonObject.getInt("id");

        shopListRegistration.removeShopList(idList);

        return Response.ok().build();
    }

    @Override
    public Response archiveShopList(String json) {

        JSONObject jsonObject = new JSONObject(json);
        int idList = jsonObject.getInt("id");

        shopListOperation.archiveShopList(idList);

        return Response.ok().build();
    }
}
