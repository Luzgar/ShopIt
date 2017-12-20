package shopit.webservice;

import com.google.gson.Gson;
import org.json.JSONObject;
import shopit.MedalFinder;
import shopit.MedalOperation;
import shopit.ShopListFinder;
import shopit.UserFinder;
import shopit.entities.Medal;
import shopit.entities.ShopList;
import shopit.entities.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 13/06/2017.
 */
@Stateless
public class MedalWebServiceImpl implements MedalWebService{

    @EJB
    MedalOperation medalOperation;
    @EJB
    MedalFinder medalFinder;
    @EJB
    UserFinder userFinder;
    @EJB
    ShopListFinder shopListFinder;

    @Override
    public Response getUserMedalExposed(int id) {

        User user = userFinder.findUserById(id);

        if(user==null){
            return Response.status(Response.Status.FORBIDDEN).
                    type(MediaType.TEXT_PLAIN).entity("User not found").build();
        }

        Medal medal = medalFinder.getUserMedal(user);

        Gson gson = new Gson();
        String json = gson.toJson(medal.getName());

        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();

    }

    @Override
    public Response getUserMedalsUnlocked(int id) {

        User user = userFinder.findUserById(id);

        if(user==null){
            return Response.status(Response.Status.FORBIDDEN).
                    type(MediaType.TEXT_PLAIN).entity("User not found").build();
        }

        List<String> medals = medalFinder.getUserMedalsUnlocked(user);

        Gson gson = new Gson();
        String json = gson.toJson(medals);

        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();

    }

    @Override
    public Response getMedalOfContributors(int id) {

        ShopList shopList = shopListFinder.getShopListById(id);
        if(shopList==null){
            return Response.status(Response.Status.FORBIDDEN).
                    type(MediaType.TEXT_PLAIN).entity("ShopList not found").build();
        }

        List<String> medals = medalFinder.getMedalOfContributors(shopList);

        Gson gson = new Gson();
        String json = gson.toJson(medals);

        return Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
    }

    @Override
    public Response setUserMedalExposed(String json) {

        JSONObject jsonObject = new JSONObject(json);

        int userId = jsonObject.getInt("id");
        String medal_name = jsonObject.getString("medal").toUpperCase();

        User user = userFinder.findUserById(userId);
        Medal medal = Medal.valueOf(medal_name);
        if(medal==null){
            medal = Medal.NOMEDAL;
        }
        try {
            medalOperation.setUserMedal(user, medal);
        }
        catch(Exception e){
            return Response.status(Response.Status.FORBIDDEN)
                    .type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().type(MediaType.TEXT_PLAIN).build();
    }

    //TODO: cheat for demo
    @Override
    public Response addUserMedal(int id, String medal) {

        User user = userFinder.findUserById(id);
        if(user!=null){
            medalOperation.addMedalUser(user,medal);
        }

        return Response.ok().type(MediaType.TEXT_PLAIN).build();
    }
}
