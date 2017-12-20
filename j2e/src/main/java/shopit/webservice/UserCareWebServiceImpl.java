package shopit.webservice;

import org.json.JSONObject;
import shopit.*;
import shopit.entities.User;
import shopit.exceptions.UserAlreadyExistingException;
import shopit.exceptions.UserNotFoundException;
import shopit.utils.MessageDevice;
import shopit.utils.NotificationType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Charly on 29/05/2017.
 */

@Stateless
public class UserCareWebServiceImpl implements UserCareWebService {

    @EJB private UserRegistration userRegistration;
    @EJB private UserFinder userFinder;
    @EJB private UserOperation userOperation;
    @EJB private TokenManaging tokenManaging;
    @EJB private Logging logging;

    @Override
    public Response addUser(String json)
    {
        JSONObject jsonObject = new JSONObject(json);

        try{
            userRegistration.createUser(
                    jsonObject.getString("pseudo"),
                    jsonObject.getString("login"),
                    jsonObject.getString("password"));
        }
        catch(UserAlreadyExistingException e)
        {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().build();
    }

    @Override
    public Response setToken(String json)
    {
        JSONObject jsonObject = new JSONObject(json);

        int idUser = jsonObject.getInt("id");
        String token = jsonObject.getString("token");

        User user = userFinder.findUserById(idUser);
        tokenManaging.setUserDeviceToken(user, token);

        return Response.ok().build();
    }

    @Override
    public Response logIn(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        String login = jsonObject.getString("login");
        String password = jsonObject.getString("password");

        int userId;
        try {
            userId = logging.logIn(login, password);
        }
        catch(UserNotFoundException e)
        {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().type(MediaType.TEXT_PLAIN).entity(userId).build();
    }

    @Override
    public Response doesLoginExist(String login)
    {
        if(userFinder.findUserByLogin(login) == null)
        {
            return Response.ok().type(MediaType.TEXT_PLAIN).entity("false").build();
        }
        return Response.ok().type(MediaType.TEXT_PLAIN).entity("true").build();
    }

    @Override
    public Response doesPseudoExist(String pseudo)
    {
        if(userFinder.findUserByPseudo(pseudo) == null)
        {
            return Response.ok().type(MediaType.TEXT_PLAIN).entity("false").build();
        }
        return Response.ok().type(MediaType.TEXT_PLAIN).entity("true").build();
    }

    @Override
    public Response setLifespanList(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        int userId = jsonObject.getInt("id");
        int newLifespan = jsonObject.getInt("lifespan");

        try{
            userOperation.setUserLifespanList(userId, newLifespan);
        }
        catch(UserNotFoundException e)
        {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().build();
    }

    @Override
    public Response getLifespanList(int userId)
    {
        User user = userFinder.findUserById(userId);
        if(user == null)
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("user not found").build();

        return Response.ok().type(MediaType.TEXT_PLAIN).entity(user.getLifespanList()).build();
    }

    @Override
    public Response setUserDoReccurency(String json) {
        JSONObject jsonObject = new JSONObject(json);
        int userId = jsonObject.getInt("id");
        boolean reccurency = jsonObject.getBoolean("reccurency");

        try{
            userOperation.setUserDoReccurency(userId, reccurency);
        }
        catch(UserNotFoundException e)
        {
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity(e.toString()).build();
        }

        return Response.ok().build();
    }

    @Override
    public Response getUserDoReccurency(int userId) {
        User user = userFinder.findUserById(userId);
        if(user == null)
            return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("user not found").build();

        return Response.ok().type(MediaType.TEXT_PLAIN).entity(user.isDoReccurency()).build();

    }
}
